package com.ft.otp.base.dao.namespace;

/**
 * 令牌命名空间类功能说明
 *
 * @Date in Apr 6, 2011,4:06:08 PM
 *
 * @author TBM
 */
public class TokenNSpace {

    //命名空间
    public static final String TOKEN_INFO_NS = "token_info";
    //令牌分发命名空间
    public static final String TOKEN_DIST_NS = "dist_managerInfo";

    //统计令牌列表数量
    public static final String TOKEN_INFO_COUNT_TK = "countTK";
    //统计令牌列表数量根据其他条件
    public static final String TOKEN_INFO_COUNT_TKBC = "countTKBC";
    //统计令牌列表数量根据其他条件
    public static final String TOKEN_INFO_COUNT_CT = "countCT";
    //查询令牌列表
    public static final String TOKEN_INFO_SELECT_TK = "selectTK";
    //查询令牌列表根据其他条件
    public static final String TOKEN_INFO_SELECT_TKBC = "selectTKBC";
    //查询令牌列表，更换令牌
    public static final String TOKEN_INFO_SELECT_CT = "selectCT";

    //查询单个令牌信息（不含PUBKEY）
    public static final String TOKEN_INFO_FIND_TK = "findTK";
    //查询单个令牌信息（含PUBKEY）
    public static final String TOKEN_INFO_FIND_NTK = "findNTK";
    //查询令牌号
    public static final String TOKEN_INFO_FIND_TOKEN = "findToken";
    //删除令牌
    public static final String TOKEN_INFO_DEL_TK = "deleteTK";
    //导入令牌
    public static final String TOKEN_INFO_INSERT_TK = "insertTK";
    //导入手机令牌
    public static final String TOKEN_INFO_INSERT_EXTTK = "insertExtTK";
    //设置令牌应急口令
    public static final String TOKEN_INFO_UPDATE_TK = "updateTK";

    //令牌机构批量或单一的变更、修改、迁移
    public static final String TOKEN_INFO_UPDATE_ORG = "updateTokenOrg";

    //批量更新令牌的启用/停用、锁定/解锁、挂失/解挂、注销等状态
    public static final String TOKEN_INFO_UPDATE_STATE = "updateTokenState";

    //按产品类型分组统计
    public static final String TOKEN_INFO_COUNT_PRODUCTTYPE = "countProducttype";
    //按物理类型分组统计
    public static final String TOKEN_INFO_COUNT_PHYSICALTYPE = "countPhysicaltype";

    //按状态统计
    public static final String TOKEN_INFO_COUNT_PRODUCTSTATE = "countProductstate";

    //软件令牌分发
    public static final String TOKEN_INFO_SOFT_DIST = "distSoftTkn";
    
    //锁定令牌
    public static final String TOKEN_INFO_LOCKED_TK = "lockedTK";

    /**
     * 令牌分发
     */
    //引用标识
    //查询手机令牌列表
    public static final String TOKEN_DIST_SELECT_TD = "selectTD";
    //统计手机令牌数量
    public static final String TOKEN_DIST_COUNT_TD = "countTD";
    //修改手机令牌分发信息
    public static final String TOKEN_DIST_UPDATE_TD = "updateTD";
    //查询单条手机令牌分发信息
    public static final String TOKEN_DIST_FIND_TD = "findTD";

}
