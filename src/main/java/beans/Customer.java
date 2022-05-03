package beans;
import java.util.List;

public class Customer {
    //    CUSTOMERS – טבלת לקוחות:
//
//            (מפתח ראשי, מספור אוטומטי – int (ID●
//            ● FIRST_NAME (string – פרטי שם (
//            ● LAST_NAME (string – משפחה שם (
//            (אימייל הלקוח – string (EMAIL●
//            (סיסמת כניסה – string (PASSWORD●PASSWORD
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Coupon> coupons;

    public Customer(int id, String firstName, String lastName, String email, String password, List<Coupon> coupons ) {
        this(firstName, lastName, email, password, coupons);
        this.id = id;
    }

    public Customer(String firstName, String lastName, String email, String password, List<Coupon> coupons) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.coupons= coupons;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", coupons=" + coupons +
                '}';
    }
}
