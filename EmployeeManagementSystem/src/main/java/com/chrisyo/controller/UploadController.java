package com.chrisyo.controller;

import com.chrisyo.aop.Log;
import com.chrisyo.entity.Result;
import com.chrisyo.entity.S3Image;
import com.chrisyo.utils.AwsS3Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.presigner.PresignRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {
    @Autowired
    private AwsS3Utils awsS3Utils;

//    @PostMapping("/upload")
//    public Result upload(String username, Integer age, MultipartFile file) throws IOException {
//        log.info("username: {}, age: {}, file: {}", username, age, file.getOriginalFilename());
//
//        //1. Get original file name
//        String originalFilename = file.getOriginalFilename(); //eg. 002.png
//        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")); //get file extension
//
//        //2. Generate UUID
//        String randomStr = UUID.randomUUID().toString();
//
//        String newFileName = randomStr + extension;
//        //invoke method of multi-part file
//        file.transferTo(new File("D:\\SelfLearning\\Java\\" + newFileName));
//        return Result.success(newFileName + " uploaded successfully.");
//    }

    @Log
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException, URISyntaxException {
        log.info("file: {}", file.getOriginalFilename());

        //1.Get the original file name but only the extension
        String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        String key = UUID.randomUUID() + extName;
        System.out.println("file: " + file.getInputStream());
        //2.Upload to AWS S3
        String savedKey = awsS3Utils.asyncUpload_multipart_stream(key, file.getInputStream());
        log.info("Saved key: {}", savedKey);

        //3.Generate Pre-signed Url
        String url = awsS3Utils.createPresignedGetUrl(savedKey);

        S3Image s3Image = new S3Image();
        s3Image.setKey(savedKey);
        s3Image.setShared_url(url);

        //Image/file shared url to be used by the frontend
        //System.out.println("Image/file shared url to be used by the frontend: " + url);
        return Result.success(s3Image);
    }


    /**
     * Get a presigned Put URL for Frontend to upload the file
     *
     * @param metadata key-value pair such as content type:
     *                 image/jpeg
     *                 image/png
     *                 image/gif
     *                 image/webp
     * @return presigned Put URL
     */
    @GetMapping("/uploads/presign")
    public Result getPresignedUrl(Map<String, String> metadata) {
        log.info("Trying initializing presigned url");
        S3Image s3Image = new S3Image();
        s3Image.setKey(UUID.randomUUID().toString());
        String url = awsS3Utils.createPresignedGetUrl(s3Image.getKey(), metadata);
        s3Image.setShared_url(url);
        return Result.success(s3Image);
    }


}
