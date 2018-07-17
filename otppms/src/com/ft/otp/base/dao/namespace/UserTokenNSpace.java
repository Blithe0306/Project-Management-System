/**
 *Administrator
 */
package com.ft.otp.base.dao.namespace;

/**
 * iBatis命名空间及引用标识
 * 用户-令牌
 *
 * @Date in Apr 21, 2011,6:53:44 PM
 *
 * @author TBM
 */
public class UserTokenNSpace {

    //命名空间
    public static final String USER_TOKEN_NS = "user_token";

    //添加用户令牌对应关系
    public static final String USER_TOKEN_ADD = "insertUT";

    //解除用户令牌绑定关系
    public static final String USER_TOKEN_DEL = "deleteUT";
    
    //解除除当前用户令牌绑定关系
    public static final String USER_TOKEN_DEL_ONE = "deleteUse";
    
    //用户与令牌绑定关系
    public static final String USER_TOKEN_SEL = "selectUTS";
    
    //用户与令牌绑定关系，查询条件用户ID与域ID，用于解绑操作
    public static final String USER_TOKEN_SEL_BIND = "selectBTH";
    
    //根据用户ID查出此用户所有的绑定令牌号 
    public static final String USER_TOKEN_SELTOKENS = "selectTOKS";

    //解除用户令牌一对多的绑定关系
    public static final String USER_TOKEN_DELS = "deleteUTS";

    //更新用户令牌绑定关系
    public static final String USER_TOKEN_UPDA = "updateUT";

    //根据用户名或令牌查找用户或令牌
    public static final String USER_TOKEN_FIND = "findUT";

    //根据用户名或令牌查找用户或令牌
    public static final String USER_TOKEN_QUERY = "selectUT";

    //根据用户名或令牌统计用户或令牌
    public static final String USER_TOKEN_COUNT = "countUT";

    //根据用户名或令牌批量查找用户或令牌，使用IN方式
    public static final String USER_TOKEN_QUERY_IN = "selectUT_In";

    //根据用户名连接查询用户对应的令牌及其主要信息
    public static final String USER_TOKEN_QUERY_JOIN_TKN = "selectUT_Join_Tkn";
    
    //根据用户账号查询多用户绑定的令牌
    public static final String USER_TOKEN_QUERY_MulUserToken = "queryMulUserToken";
    
    //根据用户令牌号与用户ID查出除此用户下其它用户绑定的同一令牌
    public static final String USER_TOKEN_QUERY_TOKENOTH = "selectOTH";
    
    //根据用户令牌号与用户ID查出除此用户下其它用户绑定的同一令牌
    public static final String USER_QUERY_TOKEN = "selectToken_IN";
    
    //根据管理员ID查出此管理员与令牌的绑定数据 
    public static final String ADMIN_TOKEN_SELADMBINDTOKENS = "selectAdmBindTokens";
    

}
