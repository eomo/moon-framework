package cn.moondev.framework.model;


public class ResponseDTO<T> {

    public boolean result;

    public String mcode;

    public String message;

    public T data;

    public Long time = System.currentTimeMillis();

    public ResponseDTO() {
    }

    private ResponseDTO(boolean result, String mcode, String message, T data) {
        this.result = result;
        this.mcode = mcode;
        this.message = message;
        this.data = data;
    }

    public static <R> ResponseDTO<R> success() {
        return new ResponseDTO<>(true, "000000", null, null);
    }

    public static <R> ResponseDTO<R> success(R data) {
        return new ResponseDTO<>(true, "000000", null, data);
    }

    public static <R> ResponseDTO<R> success(String mcode, R data) {
        return new ResponseDTO<>(true, mcode, null, data);
    }


    public static ResponseDTO<Void> failed(String mcode) {
        return new ResponseDTO<>(false, mcode, null, null);
    }

    public static ResponseDTO<Void> failed(String mcode, String message) {
        return new ResponseDTO<>(false, mcode, message, null);
    }
}
