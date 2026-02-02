package com.sentimentapi.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record SentimentPredictionDto(


        @JsonProperty("previsao")
        String label,

         @JsonProperty("probabilidade")
        double probability,

        @JsonProperty("analise_pesos")
        List<AnalisePesoDto> topFeatures

       ){
}

