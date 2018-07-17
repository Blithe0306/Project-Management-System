/**
 *Administrator
 */
package com.ft.otp.util.alg.dist;

/**
 * 类功能说明
 *
 * @Date in May 11, 2011,6:06:12 PM
 *
 * @author ZJY
 */
public class Luhn {
    private static final char base36[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7,
            8, 9, 0, 0, 0, 0, 0, 0, 0, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a,
            0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20, 0x21, 0x22, 0x23, 0, 0, 0, 0,
            0, 0, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13,
            0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e,
            0x1f, 0x20, 0x21, 0x22, 0x23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    public static int getCheckSum(String strNumber, boolean alter) {
        int len = strNumber.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isLetterOrDigit(strNumber.charAt(i))) {
                return -1;
            }

        }

        int sum = 0;
        boolean alternate = alter;
        for (int j = len; 0 < j--;) {
            char s = base36[strNumber.charAt(j)];

            do {
                int n = s % 10;
                if (alternate) {
                    sum += n * 2 > 9 ? (1 + (n * 2) % 10) : n * 2;
                } else {
                    sum += n;
                }

                s /= 10;

                alternate = !alternate;
            } while (s > 0);
        }

        //System.out.println(sum);

        sum %= 10;
        return (sum != 0 ? 10 - sum : 0);
    }

    public static int getCheckSum(String strNumber) {
        return getCheckSum(strNumber, true);
    }

    public static boolean isValid(String number) {
        int sum = 0;

        sum = getCheckSum(number, false);

        return (sum % 10 == 0);
    }
}
