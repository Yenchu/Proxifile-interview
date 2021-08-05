package idv.demo;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.HashMap;
import java.util.Map;

public class DataSinkDynamodb implements DataSink<Company> {

    private static final String TABLE_NAME = System.getenv("TABLE_NAME");

    private static final DynamoDbClient ddb = DynamoDbClient.create();

    public void load(Company... companies) {

        for (Company c : companies) {
            Map<String, AttributeValue> item = new HashMap<>();

            item.put("name", AttributeValue.builder()
                    .s(c.getName())
                    .build());

            item.put("address1", AttributeValue.builder()
                    .s(c.getAddress1())
                    .build());

            if (c.getAddress2() != null && c.getAddress2().length() > 0) {
                item.put("address2", AttributeValue.builder()
                        .s(c.getAddress2())
                        .build());
            }
            if (c.getCity() != null && c.getCity().length() > 0) {
                item.put("city", AttributeValue.builder()
                        .s(c.getCity())
                        .build());
            }
            if (c.getState() != null && c.getState().length() > 0) {
                item.put("state", AttributeValue.builder()
                        .s(c.getState())
                        .build());
            }
            if (c.getZip() != null && c.getZip().length() > 0) {
                item.put("zip", AttributeValue.builder()
                        .s(c.getZip())
                        .build());
            }
            if (c.getAttention() != null && c.getAttention().length() > 0) {
                item.put("attention", AttributeValue.builder()
                        .s(c.getAttention())
                        .build());
            }

            PutItemResponse putItemResponse = ddb.putItem(PutItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .item(item)
                    .build());
        }
    }
}
