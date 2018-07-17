package com.ft.otp.util.alg.pbkdf2;

public interface PRF
{
    /**
     * Initialize this instance with the user-supplied password.
     * 
     * @param P
     *            The password supplied as array of bytes. It is the caller's
     *            task to convert String passwords to bytes as appropriate.
     */
    public void init(byte[] P);

    /**
     * Pseudo Random Function
     * 
     * @param M
     *            Input data/message etc. Together with any data supplied during
     *            initilization.
     * @return Random bytes of hLen length.
     */
    public byte[] doFinal(byte[] M);

    /**
     * Query block size of underlying algorithm/mechanism.
     * 
     * @return block size
     */
    public int getHLen();
}