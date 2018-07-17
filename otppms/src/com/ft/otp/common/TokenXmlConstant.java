/**
 *Administrator
 */
package com.ft.otp.common;

/**
 * XML令牌文件属性常量文件
 *
 * @Date in Mar 26, 2013,3:32:06 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class TokenXmlConstant {

    public static final String MAP_SPEC = "specMap"; //存放令牌规格
    public static final String MAP_TOKEN = "tokenMap"; //存放令牌
    public static final String MAP_MOBILE = "mobileMap"; //存放手机令牌

    /**
     * 令牌规格
     */
    /**容器*/
    public static final String SPEC_TOKENSPEC = "tokenspec"; //令牌规格存放的容器
    public static final String SPEC_SPEC = "spec"; //规格
    public static final String SPEC_SPEC_ID = "id"; //规格ID

    /**属性*/
    public static final String SPEC_TOKENTYPE = "tokentype";//令牌类型
    public static final String SPEC_ALGID = "algid";//令牌算法
    public static final String SPEC_SIGNSUITE = "signsuite";//交易签名suite
    public static final String SPEC_CVSSUITE = "cvssuite";//主机认证suite
    public static final String SPEC_CRSUITE = "crsuite";//挑战应答suite
    public static final String SPEC_ATID = "atid";//手机令牌组合类型(00、01、02、03、04、05、06、07)
    public static final String SPEC_OTPLEN = "otplen";//动态口令长度
    public static final String SPEC_INTERVALTIME = "intervaltime";//时间间隔
    public static final String SPEC_BEGINTIME = "begintime";//起始时间
    public static final String SPEC_MAXAUTHCNT = "maxauthcnt";//最大的认证次数（刮刮卡、矩阵卡）
    public static final String SPEC_INITAUTHNUM = "initauthnum";//初始认证基数（刮刮卡）
    public static final String SPEC_HAFORMAT = "haformat";//主机认证刮刮卡主机认证码格式
    public static final String SPEC_HALEN = "halen";//主机认证码长度
    public static final String SPEC_CARDROW = "cardrow";//矩阵卡行数
    public static final String SPEC_CARDCOL = "cardcol";//矩阵卡列数
    public static final String SPEC_ROWSTART = "rowstart";//矩阵卡行起始字符
    public static final String SPEC_COLSTART = "colstart";//矩阵卡列起始字符
    //密钥更新模式（disable(0)、令牌及服务器共同主导(1)、令牌主导(2)、服务器主导(3)）
    public static final String SPEC_UPDATEMODE = "updatemode";
    public static final String SPEC_UPDATELIMIT = "updatelimit";//密钥更新次数（0：不限制、1：只能一次）
    //密钥更新应答长度（对于密钥更新模式为令牌及服务器共同主导（该值为8）
    //和服务器主导时（该值为12（工行C300）或 8（稠州银行Z300）），该值为非0正值）
    public static final String SPEC_UPDATERESPLEN = "updateresplen";
    //一级解锁码解锁模式（disable(0)、时间型(1)、挑战型(2)）
    public static final String SPEC_PUK1MODE = "puk1mode";
    public static final String SPEC_PUK1LEN = "puk1len";//一级解锁码解锁码长度（6位、8位等)
    public static final String SPEC_PUK1ITV = "puk1itv";//一级解锁码时钟周期（7200秒等，当模式为时间型时有效）
    public static final String SPEC_PUK2MODE = "puk2mode";//二级解锁码解锁模式（disable（0）、时间型（1）、挑战型（2））
    public static final String SPEC_PUK2LEN = "puk2len";//二级解锁码解锁码长度（6位、8位等）
    public static final String SPEC_PUK2ITV = "puk2itv";//二级解锁码时钟周期（7200秒等，当模式为时间型时有效）
    public static final String SPEC_MAXCOUNTER = "maxcounter";//T内C令牌的周期最大计数值
    //0和1表示使用两个普通口令进行同步、2使用挑战型口令+普通口令进行同步、4使用两个挑战应答口令同步
    public static final String SPEC_SYNCMODE = "syncmode";

    /**
     * 令牌
     */
    /**容器*/
    public static final String TOKEN_TOKENLIST = "tokenlist"; //令牌存放的容器
    public static final String TOKEN_TOKEN = "token"; //令牌

    /**属性*/
    public static final String TOKEN_SN = "sn"; //令牌号
    public static final String TOKEN_SPECID = "specid";//令牌规格
    public static final String TOKEN_PHYSICALTYPE = "physicaltype";//物理类型
    public static final String TOKEN_PRODUCTTYPE = "producttype";//产品类型
    public static final String TOKEN_DEATH = "death";//过期时间
    public static final String TOKEN_SEED = "seed";//令牌种子
    public static final String TOKEN_VALUE = "value";//密钥值
    public static final String TOKEN_MACVALUE = "macvalue";//HMAC值
    public static final String TOKEN_CIPHER = "cipher";//密钥加密方式

    public static final String TOKEN_SEEDFACTOR = "seedfactor";//手机令牌因子

    public static final String TOKEN_PLAIN = "plain";//明文种子
    public static final String TOKEN_ENCRYPTIONKEY = "encryptionkey";//加密密钥
    public static final String TOKEN_SYMMETRIC = "symmetric";//对称
    public static final String TOKEN_TRIPLEDES_CBC = "tripledes-cbc";//CBC模式
    public static final String TOKEN_RSA_1_5 = "rsa-1_5";//RSA
    public static final String TOKEN_SALT = "salt";//盐
    public static final String TOKEN_ITERATIONCOUNT = "iterationcount";//迭代次数
    public static final String TOKEN_KEYLENGTH = "keylength";//密钥长度
    public static final String TOKEN_MACKEY = "mackey";//校验和    
    public static final String TOKEN_CERT = "cert";//证书
    public static final String TOKEN_TYPE = "type";//类型

}
