package br.com.studies.localstack.inbound.controller;

import br.com.studies.localstack.core.dto.Mensagem;
import br.com.studies.localstack.core.services.Mensageria;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("localstack/mensageria/enviar")
public class MensageriaController {

    private final Mensageria mensageria;

    @Value("${spring.cloud.aws.sns.topic-1}")
    private String topico;

    @Value("${spring.cloud.aws.sqs.queue-1}")
    private String fila1;

    @Value("${spring.cloud.aws.sqs.queue-2}")
    private String fila2;

    @PostMapping("topico-1")
    public ResponseEntity<String> sendToTopic1(@RequestBody Mensagem mensagem) {
        mensageria.sendToTopic(mensagem, topico);
        return ResponseEntity.ok("Publicado!");
    }

    @PostMapping("fila-1")
    public ResponseEntity<String> sendToQueue1(@RequestBody Mensagem mensagem) {
        mensageria.sendToQueue(mensagem, fila1);
        return ResponseEntity.ok("Publicado!");
    }

    @PostMapping("fila-2")
    public ResponseEntity<String> sendToQueue2(@RequestBody Mensagem mensagem) {
        mensageria.sendToQueue(mensagem, fila2);
        return ResponseEntity.ok("Publicado!");
    }

}
