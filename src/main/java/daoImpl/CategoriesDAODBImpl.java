package daoImpl;

import beans.Category;
import dao.CategoriesDAO;
import db.JDBCUtils;
import exceptions.MyCouponException;

import java.util.HashMap;
import java.util.Map;

public class CategoriesDAODBImpl implements CategoriesDAO {
    private static final String QUERY_CATEGORIES_INSERT = "INSERT INTO `coupons_system`.`categories` \n" +
            "(`name`) \n" +
            "VALUES (?);";

    @Override
    public void addCategory(Category category) throws MyCouponException {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, category.name());

        try {
            JDBCUtils.execute(QUERY_CATEGORIES_INSERT, map);
        } catch (Exception e) {
            throw new MyCouponException("Repository Error: Add Category Failed", e);

        }
    }


}
