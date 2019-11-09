package unal.edu.co.busdir.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import unal.edu.co.busdir.dao.CategoryDAO;
import unal.edu.co.busdir.dao.CompanyCategoryDAO;
import unal.edu.co.busdir.dao.CompanyDAO;
import unal.edu.co.busdir.dao.CompanyServiceDAO;
import unal.edu.co.busdir.dao.ServiceDAO;
import unal.edu.co.busdir.model.Category;
import unal.edu.co.busdir.model.Company;
import unal.edu.co.busdir.model.CompanyCategory;
import unal.edu.co.busdir.model.CompanyService;
import unal.edu.co.busdir.model.Service;

@Database( entities = { Category.class, Company.class, CompanyCategory.class, CompanyService.class, Service.class }, version = 1 )
public abstract class AppDatabase extends RoomDatabase
{
    public abstract CategoryDAO categoryDAO( );
    public abstract CompanyCategoryDAO companyCategoryDAO( );
    public abstract CompanyDAO companyDAO( );
    public abstract CompanyServiceDAO companyServiceDAO( );
    public abstract ServiceDAO serviceDAO( );
}