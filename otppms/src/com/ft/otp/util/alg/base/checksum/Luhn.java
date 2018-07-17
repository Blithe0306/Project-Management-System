package com.ft.otp.util.alg.base.checksum;

/**
 * 
 * LUHN校验和算法
 *
 * @Date in Mar 5, 2013,3:14:15 PM
 *
 * @version v1.0
 *
 * @author KK David
 */
public class Luhn {

    //	0123456789 ABCDEFGHIJKLMNOPQRSTUVWXYZ(abcdefghijklmnopqrstuvwxyz)
    private static final char base36[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0,
            0, 0, 0, 0, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19,
            0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20, 0x21, 0x22, 0x23, 0, 0, 0, 0, 0, 0, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e,
            0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20,
            0x21, 0x22, 0x23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    /**
     * 
     * 获取校验和
     * @Date in Mar 5, 2013,3:22:22 PM
     * @param strNumber 需要计算校验和的字符串
     * @param alter 计算还是验证
     * @return 校验和
     */
    public static int getCheckSum(String strNumber, boolean alter) {
        int len = strNumber.length();
        for (int i = 0; i < len; i++) {// 必须是字母或数字
            char ch = strNumber.charAt(i);
            if (!Character.isLowerCase(ch) && !Character.isUpperCase(ch) && !Character.isDigit(ch)) {
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

        return ((10 - sum % 10)) % 10;
    }

    /**
     * 
     * 获取校验和
     * @Date in Mar 5, 2013,3:22:22 PM
     * @param strNumber 需要计算校验和的字符串
     * @return 校验和
     */
    public static int getCheckSum(String strNumber) {
        return getCheckSum(strNumber, true);
    }

    /**
     * 
     * 验证校验和
     * @Date in Mar 5, 2013,3:22:22 PM
     * @param number 包含校验和的字符串
     * @return 校验和是否正确
     */
    public static boolean isValid(String number) {
        int sum = 0;

        sum = getCheckSum(number, false);

        return (sum % 10 == 0);
    }
}
