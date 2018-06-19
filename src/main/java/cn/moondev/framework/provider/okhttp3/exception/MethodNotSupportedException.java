package cn.moondev.framework.provider.okhttp3.exception;

/**
 * OkHttpOperations暂时只支持GET和POST方法，如果使用其他方法，抛出此异常
 */
public class MethodNotSupportedException extends RuntimeException {
}
