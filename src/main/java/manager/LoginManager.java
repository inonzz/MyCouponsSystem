package manager;

import beans.ClientType;
import exceptions.MyCouponException;
import facade.ClientFacade;
import facadeImpl.AdminFacade;
import facadeImpl.CompanyFacade;
import facadeImpl.CustomerFacade;
import facadeImpl.SystemMaintenanceFacade;

public class LoginManager {

    private static LoginManager instance = new LoginManager();

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        return instance;
    }

    public ClientFacade login(String email, String password, ClientType clientType) throws MyCouponException {
        ClientFacade clientFacade;

        switch (clientType) {
            case ADMINISTRATOR:
                clientFacade = new AdminFacade();
                break;

            case COMPANY:
                clientFacade = new CompanyFacade();
                break;

            case CUSTOMER:
                clientFacade = new CustomerFacade();
                break;

            case SYSTEM_MAINTENANCE:
                clientFacade = new SystemMaintenanceFacade();
                break;
            default:
                return null;

        }
        if (clientFacade.login(email, password))
            return clientFacade;
        return null;
    }
    public  ClientFacade loginWrapper(LoginManager loginManager,String email, String password, ClientType clientType ) throws MyCouponException {
        ClientFacade clientFacade= null;
        try {
            clientFacade = loginManager.login( email, password,clientType);
        } catch (MyCouponException e) {
            e.printStackTrace();
        }
        if (clientFacade == null)
            throw new MyCouponException(String.format("Error Login(%s)  failed\n",clientType.name()));
        return clientFacade;
    }

}





