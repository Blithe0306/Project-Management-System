/**
 *Administrator
 */
package com.ft.otp.manager.token.tokenimport.service.impl;

import java.io.File;

import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.tokenimport.service.ITokenImport;

/**
 * 令牌导入基础实现类
 *
 * @Date in Mar 26, 2013,10:48:03 AM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class BaseTokenImport implements ITokenImport {

    public int initTokenImport(File seedFile, File keyFile, String pass) {
        return 0;
    }

    public TokenInfo getNextToken() {
        return null;
    }

    public int getTokenCount() {
        return 0;
    }

    public Object getTokenData(String key) {
        return null;
    }

    public void unInit() {
    }

}
