package dao;

import beans.Customer;
import exceptions.MyCouponException;

import java.util.List;

public interface CustomersDAO {

    public boolean isCustomerExist(String email, String password) throws MyCouponException;

    public void addCustomer(Customer customer) throws MyCouponException;

    public void updateCustomer(Customer customer) throws MyCouponException;

    public void deleteCustomer(int customerId) throws MyCouponException;

    public List<Customer> getAllCustomers() throws MyCouponException;

    public Customer getOneCustomer(int customerId) throws MyCouponException;

     public int getOneCustomerId(String email, String password) throws MyCouponException;;
}
