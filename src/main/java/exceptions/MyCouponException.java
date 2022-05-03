package exceptions;

public class MyCouponException extends Exception {
    public MyCouponException(String s)
    {
        // Call constructor of parent Exception
        super(s);
    }

    public MyCouponException(String s, Throwable cause)
    {
        // Call constructor of parent Exception
        super(s,cause);
    }

}
