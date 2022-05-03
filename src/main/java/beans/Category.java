package beans;

public enum Category {

    FOOD(1),//MUST START IN ONE
    ELECTRICITY(2),
    RESTAURANT(3),
    VACATION(4);
    int id;

    private Category(int Id) {
        id = Id;
    }
     public int getId(){return id;}

    public static Category getCategory(int id) {
        switch (id) {
            case 1:
                return Category.FOOD;
            case 2:
                return Category.ELECTRICITY;
            case 3:
                return Category.RESTAURANT;
            case 4:
                return Category.VACATION;
            default:
                return null;
        }

    }


}
