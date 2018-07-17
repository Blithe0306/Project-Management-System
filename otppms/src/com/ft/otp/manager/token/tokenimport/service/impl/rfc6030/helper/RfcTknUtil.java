/**
 *Administrator
 */
package com.ft.otp.manager.token.tokenimport.service.impl.rfc6030.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Element;

import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.TokenXmlConstant;
import com.ft.otp.manager.login.entity.LinkUser;
import com.ft.otp.manager.login.service.OnlineUsers;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.tokenspec.entity.TokenSpec;
import com.ft.otp.util.alg.AESUtil;
import com.ft.otp.util.alg.AlgConvert;
import com.ft.otp.util.alg.Base64;
import com.ft.otp.util.alg.CertKeyUtil;
import com.ft.otp.util.alg.DESUtil;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * RFC 种子文件解析帮助类
 *
 * @Date in Nov 1, 2013,11:45:39 AM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class RfcTknUtil {

    private static final String TOKEN_LEN = "tknLen";
    private static final String TOKEN_TYPE = "tknType";
    private static final String TOKEN_NUM = "tknNum";
    private static final String CUST_NUM = "custNum";
    
    private static Map<String, String> tknSpecMap;
    private static Map<String, String> tempSpecMap = null;//临时令牌规格Map
    static {
        tknSpecMap = new HashMap<String, String>();
        
        tknSpecMap.put("totp_6_60", "TOTP01");
        tknSpecMap.put("totp_8_60", "TOTP02");
        tknSpecMap.put("totp_6_30", "TOTP03");
        tknSpecMap.put("totp_8_30", "TOTP04");
        
        tknSpecMap.put("hotp_6_0", "HOTP01");
        tknSpecMap.put("hotp_8_0", "HOTP02");
    }

    /**
     * 解析RFC6030种子文件
     * @Date in Nov 1, 2013,2:44:19 PM
     * @param rootElement
     * @param key
     * @param objMap
     * @return
     * @throws Exception
     */
    public ConcurrentHashMap<String, Object> parseXml(Element rootElement, byte[] key,
            ConcurrentHashMap<String, Object> objMap) throws Exception {
        
        List<TokenInfo> tokenList = new ArrayList<TokenInfo>();
        List<TokenSpec> specList = new ArrayList<TokenSpec>();
        TokenInfo token = null;
        TokenSpec tokenspec = null;
        Element encryptionNode = rootElement.element("EncryptionKey");
        Map<String, String> otpMap = new HashMap<String, String>();
        
        List<?> keyPackageNode = rootElement.elements("KeyPackage");
        
        int perCount = 0;
        int perIndex = 0; //进度条
        LinkUser linkUser = null;
        HttpSession session = getSession();
        if (null != session) {
            String sessionId = session.getId();
            linkUser = OnlineUsers.getUser(sessionId);
        } else {
            linkUser = new LinkUser();
        }
        
        tempSpecMap = new HashMap<String, String>();
        for (Iterator<?> rootIter = keyPackageNode.iterator(); rootIter.hasNext();) {
            Element node = (Element) rootIter.next();
            
            int timeInterval = 0;
            
            token = new TokenInfo();
            Element deviceElem = node.element("DeviceInfo");
            //令牌号
            Element serialNoNode = deviceElem.element("SerialNo");
            if (null != serialNoNode) {
                otpMap.put(TOKEN_NUM, serialNoNode.getText());
            }
            //客户编号
            Element custnameNode = deviceElem.element("Manufacturer");
            if (null != custnameNode) {
                otpMap.put(CUST_NUM, custnameNode.getText());
            }
            
            //令牌规格
            Element keyElem = node.element("Key");
            if (null != keyElem) {
                String algorithm = keyElem.attributeValue("Algorithm");
                otpMap.put(TOKEN_TYPE, algorithm.substring(algorithm.lastIndexOf(":")+1));
            }
            
            //令牌长度
            Element algParamNode = keyElem.element("AlgorithmParameters");
            Element responseFormatNode = algParamNode.element("ResponseFormat");
            if (null != responseFormatNode) {
                otpMap.put(TOKEN_LEN, responseFormatNode.attributeValue("Length"));
            }
            
            if (otpMap.size() < 1) {
                return null;
            }
            
            //Data
            Element dataNode = keyElem.element("Data");
            String pubkey = null;
            if (null != dataNode) {
                Element secretNode = dataNode.element("Secret");
                //解析存在bin文件的
                if (null != encryptionNode) {
                    
                    Element saltNode = encryptionNode.element("salt");
                    //算法类型
                    String desType = null;
                    //加密后种子
                    String cipherValue = null;
                    if (null != secretNode) {
                        Element enctElem = secretNode.element("EncryptedValue");
                        if (null != enctElem) {
                            Element encmedElem = enctElem.element("EncryptionMethod");
                            String algoAttr = encmedElem.attributeValue("Algorithm");
                            if (null != algoAttr) {
                                String arrs[] = algoAttr.split("#");
                                desType = arrs[1];
                            }
                            Element ciphdElem = enctElem.element("CipherData");
                            if (null != ciphdElem) {
                                Element ciphvEm = ciphdElem.element("CipherValue");
                                cipherValue = ciphvEm.getText();
                            }
                        }
                    }
                    
                    if (null == cipherValue) {
                        return null;
                    }
                    
                    //PBE-AES算法
                    if (null != saltNode) {
                        
                        byte[] pinkey = getPBEKey(key, encryptionNode);
                        byte[] mackey = Base64.decode(cipherValue);
                        pubkey = AlgConvert.bytes2HexString(AESUtil.PBEDescrypty(pinkey, mackey, StrConstant.FILTYPE_PKCS5PADDING));
                    } else {
                        if (null != desType && desType.equals("tripledes-cbc")) {
                            pubkey = DESUtil.DES3Decrypt(StrConstant.FILTYPE_PKCS5PADDING, key, cipherValue);
                        } else if (null != desType && desType.equals("aes256-cbc")) {
                            pubkey = AESUtil.AESDecrypt(StrConstant.FILTYPE_ISO10126PADDING, cipherValue, key);
                        } else if (null != desType && desType.equals("aes128-cbc")) {
                            pubkey = AESUtil.AESDecrypt(StrConstant.FILTYPE_PKCS5PADDING, cipherValue, key);
                        }
                    }
                } else {
                    if (null != secretNode) {
                        Element plainValueNode = secretNode.element("PlainValue");
                        if (null != plainValueNode.getText()) {
                            pubkey = AlgConvert.base64DecCode(plainValueNode.getText());
                        }
                    }
                }
                
                //Time
                Element timeNode = dataNode.element("Time");
                String timeStr = "";
                if (null != timeNode) {
                    Element plainValueNode = timeNode.element("PlainValue");
                    if (null != plainValueNode.getText()) {
                        timeStr = plainValueNode.getText();
                    } 
                }
                //TimeInterval
                Element timeIntervalNode = dataNode.element("TimeInterval");
                if (null != timeIntervalNode) {
                    Element plainValueNode = timeIntervalNode.element("PlainValue");
                    if (null != plainValueNode.getText()) {
                        timeInterval = Integer.parseInt(plainValueNode.getText());
                    } 
                }
                
            }
            Element policyNode = keyElem.element("Policy");
            int startdate = 0;
            int expirydate = 0;
            if (null != policyNode) {
                Element startDateNode = policyNode.element("StartDate");
                if (null != startDateNode.getText()) {
                    startdate = DateTool.dateToInt(DateTool.strZToDate(startDateNode.getText()));
                } 
                
                Element expiryDateNode = policyNode.element("ExpiryDate");
                if (null != expiryDateNode.getText()) {
                    expirydate = DateTool.dateToInt(DateTool.strZToDate(expiryDateNode.getText()));      
                } 
            }
            
            String speckey = otpMap.get(TOKEN_TYPE)+"_"+otpMap.get(TOKEN_LEN)+"_"+timeInterval;
            tokenspec = new TokenSpec();
            
            
            String specId = tknSpecMap.get(speckey);
            if (!StrTool.strNotNull(tempSpecMap.get(specId))) {
                tokenspec.setSpecid(tknSpecMap.get(speckey));
                tokenspec.setOtplen(StrTool.parseInt(otpMap.get(TOKEN_LEN)));
                tokenspec.setIntervaltime(timeInterval);
                if (StrTool.strEquals(otpMap.get(TOKEN_TYPE), "totp")) {
                    tokenspec.setTokenType(1);//OATH标准时间型
                } else if (StrTool.strEquals(otpMap.get(TOKEN_TYPE), "hotp")) {
                    tokenspec.setTokenType(0);//OATH标准事件型
                }
                specList.add(tokenspec);
                tempSpecMap.put(specId, specId);
                objMap.put(TokenXmlConstant.MAP_SPEC, specList);
            }
            
            token.setToken(otpMap.get(TOKEN_NUM));
            token.setPubkey(pubkey);
            token.setSpecid(tknSpecMap.get(speckey));
            if (StrTool.strEquals(otpMap.get(TOKEN_TYPE), "totp")) {
                token.setProducttype(1);
            }else if (StrTool.strEquals(otpMap.get(TOKEN_TYPE), "hotp")) {
                token.setProducttype(0);
            }
            token.setPubkeystate(NumConstant.common_number_na_1);
            token.setPhysicaltype(0);
            token.setExpiretime(expirydate);
            tokenList.add(token);
            
            perCount++;
            perIndex = (perCount * 40) / keyPackageNode.size();
            linkUser.setPercent(perIndex);
        }
        //清空map
        if (null != tempSpecMap && tempSpecMap.size() > 0) {
            tempSpecMap.clear();
        }
        
        objMap.put(TokenXmlConstant.MAP_TOKEN, tokenList);
        
        return objMap;
    }
    
    /**
     * 获取pin key
     * @Date in Nov 4, 2013,1:17:57 PM
     * @param deskey
     * @param enkeyEle
     * @return
     * @throws IOException
     */
    public byte[] getPBEKey(byte[] deskey, Element enkeyEle) throws IOException {
        byte[] saltVal = null;
        int counter = 0;
        int keyLength = 0;
        if (null != enkeyEle) {
            Element saltEle = enkeyEle.element("salt");
            if (null != saltEle) {
                Element specifiedEle = saltEle.element("specified");
                saltVal = Base64.decode(specifiedEle.getText());
            }
            Element countEle = enkeyEle.element("iterationcount");
            if (null != countEle) {
                counter = Integer.parseInt(countEle.getText());
            }
            Element keylengthEle = enkeyEle.element("keylength");
            if (null != keylengthEle) {
                keyLength = Integer.parseInt(keylengthEle.getText());
            }
        }

        if (!StrTool.byteNotNull(saltVal) || counter <= 0 || keyLength <= 0) {
            return null;
        }

        return AESUtil.getKey(deskey, saltVal, counter, keyLength);

    }
    
    /**
     * 取得令牌密钥文件密钥
     * @Date in Nov 12, 2013,1:29:56 PM
     * @param keyFile
     * @param fileName
     * @param keyPass
     * @return
     * @throws Exception
     */
    public byte[] getDecKey(File keyFile, String fileName, String keyPass) throws Exception {
        byte[] key = null;
        //密钥为.bin文件
        if (fileName.toLowerCase().endsWith(Constant.FILE_BIN)) {
            key = Base64.decode(CertKeyUtil.getFileContent(keyFile, fileName));
        }
        return key;
    }
    
    /**
     * 取得会话对象
     * 
     * @Date in Jun 21, 2012,3:26:36 PM
     * @return
     */
    private HttpSession getSession() {
        return ServletActionContext.getRequest().getSession();
    }
}
