package com.sentimentapi.infra.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrosDadosValidacao>> tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest()
                .body(erros.stream()
                        .map(ErrosDadosValidacao::new)
                        .toList());
    }

    private record ErrosDadosValidacao(

            String campo,
            String mensagem

    ) {
        public ErrosDadosValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

}


