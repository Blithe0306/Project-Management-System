/**
 *Administrator
 */
package com.ft.otp.manager.token.tokenimport.service.impl.ft;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.ft.otp.common.TokenXmlConstant;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.tokenimport.service.impl.BaseTokenImport;
import com.ft.otp.manager.token.tokenimport.service.impl.ft.helper.LoadXmlFile;
import com.ft.otp.manager.tokenspec.entity.TokenSpec;
import com.ft.otp.util.tool.StrTool;
import com.ft.otp.util.xml.XmlUtil;

/**
 * 令牌导入业务接口实现(FT)
 *
 * @Date in Mar 26, 2013,1:26:28 PM
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
    private static int error_spec = 1002; //读取令牌规格失败
    private static int error_data = 1003; //读取令牌数据失败

    private LoadXmlFile loadXmlFile = new LoadXmlFile();

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
        byte[] macKey = null;//用于HMac校验的密钥
        XmlUtil xmlUtil = new XmlUtil();

        //取得XML令牌文件根目录
        rootElement = xmlUtil.getElement(seedFile);
        try {
            if (null != keyFile) {
                key = loadXmlFile.getDecKey(keyFile, keyFile.getName(), pass);
            } else { // 4.0TNK属于这种情况
                key = loadXmlFile.getPBEKey(rootElement);
                macKey = loadXmlFile.getHMacKey(key, rootElement);
                if (macKey == null) {
                    logger.error("failed to get mac key from seed file");
                    return error_key;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return error_key;
        }

        //令牌规格
        try {
            List<TokenSpec> specList = loadXmlFile.getTokenSpecs(rootElement);
            if (null == specList || specList.size() <= 0) {
                return error_spec;
            }
            objMap.put(TokenXmlConstant.MAP_SPEC, specList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return error_spec;
        }

        //令牌数据
        try {
            objMap = loadXmlFile.getTokens(rootElement, key, macKey, objMap);
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
