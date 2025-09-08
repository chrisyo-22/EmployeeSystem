package com.chrisyo;

import com.chrisyo.entity.ServiceClientSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.ContentStreamProvider;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.InputStream;

@Service
public class AwsTest {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public PutObjectResponse upload(String key, InputStream inputStream, String contentType) {
        var s3Client = ServiceClientSource.getS3Client();

        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        RequestBody body = RequestBody.fromContentProvider(
                ContentStreamProvider.fromInputStream(inputStream),
                contentType
        );

        return s3Client.putObject(req, body);
    }
}