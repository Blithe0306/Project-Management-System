/**
 *Administrator
 */
package com.ft.otp.manager.token.tokenimport.service;

import java.io.File;

import com.ft.otp.manager.token.entity.TokenInfo;

/**
 * 令牌导入业务接口
 *
 * @Date in Mar 26, 2013,10:47:16 AM
 *
 * @version v1.0
 *
 * @author TBM
 */
public interface ITokenImport {

    /**
     * 令牌导入初始化
     * 
     * @Date in Mar 26, 2013,11:53:00 AM
     * @param seedFile 种子文件
     * @param keyFile 密钥文件
     * @param pass 密钥密码
     * @return
     */
    int initTokenImport(File seedFile, File keyFile, String pass);

    /**
     * 导入令牌的总数统计
     * 
     * @Date in Mar 26, 2013,11:53:48 AM
     * @return
     */
    int getTokenCount();

    /**
     * 获取下一个令牌
     * 
     * @Date in Mar 26, 2013,11:54:11 AM
     * @return
     */
    TokenInfo getNextToken();

    /**
     * 获取相关列表数据
     * 飞天的实现需要，其它厂商直接在实现方法中返回null即可
     * 
     * @Date in Mar 29, 2013,3:01:22 PM
     * @param key
     * @return
     */
    Object getTokenData(String key);

    /**
     * 反初始化
     * 
     * @Date in Mar 29, 2013,3:01:27 PM
     */
    void unInit();

}
