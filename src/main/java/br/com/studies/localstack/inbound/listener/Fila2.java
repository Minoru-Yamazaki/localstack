package br.com.studies.localstack.inbound.listener;

import br.com.studies.localstack.inbound.listener.dto.SqsMessage;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class Fila2 {

    @SqsListener("${spring.cloud.aws.sqs.queue-2}")
    public void consumir(@Payload SqsMessage mensagem) {
        log.info("Fila 2 - Recebido: {}", mensagem.getMessage());
    }

}
