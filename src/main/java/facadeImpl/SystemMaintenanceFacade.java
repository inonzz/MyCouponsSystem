package facadeImpl;

import beans.Coupon;
import exceptions.MyCouponException;
import facade.ClientFacade;

import java.util.List;

public class SystemMaintenanceFacade extends ClientFacade {

    private static final String EMAIL = "SystemMaintenance@system.com";
    private static final String PASSWORD = "SystemMaintenance";
    private boolean isLogin;

    public SystemMaintenanceFacade() {
        super();
        isLogin = false;
    }

    @Override
    public boolean login(String email, String password) throws MyCouponException {
        if (EMAIL.equals(email) && (PASSWORD.equals(password))) {
            initSession();
            return true;
        }
        return false;
    }


    public void deleteExpiredCoupons() throws MyCouponException {
        if (!isLogin) {
            throw new MyCouponException(String.format("Input Error: Login was not established.  deleteExpiredCoupons Failed\n"));
        }
        List<Coupon> coupons = couponsDAO.getAllExpiredCoupons();
        for (Coupon coupon : coupons) {
            couponsDAO.deleteAllCouponsPurchasesById(coupon.getId());
            couponsDAO.deleteCoupon(coupon.getId());
        }

    }

    private void initSession(){
        isLogin = true;
   }

}

