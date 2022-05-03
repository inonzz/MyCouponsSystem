package facade;

import dao.CompaniesDAO;
import dao.CouponsDAO;
import dao.CustomersDAO;
import daoImpl.CompaniesDAODBImpl;
import daoImpl.CouponsDAODBImpl;
import daoImpl.CustomersDAODBImpl;
import exceptions.MyCouponException;

public abstract class ClientFacade {

    protected CompaniesDAO companiesDAO;
    protected CustomersDAO customersDAO;
    protected CouponsDAO couponsDAO;

    public abstract boolean login(String email, String password) throws MyCouponException;

    public ClientFacade() {
        companiesDAO = new CompaniesDAODBImpl();
        couponsDAO = new CouponsDAODBImpl();
        customersDAO = new CustomersDAODBImpl();
    }
}
