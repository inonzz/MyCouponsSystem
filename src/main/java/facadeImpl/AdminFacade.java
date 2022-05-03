package facadeImpl;

import beans.Company;
import beans.Coupon;
import beans.Customer;
import exceptions.MyCouponException;
import facade.ClientFacade;
import helper.AdminFacadeVerification;

import java.util.List;

public class AdminFacade extends ClientFacade {
    private static final String EMAIL = "admin@admin.com";
    private static final String PASSWORD = "admin";
    private AdminFacadeVerification adminFacadeVerification;

    //Login API ------------------------------------------------------------------------------------------

    public AdminFacade() {
        super();
    }

    @Override
    public boolean login(String email, String password) {
        if (EMAIL.equals(email) && (PASSWORD.equals(password))) {
            initSession();
            return true;
        }
        return false;
    }


    //COMPANY API ------------------------------------------------------------------------------------------
    public void addCompany(Company company) throws MyCouponException {
        adminFacadeVerification.addCompanyValidation(company);
        companiesDAO.addCompany(company);
    }

    public void updateCompany(Company company) throws MyCouponException {
        adminFacadeVerification.updateCompanyValidation(company);
        companiesDAO.updateCompany(company);

    }

    public void deleteCompany(int companyId) throws MyCouponException {
        Company company = companiesDAO.getOneCompany(companyId);

        if (company == null)
            throw new MyCouponException(String.format("Error : Company (id:%d) Does not Exist!\n", company.getId()));

        List<Coupon> coupons = company.getCoupons();

        for (Coupon coupon : coupons) {
            couponsDAO.deleteAllCouponsPurchasesById(coupon.getId());
            couponsDAO.deleteCoupon(coupon.getId());
        }
        companiesDAO.deleteCompany(companyId);

    }

    public List<Company> getAllCompanies() throws MyCouponException {
        return companiesDAO.getAllCompanies();
    }

    public Company getOneCompany(int companyId) throws MyCouponException {
        return companiesDAO.getOneCompany(companyId);
    }

    //CUSTOMER API ------------------------------------------------------------------------------------------
    public void addCustomer(Customer customer) throws MyCouponException {

        adminFacadeVerification.addCustomerValidation(customer);
        customersDAO.addCustomer(customer);
    }

    public void updateCustomer(Customer customer) throws MyCouponException {

        adminFacadeVerification.updateCustomerValidation(customer);
        customersDAO.updateCustomer(customer);
    }

    public void deleteCustomer(int customerId) throws MyCouponException {
        Customer customer = customersDAO.getOneCustomer(customerId);

        if (customer == null)
            throw new MyCouponException(String.format("Input Error : Company (id:%d) Does not Exist!. Delete Customer Failed\n", customer.getId()));

        List<Coupon> coupons = customer.getCoupons();
        for (Coupon coupon : coupons) {
            couponsDAO.deleteCouponPurchase(customerId, coupon.getId());
        }
        customersDAO.deleteCustomer(customerId);
    }

    public List<Customer> getAllCustomers() throws MyCouponException {
        return customersDAO.getAllCustomers();
    }

    public Customer getOneCustomer(int customerId) throws MyCouponException {
        return customersDAO.getOneCustomer(customerId);
    }

    private void initSession() {
        this.adminFacadeVerification = new AdminFacadeVerification(companiesDAO, couponsDAO, customersDAO, true);
}

}
