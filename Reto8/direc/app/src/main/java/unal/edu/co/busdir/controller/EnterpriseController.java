package unal.edu.co.busdir.controller;

import android.content.Context;

import unal.edu.co.busdir.model.Company;
import unal.edu.co.busdir.service.CompanyCategoryService;
import unal.edu.co.busdir.service.CompanyService;
import unal.edu.co.busdir.service.CompanyServiceService;

public class EnterpriseController
{
    private CompanyService companyService;
    private CompanyServiceService companyServiceService;
    private CompanyCategoryService companyCategoryService;

    public EnterpriseController( Context context ){
        companyService = new CompanyService( context );
        companyServiceService = new CompanyServiceService( context );
        companyCategoryService = new CompanyCategoryService( context );
    }

    public Company getCompany( String username ){
        return companyService.findByUsername( username );
    }

    public void delete( Company company ){
        companyCategoryService.delete( companyCategoryService.findByCompany( company ) );
        companyServiceService.delete( companyServiceService.findByCompany( company ) );
        companyService.delete( company );
    }
}
