package dao;

import beans.Category;
import exceptions.MyCouponException;

public interface CategoriesDAO {

    public void addCategory(Category category) throws  MyCouponException;

 }
