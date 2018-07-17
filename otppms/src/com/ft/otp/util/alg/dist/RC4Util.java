package com.ft.otp.util.alg.dist;

public class RC4Util {

    //RC4 key
    private static char[] rc4key_char = { 0x4a, 0xe7, 0x3a, 0xd4, 0x74, 0x4f, 0xdb, 0xc4 };
    private static String rc4key_Str_char = new String(rc4key_char);

    public static String runRC4(String aInput) throws Exception {

        if (aInput == null || aInput.equals("")) {
            return "";
        }
        int[] iS = new int[256];
        byte[] iK = new byte[256];

        for (int i = 0; i < 256; i++)
            iS[i] = i;

        int j = 1;

        byte[] tempB = rc4key_Str_char.getBytes("ISO-8859-1");

        for (short i = 0; i < 256; i++) {
            iK[i] = tempB[(i % rc4key_Str_char.length())];
        }

        j = 0;

        for (int i = 0; i < 255; i++) {
            j = (j + iS[i] + iK[i] & 0xff) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
        }

        int i = 0;
        j = 0;
        char[] iInputChar = aInput.toCharArray();
        char[] iOutputChar = new char[iInputChar.length];
        for (short x = 0; x < iInputChar.length; x++) {
            i = (i + 1) % 256;
            j = (j + iS[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
            int t = (iS[i] + (iS[j] % 256)) % 256;
            int iY = iS[t];
            char iCY = (char) iY;
            iOutputChar[x] = (char) (iInputChar[x] ^ iCY);
        }
        return new String(iOutputChar);
    }

}
