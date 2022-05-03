package daoImpl;


import beans.Coupon;
import dao.CouponsDAO;
import db.JDBCUtils;
import db.ResultsUtils;
import exceptions.MyCouponException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CouponsDAODBImpl implements CouponsDAO {

    //QUERIES
      private static final String QUERY_COUPONS_INSERT    = "INSERT INTO `coupons_system`.`coupons` \n"+
        "(`company_id`, `category_id`, `title`, \n"+
        "`description`, `start_date`, `end_date`, \n"+
        "`amount`, `price`, `image`) \n"+
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String QUERY_COUPONS_UPDATE    = "UPDATE `coupons_system`.`coupons` \n"+
            "SET `company_id` = ?, `category_id` = ?, `title` = ?, \n"+
            "`description` = ?, `start_date` = ?, `end_date` = ?, \n"+
            "`amount` = ?, `price` = ?, `image` = ? \n"+
            "WHERE (`id` = ?);\n";


    private static final String QUERY_COUPONS_DELETE    = "DELETE FROM `coupons_system`.`coupons` \n"+
            "WHERE (`id` = ?);";

    private static final String QUERY_COUPONS_EXPIRED    = "SELECT * FROM `coupons_system`.`coupons` \n"+
            " where end_date > ?";

    private static final String QUERY_ALL_COUPONS     = "SELECT * FROM `coupons_system`.`coupons`;";

    private static final String QUERY_COUPONS_BY_ID     = "SELECT * FROM `coupons_system`.`coupons` \n"+
            " where id = ?";

    private static final String QUERY_ALL_AVAILABLED_NON_EXPIRED_COUPONS     = "SELECT * FROM `coupons_system`.`coupons` \n"+
            " where (amount > 0 and end_date >= ?)";

    private static final String QUERY_COUPONS_ID_BY_COMPANY_ID     = "SELECT id FROM `coupons_system`.`coupons` \n"+
            " where company_id = ? ";

    private static final String QUERY_COUPONS_IS_EXIST_TITLE_AND_COMPANY_ID    = "SELECT EXISTS(SELECT * FROM `coupons_system`.`coupons` \n"+
            "where  company_id = ? and title=? ) as res";

    private static final String QUERY_CUSTOMERS_VS_COUPONS_INSERT    = "INSERT INTO `coupons_system`.`customers_vs_coupons` \n"+
            "(`customer_id`, `coupon_id`) VALUES (?, ?);";

    private static final String QUERY_CUSTOMERS_VS_COUPONS_DELETE    = "DELETE FROM `coupons_system`.`customers_vs_coupons` \n"+
            "WHERE (`customer_id` = ? AND `coupon_id` = ?);";

     private static final String QUERY_CUSTOMERS_VS_COUPONS_IS_EXIST_COUPON_ID    = "SELECT EXISTS(SELECT * FROM `coupons_system`.`customers_vs_coupons` \n"+
            "where coupon_id = ? ) as res";

    private static final String  QUERY_CUSTOMERS_VS_COUPONS_COUPON_ID_BY_CUSTOMER_ID = "select `coupon_id` FROM `coupons_system`.`customers_vs_coupons` \n"+
            "where customer_id = ?;";

    private static final String QUERY_CUSTOMERS_VS_COUPONS_DELETE_BY_COUPON_ID    = "DELETE FROM `coupons_system`.`customers_vs_coupons` \n"+
            "WHERE (`coupon_id` = ?);";

    //Coupon Interface API -----------------------------------------------------------------
    @Override
    public void addCoupon(Coupon coupon) throws MyCouponException {

        Map<Integer, Object> map = new HashMap<>();
        map.put(1, coupon.getCompanyId());
        map.put(2, coupon.getCategory().getId());
        map.put(3, coupon.getTitle());
        map.put(4, coupon.getDescription());
        map.put(5, coupon.getStartDate());
        map.put(6, coupon.getEndDate());
        map.put(7, coupon.getAmount());
        map.put(8, coupon.getPrice());
        map.put(9, coupon.getImage());

        try {
            JDBCUtils.execute(QUERY_COUPONS_INSERT, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: addCoupon Failed",e);
        }
  }


    @Override
    public void updateCoupon(Coupon coupon) throws MyCouponException {

        Map<Integer, Object> map = new HashMap<>();
        map.put(1, coupon.getCompanyId());
        map.put(2, coupon.getCategory().getId());
        map.put(3, coupon.getTitle());
        map.put(4, coupon.getDescription());
        map.put(5, coupon.getStartDate());
        map.put(6, coupon.getEndDate());
        map.put(7, coupon.getAmount());
        map.put(8, coupon.getPrice());
        map.put(9, coupon.getImage());
        map.put(10, coupon.getId());
        try {
            JDBCUtils.execute(QUERY_COUPONS_UPDATE, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: updateCoupon Failed",e);
        }
    }

    @Override
    public void deleteCoupon(int couponId) throws MyCouponException {
        //delete Coupon
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, couponId);
        try {
            JDBCUtils.execute(QUERY_COUPONS_DELETE, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: deleteCoupon Failed",e);
        }

    }

    @Override
    public List<Coupon> getAllCoupons() throws MyCouponException {
        List<Coupon> coupons = new ArrayList<>();

        List<?> res = null;
        try {
            res = JDBCUtils.executeResults(QUERY_ALL_COUPONS);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: getAllCoupons Failed",e);
        }
        for (Object row : res) {
            coupons.add( ResultsUtils.fromHashMapToCoupon((HashMap<Integer, Object>) row));
        }
        return coupons;
    }

    public List<Coupon> getAllExpiredCoupons() throws MyCouponException {
        List<Coupon> coupons = new ArrayList<>();
        Date end_date = Date.valueOf(LocalDate.now());
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, end_date);

        List<?> res = null;
        try {
            res = JDBCUtils.executeResults(QUERY_COUPONS_EXPIRED,map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: getAllExpiredCoupons Failed",e);
        }
        for (Object row : res) {
            coupons.add( ResultsUtils.fromHashMapToCoupon((HashMap<Integer, Object>) row));
        }
        return coupons;
    }

    @Override
    public List<Coupon> getAllAvailableCoupons() throws MyCouponException {

        List<Coupon> coupons = new ArrayList<>();
        Date end_date = Date.valueOf(LocalDate.now());
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, end_date);

        List<?> res = null;
        try {
            res = JDBCUtils.executeResults(QUERY_ALL_AVAILABLED_NON_EXPIRED_COUPONS,map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: getAllAvailableCoupons Failed",e);
        }
        for (Object row : res) {
            coupons.add( ResultsUtils.fromHashMapToCoupon((HashMap<Integer, Object>) row));
        }
        return coupons;
    }


    @Override
    public Coupon getOneCoupon(int couponId) throws MyCouponException {
        Coupon coupon = null;

        Map<Integer, Object> map = new HashMap<>();
        map.put(1, couponId);
        List<?> res = null;
        try {
            res = JDBCUtils.executeResults(QUERY_COUPONS_BY_ID, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: getOneCoupon Failed",e);
        }
        for (Object row : res) {
            coupon = (ResultsUtils.fromHashMapToCoupon((HashMap<Integer, Object>) row));
            break;
        }
        return coupon;
    }


    @Override
    public void addCouponPurchase(int customerId, int couponId) throws MyCouponException {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, customerId);
        map.put(2, couponId);
        try {
            JDBCUtils.execute(QUERY_CUSTOMERS_VS_COUPONS_INSERT, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: addCouponPurchase Failed",e);
        }

    }

    @Override
    public void deleteCouponPurchase(int customerId, int couponId) throws MyCouponException {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, customerId);
        map.put(2, couponId);
        try {
            JDBCUtils.execute(QUERY_CUSTOMERS_VS_COUPONS_DELETE, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: deleteCouponPurchase Failed",e);
        }
    }

    @Override
    //Addtional Methods ----------------------------------------------------------------------------------------
    public boolean isCouponPurchased(int couponId) throws MyCouponException {
        boolean isExist = false;

        Map<Integer, Object> map = new HashMap<>();
        map.put(1, couponId);


        List<?> res = null;
        try {
            res = JDBCUtils.executeResults(QUERY_CUSTOMERS_VS_COUPONS_IS_EXIST_COUPON_ID, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: isCouponPurchased Failed",e);
        }
        for (Object row : res) {
            isExist = ResultsUtils.fromHashMapToBoolean((HashMap<Integer, Object>) row);
            break;
        }

        return isExist;

    }

    public boolean isCompanyCouponsTitleExist(int companyId, String couponTitle) throws MyCouponException {
        boolean isExist = false;

        Map<Integer, Object> map = new HashMap<>();
        map.put(1, companyId);
        map.put(2, couponTitle);


        List<?> res = null;
        try {
            res = JDBCUtils.executeResults(QUERY_COUPONS_IS_EXIST_TITLE_AND_COMPANY_ID, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: isCompanyCouponsTitleExist Failed",e);
        }
        for (Object row : res) {
            isExist = ResultsUtils.fromHashMapToBoolean((HashMap<Integer, Object>) row);
            break;
        }

        return isExist;

    }

    public List<Coupon> getAllCouponsByCompanyId(int companyId) throws MyCouponException {

        Map<Integer, Object> map = new HashMap<>();
        map.put(1, companyId);

        List<?> res = null;
        try {
            res = JDBCUtils.executeResults(QUERY_COUPONS_ID_BY_COMPANY_ID,map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: getAllCouponsByCompanyId Failed",e);
        }

        int couponId = -1;
        List<Coupon> coupons = new ArrayList<>();
        for (Object row : res) {
            couponId = ResultsUtils.fromHashMapToCouponId((HashMap<Integer, Object>) row,"id");
            coupons.add(getOneCoupon(couponId));
        }
        return coupons;

    }

    @Override
    public List<Coupon> getAllCouponsByCustomerId(int customerId) throws MyCouponException{

        Map<Integer, Object> map = new HashMap<>();
        map.put(1, customerId);

        List<?> res = null;
        try {
            res = JDBCUtils.executeResults(QUERY_CUSTOMERS_VS_COUPONS_COUPON_ID_BY_CUSTOMER_ID,map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: getAllCouponsByCustomerId Failed",e);
        }
        List<Coupon> coupons = new ArrayList<>();
        int couponId = 0;

        for (Object row : res) {
            couponId = ResultsUtils.fromHashMapToCouponId((HashMap<Integer, Object>) row,"coupon_id");
            coupons.add(getOneCoupon(couponId));
        }
        return coupons;
    }

    public void deleteAllCouponsPurchasesById(int couponId) throws MyCouponException{
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, couponId);
        try {
            JDBCUtils.execute(QUERY_CUSTOMERS_VS_COUPONS_DELETE_BY_COUPON_ID, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: deleteAllCouponsPurchasesById Failed",e);
        }

    }
}
