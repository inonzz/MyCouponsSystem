package daoImpl;

import beans.Company;
import beans.Coupon;
import dao.CompaniesDAO;
import dao.CouponsDAO;
import db.JDBCUtils;
import db.ResultsUtils;
import exceptions.MyCouponException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompaniesDAODBImpl implements CompaniesDAO {

    //QUERIES
    private static final String QUERY_COMPANIES_INSERT = "INSERT INTO `coupons_system`.`companies` (`name`, `email`, `password`) VALUES (?, ?, ?);\n";

    private static final String QUERY_COMPANIES_UPDATE = "UPDATE `coupons_system`.`companies` SET `name` = ?, `email` = ?, `password` = ? WHERE (`id` = ?);\n";

    private static final String QUERY_COMPANIES_DELETE = "DELETE FROM `coupons_system`.`companies` WHERE (`id` = ?);";

    private static final String QUERY_COMPANIES_IS_EXIST_BY_EMAIL_AND_PASSWORD = "SELECT EXISTS(SELECT * FROM `coupons_system`.`companies` where `email`=? and `password`=?) as res";

    private static final String QUERY_ALL_COMPANIES_IDS = "SELECT id FROM `coupons_system`.`companies`;";

    private static final String QUERY_ALL_COMPANIES_IDS_BY_EMAIL_AND_PASSWORD = "SELECT id FROM `coupons_system`.`companies` where (email=? and password=?) ;";

    private static final String QUERY_COMPANIES_BY_ID = "SELECT * FROM `coupons_system`.`companies` where id=?";


    @Override
    public boolean isCompanyExists(String email, String password) throws MyCouponException {
        boolean isExist = false;

        Map<Integer, Object> map = new HashMap<>();
        map.put(1, email);
        map.put(2, password);


        List<?> res = null;
        try {
            res = JDBCUtils.executeResults(QUERY_COMPANIES_IS_EXIST_BY_EMAIL_AND_PASSWORD, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: isCompanyExists Failed", e);
        }
        for (Object row : res) {
            isExist = ResultsUtils.fromHashMapToBoolean((HashMap<Integer, Object>) row);
            break;
        }

        return isExist;

    }

    @Override
    public void addCompany(Company company) throws MyCouponException {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, company.getName());
        map.put(2, company.getEmail());
        map.put(3, company.getPassword());
        List<Coupon> coupons = company.getCoupons();

        try {
            JDBCUtils.execute(QUERY_COMPANIES_INSERT, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: addCompany Failed", e);
        }

        CouponsDAO couponsDAO = new CouponsDAODBImpl();
        for (Coupon coupon : coupons) {
            try {
                couponsDAO.addCoupon(coupon);
            } catch (Exception e) {
                throw new MyCouponException("Repository Error: addCoupon in addCompany Failed", e);
            }
        }
    }

    @Override
    public void updateCompany(Company company) throws MyCouponException {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, company.getName());
        map.put(2, company.getEmail());
        map.put(3, company.getPassword());
        map.put(4, company.getId());
        List<Coupon> coupons = company.getCoupons();

        try {
            JDBCUtils.execute(QUERY_COMPANIES_UPDATE, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: updateCompany[1] Failed", e);
        }
        CouponsDAO couponsDAO = new CouponsDAODBImpl();
        for (Coupon coupon : coupons) {
            try {
                couponsDAO.updateCoupon(coupon);
            } catch (Exception e) {
                throw new MyCouponException("Repository Error: updateCompany[2] Failed", e);
            }
        }
    }

    @Override
    public void deleteCompany(int companyId) throws MyCouponException {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, companyId);
        try {
            JDBCUtils.execute(QUERY_COMPANIES_DELETE, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: deleteCompany Failed", e);
        }
    }

    @Override
    public int getOneCompanyId(String email, String password) throws MyCouponException {
        List<Company> companies = new ArrayList<>();
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, email);
        map.put(2, password);

        List<?> res = null;
        try {
            res = JDBCUtils.executeResults(QUERY_ALL_COMPANIES_IDS_BY_EMAIL_AND_PASSWORD, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: getOneCompanyId(email,password) Failed", e);
        }
        int companyId = 0;
        for (Object row : res) {
            companyId = (int) ((HashMap<?, ?>) row).get("id");
        }
        return companyId;
    }

    @Override
    public List<Company> getAllCompanies() throws MyCouponException {
        List<Company> companies = new ArrayList<>();

        List<?> res = null;
        try {
            res = JDBCUtils.executeResults(QUERY_ALL_COMPANIES_IDS);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: getAllCompanies Failed", e);
        }
        int companyId = 0;
        for (Object row : res) {
            companyId = (int) ((HashMap<?, ?>) row).get("id");
            companies.add(getOneCompany(companyId));
        }
        return companies;
    }

    @Override
    public Company getOneCompany(int companyId) throws MyCouponException {
        Company company = null;

        Map<Integer, Object> map = new HashMap<>();
        map.put(1, companyId);
        List<?> resCompanyTable = null;
        try {
            resCompanyTable = JDBCUtils.executeResults(QUERY_COMPANIES_BY_ID, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error:  getOneCompany Failed", e);
        }

        CouponsDAO couponsDAO = new CouponsDAODBImpl();

        for (Object rowCompnayTable : resCompanyTable) {
            try {
                company = ResultsUtils.fromHashMapToCompany((HashMap<Integer, Object>) rowCompnayTable,
                        couponsDAO.getAllCouponsByCompanyId(companyId));
            } catch (Exception e) {
                throw new MyCouponException("Repository Error:  getAllCouponsByCompanyId Failed", e);
            }
            break;
        }

        return company;
    }
}
