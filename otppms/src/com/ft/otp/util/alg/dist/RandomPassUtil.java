/**
 *Administrator
 */
package com.ft.otp.util.alg.dist;

/**
 * 生成随机数类功能说明
 *
 * @Date in May 11, 2011,1:08:17 PM
 *
 * @author ZJY
 */
public class RandomPassUtil {
    private static final int alphanumeric = 1;
    private static final int alphanum_size = 62; //0-9 a-z A-Z
    private static final int num_size = 10; //0-9
    private static final int hexadecimal = 3;
    private static final int hex_size = 16; //0-9 a-f

    private static final String alphanum_table = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int numeric = 2;

    public static String genActivePass(int length) {
        // TODO Auto-generated method stub
        if (length < 4 || length > 16) {
            return null;
        }

        return randomString(length, numeric);
    }

    private static int[] randomNext(int len) {
        int[] random = new int[len];

        for (int i = 0; i < len; i++) {
            random[i] = (int) (Math.random() * 255);
        }

        return random;
    }

    private static String randomString(int len, int fmt) {
        int size = 0;
        int[] random = randomNext(len);
        StringBuffer buf = new StringBuffer(len);

        switch (fmt) {
            case alphanumeric:
                size = alphanum_size;
                break;

            case numeric:
                size = num_size;
                break;

            case hexadecimal:
                size = hex_size;
                break;
            default:
                return null;
        }

        for (int i = 0; i < len; i++) {
            buf.append(alphanum_table.charAt(random[i] % size));
        }

        return buf.toString();
    }

}
