package com.example.contabos3api;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadDTO {
    public String bucket;
    public String location;
    public MultipartFile file;

    public FileUploadDTO(String bucket, String location, MultipartFile file) {
        this.bucket = bucket;
        this.location = location;
        this.file = file;
    }

}
