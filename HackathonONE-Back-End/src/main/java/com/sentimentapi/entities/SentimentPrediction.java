package com.sentimentapi.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sentimentapi.dtos.SentimentPredictionDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sentiment_prediction_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SentimentPrediction {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("previsao")
    private String label; // Rótulo do sentimento, como "Positivo" ou "Negativo"

    @JsonProperty("probabilidade")
    private double probability; // Probabilidade do sentimento ser o que


    public SentimentPrediction(SentimentPredictionDto sentimentPredictionDto){
        this.label = sentimentPredictionDto.label();
        this.probability = sentimentPredictionDto.probability();

    }

    public SentimentPrediction(String label, double probability) {
        this.label = label;
        this.probability = probability;
    }


}

