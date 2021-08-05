package idv.demo.backend.service;

import idv.demo.backend.model.Company;

import java.util.List;

public interface CompanyService {

    List<Company> findCompanyByName(String name);

    void saveCompanies(Company... companies);
}
