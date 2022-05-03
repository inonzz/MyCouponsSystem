package db;

import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kobis on 21 Mar, 2022
 */
public class ResultsUtils {

    public static Company fromHashMapToCompany(HashMap<Integer, Object> map, List<?> CouponsPerCompany) {
        int id = (int) map.get("id");
        String name = (String) map.get("name");
        String email = (String) (map.get("email"));
        String password = (String) (map.get("password"));
        List<Coupon> coupons = (ArrayList<Coupon>) CouponsPerCompany;


        Company company = new Company(id, name, email, password, coupons);
        return company;
    }

    public static boolean fromHashMapToBoolean(HashMap<Integer, Object> map) {
        long res = (long) map.get("res");
        return (res == 1);
    }

    public static int fromHashMapToInt(HashMap<Integer, Object> map) {
        int res = (int) map.get("id");
        return res;
    }

    public static int fromHashMapToCouponId(HashMap<Integer, Object> map, String key) {
        int id = (int) map.get(key);
        return id;
    }

    public static double fromHashMapToDouble(HashMap<Integer, Object> map) {
        double res = (double) map.get("res");
        return res;
    }

    public static String fromHashMapToString(HashMap<Integer, Object> map) {

        String res = (String) map.get("res");

        return res;
    }

    public static Coupon fromHashMapToCoupon(HashMap<Integer, Object> map) {

        int id = (int) map.get("id");
        int companyId = (int) map.get("company_id");
        Category category = Category.getCategory((int) map.get("category_id"));
        String title = (String) map.get("title");
        String description = (String) map.get("description");
        Date startDate = (Date) map.get("start_date");
        Date endDate = (Date) map.get("end_date");
        int amount = (int) map.get("amount");
        double price = (double) map.get("price");
        String image = (String) map.get("image");

        Coupon coupon = new Coupon(id, companyId, category, title,
                description, startDate, endDate, amount, price, image);

        return coupon;
    }

    public static Customer fromHashMapToCustomer(HashMap<Integer, Object> mapCustomer, List<?> mapCouponsPerCustomer) {

        int id = (int) mapCustomer.get("id");
        String firstName = (String) mapCustomer.get("first_name");
        String lastName = (String) mapCustomer.get("last_name");
        String email = (String) mapCustomer.get("email");
        String password = (String) mapCustomer.get("password");
        List<Coupon> coupons = (ArrayList<Coupon>) mapCouponsPerCustomer;

        Customer customer = new Customer(id, firstName, lastName, email, password, coupons);

        return customer;

    }

}
