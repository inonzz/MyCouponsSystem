package db;

import java.sql.SQLException;

/**
 * Created by kobis on 21 Mar, 2022
 */
public class DatabaseManager {


    //SCHEMA DATA
    private static final String CREATE_COUPONS_SYSTEM_SCHEMA = "CREATE SCHEMA coupons_system";
    private static final String DROP_COUPONS_SYSTEM_SCHEMA = "DROP SCHEMA coupons_system";

    //TABLE1 -COMPANIES
    private static final String CREATE_TABLE_COMPANIES = "CREATE TABLE `coupons_system`.`companies` (\n" +
            "`id` INT NOT NULL AUTO_INCREMENT,\n" +
            "`name` VARCHAR(45) NOT NULL,\n" +
            "`email` VARCHAR(45) NOT NULL,\n" +
            "`password` VARCHAR(45) NOT NULL,\n" +
            "  PRIMARY KEY (`id`));";

    //TABLE 2 - Customers
    private static final String CREATE_TABLE_CUSTOMERS = "CREATE TABLE `coupons_system`.`customers` (\n" +
            "`id` INT NOT NULL AUTO_INCREMENT,\n" +
            "`first_name` VARCHAR(45) NOT NULL,\n" +
            "`last_name` VARCHAR(45) NOT NULL,\n" +
            "`email` VARCHAR(45) NOT NULL,\n" +
            "`password` VARCHAR(45) NOT NULL,\n" +
            "  PRIMARY KEY (`id`));";

    //Table 3- Categories
    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE `coupons_system`.`categories` (\n" +
            "`id` INT NOT NULL AUTO_INCREMENT,\n" +
            "`name` VARCHAR(45) NOT NULL,\n" +
            "  PRIMARY KEY (`id`));";

    //Table 4 - coupons
    private static final String CREATE_TABLE_COUPONS = "CREATE TABLE `coupons_system`.`coupons` (\n" +
            "`id` INT NOT NULL AUTO_INCREMENT,\n" +
            "`company_id` INT NOT NULL,\n" +
            "`category_id` INT NOT NULL,\n" +
            "`title` VARCHAR(45) NOT NULL,\n" +
            "`description` VARCHAR(45) NOT NULL,\n" +
            "`start_date` DATE NOT NULL,\n" +
            "`end_date` DATE NOT NULL,\n" +
            "`amount` INT NOT NULL,\n" +
            "`price` DOUBLE NOT NULL,\n" +
            "`image` VARCHAR(45) NOT NULL,\n" +
            "PRIMARY KEY (`id`),\n" +
            "INDEX `company_id_idx` (`company_id` ASC) VISIBLE,\n" +
            "INDEX `category_id_idx` (`category_id` ASC) VISIBLE,\n" +
            "CONSTRAINT `company_id`\n" +
            "FOREIGN KEY (`company_id`)\n" +
            "REFERENCES `coupons_system`.`companies` (`id`)\n" +
            "ON DELETE NO ACTION\n" +
            "ON UPDATE NO ACTION,\n" +
            "CONSTRAINT `category_id`\n" +
            "FOREIGN KEY (`category_id`)\n" +
            "REFERENCES `coupons_system`.`categories` (`id`)\n" +
            "ON DELETE NO ACTION\n" +
            "ON UPDATE NO ACTION);";

    //Table 5 - customers_vs_coupons
    private static final String CUSTOMERS_VS_COUPONS = "CREATE TABLE `coupons_system`.`customers_vs_coupons` (\n" +
            "  `customer_id` INT NOT NULL,\n" +
            "  `coupon_id` INT NOT NULL,\n" +
            "  PRIMARY KEY (`customer_id`, `coupon_id`),\n" +
            "  INDEX `coupon_id_idx` (`coupon_id` ASC) VISIBLE,\n" +
            "  CONSTRAINT `customer_id`\n" +
            "    FOREIGN KEY (`customer_id`)\n" +
            "    REFERENCES `coupons_system`.`customers` (`id`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION,\n" +
            "  CONSTRAINT `coupon_id`\n" +
            "    FOREIGN KEY (`coupon_id`)\n" +
            "    REFERENCES `coupons_system`.`coupons` (`id`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION);\n";


    public static void databaseStrategy() throws SQLException, InterruptedException {
        try {
            JDBCUtils.execute(DROP_COUPONS_SYSTEM_SCHEMA);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        JDBCUtils.execute(CREATE_COUPONS_SYSTEM_SCHEMA);
        //TABLE1 -COMPANIES
        JDBCUtils.execute(CREATE_TABLE_COMPANIES);
        //TABLE 2 - Customers
        JDBCUtils.execute(CREATE_TABLE_CUSTOMERS);
        //Table 3- Categories
        JDBCUtils.execute(CREATE_TABLE_CATEGORIES);
        //Table 4 - coupons
        JDBCUtils.execute(CREATE_TABLE_COUPONS);
        //Table 5 - customers_vs_coupons
        JDBCUtils.execute(CUSTOMERS_VS_COUPONS);

    }


}
