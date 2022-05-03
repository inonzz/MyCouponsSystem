package beans;
import java.sql.Date;

public class Coupon {
//    COUPONS – טבלת קופונים:
//
//            (מפתח ראשי, מספור אוטומטי – int (ID●
//            (מפתח זר לקוד החברה בטבלת החברות – int (ID_COMPANY●
//            (מפתח זר לקוד הקטגוריה בטבלת הקטגוריות – int (ID_CATEGORY●
//            (כותרת הקופון – string (TITLE●
//            (תיאור מפורט של הקופון – string (DESCRIPTION●
//            (תאריך יצירת הקופון – date (DATE_START●
//            (תאריך תפוגת הקופון – date (DATE_END●
//            (כמות קופונים במלאי – integer (AMOUNT●
//            (מחיר הקופון ללקוח – double (PRICE●
//            (שם קובץ תמונה עבור הקופון – string (IMAGE●

    //MEMBERS
    private int id;
    private int companyId;
    private Category category;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int amount;
    private double price;
    private String image;

    public Coupon(int companyId, Category category, String title, String description,
                  Date startDate, Date endDate, int amount, double price, String image) {
        this.companyId = companyId;
        this.category = category;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.price = price;
        this.image = image;
    }

    public Coupon(int id, int companyId, Category category, String title, String description,
                  Date startDate, Date endDate, int amount, double price, String image) {
        this(companyId, category, title, description,
                startDate, endDate, amount, price, image);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", companyId=" + companyId +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", amount=" + amount +
                ", price=" + price +
                ", image='" + image + '\'' +
                '}';
    }
}
