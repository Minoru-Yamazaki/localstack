package br.com.studies.localstack.inbound.listener;

import br.com.studies.localstack.inbound.listener.dto.SqsMessage;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class Fila1 {

    @SqsListener("${spring.cloud.aws.sqs.queue-1}")
    public void consumir(@Payload SqsMessage mensagem) {
        log.info("Fila 1 - Recebido: {}", mensagem.getMessage());
    }

}
