package com.sentimentapi.services;

import com.sentimentapi.dtos.*;
import com.sentimentapi.entities.CommentEntity;
import com.sentimentapi.entities.SentimentPrediction;
import com.sentimentapi.repositories.CommentRepository;
import com.sentimentapi.repositories.SentimentPredictionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Classe de serviço responsável pela lógica de negócio
// relacionada à análise de sentimentos
@Service
@RequiredArgsConstructor
public class SentimentService {

    // Cliente HTTP usado para se comunicar com o microserviço Python
    private final RestTemplate restTemplate;

    // Repositório responsável por persistir comentários
    private final CommentRepository commentRepository;

    // Repositório responsável por persistir previsões de sentimento
    private final SentimentPredictionRepository sentimentPredictionRepository;

    // URL do microserviço Python.
    // Caso não exista configuração externa, usa o valor padrão
    @Value("${sentiment.python.url:http://python_service:5000/predict}")
    private String pythonUrl;

    /**
     * Envia um texto ao microserviço Python e retorna a previsão de sentimento.
     * Esse método centraliza a integração externa da aplicação.
     */
    public SentimentPredictionDto predictSentiment(String text) {

        // Corpo da requisição enviado ao serviço Python
        Map<String, String> body = Map.of("text", text);

        // Chamada HTTP POST ao microserviço
        SentimentPredictionDto prediction =
                restTemplate.postForObject(
                        pythonUrl,
                        body,
                        SentimentPredictionDto.class
                );

        // Tratamento defensivo:
        // garante que a aplicação não quebre caso o serviço Python falhe
        if (prediction == null) {
            return new SentimentPredictionDto("Indefinido", 0.0, null);
        }

        return prediction;
    }


    /**
     * Busca um comentário e sua previsão pelo ID.
     */
    public CommentEntityDto getPredictionById(Long id) {
        // Retorna o comentário se existir ou null caso contrário
        Optional<CommentEntity> commentEntity = commentRepository.findById(id);

        if (commentEntity.isPresent()) {
            CommentEntity comment = commentEntity.get();

            return new CommentEntityDto(comment);
        }
        return null;
    }

    /**
     * Calcula estatísticas de sentimento (percentual)
     * com base nos últimos N comentários.
     */
    public StatsDto getStats(int quantidade) {

        // Total de comentários existentes no banco
        long totalComments = commentRepository.count();

        // Evita divisão por zero quando não há dados
        if (totalComments == 0) {
            return new StatsDto(0.0, 0.0);
        }

        double positivo = 0;
        double negativo = 0;

        // Limita a consulta aos últimos N registros
        Pageable pageable = PageRequest.of(0, quantidade);
        List<CommentEntity> comments =
                commentRepository.buscarPorUltimos(pageable);

        // Contabiliza os sentimentos encontrados
        for (CommentEntity comment : comments) {

            // Aqui ocorre o erro que você viu antes se previsao for null
            if (comment.getPrevisao() == null) {
                continue;
            }

            String label = comment.getPrevisao().getLabel();

            if ("positivo".equalsIgnoreCase(label)) {
                positivo++;
            } else if ("negativo".equalsIgnoreCase(label)) {
                negativo++;
            }
        }

        double total = positivo + negativo;

        // Caso existam comentários sem classificação válida
        if (total == 0) {
            return new StatsDto(0.0, 0.0);
        }

        // Cálculo percentual
        double porcentagemPositivo = (positivo * 100.0) / total;
        double porcentagemNegativo = (negativo * 100.0) / total;

        porcentagemPositivo = Math.round(porcentagemPositivo * 100.0) / 100.0;
        porcentagemNegativo = Math.round(porcentagemNegativo * 100.0) / 100.0;


        return new StatsDto(porcentagemPositivo, porcentagemNegativo);
    }

