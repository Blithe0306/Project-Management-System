/**
 *Administrator
 */
package com.ft.otp.manager.token.tokenimport.service.impl.rfc6030;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.ft.otp.common.TokenXmlConstant;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.tokenimport.service.impl.BaseTokenImport;
import com.ft.otp.manager.token.tokenimport.service.impl.rfc6030.helper.RfcTknUtil;
import com.ft.otp.util.tool.StrTool;
import com.ft.otp.util.xml.XmlUtil;

/**
 * 令牌导入业务接口实现(RFC6030)
 *
 * @Date in Mar 27, 2013,2:17:43 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class TokenImportImpl extends BaseTokenImport {

    private Logger logger = Logger.getLogger(TokenImportImpl.class);
    private ConcurrentHashMap<String, Object> objMap = null;
    private static int indexNum = 0;

    private static int error_key = 1001; //解密Key失败
    private static int error_data = 1003; //读取令牌数据失败

    private RfcTknUtil rfcUtil = new RfcTknUtil();

    public TokenImportImpl() {
        objMap = new ConcurrentHashMap<String, Object>();
    }

    /**
     * 初始化令牌导入
     */
    public int initTokenImport(File seedFile, File keyFile, String pass) {
        indexNum = 0;
        Element rootElement = null;//XML目录根节点
        byte[] key = null;//私有数据解密密钥

        XmlUtil xmlUtil = new XmlUtil();

        //取得XML令牌文件根目录
        rootElement = xmlUtil.getElement(seedFile);

        try {
            if (null != keyFile) {
                key = rfcUtil.getDecKey(keyFile, keyFile.getName(), pass);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return error_key;
        }

        try {
            objMap = rfcUtil.parseXml(rootElement, key, objMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return error_data;
        }

        return 0;
    }

    /**
     * 获取令牌总数
     */
    @SuppressWarnings("unchecked")
    public int getTokenCount() {
        if (null != objMap && objMap.size() > 0) {
            List<TokenInfo> tokens = (List<TokenInfo>) objMap.get(TokenXmlConstant.MAP_TOKEN);
            return tokens.size();
        }

        return 0;
    }

    /**
     * 获取下一个令牌
     */
    @SuppressWarnings("unchecked")
    public TokenInfo getNextToken() {
        if (null == objMap) {
            return null;
        }

        TokenInfo tokenInfo = null;
        List<TokenInfo> tokens = (List<TokenInfo>) objMap.get(TokenXmlConstant.MAP_TOKEN);
        if (null != tokens && tokens.size() > 0) {
            tokenInfo = tokens.get(indexNum);
            indexNum++;
        }

        return tokenInfo;
    }

    /**
     * 获取Map
     * 
     * @Date in Mar 27, 2013,11:52:28 AM
     * @return
     */
    public ConcurrentHashMap<String, Object> getObjMap() {
        return objMap;
    }

    /**
     * 获取Map中的数据列表
     * 
     * @Date in Mar 29, 2013,2:56:42 PM
     * @param key
     * @return
     */
    public Object getTokenData(String key) {
        try {
            if (StrTool.strNotNull(key)) {
                return (Object) objMap.get(key);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 清空Map
     * 
     * @Date in Mar 27, 2013,11:52:49 AM
     */
    public void unInit() {
        if (null != objMap && objMap.size() > 0) {
            objMap.clear();
        }
    }
}
