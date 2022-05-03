package daoImpl;

import beans.Coupon;
import beans.Customer;
import dao.CouponsDAO;
import dao.CustomersDAO;
import db.JDBCUtils;
import db.ResultsUtils;
import exceptions.MyCouponException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomersDAODBImpl implements CustomersDAO {


    //QUERIES- customers
    private static final String QUERY_CUSTOMERS_INSERT = "INSERT INTO `coupons_system`.`customers` (`first_name`, `last_name`,`email`, `password`) VALUES (?, ?, ?, ?);\n";

    private static final String QUERY_CUSTOMERS_UPDATE = "UPDATE `coupons_system`.`customers` SET `first_name` = ?, `last_name` = ?,`email` = ?, `password` = ? WHERE (`id` = ?);\n";

    private static final String QUERY_CUSTOMERS_DELETE = "DELETE FROM `coupons_system`.`customers` WHERE (`id` = ?);";

    private static final String QUERY_CUSTOMERS_IS_EXIST_BY_EMAIL_AND_PASSWORD = "SELECT EXISTS(SELECT * FROM `coupons_system`.`customers` where email=? and password=?) as res";

    private static final String QUERY_CUSTOMERS_IDS_BY_EMAIL_AND_PASSWORD = "SELECT `id` FROM `coupons_system`.`customers` where (email=? and password=?) ;";

    private static final String QUERY_ALL_CUSTOMERS_ID = "SELECT `id` FROM `coupons_system`.`customers`;";

    private static final String QUERY_CUSTOMERS_BY_ID = "SELECT * FROM `coupons_system`.`customers` where id=?";

    @Override
    public boolean isCustomerExist(String email, String password) throws MyCouponException {
        boolean isExist = false;

        Map<Integer, Object> map = new HashMap<>();
        map.put(1, email);
        map.put(2, password);


        List<?> res = null;
        try {
            res = JDBCUtils.executeResults(QUERY_CUSTOMERS_IS_EXIST_BY_EMAIL_AND_PASSWORD, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: isCustomerExists Failed", e);
        }
        for (Object row : res) {
            isExist = ResultsUtils.fromHashMapToBoolean((HashMap<Integer, Object>) row);
            break;
        }

        return isExist;
    }

    @Override
    public void addCustomer(Customer customer) throws MyCouponException {
        //add to customer table
        Map<Integer, Object> mapCustomer = new HashMap<>();
        mapCustomer.put(1, customer.getFirstName());
        mapCustomer.put(2, customer.getLastName());
        mapCustomer.put(3, customer.getEmail());
        mapCustomer.put(4, customer.getPassword());
        try {
            JDBCUtils.execute(QUERY_CUSTOMERS_INSERT, mapCustomer);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: addCustomer Failed", e);
        }

        //add customer coupons purchases
        List<Coupon> coupons = customer.getCoupons();
        CouponsDAO couponsDAU = new CouponsDAODBImpl();

        for (Coupon coupon : coupons) {
            couponsDAU.addCouponPurchase(customer.getId(), coupon.getId());
        }
    }

    @Override
    public void updateCustomer(Customer customer) throws MyCouponException {

        Map<Integer, Object> mapCustomer = new HashMap<>();
        mapCustomer.put(1, customer.getFirstName());
        mapCustomer.put(2, customer.getLastName());
        mapCustomer.put(3, customer.getEmail());
        mapCustomer.put(4, customer.getPassword());
        mapCustomer.put(5, customer.getId());

        try {
            JDBCUtils.execute(QUERY_CUSTOMERS_UPDATE, mapCustomer);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: updateCustomer Failed", e);
        }

        //update customer purchases
        List<Coupon> coupons = customer.getCoupons();
        CouponsDAO couponsDAU = new CouponsDAODBImpl();
        int customerId = 0;

        for (Coupon coupon : coupons) {
            customerId = customer.getId();
            if (!couponsDAU.isCouponPurchased(customerId)) {
                couponsDAU.addCouponPurchase(customer.getId(), coupon.getId());
            }
        }
    }

    @Override
    public void deleteCustomer(int customerId) throws MyCouponException {

        Map<Integer, Object> map = new HashMap<>();
        map.put(1, customerId);
        try {
            JDBCUtils.execute(QUERY_CUSTOMERS_DELETE, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: deleteCustomer Failed", e);
        }
    }

    @Override
    public List<Customer> getAllCustomers() throws MyCouponException {
        List<Customer> customers = new ArrayList<>();

        List<?> res = null;
        try {
            res = JDBCUtils.executeResults(QUERY_ALL_CUSTOMERS_ID);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: getAllCustomers Failed", e);
        }

        int customerid = 0;
        for (Object row : res) {
            customerid = (int) ((HashMap<?, ?>) row).get("id");
            customers.add(getOneCustomer(customerid));
        }
        return customers;
    }


    @Override
    public Customer getOneCustomer(int customerId) throws MyCouponException {
        Customer customer = null;

        Map<Integer, Object> map = new HashMap<>();
        map.put(1, customerId);
        List<?> resCustomerTable = null;
        try {
            resCustomerTable = JDBCUtils.executeResults(QUERY_CUSTOMERS_BY_ID, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: getOneCustomer Failed", e);
        }


        CouponsDAO couponsDAO = new CouponsDAODBImpl();

        for (Object rowCustomerTable : resCustomerTable) {
            customer = ResultsUtils.fromHashMapToCustomer((HashMap<Integer, Object>) rowCustomerTable,
                    couponsDAO.getAllCouponsByCustomerId(customerId));

        }
        return customer;
    }

    public int getOneCustomerId(String email, String password) throws MyCouponException {

        int customerId = -1;

        Map<Integer, Object> map = new HashMap<>();
        map.put(1, email);
        map.put(2, password);
        List<?> resCustomerTable = null;
        try {
            resCustomerTable = JDBCUtils.executeResults(QUERY_CUSTOMERS_IDS_BY_EMAIL_AND_PASSWORD, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: getOneCustomerId Failed", e);
        }
        for (Object rowCustomerTable : resCustomerTable) {
            customerId = ResultsUtils.fromHashMapToInt((HashMap<Integer, Object>) rowCustomerTable);
        }
        return customerId;

    }

}
