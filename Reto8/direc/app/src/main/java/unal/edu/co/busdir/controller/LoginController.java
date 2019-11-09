package unal.edu.co.busdir.controller;

import android.content.Context;

import unal.edu.co.busdir.model.Company;
import unal.edu.co.busdir.service.CompanyService;

public class LoginController
{
    private CompanyService companyService;

    public LoginController( Context context ){
        companyService = new CompanyService( context );
    }

    public boolean validateLogin( String username, String password ){
        Company company = companyService.findByUsername( username );
        return (company == null) ? false : (password.equals( company.getPassword( ) )) ? true : false;
    }
}