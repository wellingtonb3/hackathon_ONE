package com.sentimentapi.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

// Entidade que representa um comentário analisado
// Armazena o texto original, a previsão de sentimento
// associada e a data de criação do registro
@Entity
@Table(name = "comentario_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Texto do comentário enviado para análise
    @Size(max = 500, message = "O comentário não pode ter tamanho maior que 500 caracteres")
    @Column(length = 500)
    private String text;

    // Relacionamento com a previsão de sentimento gerada
    @ManyToOne
    @JoinColumn(name = "sentiment_prediction_id")
    private SentimentPrediction previsao;

    // Data e hora em que o comentário foi salvo
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

}
