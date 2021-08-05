package idv.demo;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;

public class DataSourceS3 implements DataSource<byte[]> {

    private static final S3Client s3Client = S3Client.create();

    private String bucket;

    private String key;

    public DataSourceS3(String bucket, String key) {
        this.bucket = bucket;
        this.key = key;
    }

    @Override
    public byte[] extract() {

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        try {
            return s3Client.getObject(request).readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
