package helper;

import beans.Coupon;
import beans.Customer;
import dao.CompaniesDAO;
import dao.CouponsDAO;
import dao.CustomersDAO;
import exceptions.MyCouponException;

import java.sql.Date;
import java.time.LocalDate;

public class CustomerFacadeVerification {
    private CompaniesDAO  companiesDAO;
    private CustomersDAO customersDAO;
    private CouponsDAO    couponsDAO;
    private int  customerId;
    private boolean isSuccessfullLogin;

    public CustomerFacadeVerification(CompaniesDAO companiesDAO, CouponsDAO couponsDAO,
                                      CustomersDAO customersDAO, int customerId, boolean isSuccessfullLogin) {
        this.companiesDAO = companiesDAO;
        this.couponsDAO = couponsDAO;
        this.customersDAO = customersDAO;
        this.customerId = customerId;
        this.isSuccessfullLogin = isSuccessfullLogin;

    }

    public void purchaseCouponValidation(Coupon coupon) throws MyCouponException {

        Customer customerDB;

        if (isSuccessfullLogin == false) {
            throw new MyCouponException(String.format("Input Error: Login was not established.  purchaseCouponValidation Failed\n"));
        }
        if (coupon == null ){
            throw new MyCouponException(String.format("Input Error: coupon is empty  purchase Coupon Failed\n"));
        }//can not purchase coupon that its amount is zero
        else if (coupon.getAmount() == 0){
            throw new MyCouponException(String.format("Input Error: coupon (id:%d) Already Exist. purchaseCouponValidation Failed\n",
                    coupon.getId()));
        } //can not purchase if its expired
        else if (coupon.getEndDate().compareTo(Date.valueOf(LocalDate.now())) < 0){
            throw new MyCouponException(String.format("Input Error: coupon (id:%d) Already Expired(%s). purchaseCouponValidation Failed\n",
                    coupon.getId(),coupon.getEndDate().toString()));
        }//can not purchase if already purchased
        else if ((customerDB = customersDAO.getOneCustomer(customerId)) != null )
        {
            for(Coupon couponDB: customerDB.getCoupons()){
                if (couponDB.getId() == coupon.getId()){
                    throw new MyCouponException(String.format("Input Error: coupon (id:%d) Already purchase. purchaseCouponValidation Failed\n",
                            coupon.getId()));
                }

            }
        }
    }

    public void getCustomerCouponsValidation() throws MyCouponException {
        if (isSuccessfullLogin == false) {
            throw new MyCouponException(String.format("Input Error: Login was not established.  getCustomerCouponsValidation Failed\n"));
        }
    }

    public void getCustomerDetailsValidation()throws MyCouponException {
        if (isSuccessfullLogin == false) {
            throw new MyCouponException(String.format("Input Error: Login was not established.  getCustomerDetailsValidation Failed\n"));
        }
    }

    public void getAllAvailableCouponsValidation() throws MyCouponException {
        if (isSuccessfullLogin == false) {
            throw new MyCouponException(String.format("Input Error: Login was not established.  getAllAvailableCouponsValidation Failed\n"));
        }
    }
}
