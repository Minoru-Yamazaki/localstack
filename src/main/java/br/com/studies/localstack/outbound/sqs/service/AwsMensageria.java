package br.com.studies.localstack.outbound.sqs.service;

import br.com.studies.localstack.core.services.Mensageria;
import br.com.studies.localstack.core.dto.Mensagem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Log4j2
@Service
@RequiredArgsConstructor
public class AwsMensageria implements Mensageria<Mensagem> {

    private final SnsClient snsClient;

    private final SqsAsyncClient sqsAsyncClient;

    @Value("${spring.cloud.aws.sns.topic-arn}")
    private String topicArn;

    @Value("${spring.cloud.aws.sqs.endpoint}")
    private String sqsEndpoint;

    @Value("${spring.cloud.aws.credentials.account-id}")
    private String accountId;

    @Override
    public void sendToTopic(Mensagem dto, String topic) {
        log.info("enviando mensagem: '{}' no tópico: {}", dto.getMessage(), topic);
        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn.concat(topic))
                .message(dto.getMessage())
                .build();
        snsClient.publish(request);
    }

    @Override
    public void sendToQueue(Mensagem dto, String queue) {
        log.info("enviando mensagem: '{}', para fila: {}", dto.getMessage(), queue);
        String queueUrl = sqsEndpoint.concat("/").concat(accountId).concat("/").concat(queue);
        sqsAsyncClient.sendMessage(
                SendMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .messageBody(toJson(dto))
                        .build()
        );
    }

    private String toJson(Mensagem mensagem){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(mensagem);
        } catch (JsonProcessingException e) {
            log.error("Erro ao converter dto em JSON, mensagem de erro: {}", e.getMessage());
        }
        return null;
    }

}
