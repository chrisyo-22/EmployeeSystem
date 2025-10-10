package com.chrisyo.entity;// Java
import lombok.Getter;
import software.amazon.awssdk.services.s3.S3Client;


public class ServiceClientSource {
        @Getter
        private static final S3Client s3Client = S3Client.create();

}
