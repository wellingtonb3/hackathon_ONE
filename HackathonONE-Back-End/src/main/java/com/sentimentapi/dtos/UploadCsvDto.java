package com.sentimentapi.dtos;

public record UploadCsvDto(
        int totalLidos,
        int sucesso,
        int erro,
        String mensagem
) {
}
