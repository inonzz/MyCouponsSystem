package task;

import beans.ClientType;
import exceptions.MyCouponException;
import facadeImpl.SystemMaintenanceFacade;
import manager.LoginManager;

public class CouponExpirationDailyJob implements Runnable {

    private boolean quit = false;
    private static final String EMAIL = "SystemMaintenance@system.com";
    private static final String PASSWORD = "SystemMaintenance";

    @Override
    public void run() {
        //need to run it after we did initSetting in the main method
        LoginManager loginManager = LoginManager.getInstance();
        SystemMaintenanceFacade systemMaintenanceFacade = null;
        try {
            System.out.println("Start Login to SystemMaintenance\n");
            systemMaintenanceFacade = (SystemMaintenanceFacade) loginManager.loginWrapper(loginManager, EMAIL,
                    PASSWORD, ClientType.SYSTEM_MAINTENANCE);
        } catch (MyCouponException e) {
            e.printStackTrace();
        }
        while (!quit) {

            try {
                System.out.println("Search for Expired coupons\n");
                systemMaintenanceFacade.deleteExpiredCoupons();
                Thread.sleep(1000 * 86_400);
            } catch (MyCouponException | InterruptedException e) {
                System.out.println("CouponExpirationDailyJob thrown >> exiting\n info: " + e.getMessage());
                ;
            }
        }
    }

    public void stop() {
        quit = true;
    }

}