    /**
     * Atualiza o texto de um comentário existente
     * e recalcula sua previsão de sentimento.
     */
    public Optional<SentimentResponseDto> updatePrediction(Long id, String newText) {

        // Busca o comentário pelo ID
        Optional<CommentEntity> optionalComment =
                commentRepository.findById(id);

        // Retorna vazio caso o comentário não exista
        if (optionalComment.isEmpty()) {
            return Optional.empty();
        }

        CommentEntity commentEntity = optionalComment.get();

        // Gera nova previsão para o texto atualizado
        SentimentPredictionDto predictionDto =
                predictSentiment(newText);

        SentimentPrediction newPrediction = new SentimentPrediction(
                            predictionDto.label(),
                            predictionDto.probability()
                            );
        // Persiste a nova previsão
       SentimentPrediction sentimentPrediction =
               sentimentPredictionRepository.save(newPrediction);

        commentEntity.setText(newText);
        commentEntity.setPrevisao(sentimentPrediction);


        commentRepository.save(commentEntity);

        return Optional.of(new SentimentResponseDto(predictionDto));
    }

    /**
     * Remove um comentário e retorna o registro excluído.
     */
    public Optional<CommentEntityDto> deletePrediction(Long id) {

        Optional<CommentEntity> optionalComment =
                commentRepository.findById(id);

        if (optionalComment.isPresent()) {
            CommentEntity comment = optionalComment.get();

            commentRepository.delete(comment);

            CommentEntityDto commentEntityDto = new CommentEntityDto(comment);


            return Optional.of(commentEntityDto);
        }
        return Optional.empty();
    }
    /**
     * Processa um arquivo CSV contendo textos,
     * gera previsões de sentimento e persiste os dados.
     */
    public UploadCsvDto processoUploadCsv(
            MultipartFile file) {

        int total = 0;
        int sucesso = 0;
        int erro = 0;

        try (Reader reader =
                     new InputStreamReader(file.getInputStream())) {

            // Configuração do parser considerando cabeçalho
            CSVParser parser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(reader);

            for (CSVRecord record : parser) {
                total++;
                // Obtém o texto da coluna "text"
                String text = record.get("text");

                Map<String, String> body =
                        Map.of("text", text);

                // Chamada ao microserviço Python
                SentimentPredictionDto prediction =
                        restTemplate.postForObject(
                                pythonUrl,
                                body,
                                SentimentPredictionDto.class
                        );

                if (prediction != null) {

                    SentimentPrediction sentimentPrediction = new SentimentPrediction(
                            prediction.label(),
                            prediction.probability()
                    );
                    // Persiste a previsão
                      sentimentPredictionRepository.save(sentimentPrediction);

                    // Cria e persiste o comentário

                    CommentEntity commentEntity = new CommentEntity();
                    commentEntity.setText(text);
                    commentEntity.setPrevisao(sentimentPrediction);

                    commentRepository.save(commentEntity);

                   sucesso++;
                } else {
                    erro++;
                }
            }

        } catch (Exception e) {
            // Encapsula qualquer erro de IO ou parsing
            throw new RuntimeException("Erro ao processar csv", e);
        }
        return  new UploadCsvDto(
                total,
                sucesso,
                erro,
                "Upload precessado com sucesso"
        );
    }

    /**
     * Cria um comentário individual,
     * gera a previsão e persiste os dados.
     */
    public SentimentResponseDto createComment(String text) {

        // Gera a previsão
        SentimentPredictionDto predictionDto =
                predictSentiment(text);


        SentimentPrediction prediction = new SentimentPrediction(
                predictionDto.label(),
                predictionDto.probability()
        );

        // Salva a previsão
        prediction = sentimentPredictionRepository.save(prediction);

        // Cria o comentário associado

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setText(text);
        commentEntity.setPrevisao(prediction);

        commentRepository.save(commentEntity);

       return toResponse(predictionDto);

    }

    public Page<CommentEntityDto> listarPagina(Pageable pageable) {

       Page<CommentEntity> pageCommentEntity =
               commentRepository.findAll(pageable);

       List<CommentEntityDto> commentEntityDtosList = new ArrayList<>();

        for (CommentEntity entity : pageCommentEntity.getContent()) {
            commentEntityDtosList.add(new CommentEntityDto(entity));
        }

        return new PageImpl<>(
                commentEntityDtosList,
                pageable,
                pageCommentEntity.getTotalElements()
        );

    }

    private SentimentResponseDto toResponse(SentimentPredictionDto  prediction) {


        List<String> palavras = new ArrayList<>();

        if (prediction.topFeatures() != null) {
            for (var feature : prediction.topFeatures()) {
                palavras.add(feature.palavra());
            }
        }

        return new SentimentResponseDto(
                prediction.label(),
                prediction.probability(),
                palavras
        );
    }
}