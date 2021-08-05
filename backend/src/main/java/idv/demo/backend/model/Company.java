package idv.demo.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    private String name;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String attention;

    @DynamoDbPartitionKey
    public String getName() {
        return name;
    }

    @DynamoDbSortKey
    public String getAddress1() {
        return address1;
    }

}
