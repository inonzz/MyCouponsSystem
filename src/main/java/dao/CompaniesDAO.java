package dao;

import beans.Company;
import exceptions.MyCouponException;

import java.util.List;

public interface CompaniesDAO {

    public boolean isCompanyExists(String email, String password) throws MyCouponException;

    public void addCompany(Company company) throws MyCouponException;

    public void updateCompany(Company company) throws MyCouponException;

    public void deleteCompany(int companyId) throws MyCouponException;

    public List<Company> getAllCompanies() throws MyCouponException;

    public Company getOneCompany(int companyId) throws MyCouponException;

    public int getOneCompanyId(String email,String password) throws MyCouponException;
}
