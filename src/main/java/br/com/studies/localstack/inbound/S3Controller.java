package br.com.studies.localstack.inbound;

import br.com.studies.localstack.core.services.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.http.HttpStatusCode;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("localstack/s3")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        s3Service.uploadFile(file);
//        return "Arquivo enviado para o S3 (LocalStack): " + key;

        return ResponseEntity.status(HttpStatusCode.CREATED).build();
    }

    @GetMapping("/download/{key}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String key) throws IOException {
        Resource resource = s3Service.downloadFile(key);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFile().getName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

}
