/**
 *Administrator
 */
package com.ft.otp.util.alg.dist;

/**
 * 离线分发时激活码生成类功能说明
 *
 * @Date in May 11, 2011,5:48:25 PM
 *
 * @author ZJY
 */
public class ActiveCodeUtil {
    private static final int ac_length = 22;
    private static final int exttype_step = 50;
    private static final int max_days = 9999;
    private static final long VALIDBEGINTIME = 1262304000;
    private static final String RESERVE_DATA = "00";
    private static final int max_authnum = 99999;
    public static String genActiveCod(String strTokenNum, String strExtType,
            String strFactor, long life, String authnum, String strActivePass,
            String strUDID) {
        StringBuffer buf = new StringBuffer(ac_length);
        if (strFactor.length() != 10) {
            return null;
        }
        if (strUDID != null) {
            int extType = Integer.parseInt(strExtType) + exttype_step;
            strExtType = "" + extType;
        }

        if (strExtType.length() != 2) {
            return null;
        }
        //type
        buf.append(strExtType);
        //factor
        buf.append(strFactor);

        long validDays = (life - VALIDBEGINTIME) / (24 * 60 * 60);

        validDays = validDays > max_days ? max_days : validDays;
        String strDay = "" + validDays;
        while (strDay.length() < 4)
            strDay = "0" + strDay;

        if (strDay.length() != 4) {
            return null;
        }

        //valid time
        buf.append(strDay);

        //auth num
        if (authnum.length() > 5) {
            int num = Integer.parseInt(authnum);
            int remainder = num % 10;
            while ((num /= 10) > max_authnum)
                continue;

            if (remainder > 0 && num != max_authnum) {
                num += 1;
            }

            buf.append(num);
        } else {
            while (authnum.length() != 5)
                authnum = "0" + authnum;

            buf.append(authnum);
        }

        buf.append(RESERVE_DATA);
        StringBuffer strAc = new StringBuffer(1024);
        strAc.append(buf.toString());
        strAc.append(strActivePass);
        if (strUDID != null) {
            strAc.append(strUDID);
        }
        int sum = Luhn.getCheckSum(strAc.toString());
        if (sum == -1) {
            return null;
        }
        buf.append(sum);
        if (strTokenNum != null && strTokenNum.length() != 0) {
            StringBuffer blk2 = new StringBuffer(1024);
            blk2.append(strTokenNum);
            blk2.append(sum);
            blk2.append(strAc.toString());
            sum = Luhn.getCheckSum(blk2.toString());
            if (sum == -1) {
                return null;
            }
            buf.append(sum);
        }
        return buf.toString();
    }

    public static String encodeActiveCode(String strAC, String strActivePass) {
        if (strAC == null || strActivePass == null) {
            return null;
        }
        StringBuffer ac = new StringBuffer(ac_length);
        char[] source = strAC.toCharArray();
        int colums = strActivePass.length();
        int rows = strAC.length() % colums > 0 ? strAC.length() / colums + 1
                : strAC.length() / colums;
        char[][] matrix = new char[rows][colums];

        int pos = 0;
        int step = colums;
        int i;
        for (i = 0; i < rows - 1; i++) {
            System.arraycopy(source, pos, matrix[i], 0, step);
            pos += step;
        }
        step = source.length - pos;
        System.arraycopy(source, pos, matrix[i], 0, step);
        int len = strActivePass.length();
        char[] pass = strActivePass.toCharArray();
        int index = -1;
        int min = 0;
        for (i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (!Character.isDigit(pass[j])) {
                    continue;
                }
                min = Character.digit(pass[j], 10);
                index = j;
                for (int k = 0; k < len; k++) {
                    if (!Character.isDigit(pass[k])) {
                        continue;
                    }
                    int y = Character.digit(pass[k], 10);

                    if (min > y) {
                        min = y;
                        index = k;
                    }
                }
                pass[index] = 'x';

                break;

            }
            for (int x = 0; x < rows; x++) {
                if (Character.isDigit(matrix[x][index]))
                    ac.append(matrix[x][index]);
            }
        }
        return ac.toString();
    }

    public static String getPrivsStr(int privs) {

        if (privs < 1) {
            return "";
        }
        String binary = Integer.toBinaryString(privs);
        StringBuffer buffer = new StringBuffer();
        int l = binary.length() - 1;
        for (int i = 0; i < binary.length(); i++) {
            char s = binary.charAt(i);
            if (s == '0') {
                continue;
            } else {
                buffer.append(l - i).append(";");
            }
        }
        return buffer.toString();
    }

    public static int getPrivsNum(String privs) {

        if (privs == null || "".equals(privs.trim())) {
            return 0;
        }
        int pr = 0;
        try {
            String[] p = privs.split(";");
            for (int i = 0; i < p.length; i++) {
                int pi = Integer.parseInt(p[i]);
                int y = new Double(Math.pow(2, pi)).intValue();
                pr = pr + y;
            }
            return pr;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String spitStr(String src, String spit, int n) {

        if (src == null || "".equals(src.trim())) {
            return "";
        }
        src = src.trim();
        char[] c = src.toCharArray();
        if (c.length <= n) {
            return src;
        }
        String result = "";
        char[] temp = null;
        int j = 0;
        for (int y = 0; y < c.length; y++) {
            if (temp == null) {
                temp = new char[n];
            }
            temp[j] = c[y];
            j++;
            if ((y + 1) % n == 0) {
                j = 0;
                result = result + new String(temp) + spit;
                temp = null;
            }
        }
        String surplus = "";
        if (temp != null) {
            surplus = new String(temp);
        }
        result = result + surplus;
        return result.trim();
    }
}
