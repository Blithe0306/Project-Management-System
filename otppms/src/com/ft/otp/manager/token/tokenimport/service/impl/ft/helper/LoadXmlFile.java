/**
 *Administrator
 */
package com.ft.otp.manager.token.tokenimport.service.impl.ft.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Attribute;
import org.dom4j.Element;

import com.ft.otp.common.Constant;
import com.ft.otp.common.LicenseConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.TokenXmlConstant;
import com.ft.otp.common.XmlConstant;
import com.ft.otp.common.config.LicenseConfig;
import com.ft.otp.common.config.SystemConfig;
import com.ft.otp.manager.login.entity.LinkUser;
import com.ft.otp.manager.login.service.OnlineUsers;
import com.ft.otp.manager.token.distmanager.entity.DistManagerInfo;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.tokenspec.entity.TokenSpec;
import com.ft.otp.util.alg.AESUtil;
import com.ft.otp.util.alg.AlgConvert;
import com.ft.otp.util.alg.AlgHelper;
import com.ft.otp.util.alg.Base64;
import com.ft.otp.util.alg.CertKeyUtil;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 加载XML种子文件
 *
 * @Date in Mar 26, 2013,2:06:30 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class LoadXmlFile {

    /**
     * 取得令牌私有数据解密密钥
     * 
     * @Date in Mar 26, 2013,8:59:49 PM
     * @param keyFile
     * @param fileName
     * @param keyPass
     * @return
     */
    public byte[] getDecKey(File keyFile, String fileName, String keyPass) throws Exception {
        byte[] key = null;
        //密钥为.bin文件
        if (fileName.toLowerCase().endsWith(Constant.FILE_BIN)) {
            key = Base64.decode(CertKeyUtil.getFileContent(keyFile, fileName));
        }
        //私钥.p12文件
        else if (fileName.toLowerCase().endsWith(Constant.FILE_P12)) {
            key = AESUtil.loadPriKey(keyFile, keyPass).getEncoded();
        }

        return key;
    }

    public byte[] getPBEKey(Element rootElement) throws IOException {
        String owner = (String) LicenseConfig.getValue(LicenseConstant.OTP_SERVER_LIC_OWNER);//授权客户编号
        Element enkeyEle = rootElement.element(TokenXmlConstant.TOKEN_ENCRYPTIONKEY);
        if (enkeyEle == null) {
            return null;
        }
        byte[] saltVal = Base64.decode(enkeyEle.element(TokenXmlConstant.TOKEN_SALT).getText());
        int counter = StrTool.parseInt(enkeyEle.element(TokenXmlConstant.TOKEN_ITERATIONCOUNT).getText());
        int keyLength = StrTool.parseInt(enkeyEle.element(TokenXmlConstant.TOKEN_KEYLENGTH).getText());

        if (!StrTool.strNotNull(owner) || !StrTool.byteNotNull(saltVal) || counter <= 0 || keyLength <= 0) {
            return null;
        }

        return AESUtil.getKey(owner.getBytes(), saltVal, counter, keyLength);
    }

    public byte[] getHMacKey(byte[] key, Element rootElement) throws Exception {
        byte[] mackey = Base64.decode(rootElement.element(TokenXmlConstant.TOKEN_MACKEY).getText());

        return AESUtil.PBEDescrypty(key, mackey, StrConstant.FILTYPE_PKCS5PADDING);
    }

    /**
     * 获取XML容器中的令牌规格数据
     * 
     * @Date in Mar 26, 2013,4:04:02 PM
     * @param rootElement
     * @return
     */
    public List<TokenSpec> getTokenSpecs(Element rootElement) throws Exception {
        List<TokenSpec> specList = new ArrayList<TokenSpec>();
        //tokenspec
        Element tokenSpecNode = rootElement.element(TokenXmlConstant.SPEC_TOKENSPEC);
        //ͳspec
        if (null == tokenSpecNode) {
            return null;
        }
        List<?> specsNodes = tokenSpecNode.elements(TokenXmlConstant.SPEC_SPEC);
        TokenSpec tokenspec = null;
        for (Iterator<?> rootIter = specsNodes.iterator(); rootIter.hasNext();) {
            tokenspec = new TokenSpec();
            String specid = null;
            Element specnode = (Element) rootIter.next();
            Attribute keyAlgorithmAttr = specnode.attribute(TokenXmlConstant.SPEC_SPEC_ID);
            specid = keyAlgorithmAttr.getValue();
            tokenspec.setSpecid(specid);
            //tokentype
            Element tokentypeNode = specnode.element(TokenXmlConstant.SPEC_TOKENTYPE);
            if (null != tokentypeNode) {
                tokenspec.setTokenType(StrTool.parseInt(tokentypeNode.getText()));
            }
            //algid
            Element algidNode = specnode.element(TokenXmlConstant.SPEC_ALGID);
            if (null != algidNode) {
                tokenspec.setAlgid(StrTool.parseInt(algidNode.getText()));
            }
            //otplen
            Element otplenNode = specnode.element(TokenXmlConstant.SPEC_OTPLEN);
            if (null != otplenNode) {
                tokenspec.setOtplen(StrTool.parseInt(otplenNode.getText()));
            }
            //intervaltime
            Element intervaltimeNode = specnode.element(TokenXmlConstant.SPEC_INTERVALTIME);
            if (null != intervaltimeNode) {
                tokenspec.setIntervaltime(StrTool.parseInt(intervaltimeNode.getText()));
            }
            //signsuite
            Element signsuiteNode = specnode.element(TokenXmlConstant.SPEC_SIGNSUITE);
            if (null != signsuiteNode) {
                tokenspec.setSignsuite(signsuiteNode.getText());
            }
            //cvssuite
            Element cvssuiteNode = specnode.element(TokenXmlConstant.SPEC_CVSSUITE);
            if (null != cvssuiteNode) {
                tokenspec.setCvssuite(cvssuiteNode.getText());
            }
            //crsuite
            Element crsuiteNode = specnode.element(TokenXmlConstant.SPEC_CRSUITE);
            if (null != crsuiteNode) {
                tokenspec.setCrsuite(crsuiteNode.getText());
            }
            //initauthnum
            Element initauthnumNode = specnode.element(TokenXmlConstant.SPEC_INITAUTHNUM);
            if (null != initauthnumNode) {
                tokenspec.setInitauthnum(StrTool.parseInt(initauthnumNode.getText()));
            }

            //maxauthcnt
            Element maxauthcntNode = specnode.element(TokenXmlConstant.SPEC_MAXAUTHCNT);
            if (null != maxauthcntNode) {
                tokenspec.setMaxauthcnt(StrTool.parseInt(maxauthcntNode.getText()));
            }
            //halen
            Element halenNode = specnode.element(TokenXmlConstant.SPEC_HALEN);
            if (null != halenNode) {
                tokenspec.setHalen(StrTool.parseInt(halenNode.getText()));
            }
            //haformat
            Element haformatNode = specnode.element(TokenXmlConstant.SPEC_HAFORMAT);
            if (null != haformatNode) {
                tokenspec.setHaformat(StrTool.parseInt(haformatNode.getText()));
            }
            //cardrow
            Element cardrowNode = specnode.element(TokenXmlConstant.SPEC_CARDROW);
            if (null != cardrowNode) {
                tokenspec.setCardrow(StrTool.parseInt(cardrowNode.getText()));
            }
            //cardcol
            Element cardcolNode = specnode.element(TokenXmlConstant.SPEC_CARDCOL);
            if (null != cardcolNode) {
                tokenspec.setCardcol(StrTool.parseInt(cardcolNode.getText()));
            }
            //rowstart
            Element rowstartNode = specnode.element(TokenXmlConstant.SPEC_ROWSTART);
            if (null != rowstartNode) {
                tokenspec.setRowstart(rowstartNode.getText());
            }
            //colstart
            Element colstartNode = specnode.element(TokenXmlConstant.SPEC_COLSTART);
            if (null != colstartNode) {
                tokenspec.setColstart(colstartNode.getText());
            }
            //atid
            Element atidNode = specnode.element(TokenXmlConstant.SPEC_ATID);
            if (null != atidNode) {
                tokenspec.setAtid(atidNode.getText());
            }

            //updatemode
            Element updatemodeNode = specnode.element(TokenXmlConstant.SPEC_UPDATEMODE);
            if (null != updatemodeNode) {
                tokenspec.setUpdatemode(StrTool.parseInt(updatemodeNode.getText()));
            }
            //updatelimit
            Element updatelimitNode = specnode.element(TokenXmlConstant.SPEC_UPDATELIMIT);
            if (null != updatelimitNode) {
                tokenspec.setUpdatelimit(StrTool.parseInt(updatelimitNode.getText()));
            }
            //updateresplen
            Element updateresplenNode = specnode.element(TokenXmlConstant.SPEC_UPDATERESPLEN);
            if (null != updateresplenNode) {
                tokenspec.setUpdateresplen(StrTool.parseInt(updateresplenNode.getText()));
            }
            //puk1mode
            Element puk1modeNode = specnode.element(TokenXmlConstant.SPEC_PUK1MODE);
            if (null != puk1modeNode) {
                tokenspec.setPuk1mode(StrTool.parseInt(puk1modeNode.getText()));
            }
            //puk1len
            Element puk1lenNode = specnode.element(TokenXmlConstant.SPEC_PUK1LEN);
            if (null != puk1lenNode) {
                tokenspec.setPuk1len(StrTool.parseInt(puk1lenNode.getText()));
            }
            //puk1itv
            Element puk1itvNode = specnode.element(TokenXmlConstant.SPEC_PUK1ITV);
            if (null != puk1itvNode) {
                tokenspec.setPuk1itv(StrTool.parseInt(puk1itvNode.getText()));
            }
            //puk2mode
            Element puk2modeNode = specnode.element(TokenXmlConstant.SPEC_PUK2MODE);
            if (null != puk2modeNode) {
                tokenspec.setPuk2mode(StrTool.parseInt(puk2modeNode.getText()));
            }
            //puk2len
            Element puk2lenNode = specnode.element(TokenXmlConstant.SPEC_PUK2LEN);
            if (null != puk2lenNode) {
                tokenspec.setPuk2len(StrTool.parseInt(puk2lenNode.getText()));
            }
            //puk2itv
            Element puk2itvNode = specnode.element(TokenXmlConstant.SPEC_PUK2ITV);
            if (null != puk2itvNode) {
                tokenspec.setPuk2itv(StrTool.parseInt(puk2itvNode.getText()));
            }
            //begintime
            Element begintimeNode = specnode.element(TokenXmlConstant.SPEC_BEGINTIME);
            if (null != begintimeNode) {
                tokenspec.setBegintime(StrTool.parseInt(begintimeNode.getText()));
            }
            //maxcounter
            Element maxcounterNode = specnode.element(TokenXmlConstant.SPEC_MAXCOUNTER);
            if (null != maxcounterNode) {
                tokenspec.setMaxcounter(StrTool.parseInt(maxcounterNode.getText()));
            }
            //syncmode
            Element syncmodeNode = specnode.element(TokenXmlConstant.SPEC_SYNCMODE);
            if (null != syncmodeNode) {
                tokenspec.setSyncmode(StrTool.parseInt(syncmodeNode.getText()));
            }

            specList.add(tokenspec);
        }

        return specList;
    }

    /**
     * 遍历获取令牌数据
     * 
     * @Date in Mar 26, 2013,6:30:34 PM
     * @param rootElement
     * @param key
     * @param objMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public ConcurrentHashMap<String, Object> getTokens(Element rootElement, byte[] key, byte[] macKey,
            ConcurrentHashMap<String, Object> objMap) throws Exception {
        List<DistManagerInfo> mobileList = new ArrayList<DistManagerInfo>();
        List<TokenInfo> tokenList = new ArrayList<TokenInfo>();

        //tokenlist
        Element elements = rootElement.element(TokenXmlConstant.TOKEN_TOKENLIST);
        if (null == elements) {
            return null;
        }
        List<?> tokenNodes = elements.elements(TokenXmlConstant.TOKEN_TOKEN);
        List<TokenSpec> specList = (List<TokenSpec>) objMap.get(TokenXmlConstant.MAP_SPEC);

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

        for (Iterator<?> iter = tokenNodes.iterator(); iter.hasNext();) {
            TokenInfo tokenInfo = new TokenInfo();
            String specId = "";//令牌规格ID
            String tokenSn = "";//令牌号
            int physicaltype = 0;//物理类型
            int productType = 0;//产品类型

            Element element = (Element) iter.next();
            Attribute snAttr = element.attribute(TokenXmlConstant.TOKEN_SN);
            tokenSn = snAttr.getValue();
            Attribute specIdAttr = element.attribute(TokenXmlConstant.TOKEN_SPECID);
            specId = specIdAttr.getValue();
            Attribute physicaltypeAttr = element.attribute(TokenXmlConstant.TOKEN_PHYSICALTYPE);
            physicaltype = StrTool.parseInt(physicaltypeAttr.getValue());
            Attribute productTypeAttr = element.attribute(TokenXmlConstant.TOKEN_PRODUCTTYPE);
            productType = StrTool.parseInt(productTypeAttr.getValue());

            String[] pubkeyAndFac = null;
            //解析pubkey 和 pubkeyfactor
            try {
                pubkeyAndFac = getPubkeyAndFacStr(element, rootElement, key, macKey, physicaltype, tokenSn);
            } catch (Exception ex) {
                continue;
            }

            String pubkey = "";
            String pubkeyfactor = "";
            if (StrTool.objNotNull(pubkeyAndFac)) {
                pubkey = pubkeyAndFac[0];
                pubkeyfactor = pubkeyAndFac[1];
            } else {
                continue;
            }

            //获取令牌规格
            TokenSpec tknSpec = null;
            for (TokenSpec tSpec : specList) {
                if (tSpec.getSpecid().equals(specId)) {
                    tknSpec = tSpec;
                    break;
                }
            }

            //区分c300令牌 判断规格中密钥更新模式updatemode为0表示不需要激活 pubkeystate 为-1、否则需要激活 pubkeystate 为0
            if (tknSpec.getUpdatemode() != 0) {//c300
                tokenInfo.setPubkeystate(NumConstant.common_number_0);
            } else {
                tokenInfo.setPubkeystate(NumConstant.common_number_na_1);
            }

            //手机令牌
            if (physicaltype == NumConstant.MOBILE_TOKEN) {
                DistManagerInfo disInfo = new DistManagerInfo();
                disInfo.setToken(snAttr.getValue());
                disInfo.setPubkeyfactor(pubkeyfactor);
                disInfo.setExttype(tknSpec.getAtid());
                disInfo.setActived(0);
                disInfo.setApretry(0);
                disInfo.setProvtype(-1); // 默认未分发
                mobileList.add(disInfo);
            }

            Element deathElem = element.element(TokenXmlConstant.TOKEN_DEATH);
            //令牌有效期
            int expiretime = DateTool.dateToInt(DateTool.strZToDate(deathElem.getText()));
            tokenInfo.setSpecid(specId);
            tokenInfo.setToken(tokenSn);
            tokenInfo.setPubkey(pubkey);
            tokenInfo.setPhysicaltype(physicaltype);
            tokenInfo.setProducttype(productType);
            tokenInfo.setExpiretime(expiretime);
            tokenList.add(tokenInfo);

            perCount++;
            perIndex = (perCount * 40) / tokenNodes.size();
            linkUser.setPercent(perIndex);
        }

        objMap.put(TokenXmlConstant.MAP_TOKEN, tokenList);
        objMap.put(TokenXmlConstant.MAP_MOBILE, mobileList);

        return objMap;
    }

    private static byte[] HMacSHA1(byte[] key, byte[] text) {
        if (key == null || text == null) {
            return null;
        }

        Mac hmac;
        try {
            hmac = Mac.getInstance("HmacSHA1");
            SecretKeySpec keySpec = new SecretKeySpec(key, "RAW");
            hmac.init(keySpec);
            return hmac.doFinal(text);
        } catch (Exception ex) {
            ex.printStackTrace();

            return null;
        }

    }

    /**
     * 获取令牌种子数据
     * 
     * @Date in Mar 27, 2013,1:40:13 PM
     * @param tokennodeElem
     * @param rootEle
     * @param key
     * @param physicaltype
     * @return
     * @throws Exception
     */
    //FIXME:后续需要进行下面方法代码的调整
    private String[] getPubkeyAndFacStr(Element tokennodeElem, Element rootEle, byte[] key, byte[] macKey,
            int physicaltype, String tokenSn) throws Exception {
        String pubkeyAndFac[] = new String[2];

        String pubkey = "";//明文种子
        String pubkeyfac = "";//手机种子因子
        byte[] bPubkey = null;
        byte[] bPubkeyFac = null;

        boolean flagSeed = false;//是否解析种子
        boolean flagFac = false;//是否解析手机种子因子

        //硬件令牌相关
        Element seedElem = tokennodeElem.element(TokenXmlConstant.TOKEN_SEED);
        byte[] pubkeyData = null;
        byte[] macValue = null;
        String desType = "";//解密方式
        if (null != seedElem) {
            flagSeed = true;
            String pubkeyText = seedElem.element(TokenXmlConstant.TOKEN_VALUE).getText();
            pubkeyData = Base64.decode(pubkeyText);
            desType = seedElem.attribute(TokenXmlConstant.TOKEN_CIPHER).getValue();
            macValue = Base64.decode(seedElem.element(TokenXmlConstant.TOKEN_MACVALUE).getText());
        }

        //手机令牌相关
        byte[] facData = null;
        byte[] facMacValue = null;
        if (physicaltype == NumConstant.MOBILE_TOKEN) {
            Element seedfactorElem = tokennodeElem.element(TokenXmlConstant.TOKEN_SEEDFACTOR);
            if (null != seedfactorElem) {
                flagFac = true;
                Element valuefacElem = seedfactorElem.element(TokenXmlConstant.TOKEN_VALUE);
                facData = Base64.decode(valuefacElem.getText());
                facMacValue = Base64.decode(seedfactorElem.element(TokenXmlConstant.TOKEN_MACVALUE).getText());
            }
        }

        if (StrTool.objNotNull(desType) && desType.equals(TokenXmlConstant.TOKEN_PLAIN)) {//明文 无encryptionkey节点
            if (flagSeed) {
                pubkey = AlgHelper.bytes2HexString(pubkeyData);
            }
            if (flagFac) {
                pubkeyfac = new String(facData);
            }
        } else {
            Element enkeyEle = rootEle.element(TokenXmlConstant.TOKEN_ENCRYPTIONKEY);
            String enkeyType = enkeyEle.attributeValue(TokenXmlConstant.TOKEN_TYPE);
            if (enkeyType.equals(TokenXmlConstant.TOKEN_SYMMETRIC)) {//对称的
                if (desType.equals(TokenXmlConstant.TOKEN_TRIPLEDES_CBC)) {//3des
                    if (flagSeed) {
                        pubkey = AESUtil.DES3Descrypty(key, pubkeyData);
                    }
                    if (flagFac) {
                        pubkeyfac = AESUtil.DES3Descrypty(key, facData);//获取手机种子因子明文
                    }
                } else if (desType.equals(TokenXmlConstant.TOKEN_RSA_1_5)) {//rsa
                    byte[] cert = Base64.decode(enkeyEle.element(TokenXmlConstant.TOKEN_CERT).getText());
                    if (flagSeed) {
                        pubkey = AESUtil.decryptByPrivateKey(pubkeyData, key);
                    }
                    if (flagFac) {
                        pubkeyfac = AESUtil.decryptByPrivateKey(facData, key);
                    }
                } else {//AES
                    if (flagSeed) {
                        pubkey = AESUtil.pubkeyDecrypt(pubkeyData, key, StrConstant.FILTYPE_PKCS5PADDING);//获取解密密钥
                    }
                    if (flagFac) {
                        pubkeyfac = AESUtil.pubkeyDecrypt(facData, key, StrConstant.FILTYPE_PKCS5PADDING);//获取手机种子因子明文
                    }
                }
            } else {//PBE
                if (flagSeed) { //解析种子
                    bPubkey = AESUtil.PBEDescrypty(key, pubkeyData, StrConstant.FILTYPE_PKCS5PADDING);
                    byte[] hash = HMacSHA1(macKey, pubkeyData);
                    if (!AlgHelper.bytesCompare(hash, macValue)) { // Mac值不匹配
                        return null;
                    }
                }
                if (flagFac) {//解析手机因子
                    bPubkeyFac = AESUtil.PBEDescrypty(key, facData, StrConstant.FILTYPE_PKCS5PADDING);
                    pubkeyfac = AlgConvert.bytes2HexString(bPubkeyFac);

                    byte[] hash = HMacSHA1(macKey, facData);
                    if (!AlgHelper.bytesCompare(hash, facMacValue)) { // Mac值不匹配
                        return null;
                    }
                }
            }
        }

        //授权类型
        Object object = LicenseConfig.getValue(LicenseConstant.OTP_SERVER_LIC_LICTYPE);
        int licType = 0;
        if (null != object) {
            licType = (Integer) object;
        }
        if (licType != LicenseConstant.LICENSE_TYPE_EVALUATION && StrTool.byteNotNull(TokenCryptoUtil.aesKey)) {//非评估授权，加密令牌种子
            String tpyrcne = SystemConfig.getValue(XmlConstant.XML_TOKEN_TPYRCNE);
            if (StrTool.strEqualsIgnoreCase(tpyrcne, "n")) {
                pubkey = AlgConvert.bytes2HexString(bPubkey);
            } else {
                pubkey = TokenCryptoUtil.encryptPubkey(tokenSn, bPubkey);
            }
        } else {
            pubkey = AlgConvert.bytes2HexString(bPubkey);
        }

        pubkeyAndFac[0] = pubkey;
        pubkeyAndFac[1] = pubkeyfac;

        return pubkeyAndFac;
    }

    /**
     * 封装令牌导入的域和组织机构
     * @Date in Apr 28, 2011,6:11:38 PM
     * @param deb
     * @return
     * @throws Exception
     */
    public TokenInfo setDomainAndOrgunit(TokenInfo tokenInfo, String orgunitStr) {
        if (StrTool.strNotNull(orgunitStr)) {
            String orgunits = orgunitStr.substring(0, orgunitStr.length() - 1);
            String orgunit[] = orgunits.split(":");
            //组织机构所在的域
            if (StrTool.strNotNull(orgunit[0])) {
                tokenInfo.setDomainid(StrTool.parseInt(orgunit[0]));
            }
            //组织机构
            if (StrTool.strNotNull(orgunit[1]) && !StrTool.strEquals(StrConstant.common_number_0, orgunit[1])) {
                tokenInfo.setOrgunitid(StrTool.parseInt(orgunit[1]));
            }
        }

        return tokenInfo;
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
