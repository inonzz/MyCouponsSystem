package dao;

import beans.Coupon;
import exceptions.MyCouponException;

import java.util.List;

public interface CouponsDAO {

    public void addCoupon(Coupon coupon) throws MyCouponException;

    public void updateCoupon(Coupon coupon) throws MyCouponException;

    public void deleteCoupon(int couponId) throws MyCouponException;

    public List<Coupon> getAllCoupons() throws MyCouponException;

    public Coupon getOneCoupon(int CouponId) throws MyCouponException;

    public void addCouponPurchase(int CustomerId, int CouponId) throws MyCouponException;

    public void deleteCouponPurchase(int CustomerId, int CouponId) throws MyCouponException;

//Additional  Methods
    public boolean isCouponPurchased(int couponId) throws MyCouponException;

    public List<Coupon> getAllCouponsByCompanyId(int companyId) throws MyCouponException;

    List<Coupon> getAllCouponsByCustomerId(int customerId) throws MyCouponException;

    public void deleteAllCouponsPurchasesById(int coupon_id) throws MyCouponException;

    public boolean isCompanyCouponsTitleExist(int companyId, String couponTitle) throws MyCouponException;

    public List<Coupon> getAllExpiredCoupons() throws MyCouponException;

    List<Coupon> getAllAvailableCoupons() throws MyCouponException;;
}
