package cn.moondev.framework.model;

public class AppException extends RuntimeException {

    public String mcode;
    public String message;

    public AppException(String mcode) {
        this(mcode, "");
    }

    public AppException(String mcode, String message) {
        super(mcode);
        this.mcode = mcode;
        this.message = message;
    }

    @Override
    public String toString() {
        return "mcode: " + mcode + ". message: " + message + super.toString();
    }
}