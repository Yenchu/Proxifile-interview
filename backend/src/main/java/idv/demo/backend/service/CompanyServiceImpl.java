package idv.demo.backend.service;

import idv.demo.backend.model.Company;
import idv.demo.backend.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

  private CompanyRepository companyRepository;

  public CompanyServiceImpl(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
  }

  public List<Company> findCompanyByName(String name) {

    return companyRepository.findCompanyByName(name);
  }

  public void saveCompanies(Company... companies) {

    companyRepository.saveCompanies(companies);
  }
}
