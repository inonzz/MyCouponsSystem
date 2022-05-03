package facadeImpl;

import beans.Category;
import beans.Coupon;
import beans.Customer;
import exceptions.MyCouponException;
import facade.ClientFacade;
import helper.CustomerFacadeVerification;

import java.util.ArrayList;
import java.util.List;

public class CustomerFacade extends ClientFacade {
    private int customerId;
    private CustomerFacadeVerification customerFacadeVerification;

    public CustomerFacade() {
        super();
        customerId = -1;

    }

    @Override
    public boolean login(String email, String password) throws MyCouponException {
        boolean isSuccessfullLogin = false;
        if (customersDAO.isCustomerExist(email,password)) {
            isSuccessfullLogin = true;
            initSession(customersDAO.getOneCustomerId(email, password));

        }
        return isSuccessfullLogin;
    }

    public void purchaseCoupon(Coupon coupon) throws MyCouponException {


        customerFacadeVerification.purchaseCouponValidation(coupon);
        couponsDAO.addCouponPurchase(customerId,coupon.getId());
        //update coupon amount
        coupon.setAmount(coupon.getAmount()-1);
        try {
            couponsDAO.updateCoupon(coupon);
        } catch (MyCouponException e) {
            couponsDAO.deleteCouponPurchase(customerId,coupon.getId());
            throw new MyCouponException("Repository Error: Update coupon Failed after purchase.purchase rollback",e);
        }
    }

    public List<Coupon> getCustomerCoupons() throws MyCouponException {
        customerFacadeVerification.getCustomerCouponsValidation();
        return customersDAO.getOneCustomer(customerId).getCoupons();
    }

    public List<Coupon> getCustomerCoupons(Category category) throws MyCouponException {
        customerFacadeVerification.getCustomerCouponsValidation();
        List<Coupon> coupons = getCustomerCoupons();
        List<Coupon> CategorisedCoupons = new ArrayList<>();
        for (Coupon coupon: coupons)
            if (coupon.getCategory().getId() ==  category.getId())
                CategorisedCoupons.add(coupon);

        return CategorisedCoupons;
    }

    public List<Coupon> getCustomerCoupons(double maxPrice) throws MyCouponException {
        customerFacadeVerification.getCustomerCouponsValidation();
        List<Coupon> coupons = getCustomerCoupons();
        List<Coupon> MaxPricesCoupons = new ArrayList<>();
        for (Coupon coupon: coupons)
            if (coupon.getPrice() <=  maxPrice)
                MaxPricesCoupons.add(coupon);

        return MaxPricesCoupons;
    }
    public Customer getCustomerDetails() throws MyCouponException {
        customerFacadeVerification.getCustomerDetailsValidation();
        return customersDAO.getOneCustomer(customerId);
    }

    public List<Coupon> getAllAvailableCoupons() throws MyCouponException {
        customerFacadeVerification.getAllAvailableCouponsValidation();
        return couponsDAO.getAllAvailableCoupons();
    }

    private void initSession(int customerId){
        this.customerId = customerId;
        this.customerFacadeVerification = new CustomerFacadeVerification(companiesDAO,couponsDAO,
                customersDAO,customerId,true);
    }



}
