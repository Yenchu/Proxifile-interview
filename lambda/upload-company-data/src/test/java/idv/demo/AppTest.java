package idv.demo;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AppTest {

    @Test
    public void testParseCompanyData() throws IOException {

        DataSource<byte[]> dataSource = getTestDataSource();

        CompanyHandler handler = new CompanyHandler(null);

        List<Company> companies = handler.transform(dataSource.extract());

        companies.forEach(c -> System.out.println(String.format("%s %s", c.getName(), c.getAddress1())));

        int companyCount = 53;
        assertEquals(companies.size(), companyCount);
    }

    @Test
    public void testSaveCompanies() throws IOException {

        DataSource<byte[]> dataSource = getTestDataSource();

        DataSink<Company> dataSink = new DataSinkDynamodb();

        CompanyHandler handler = new CompanyHandler(dataSink);

        handler.saveCompanyData(dataSource);
    }

    private DataSource<byte[]> getTestDataSource() {

        String file = "/Users/yenchu/git/tech-interview-company-search-andrew/company_test_data.csv";

        return new DataSourceFile(file);
    }
}
