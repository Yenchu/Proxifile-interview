package idv.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CompanyHandler {

    private static final Logger log = LogManager.getLogger(CompanyHandler.class);

    private static final String DELIMITER = ",";

    private DataSink dataSink;

    public CompanyHandler(DataSink dataSink) {
        this.dataSink = dataSink;
    }

    public List<Company> saveCompanyData(DataSource<byte[]> dataSource) throws IOException {

        byte[] data = dataSource.extract();

        List<Company> companies = transform(data);

        dataSink.load(companies);

        return companies;
    }

    public List<Company> transform(byte[] data) throws IOException {

        List<Company> companies;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)))) {

            companies = br.lines().skip(1).map(toCompany).filter(c -> c != null).collect(Collectors.toList());

        }
        return companies;
    }

    private Function<String, Company> toCompany = (line) -> {

        String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        if (values.length < 7) {
            throw new IllegalArgumentException("line format is incorrect: " + line);
        }
        //log.info("line : {}", line);

        String name = values[0].replaceAll("^\"|\"$", "");
        Company company = new Company();
        company.setName(name);
        company.setAttention(values[1]);
        company.setAddress1(values[2]);
        company.setAddress2(values[3]);
        company.setCity(values[4]);
        company.setState(values[5]);
        company.setZip(values[6]);
        return company;
    };
}
