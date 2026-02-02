package com.sentimentapi.dtos;

public record SentimentPredictionVerDto(
        String previsao,
        double probabilidade
) {
}
