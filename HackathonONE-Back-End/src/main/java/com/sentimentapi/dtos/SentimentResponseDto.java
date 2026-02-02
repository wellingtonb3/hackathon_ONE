package com.sentimentapi.dtos;

import java.util.ArrayList;
import java.util.List;

public record SentimentResponseDto(
            String previsao,
            Double probabilidade,
            List<String> analise_peso
){
    public SentimentResponseDto(SentimentPredictionDto predictionDto) {
        this(
                predictionDto.label(),
                predictionDto.probability(),
                analisePeso(predictionDto)
        );

    }

    private static List<String> analisePeso(
            SentimentPredictionDto predictionDto
    ) {
        List<String> lista = new ArrayList<>();

        if (predictionDto.topFeatures() != null ) {
            for (AnalisePesoDto feature : predictionDto.topFeatures()){
                lista.add(feature.palavra());
            }
        }
        return lista;
    }

}
