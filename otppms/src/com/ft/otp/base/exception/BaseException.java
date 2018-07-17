/**
 *Administrator
 */
package com.ft.otp.base.exception;

/**
 * 基础异常类，用来转换、封装自下而上的异常，在Action层统一进行处理。
 *
 * @Date in Apr 2, 2011,4:29:11 PM
 *
 * @author TBM
 */
public class BaseException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = -4478385584393639640L;

    private static int exCode = 0;
    private static String messageStr = "";

    /**
     * @return the exCode
     */
    public static int getExCode() {
        return exCode;
    }

    /**
     * @param exCode the exCode to set
     */
    public static void setExCode(int exCode) {
        BaseException.exCode = exCode;
    }

    public static String getMessageStr() {
        return messageStr;
    }

    public static void setMessageStr(String messageStr) {
        BaseException.messageStr = messageStr;
    }

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    /**
     * 异常处理，异常标识与异常信息一一对应。
     * @param code
     * @param cause
     */
    public BaseException(int code, Throwable cause) {
        //
        setExCode(code);
        setMessageStr(cause.getMessage());
    }

    /**
     * 抛出异常信息
     * @param cause
     */
    public BaseException(Throwable cause) {
        super(cause);
    }

}
