package com.sentimentapi.dtos;

import com.sentimentapi.entities.CommentEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CommentEntityDto(

         Long id,

         @NotBlank(message = "o texto da mensagem não pode esta vazio")
         @Size(min = 5, max = 500, message = " O texto deve ter entre 5 e 500 caracteres ")
         String text,

        LocalDateTime dataCriacao,

         SentimentPredictionVerDto previsao

) {

    public CommentEntityDto(CommentEntity entity) {
        this(
                entity.getId(),
                entity.getText(),
                entity.getDataCriacao(),
                entity.getPrevisao() != null
                ? new SentimentPredictionVerDto(
                        entity.getPrevisao().getLabel(),
                        entity.getPrevisao().getProbability()
                )

                : null
        );
    }
}
