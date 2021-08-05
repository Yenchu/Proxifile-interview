package idv.demo.backend.repository;

import idv.demo.backend.model.Company;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Component
public class MockRepository implements CompanyRepository {

    public List<Company> findCompanyByName(String name) {
        return new ArrayList<>();
    }

    public void saveCompanies(Company... companies) {

    }
}
