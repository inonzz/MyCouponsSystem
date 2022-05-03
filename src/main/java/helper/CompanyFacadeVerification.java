package helper;

import beans.Coupon;
import dao.CompaniesDAO;
import dao.CouponsDAO;
import dao.CustomersDAO;
import exceptions.MyCouponException;

public class CompanyFacadeVerification {
    private CompaniesDAO companiesDAO;
    private CustomersDAO customersDAO;
    private CouponsDAO couponsDAO;
    private int companyId;
    private boolean isSuccessfullLogin;

    public CompanyFacadeVerification(CompaniesDAO companiesDAO, CouponsDAO couponsDAO,
                                     CustomersDAO customersDAO, int companyId, boolean isSuccessfullLogin) {
        this.companiesDAO = companiesDAO;
        this.couponsDAO = couponsDAO;
        this.customersDAO = customersDAO;
        this.companyId = companyId;
        this.isSuccessfullLogin = isSuccessfullLogin;
    }

    //PRIVATE METHODS------------------------------------------------------------------------------
    public void addCouponValidation(Coupon coupon) throws MyCouponException {
        if (isSuccessfullLogin == false) {
            throw new MyCouponException(String.format("Input Error: Login was not established.  addCouponValidation Failed\n"));
        }
        if (couponsDAO.isCompanyCouponsTitleExist(companyId, coupon.getTitle())) {
            throw new MyCouponException(String.format("Input Error: Company (id:%d) Coupon title : (%s) Already Exist. Add Coupon Failed\n",
                    companyId, coupon.getTitle()));
        }
    }

    public void updateCouponValidation(Coupon coupon) throws MyCouponException {
        Coupon couponDB = couponsDAO.getOneCoupon(coupon.getId());
        if (isSuccessfullLogin == false) {
            throw new MyCouponException(String.format("Input Error: Login was not established.  updateCouponValidation Failed\n"));
        }
        if (couponDB == null)
            throw new MyCouponException(String.format("Input Error: Coupon  (id:%d) does not Exist. Add Coupon Failed\n", coupon.getId()));
       if (couponDB.getCompanyId() != coupon.getCompanyId())
            throw new MyCouponException(String.format("Input Error: Coupon  (id:%d) Company Id (%d) can not be changed. Add Coupon Failed\n", coupon.getId(), coupon.getCompanyId()));
    }

    public void deleteCouponValidation(int couponId) throws MyCouponException {

        if (isSuccessfullLogin == false) {
            throw new MyCouponException(String.format("Input Error: Login was not established.  deleteCouponValidation Failed\n"));
        }
        Coupon coupon = couponsDAO.getOneCoupon(couponId);
        if (coupon == null)
            throw new MyCouponException(String.format("Input Error : Coupon (id:%d) Does not Exist!.Delete Coupon Failed\n", coupon.getId()));

    }

    public void getCompanyDetailsValidation() throws MyCouponException {
        if (isSuccessfullLogin == false) {
            throw new MyCouponException(String.format("Input Error: Login was not established.  getCompanyDetailsValidation Failed\n"));
        }
    }

    public void getCompanyCouponsValidation() throws MyCouponException {
        if (isSuccessfullLogin == false) {
            throw new MyCouponException(String.format("Input Error: Login was not established.  getCompanyCouponsValidation Failed\n"));
        }
    }
}
