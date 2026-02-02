package com.sentimentapi.controllers;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.sentimentapi.dtos.CommentEntityDto;
import com.sentimentapi.dtos.SentimentResponseDto;
import com.sentimentapi.dtos.StatsDto;
import com.sentimentapi.dtos.UploadCsvDto;
import com.sentimentapi.services.SentimentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class SentimentController {

    private final SentimentService sentimentService;

    public SentimentController(SentimentService sentimentService) {
        this.sentimentService = sentimentService;
    }

    @PostMapping("/sentiment")
    public ResponseEntity<SentimentResponseDto> getSentiment(@RequestBody @Valid CommentEntityDto dto) {
        SentimentResponseDto prediction = sentimentService.createComment(dto.text());
        return ResponseEntity.ok(prediction);
    }

    @GetMapping("/sentiment")
    public Page<CommentEntityDto> listarPagina(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataCriacao").descending());
        return sentimentService.listarPagina(pageable);
    }

    @PostMapping(value = "/sentiment/lote", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadCsvDto> uploadCsv(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(sentimentService.processoUploadCsv(file));
    }

    @GetMapping("/sentiment/{id}")
    public ResponseEntity<Map<String, Object>> getSentimentById(@PathVariable Long id) {
        CommentEntityDto comment = sentimentService.getPredictionById(id);
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Previsão não encontrada"));
        }
        return ResponseEntity.ok(Map.of(
                "text", comment.text(),
                "previsao", comment.previsao().previsao(),
                "probabilidade", comment.previsao().probabilidade()
        ));
    }

    @GetMapping("/sentiment/stats/{quantidade}")
    public ResponseEntity<Map<String, Object>> stats(@PathVariable int quantidade) {
        if (quantidade <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "A quantidade deve ser maior que zero"));
        }
        StatsDto stats = sentimentService.getStats(quantidade);
        return ResponseEntity.ok(Map.of(
                "positivo", stats.positivo(),
                "negativo", stats.negativo()
        ));
    }

    @PutMapping("/sentiment/{id}")
    public ResponseEntity<?> updateSentiment(@PathVariable Long id, @RequestBody @Valid Map<String, String> request) {
        String newText = request.get("text");
        if (newText == null || newText.length() < 5) {
            return ResponseEntity.badRequest().body(Map.of("error", "Texto muito curto ou inválido"));
        }
        Optional<SentimentResponseDto> optionalComentario = sentimentService.updatePrediction(id, newText);
        if (optionalComentario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Comentário não encontrado"));
        }
        return ResponseEntity.ok(optionalComentario.get());
    }

    @DeleteMapping("/sentiment/{id}")
    public ResponseEntity<Map<String, Object>> deleteSentiment(@PathVariable Long id) {
        Optional<CommentEntityDto> deleted = sentimentService.deletePrediction(id);
        if (deleted.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Comentário não encontrado"));
        }
        return ResponseEntity.ok(Map.of("message", "Previsão excluída com sucesso"));
    }
}
