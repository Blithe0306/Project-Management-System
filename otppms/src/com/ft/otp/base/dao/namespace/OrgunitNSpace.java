package com.ft.otp.base.dao.namespace;

/**
 * 组织机构 命名空间说明
 *
 * @Date in January 22, 2013,10:00:00 PM
 *
 * @author BYL
 */
public class OrgunitNSpace {
   
	//组织结构 命名空间
    public static final String ORGUNIT_NS = "orgunit_info";

    //查询组织机构 列表
    public static final String ORGUNIT_INFO_SELECT_OI = "selectOI";
    public static final String ORGUNIT_INFO_SELECT_WOI = "selectWOI";
    public static final String ORGUNIT_INFO_SELECT_ORG = "selectORG";
    //插入组织机构
    public static final String ORGUNIT_INFO_INSERT_OI = "insertOI";
    //查找组织机构 单
    public static final String ORGUNIT_INFO_FIND_OI = "findOI";
    //根据机构名构查询
    public static final String ORGUNIT_INFO_FIND_OR = "findOR";
    //删除组织机构
    public static final String ORGUNIT_INFO_DELETE_OI = "deleteOI";
    //更新组织机构
    public static final String ORGUNIT_INFO_UPDATE_OI = "updateOI";
    //统计管理员
    public static final String ORGUNIT_INFO_COUNT_OI = "countOI";
    
    //查询组织机构下组织机构
    public static final String ORGUNIT_INFO_SELECT_ORG_SONORG = "selectOrgSon";
    
}
