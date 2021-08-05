package idv.demo.backend.service;

import idv.demo.backend.model.Company;
import idv.demo.backend.service.CompanyService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
class CompanyServiceTests {

    @Autowired
    private CompanyService companyService;

    @Test
    void findCompanyByName() {
        String name = "DEREK LIU"; //"ADP"
        List<Company> companies = companyService.findCompanyByName(name);
        companies.forEach(c -> {
            log.debug("company: {}, address1: {}", c.getName(), c.getAddress1());
            Assertions.assertEquals(name, c.getName());
        });
    }

    //@Test
    void saveCompanies() {
        String name = "TestComp";
        String address1 = "dev street ops road";
        Company newCompany = Company.builder().name(name).address1(address1).build();
        companyService.saveCompanies(newCompany);

        List<Company> companies = companyService.findCompanyByName(name);
        Assertions.assertEquals(1, companies.size());
        Assertions.assertEquals(name, companies.get(0).getName());
    }

}
