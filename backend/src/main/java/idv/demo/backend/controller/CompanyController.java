package idv.demo.backend.controller;

import idv.demo.backend.model.Company;
import idv.demo.backend.service.CompanyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companies/{name}")
    public List<Company> findCompanyByName(@PathVariable("name") String name) {
        return companyService.findCompanyByName(name);
    }
}
