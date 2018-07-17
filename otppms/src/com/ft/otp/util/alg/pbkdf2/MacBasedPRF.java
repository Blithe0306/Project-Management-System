package com.ft.otp.util.alg.pbkdf2;


/**
 * Default PRF implementation based on standard javax.crypt.Mac mechanisms.
 * 
 * <hr />
 * <p>
 * A free Java implementation of Password Based Key Derivation Function 2 as
 * defined by RFC 2898. Copyright (c) 2007 Matthias G&auml;rtner
 * </p>
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * </p>
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * </p>
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * </p>
 * <p>
 * For Details, see <a
 * href="http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html">http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html</a>.
 * </p>
 * 
 * @version 1.0
 */
public class MacBasedPRF implements PRF {
    protected Mac mac;

    protected int hLen;

    protected String macAlgorithm;

    /**
     * Create Mac-based Pseudo Random Function.
     * 
     * @param macAlgorithm
     *            Mac algorithm to use, i.e. HMacSHA1 or HMacMD5.
     */
    public MacBasedPRF(String macAlgorithm) {
        this.macAlgorithm = macAlgorithm;
        //        try
        //        {
        if (macAlgorithm.equals("HMacSHA1")) {
            mac = new HMac(new SHA1Digest());
        }
        //        	if(macAlgorithm.equalsIgnoreCase("HMacMD5"))
        //        	{
        //        		throw new NoSuchAlgorithmException();
        //        	}

        hLen = mac.getMacSize();
        //        }
        //        catch (NoSuchAlgorithmException e)
        //        {
        //            throw e;
        //        }
    }

    public byte[] doFinal(byte[] M) {
        byte[] r = new byte[hLen];
        mac.update(M, 0, M.length);
        mac.doFinal(r, 0);
        return r;
    }

    public int getHLen() {
        return hLen;
    }

    public void init(byte[] P) {
        try {
            mac.init(new KeyParameter(P));
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
