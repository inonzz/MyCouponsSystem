package helper;

import beans.Company;
import beans.Customer;
import dao.CompaniesDAO;
import dao.CouponsDAO;
import dao.CustomersDAO;
import exceptions.MyCouponException;

public class AdminFacadeVerification {
    private CompaniesDAO companiesDAO;
    private CustomersDAO customersDAO;
    private CouponsDAO couponsDAO;
    private boolean isSuccessfullLogin;

    public AdminFacadeVerification(CompaniesDAO companiesDAO, CouponsDAO couponsDAO, CustomersDAO customersDAO, boolean isSuccessfullLogin) {
        this.companiesDAO = companiesDAO;
        this.couponsDAO = couponsDAO;
        this.customersDAO = customersDAO;
        this.isSuccessfullLogin = isSuccessfullLogin;
    }

    // METHODS------------------------------------------------------------------------------
    public void updateCompanyValidation(Company company) throws MyCouponException {

        Company companyDB = companiesDAO.getOneCompany(company.getId());
        if (isSuccessfullLogin == false) {
            throw new MyCouponException(String.format("Input Error: Login was not established.  updateCompanyValidation Failed\n"));
        }
        if (companyDB == null)
            throw new MyCouponException(String.format("Input Error: Company  (id:%d) does not Exist. update Company Failed\n", company.getId()));
        if (!companyDB.getName().equals(company.getName()))
            throw new MyCouponException(String.format("Input Error: Company  (id:%d) new Company Name (%s) can not be changed from (%s). update Company Failed\n",
                    company.getId(), company.getName(), companyDB.getName()));
        if (companyDB.getId() != company.getId())
            throw new MyCouponException(String.format("Input Error: Company  (id:%d) can not be changed to (%s). update Company Failed\n",
                    companyDB.getId(), company.getId()));
    }


    public void addCompanyValidation(Company company) throws MyCouponException {

        if (isSuccessfullLogin == false) {
            throw new MyCouponException(String.format("Input Error: Login was not established.  addCompanyValidation Failed\n"));
        }
        //check if not similar Name,Email exist
        Company companyDB = companiesDAO.getOneCompany(company.getId());
        if ((companyDB != null) && (companyDB.getName() == company.getName())) {
            throw new MyCouponException(
                    String.format("Input Error: Company Name (%s) already exist. Add Company Failed\n",
                            company.getName()));
        }
        if ((companyDB != null) && (companyDB.getEmail() == company.getEmail())) {
            throw new MyCouponException(
                    String.format("Input Error: Company Email (%s) already exist. Add Company Failed\n",
                            company.getEmail()));
        }

    }


    public void addCustomerValidation(Customer customer) throws MyCouponException {

        if (isSuccessfullLogin == false) {
            throw new MyCouponException(String.format("Input Error: Login was not established.  addCustomerValidation Failed\n"));
        }
        //can not add customer with similar email
        Customer customerDB = customersDAO.getOneCustomer(customer.getId());
        if ((customerDB != null) && (customerDB.getEmail() == customer.getEmail())) {
            throw new MyCouponException(
                    String.format("Input Error: Customer email (%s) already exist. Add Customer Failed\n",
                            customerDB.getEmail()));
        }
    }

    public void updateCustomerValidation(Customer customer) throws MyCouponException {

        if (isSuccessfullLogin == false) {
            throw new MyCouponException(String.format("Input Error: Login was not established.  updateCustomerValidation Failed\n"));
        }
        //can not update customer id
        Customer customerDB = customersDAO.getOneCustomer(customer.getId());
        if (customerDB == null)
            throw new MyCouponException(
                    String.format("Input Error: Customer is (%d) does not  exist. Update Customer Failed\n",
                            customerDB.getId()));
    }
}
