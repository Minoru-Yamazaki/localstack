package br.com.studies.localstack.core.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    void uploadFile(MultipartFile file);

    Resource downloadFile(String key);

}
