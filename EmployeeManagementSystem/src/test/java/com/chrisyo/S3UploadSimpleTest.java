package com.chrisyo;

import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.chrisyo.utils.AwsS3Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.*;
import com.chrisyo.utils.AwsS3Utils;

import java.nio.file.Paths;


public class S3UploadSimpleTest {
    private static final Logger logger = LoggerFactory.getLogger(S3UploadSimpleTest.class);
    private static final AwsS3Utils awsS3Utils = new AwsS3Utils();


    public static void main(String[] args) {

//        S3UploadSimpleTest uploader = new S3UploadSimpleTest();
//        System.out.println("Working dir = " + System.getProperty("user.dir"));
//        try (InputStream is = new FileInputStream("test.txt")) {
//            PutObjectResponse response = uploader.asyncClient_multipart_stream_unknown_size("amz-chris-yo-22-wode-mingzi-hello-world", "test.txt", is);
//            if (response != null) {
//                logger.info("Upload completed successfully");
//            }
//            System.out.println(response);
//        } catch (IOException e) {
//            logger.error("Error during file upload", e);
//        }
//        Integer failedTransfers = uploader.downloadObjectsToDirectory();
//        if (failedTransfers > 0) {
//            logger.warn("Failed to download {} objects", failedTransfers);
//        }
        String uri = awsS3Utils.UploadFile("test.txt", Path.of("test.txt").toUri());
        awsS3Utils.createPresignedGetUrl(uri);
        System.out.println(uri);





    }

    public PutObjectResponse asyncClient_multipart_stream_unknown_size(String bucketName, String key, InputStream inputStream) {

        S3AsyncClient s3AsyncClient = S3AsyncClient.builder().multipartEnabled(true).build();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        AsyncRequestBody body = AsyncRequestBody.fromInputStream(inputStream, null, executor); // 'null' indicates that the
        // content length is unknown.
        CompletableFuture<PutObjectResponse> responseFuture =
                s3AsyncClient.putObject(r -> r.bucket(bucketName).key(key), body)
                        .exceptionally(e -> {
                            if (e != null) {
                                logger.error(e.getMessage(), e);
                            }
                            return null;
                        });

        PutObjectResponse response = responseFuture.join(); // Wait for the response.
        executor.shutdown();
        return response;
    }


    public Integer downloadObjectsToDirectory() {
        S3TransferManager transferManager = S3TransferManager.create();

        DownloadFileRequest downloadFileRequest = DownloadFileRequest.builder()
                .getObjectRequest(req -> req.bucket("amz-chris-yo-22-wode-mingzi-hello-world").key
                        ("test.txt"))
                .destination(Paths.get("myFile.txt"))
                .build();

        // Initiate the transfer
        FileDownload download =
                transferManager.downloadFile(downloadFileRequest);

        // Pause the download
        ResumableFileDownload resumableFileDownload = download.pause();

        // Optionally, persist the download object
        Path path = Paths.get("resumableFileDownload.json");
        resumableFileDownload.serializeToFile(path);

        // Retrieve the resumableFileDownload from the file
        resumableFileDownload = ResumableFileDownload.fromFile(path);

        // Resume the download
        FileDownload resumedDownload = transferManager.resumeDownloadFile(resumableFileDownload);

        // Wait for the transfer to complete
        resumedDownload.completionFuture().join();
        return 1;
    }
}