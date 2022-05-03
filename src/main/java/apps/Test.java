package apps;

import beans.*;
import dao.CategoriesDAO;
import daoImpl.CategoriesDAODBImpl;
import db.ConnectionPool;
import db.DatabaseManager;
import exceptions.MyCouponException;
import facadeImpl.AdminFacade;
import facadeImpl.CompanyFacade;
import facadeImpl.CustomerFacade;
import manager.LoginManager;
import task.CouponExpirationDailyJob;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Test {
    private static final String ADMIN_CLIENT_EMAIL = "admin@admin.com";
    private static final String ADMIN_CLIENT_PASSWORD = "admin";

    private static final String COMPANY_EMAIL = "Company1@gmail.com";
    private static final String COMPANY_PASSWORD = "1234";

    private static final String CUSTOMER_EMAIL = "Customer1@gmail.com";
    private static final String CUSTOMER_PASSWORD = "1234";


    public static void main(String[] args) {
            TestAll();
    }

    public static void TestAll()  {
        //0. init setting
        System.out.println("\ninitSettings Start -------------------------------\n");
        try {
            initSettings();
        } catch (MyCouponException e) {
            System.out.println("Error: "+e.getMessage()+"\ninitSettings failed. closing application\n");
            return;
        }
        System.out.println("\ninitSettings Pass --------------------------------\n");

        //1. Activate DailyJob
        System.out.println("\nCouponExpirationDailyJob start -------------------------------\n");
        CouponExpirationDailyJob couponExpirationDailyJob = new CouponExpirationDailyJob();
        Thread th = new Thread(couponExpirationDailyJob);
        th.start();
        try{
        //2.Connect to Login Manager  via Admin and Test all its business methods
        LoginManager loginManager = LoginManager.getInstance();
        System.out.println("\ntest AdminFacade start -------------------------------\n");
        AdminFacade adminFacade = (AdminFacade) loginManager.loginWrapper(loginManager, ADMIN_CLIENT_EMAIL,
                ADMIN_CLIENT_PASSWORD, ClientType.ADMINISTRATOR);
        testCompanyAPIinAdminFacade(adminFacade);
        testCustomerAPIinAdminFacade(adminFacade);
        System.out.println("\ntest AdminFacade Pass -------------------------------\n");

        //3.Connect to Login Manager  via Company and Test all its business methods
        System.out.println("\ntest companyFacade start -------------------------------\n");
        CompanyFacade companyFacade = (CompanyFacade) loginManager.loginWrapper(loginManager, COMPANY_EMAIL,
                COMPANY_PASSWORD, ClientType.COMPANY);
        testCouponAPIinCompanyFacade(companyFacade);
        testCompanyAPIinCompanyFacade(companyFacade);
        System.out.println("\ntest companyFacade PASS -------------------------------\n");

        //4.Connect to Login Manager  via Customer and Test all its business methods
        System.out.println("\ntest customerFacade start -------------------------------\n");
        CustomerFacade customerFacade = (CustomerFacade) loginManager.loginWrapper(loginManager, CUSTOMER_EMAIL,
                CUSTOMER_PASSWORD, ClientType.CUSTOMER);
        testCouponAPIinCustomerFacade(customerFacade);
        System.out.println("\ntest customerFacade Pass -------------------------------\n");

        System.out.println("\nspecial testing start ---------------------------------\n");
        testSpecialCases(companyFacade, adminFacade, customerFacade);
        System.out.println("\nspecial testing Pass ---------------------------------\n");
        } catch (MyCouponException e) {
            System.out.println("Error: "+e.getMessage()+"\nTesting failed. closing application\n");

        }
        //5.Stop DailyJob
        System.out.println("\nStop Daily job start ---------------------------------\n");
        couponExpirationDailyJob.stop();
        th.interrupt();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("main thread after sleep : " + e.getMessage());
            // e.printStackTrace();
        }
        System.out.println("\nStop Daily job end ---------------------------------\n");

        //6.Close All resources
        System.out.println("\ncloseSettings start -------------------------------\n");
        try {
            closeSettings();
        } catch (MyCouponException e) {
            System.out.println("Error: "+e.getMessage()+"\nClosing Setting failed. closing application\n");
            return;
        }
        System.out.println("\ncloseSettings Pass -------------------------------\n");
    }


    public static void testSpecialCases(CompanyFacade companyFacade,
                                        AdminFacade adminFacade, CustomerFacade customerFacade) throws MyCouponException {

        System.out.println("Test if deletion of noe compnay deletes all coupons links\n");
        Company company = companyFacade.getCompanyDetails();

        System.out.println("\n Before: " + company);
        System.out.println("\n relevant customer status: " + customerFacade.getCustomerDetails());
        adminFacade.deleteCompany(company.getId());
        System.out.println("\n After Deletion: " + companyFacade.getCompanyDetails());
        System.out.println("\n relevant customer status: " + customerFacade.getCustomerDetails());
    }

    public static void initSettings() throws MyCouponException {

        //1. connect to the DB
        //2. set Category Table
        try {
            DatabaseManager.databaseStrategy();
            initCategories();

        } catch (Exception e) {
            throw new MyCouponException("Error : can not connect to the DB or init Category Table \n", e);

        }

    }

    public static void closeSettings() throws MyCouponException {

        //1. close all connection to the DB
        try {
            ConnectionPool.getInstance().closeAllConnections();

        } catch (Exception e) {
            throw new MyCouponException("Error : can not close all DB connection. closeSettings failed  \n", e);

        }

    }

    private static void initCategories() throws Exception {

        CategoriesDAO categoriesDAO = new CategoriesDAODBImpl();
        categoriesDAO.addCategory(Category.FOOD);
        categoriesDAO.addCategory(Category.ELECTRICITY);
        categoriesDAO.addCategory(Category.RESTAURANT);
        categoriesDAO.addCategory(Category.VACATION);
        //System.out.println();
    }


    public static void testCustomerAPIinAdminFacade(AdminFacade adminFacade) throws MyCouponException {

        //1.AddCustomer
        System.out.println("Add Customer start\n");
        Customer customer1 = new Customer("CustName1", "CustLast1", "Customer1@gmail.com",
                "1234", new ArrayList<Coupon>());
        Customer customer2 = new Customer("CustName2", "CustLast2", "Customer2@gmail.com",
                "5678", new ArrayList<Coupon>());
        Customer customer3 = new Customer("CustName3", "CustLast3", "Customer3@gmail.com",
                "9012", new ArrayList<Coupon>());
        Customer customer4 = new Customer("CustName4", "CustLast4", "Customer4@gmail.com",
                "0000", new ArrayList<Coupon>());
        System.out.println("Bean:" + customer1 + "\n");
        System.out.println("Bean:" + customer2 + "\n");
        System.out.println("Bean:" + customer3 + "\n");
        ;
        System.out.println("Bean:" + customer4 + "\n");
        //add Customers to DB
        adminFacade.addCustomer(customer1);
        adminFacade.addCustomer(customer2);
        adminFacade.addCustomer(customer3);
        adminFacade.addCustomer(customer4);

        List<Customer> customers = adminFacade.getAllCustomers();
        int NumOfCustomers = customers.size();
        System.out.println("Number of customers created: " + NumOfCustomers + "\n");
        customers.forEach(System.out::println);
        System.out.println("Add customer pass\n");

        //2.update customer
        System.out.println("update customer start\n");
        Customer customerDB = customers.get(3);
        System.out.println("customer before update: " + customerDB + "\n");
        customerDB.setPassword("8888");
        System.out.println("customer after update: " + customerDB + "\n");
        adminFacade.updateCustomer(customerDB);
        customerDB = adminFacade.getOneCustomer(customerDB.getId());
        System.out.println("customerDB After update:" + customerDB + "\n");
        System.out.println("update customer pass\n");

        //3.delete Company
        System.out.println("delete customer start\n");
        System.out.println("before delete customer list is:\n"+adminFacade.getAllCustomers());
        adminFacade.deleteCustomer(customerDB.getId());
        System.out.println("after delete customer list is:\n"+adminFacade.getAllCustomers());;
    }

    public static void testCompanyAPIinAdminFacade(AdminFacade adminFacade) throws MyCouponException {


        //1.AddCompany
        System.out.println("Add company start\n");
        Company company1 = new Company("Company1", "Company1@gmail.com", "1234", new ArrayList<Coupon>());
        Company company2 = new Company("Company2", "company2@gmail.com", "5678", new ArrayList<Coupon>());
        Company company3 = new Company("Company3", "company3@gmail.com", "9012", new ArrayList<Coupon>());
        Company company4 = new Company("Company4", "company4@gmail.com", "0000", new ArrayList<Coupon>());
        System.out.println("Bean" + company1 + "\n");
        System.out.println("Bean" + company2 + "\n");
        System.out.println("Bean" + company3 + "\n");
        System.out.println("Bean" + company4 + "\n");
        //add Companies to DB
        adminFacade.addCompany(company1);
        adminFacade.addCompany(company2);
        adminFacade.addCompany(company3);
        adminFacade.addCompany(company4);

        List<Company> companies = adminFacade.getAllCompanies();
        int NumOfCompanies = companies.size();
        System.out.println("Number of companies created: " + NumOfCompanies + "\n");
        companies.forEach(System.out::println);
        System.out.println("Add company pass\n");

        //2.update company
        System.out.println("update company start\n");
        Company companyDB = companies.get(3);
        System.out.println("Company before update: " + companyDB + "\n");
        companyDB.setPassword("8888");
        System.out.println("Company after update: " + companyDB + "\n");
        adminFacade.updateCompany(companyDB);
        companyDB = adminFacade.getOneCompany(companyDB.getId());
        System.out.println("CompanyDB After update:" + companyDB + "\n");
        System.out.println("update company pass\n");

        //3.delete Company
        System.out.println("delete company start\n");
        System.out.println("before Delete Company. Company list is:\n"+adminFacade.getAllCompanies());
        adminFacade.deleteCompany(companyDB.getId());
        List<Company> companiesAfterDelete = adminFacade.getAllCompanies();
        System.out.println("after Delete Company. Company list is:\n"+adminFacade.getAllCompanies());;
        System.out.println(String.format("delete company : %b\n", (NumOfCompanies - 1 == companiesAfterDelete.size()) ? "pass" : "fail"));



    }

    private static void testCouponAPIinCompanyFacade(CompanyFacade companyFacade) throws MyCouponException {

        //AddCoupon
        Company company = companyFacade.getCompanyDetails();
        if (company == null){
            throw new MyCouponException("Error: Company  does not exist.getCompanyDetails failed\n");
        }

        int CompanyId = companyFacade.getCompanyDetails().getId();
        System.out.println("AddCoupon start");
        Coupon coupon1Comp1 = new Coupon(CompanyId, Category.FOOD, "PizzaDalal", "Good Pizza place", Date.valueOf("2022-04-25"), Date.valueOf("2022-04-28"), 1, 10, "*****");
        Coupon coupon2Comp1 = new Coupon(CompanyId, Category.VACATION, "ScubaDalal", "Scuba Dive", Date.valueOf("2022-04-23"), Date.valueOf("2022-04-24"), 0, 20, "*****");
        Coupon coupon3Comp1 = new Coupon(CompanyId, Category.ELECTRICITY, "CompDalal", "computer place", Date.valueOf("2022-04-25"), Date.valueOf("2022-04-28"), 10, 20, "****");

        System.out.println("bean:" + coupon1Comp1);
        System.out.println("bean:" + coupon2Comp1);
        System.out.println("bean:" + coupon3Comp1);


        //add Coupon to DB
        companyFacade.addCoupon(coupon1Comp1);
        companyFacade.addCoupon(coupon2Comp1);
        companyFacade.addCoupon(coupon3Comp1);


        List<Coupon> coupons = companyFacade.getCompanyCoupons();
        if (coupons == null){
            throw new MyCouponException("Error: Company  has invalid Coupon List .ggetCompanyCoupons failed\n");
        }
        int numOfCoupons = coupons.size();
        System.out.println("Number of Coupons created: " + numOfCoupons + "\n");
        coupons.forEach(System.out::println);
        System.out.println("Add Coupon pass\n");

        //2.update Coupon
        System.out.println("update Coupon start\n");
        if (coupons.size() == 0){
                throw new MyCouponException("Error: Company  has no Coupons  failed\n");
        }
        Coupon couponDB = coupons.get(0);
        System.out.println("Coupon before update: " + couponDB + "\n");
        couponDB.setAmount(100);
        System.out.println("Coupon after update: " + couponDB + "\n");
        companyFacade.updateCoupon(couponDB);
        List<Coupon> updatedCoupons = companyFacade.getCompanyCoupons();
        if ((updatedCoupons == null) || (updatedCoupons.size() < 3))
            throw new MyCouponException("Error: updatedCoupons  has no Coupons  failed\n");
        System.out.println("CouponDB After update:" + updatedCoupons.get(0) + "\n");
        System.out.println("update Coupon pass\n");

        //3.delete Company
        System.out.println("delete Coupon start\n");
        System.out.println("before delete Coupon .Company Coupon List is+\n"+companyFacade.getCompanyCoupons());
        companyFacade.deleteCoupon(updatedCoupons.get(2).getId());
        System.out.println("After delete Coupon .Company Coupon List is+\n"+companyFacade.getCompanyCoupons());


    }

    private static void testCompanyAPIinCompanyFacade(CompanyFacade companyFacade) throws MyCouponException {

        //getCompanyDetails

        System.out.println(companyFacade.getCompanyDetails());
        System.out.println("getCompanyDetails Pass\n");

        //getCompanyCoupons Category
        System.out.println(companyFacade.getCompanyCoupons(Category.FOOD));
        System.out.println("getCompanyCoupons-Category FOOD Pass\n");

        //getCompanyCoupons max Price
        int maxPrice = 10;
        System.out.println("getCompanyCoupons-Maxprice " + maxPrice + "\n");
        System.out.println(companyFacade.getCompanyCoupons(maxPrice));
        System.out.println("testCompanyAPIinCompanyFacade : Pass\n");
        //getCompanyCoupons

        List<Coupon> coupons = companyFacade.getCompanyCoupons();
        System.out.println(companyFacade.getCompanyCoupons());
        System.out.println("getCompanyCoupons Pass\n");


    }

    private static void testCouponAPIinCustomerFacade(CustomerFacade customerFacade) throws MyCouponException {

        List<Coupon> coupons = customerFacade.getAllAvailableCoupons();
        System.out.println("Availabled coupons:\n" + coupons + "\ngetCustomerCoupons: Pass\n");
        //purchaseCoupon
        for (Coupon coupon : coupons) {
            customerFacade.purchaseCoupon(coupon);
        }
        //getcustomerCoupons

        System.out.println(customerFacade.getCustomerCoupons());
        System.out.println("purchaseCoupon + getCustomerCoupons: Pass\n");

        //getcustomerDetails

        System.out.println(customerFacade.getCustomerDetails());
        System.out.println("getCustomerDetails : Pass\n");


        //getcustomerCoupons Category
        System.out.println(customerFacade.getCustomerCoupons(Category.FOOD));
        System.out.println("getCustomerCoupons-Category FOOD: Pass\n");
        //getcustomerCoupons max Price
        int maxPrice = 10;
        System.out.println("getCustomerCoupons-Maxprice " + maxPrice + "\n");
        System.out.println(customerFacade.getCustomerCoupons(maxPrice));
        System.out.println("getCustomerCoupons-Maxprice Pass\n");
    }


}
