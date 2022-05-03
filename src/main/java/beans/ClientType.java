package beans;

public enum ClientType {
    ADMINISTRATOR(1),//MUST START IN ONE
    COMPANY(2),
    CUSTOMER(3),
    SYSTEM_MAINTENANCE(4);
    int id;

    private ClientType(int Id) {
        id = Id;
    }

    public int getId() {
        return id;
    }

}




