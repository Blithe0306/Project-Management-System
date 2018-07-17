/**
 *Administrator
 */
package com.ft.otp.base.entity;

/**
 * 消息实体，用户存放多个消息对象
 *
 * @Date in May 7, 2011,1:28:42 PM
 *
 * @author TBM
 */
public class MegEntity {

    /**
     * 标准参数，可根据需要扩充
     */
    //消息结果，成功或失败
    private int result = -1;
    //失败信息
    private String operMsg = null;

    /**
     * 其他参数，根据不同功能模块的需要定制
     */
    //批量绑定用户个数
    private int usrNumber = 0;
    //批量绑定令牌个数
    private int tknNumber = 0;

    /**
     * @return the result
     */
    public int getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * @return the operMsg
     */
    public String getOperMsg() {
        return operMsg;
    }

    /**
     * @param operMsg the operMsg to set
     */
    public void setOperMsg(String operMsg) {
        this.operMsg = operMsg;
    }

    /**
     * @return the usrNumber
     */
    public int getUsrNumber() {
        return usrNumber;
    }

    /**
     * @param usrNumber the usrNumber to set
     */
    public void setUsrNumber(int usrNumber) {
        this.usrNumber = usrNumber;
    }

    /**
     * @return the tknNumber
     */
    public int getTknNumber() {
        return tknNumber;
    }

    /**
     * @param tknNumber the tknNumber to set
     */
    public void setTknNumber(int tknNumber) {
        this.tknNumber = tknNumber;
    }

}
