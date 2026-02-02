package com.sentimentapi.entities;

import com.sentimentapi.dtos.AnalisePesoDto;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalisePeso {

    private String palavra;

    private double peso;


    public AnalisePeso(AnalisePesoDto analisePesoDto){
        this.palavra = analisePesoDto.palavra();
        this.peso = analisePesoDto.peso();
    }
}
