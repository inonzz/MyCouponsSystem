package facadeImpl;

import beans.Category;
import beans.Company;
import beans.Coupon;
import exceptions.MyCouponException;
import facade.ClientFacade;
import helper.CompanyFacadeVerification;

import java.util.ArrayList;
import java.util.List;

public class CompanyFacade extends ClientFacade {
    private int companyId;
    private CompanyFacadeVerification companyFacadeVerification;

    public CompanyFacade(){
        super();
        companyId= -1;
    }
    //Login API ------------------------------------------------------------------------------------------
    @Override
    public boolean login(String email, String password) throws MyCouponException {

        if (companiesDAO.isCompanyExists(email,password)) {
            initSession(companiesDAO.getOneCompanyId(email, password));
            return true;
        }
        return false;
    }

    //Coupon API ------------------------------------------------------------------------------------------
    public void addCoupon(Coupon coupon) throws MyCouponException {
        companyFacadeVerification.addCouponValidation(coupon);
        couponsDAO.addCoupon(coupon);

    }

    public void updateCoupon(Coupon coupon) throws MyCouponException {
        companyFacadeVerification.updateCouponValidation(coupon);
        couponsDAO.updateCoupon(coupon);
    }

    public void deleteCoupon(int couponId) throws MyCouponException {

            companyFacadeVerification.deleteCouponValidation(couponId);
            couponsDAO.deleteAllCouponsPurchasesById(couponId);
            couponsDAO.deleteCoupon(couponId);
        }

    public Company getCompanyDetails() throws MyCouponException {

        companyFacadeVerification.getCompanyDetailsValidation();
        return companiesDAO.getOneCompany(companyId);
    }

    public List<Coupon> getCompanyCoupons() throws MyCouponException {

        companyFacadeVerification.getCompanyCouponsValidation();
        return companiesDAO.getOneCompany(companyId).getCoupons();

    }

    public List<Coupon> getCompanyCoupons(Category category) throws MyCouponException {

        companyFacadeVerification.getCompanyCouponsValidation();

        List<Coupon> coupons = getCompanyCoupons();
        List<Coupon> CategorisedCoupons = new ArrayList<>();
        for (Coupon coupon: coupons)
            if (coupon.getCategory().getId() ==  category.getId())
                CategorisedCoupons.add(coupon);

        return CategorisedCoupons;
    }

    public List<Coupon> getCompanyCoupons(double maxPrice) throws MyCouponException {

        companyFacadeVerification.getCompanyDetailsValidation();

        List<Coupon> coupons = getCompanyCoupons();
        List<Coupon> MaxPricesCoupons = new ArrayList<>();
        for (Coupon coupon: coupons)
            if (coupon.getPrice() <=  maxPrice)
                MaxPricesCoupons.add(coupon);

        return MaxPricesCoupons;
    }

    private void initSession(int companyId){
        this.companyId = companyId;
        this.companyFacadeVerification = new CompanyFacadeVerification(companiesDAO,couponsDAO,
                customersDAO,companyId,true);


    }

}
