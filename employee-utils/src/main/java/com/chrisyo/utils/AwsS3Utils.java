package com.chrisyo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class AwsS3Utils {
//    public static final String BUCKET_NAME = "amz-chris-yo-22-wode-mingzi-hello-world";

    private static final Map<String, String> ALLOWED_TYPES = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp"
    );

    static final String AWS_REGION = "us-east-1";
    public static final S3AsyncClient s3AsyncClient = S3AsyncClient.builder().multipartEnabled(true).build();
    static final S3TransferManager transferManager = S3TransferManager.builder()
            .s3Client(s3AsyncClient)
            .build();
    private static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    @Autowired
    private AwsS3Properties awsS3Properties;


    public String asyncUpload_multipart_stream(String key, InputStream inputStream) {

        AsyncRequestBody body = AsyncRequestBody.fromInputStream(inputStream, null, executor); // 'null' indicates that the
        // content length is unknown.
        CompletableFuture<PutObjectResponse> responseFuture =
                s3AsyncClient.putObject(r -> r.bucket(awsS3Properties.getBucketName()).key(key), body)
                        .exceptionally(e -> {
                            if (e != null) {
                                log.error(e.getMessage(), e);
                            }
                            return null;
                        });

        PutObjectResponse response = responseFuture.join(); // Wait for the response.
        System.out.println("Response: " + response);
        executor.shutdown();
        return key;
    }

    public String UploadFile(String key, URI filePathURI) {

        UploadFileRequest uploadFileRequest = UploadFileRequest.builder()
                .putObjectRequest(b -> b.bucket(awsS3Properties.getBucketName()).key(key))
                .addTransferListener(LoggingTransferListener.create())  // Add listener.
                .source(Paths.get(filePathURI))
                .build();

        FileUpload fileUpload = transferManager.uploadFile(uploadFileRequest);

        fileUpload.completionFuture().join();
        return key;
//        return "https://" + "amz-chris-yo-22-wode-mingzi-hello-world"+"."+"s3"+ "."+ AWS_REGION+ ".amazonaws.com/" + objectName;
        /*
            The SDK provides a LoggingTransferListener implementation of the TransferListener interface.
            You can also implement the interface to provide your own logic.

            Configure log4J2 with settings such as the following.
                <Configuration status="WARN">
                    <Appenders>
                        <Console name="AlignedConsoleAppender" target="SYSTEM_OUT">
                            <PatternLayout pattern="%m%n"/>
                        </Console>
                    </Appenders>

                    <Loggers>
                        <logger name="software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener" level="INFO" additivity="false">
                            <AppenderRef ref="AlignedConsoleAppender"/>
                        </logger>
                    </Loggers>
                </Configuration>

            Log4J2 logs the progress. The following is example output for a 21.3 MB file upload.
                Transfer initiated...
                |                    | 0.0%
                |====                | 21.1%
                |============        | 60.5%
                |====================| 100.0%
                Transfer complete!
        */
    }

    /* Create a pre-signed URL to download an object in a subsequent GET request. */
    public String createPresignedGetUrl(String keyName) {
        try (S3Presigner presigner = S3Presigner.create()) {

            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(awsS3Properties.getBucketName())
                    .key(keyName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))  // The URL will expire in 10 minutes.
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            log.info("Presigned URL: [{}]", presignedRequest.url().toString());
            log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());

            return presignedRequest.url().toExternalForm();
        }
    }


    /* Create a presigned URL to use in a subsequent PUT request */
    public String createPresignedGetUrl(String keyName, Map<String, String> metadata) {
        try (S3Presigner presigner = S3Presigner.create()) {

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(awsS3Properties.getBucketName())
                    .key(keyName)
                    .metadata(metadata)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))  // The URL expires in 10 minutes.
                    .putObjectRequest(objectRequest)
                    .build();


            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            String myURL = presignedRequest.url().toString();
            log.info("Presigned URL to upload a file to: [{}]", myURL);
            log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());

            return presignedRequest.url().toExternalForm();
        }
    }


}
