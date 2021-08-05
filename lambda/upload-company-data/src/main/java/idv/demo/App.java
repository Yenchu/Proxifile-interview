package idv.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class App implements RequestHandler<S3Event, String> {

    private static final Logger log = LogManager.getLogger(App.class);

    private DataSink<Company> dataSink = new DataSinkDynamodb();

    private CompanyHandler handler = new CompanyHandler(dataSink);

    public String handleRequest(final S3Event input, final Context context) {

        List<S3EventNotification.S3EventNotificationRecord> records = input.getRecords();

        for (S3EventNotification.S3EventNotificationRecord record : records) {

            S3EventNotification.S3Entity s3 = record.getS3();
            String bucketName = s3.getBucket().getName();
            String objectKey = s3.getObject().getUrlDecodedKey();
            log.info("Handle S3 event with bucket {} object {}", bucketName, objectKey);

            DataSource<byte[]> dataSource = new DataSourceS3(bucketName, objectKey);

            try {
                handler.saveCompanyData(dataSource);
            } catch (IOException e) {
                // TODO: send to dead letter queue instead
                log.error(e.getMessage(), e);
            }
        }
        return "";
    }
}
