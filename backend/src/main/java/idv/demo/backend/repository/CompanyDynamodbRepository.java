package idv.demo.backend.repository;

import idv.demo.backend.model.Company;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.stream.Collectors;

@Profile("prod")
@Component
public class CompanyDynamodbRepository implements CompanyRepository {

    @Value("${REGION:''}")
    private String region;

    @Value("${COMPANY_TABLE_NAME:''}")
    private String tableName;

    private DynamoDbClient ddb;

    private DynamoDbEnhancedClient ddbEnhancedClient;

    private DynamoDbTable<Company> table;

    @PostConstruct
    public void init() {

        Region awsRegion = StringUtils.hasText(region) ? Region.of(region) : Region.AP_NORTHEAST_1;

        ddb = DynamoDbClient.builder()
                .region(awsRegion)
                .build();

        ddbEnhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(ddb).build();

        table = ddbEnhancedClient.table(tableName, TableSchema.fromBean(Company.class));
    }

    @PreDestroy
    public void close() {
        if (ddb != null) {
            ddb.close();
        }
    }

    @Override
    public List<Company> findCompanyByName(String name) {

        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(k -> k.partitionValue(name));

        List<Company> companies = table.query(queryConditional)
                .items().stream().collect(Collectors.toList());

        return companies;
    }

    public void saveCompanies(Company... companies) {

        WriteBatch.Builder<Company> writeBatchBuilder = WriteBatch.builder(Company.class).mappedTableResource(table);

        for (Company company : companies) {
            writeBatchBuilder.addPutItem(company);
        }

        ddbEnhancedClient.batchWriteItem(BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBatchBuilder.build())
                .build());
    }
}
