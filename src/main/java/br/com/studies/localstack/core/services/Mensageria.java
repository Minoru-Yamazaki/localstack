package br.com.studies.localstack.core.services;

public interface Mensageria<T> {

    void sendToTopic(T dto, String topico);

    void sendToQueue(T dto, String fila);

}
