package com.ft.otp.util.alg.base.checksum;

/**
 * CRC16校验和算法,CRC-CCITT (XMODEM)
 * 验证网址：http://www.lammertbies.nl/comm/info/crc-calculation.html
 *
 * @Date in Jan 4, 2013,11:52:11 AM
 *
 * @version v1.0
 *
 * @author KK David
 */

public class CRC16 {
    private short[] crcTable = new short[256];
    private int gPloy = 0x1021; // 生成多项式

    public CRC16() {
        computeCrcTable();
    }

    /*
     * 生成0-255中某一个对应的校验码
     */
    private short getCrcOfByte(int aByte) {
        int value = aByte << 8;
        for (int count = 7; count >= 0; count--) {
            if ((value & 0x8000) != 0) { // 高第16位为1，可以按位异或
                value = (value << 1) ^ gPloy;
            } else {
                value = value << 1; // 首位为0，左移
            }
        }
        value = value & 0xFFFF; // 取低16位的值
        return (short) value;
    }

    /*
     * 生成0 - 255对应的CRC16校验码
     */
    private void computeCrcTable() {
        for (int i = 0; i < 256; i++) {
            crcTable[i] = getCrcOfByte(i);
        }
    }

    // 生成CRC16校验和
    public short getCRC16(byte[] data) {
        return getCRC16(data, 0, data.length);
    }

    // 生成CRC16校验和
    public short getCRC16(byte[] data, int off, int length) {
        int crc = 0;
        for (int i = 0; i < length; i++) {
            crc = ((crc & 0xFF) << 8) ^ crcTable[(((crc & 0xFF00) >> 8) ^ data[i + off]) & 0xFF];
        }
        crc = crc & 0xFFFF;

        return (short) crc; // 返回的值可能是负的，但不影响后续使用
    }

    // 累加计算CRC16校验和
    public short getCRC16(short oldcrc, byte[] data, int off, int length) {
        int crc = oldcrc;
        for (int i = 0; i < length; i++) {
            crc = ((crc & 0xFF) << 8) ^ crcTable[(((crc & 0xFF00) >> 8) ^ data[i + off]) & 0xFF];
        }
        crc = crc & 0xFFFF;

        return (short) crc; // 返回的值可能是负的，但不影响后续使用
    }

    //    public static void main(String[] args) {
    //        String token = "2630220000033";
    //        byte[] key = AlgHelper.hexStringToBytes("18FC45F3B4ABA55584A4A632C39EE00424D07651");
    //        
    //        short crc = 0;
    //        CRC16 crc16 = new CRC16();
    //        crc = crc16.getCRC16(crc, token.getBytes(), 0, token.length());
    //        crc = crc16.getCRC16(crc, key, 0, key.length);
    //        System.out.println(crc);
    //        
    //        byte[] data = new byte[token.length() + key.length];
    //        int tlen = token.length();
    //        int klen = key.length;
    //        for (int i = 0; i < tlen; i++) {
    //            data[i] = token.getBytes()[i];
    //        }
    //        for (int i = 0; i < klen; i++) {
    //            data[tlen+i] = key[i];
    //        }
    //        
    //        crc = crc16.getCRC16(data);
    //        System.out.println(crc);
    //    }
}
