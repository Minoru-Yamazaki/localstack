package br.com.studies.localstack.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Mensagem {

    @JsonProperty("Message")
    private String message;

}
