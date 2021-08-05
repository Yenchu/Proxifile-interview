package idv.demo.backend.repository;

import idv.demo.backend.model.Company;

import java.util.List;

public interface CompanyRepository {

    List<Company> findCompanyByName(String name);

    void saveCompanies(Company... companies);

}
