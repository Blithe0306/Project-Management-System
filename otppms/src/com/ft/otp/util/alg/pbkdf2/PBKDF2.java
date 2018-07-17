package com.ft.otp.util.alg.pbkdf2;

//import otp.alg.base.pbkdf2.PBKDF2Parameters;
//import otp.alg.base.pbkdf2.PRF;

public interface PBKDF2
{
    /**
     * Convert String-based input to internal byte array, then invoke PBKDF2.
     * Desired key length defaults to Pseudo Random Function block size.
     * 
     * @param inputPassword
     *            Candidate password to compute the derived key for.
     * @return internal byte array
     */
    public abstract byte[] deriveKey(String inputPassword);

    /**
     * Convert String-based input to internal byte array, then invoke PBKDF2.
     * 
     * @param inputPassword
     *            Candidate password to compute the derived key for.
     * @param dkLen
     *            Specify desired key length
     * @return internal byte array
     */
    public abstract byte[] deriveKey(String inputPassword, int dkLen);
    
    public abstract byte[] deriveKey(byte[] inputPassword, int dkLen);

    /**
     * Convert String-based input to internal byte arrays, then invoke PBKDF2
     * and verify result against the reference data that is supplied in the
     * PBKDF2Parameters.
     * 
     * @param inputPassword
     *            Candidate password to compute the derived key for.
     * @return <code>true</code> password match; <code>false</code>
     *         incorrect password
     */
    public abstract boolean verifyKey(String inputPassword);

    /**
     * Allow reading of configured parameters.
     * 
     * @return Currently set parameters.
     */
    public abstract PBKDF2Parameters getParameters();

    /**
     * Allow setting of configured parameters.
     * 
     * @param parameters
     */
    public abstract void setParameters(PBKDF2Parameters parameters);

    /**
     * Get currently set Pseudo Random Function.
     * 
     * @return Currently set Pseudo Random Function
     */
    public abstract PRF getPseudoRandomFunction();

    /**
     * Set the Pseudo Random Function to use. Note that deriveKeys/getPRF does
     * init this object using the supplied candidate password. If this is
     * undesired, one has to override getPRF.
     * 
     * @param prf
     *            Pseudo Random Function to set.
     */
    public abstract void setPseudoRandomFunction(PRF prf);
}
