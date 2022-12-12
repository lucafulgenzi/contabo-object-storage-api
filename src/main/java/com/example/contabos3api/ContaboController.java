package com.example.contabos3api;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("contabo")
public class ContaboController {

    private final AWSConfig awsConfig;

    @Autowired
    ContaboController(AWSConfig awsConfig) {
        this.awsConfig = awsConfig;
    }

   @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
   public String uploadFile(
           @ModelAttribute FileUploadDTO fileUpload
   ) {

      TransferManager tm = TransferManagerBuilder.standard()
              .withS3Client(awsConfig.getS3Client())
              .withMultipartUploadThreshold((long) (5248000))
              .withMinimumUploadPartSize((long) (5248000))
              .build();

      try {
          Upload upload = tm.upload(getBucketName(fileUpload.bucket), fileUpload.location, fileUpload.file.getInputStream(), null);
          System.out.println("Start upload");
          upload.waitForCompletion();
          System.out.println("UploadBody complete");
          tm.shutdownNow();
      }catch (IOException e){
          e.printStackTrace();
      } catch (InterruptedException e) {
          throw new RuntimeException(e);
      }

      return "File uploaded successfully";
   }

   @GetMapping("/list/{bucket}")
   public Object getAllObjects(
           @PathVariable String bucket,
           @RequestParam(defaultValue = "false") String simple
   ) {

        ObjectListing objectListing = awsConfig.getS3Client().listObjects(getBucketName(bucket));

        if (simple.equals("true")) {
            List<String> summaries = new ArrayList<>();
            for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
                summaries.add(os.getKey());
            }
            return summaries;
        }

        return  objectListing.getObjectSummaries();

   }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadObject(
            @RequestParam String bucket,
            @RequestParam String location
    ) throws IOException {


        S3Object s3object = awsConfig.getS3Client().getObject(getBucketName(bucket), location);
        String contentType = s3object.getObjectMetadata().getContentType();
        S3ObjectInputStream stream = s3object.getObjectContent();

        String fileName = s3object.getKey().split("/")[s3object.getKey().split("/").length - 1];

        byte[] content = null;

        content = IOUtils.toByteArray(stream);
        s3object.close();

        final ByteArrayResource resource = new ByteArrayResource(content);
        return ResponseEntity
                .ok()
                .contentLength(content.length)
                .header("Content-type", contentType)
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete")
    public void deleteObject(
            @RequestParam String bucket,
            @RequestParam String location
    ){

        awsConfig.getS3Client().deleteObject(getBucketName(bucket), location);

    }


   @GetMapping("/buckets")
   public Object getAllBuckets() {
      return awsConfig.getS3Client().listBuckets();
   }


   private String getBucketName(String bucket) {
      return "/" + bucket;
   }


}

