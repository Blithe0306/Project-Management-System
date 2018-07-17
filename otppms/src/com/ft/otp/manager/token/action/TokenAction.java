/**
 *Administrator
 */
package com.ft.otp.manager.token.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.DomainConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.mail.MailInfo;
import com.ft.otp.common.mail.SendMailUtil;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.soft.SoftTknDistriHelp;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.user.service.IAdminUserServ;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.manager.confinfo.email.service.IEmailInfoServ;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.manager.orgunit.orgunit.service.IOrgunitInfoServ;
import com.ft.otp.manager.token.action.aide.TokenActionAide;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.form.TokenQueryForm;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.tokenspec.entity.TokenSpec;
import com.ft.otp.manager.tokenspec.service.ITokenSpecServ;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.manager.user_token.form.UTokenQueryForm;
import com.ft.otp.manager.user_token.service.IUserTokenServ;
import com.ft.otp.util.alg.dist.RandomPassUtil;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.PwdEncTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 
 * 令牌操作Action控制类
 *
 * @Date in Apr 28, 2011,3:05:25 PM
 *
 * @author TBM
 */
public class TokenAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = 5783679697486689359L;
    private Logger logger = Logger.getLogger(TokenAction.class);

    //用户令牌服务接口
    private IUserTokenServ userTokenServ = (IUserTokenServ) AppContextMgr.getObject("userTokenServ");
    //用户服务接口
    private IUserInfoServ userInfoServ = (IUserInfoServ) AppContextMgr.getObject("userInfoServ");
    //管理员服务接口
    private IAdminUserServ adminUserServ = (IAdminUserServ) AppContextMgr.getObject("adminUserServ");
    //管理中心配置接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");
    //邮件分发服务配置
    private IEmailInfoServ emailServ = (IEmailInfoServ) AppContextMgr.getObject("emailServ");

    //令牌的辅助类
    private TokenActionAide aide = new TokenActionAide();
	//组织机构
    private IOrgunitInfoServ orgunitInfoServ = (IOrgunitInfoServ) AppContextMgr.getObject("orgunitInfoServ");
    //令牌服务接口
    private ITokenServ tokenServ;
    //令牌规格服务接口
    private ITokenSpecServ tokenSpecServ = (ITokenSpecServ) AppContextMgr.getObject("tokenSpecServ");
    private TokenQueryForm tokenQueryForm;
    private TokenInfo tokenInfo;
    //批量查询文件
    private File upFile;
    //批量查询文件名称
    private String upFileName;
    int method = 0;

    //日志记录
    public String tknInfo;

    /**
     * @return the upFile
     */
    public File getUpFile() {
        return upFile;
    }

    /**
     * @param upFile the upFile to set
     */
    public void setUpFile(File upFile) {
        this.upFile = upFile;
    }

    /**
     * @return the upFileName
     */
    public String getUpFileName() {
        return upFileName;
    }

    /**
     * @param upFileName the upFileName to set
     */
    public void setUpFileName(String upFileName) {
        this.upFileName = upFileName;
    }

    /**
     * @return the tokenInfo
     */
    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }

    /**
     * @param tokenInfo the tokenInfo to set
     */
    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    /**
     * @return the tokenServz
     */
    public ITokenServ getTokenServ() {
        return tokenServ;
    }

    /**
     * @param tokenServ the tokenServ to set
     */
    public void setTokenServ(ITokenServ tokenServ) {
        this.tokenServ = tokenServ;
    }

    /**
     * @return the tokenQueryForm
     */
    public TokenQueryForm getTokenQueryForm() {
        return tokenQueryForm;
    }

    /**
     * @param tokenQueryForm the tokenQueryForm to set
     */
    public void setTokenQueryForm(TokenQueryForm tokenQueryForm) {
        this.tokenQueryForm = tokenQueryForm;
    }

    /**
     * 取出QueryForm中的实体
     * @Date in May 5, 2011,6:30:40 PM
     * @param tokenQueryForm
     * @return TokenInfo
     */
    private TokenInfo getTokenInfo(TokenQueryForm tokenQueryForm) throws BaseException {
        TokenInfo tInfo = new TokenInfo();

        if (StrTool.objNotNull(tokenQueryForm)) {
            tInfo = tokenQueryForm.getTokenInfo();
            //封装域或组织机构查询条件
            tInfo = aide.setDomainAndOrgunit(tInfo, tokenQueryForm.getOrgunitIds());
        }

        //来源于top的用户、令牌查询
        if (StrTool.strEquals(super.getSource(request), "top")) {
            if (StrTool.strNotNull(request.getParameter("token"))) {
                tInfo.setToken(request.getParameter("token"));
            }
        }

        if (StrTool.strNotNull(request.getParameter("physicaltype"))) {
            tInfo.setPhysicaltype(StrTool.parseInt(request.getParameter("physicaltype")));
        }

        // 令牌查询来源
        // 1    代表查询来源于令牌分配====》查询列表
        // null 代表来源于令牌管理====》令牌列表
        String tknsource = request.getParameter("tknSource");
        if (StrTool.strNotNull(tknsource)) {
            tInfo.setQueryMark(1);
            tInfo.setBind(0);
        }

        //令牌批量查询=====》获取的令牌号字符串
        String tknbatchSn = request.getParameter("batchTknSn");
        if (StrTool.strNotNull(tknbatchSn)) {
            TokenInfo toInfo = aide.getTknBatch(tknbatchSn);
            tInfo.setBatchIds(toInfo.getBatchIds());
        }

        String curLoginUserId = (String) super.getCurLoginUser();//获得当前管理员id号
        String curLoginUserRoleMark = (String) super.getCurLoginUserRole(); //当前管理员所拥有的角色 对应角色表中的rolemark字段
        if (StrTool.strEquals(curLoginUserRoleMark, "ADMIN")) {//如果是超级管理员
            tInfo.setIsFliterTag(null); //不根据组织机构顾虑
        } else {
            tInfo.setIsFliterTag(1); //根据组织机构顾虑
            tInfo.setCurrentAdminId(curLoginUserId);
        }

        //赋值管理员管理的域
        tInfo = aide.setAdminDomainIds(tInfo, super.getCurLoginUserRole(), super.getCurLoginUser());

        return tInfo;
    }

    /**
     * 初始化令牌列表
     * @Date in Apr 15, 2011,11:33:36 AM
     * @return
     */
    public String init() {
        if (isNeedClearForm()) {
            tokenQueryForm = null;
        }
        try {
            PageArgument pageArg = pageArgument();
            pageArg.setCurPage(getPage());
            pageArg.setPageSize(getPagesize());
            List<?> resultList = query(pageArg);

            String jsonStr = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
            setResponseWrite(jsonStr);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        request.setAttribute("over", "over");
        return null;
    }

    /**
     * 行数统计
     * 分页处理
     * @Date in Apr 15, 2011,11:33:36 AM
     * @return PageArgument
     */
    private PageArgument pageArgument() throws BaseException {
        TokenInfo tknInfo = getTokenInfo(tokenQueryForm);

        int totalRow = tokenServ.count(tknInfo);
        PageArgument pageArg = getArgument(totalRow);

        //令牌分配初始化，返回空数据
        if (StrTool.strEquals(request.getParameter("assignInit"), StrConstant.common_number_1)
                && !StrTool.objNotNull(tokenQueryForm)) {
            pageArg.setTotalRow(0);
        }

        return pageArg;
    }

    /**
     * 令牌列表查询数据处理
     * @Date in Apr 18, 2011,11:48:33 AM
     * @param pageArg
     * @return tokenList
     * @throws  
     */
    private List<?> query(PageArgument pageArg) {
        List<TokenInfo> tokenList = null;
        try {
            TokenInfo tknInfo = getTokenInfo(tokenQueryForm);

            //令牌分配======>初始化，返回空数据
            if (StrTool.strEquals(request.getParameter("assignInit"), StrConstant.common_number_1)
                    && !StrTool.objNotNull(tokenQueryForm)) {
                return new ArrayList();
            }

            tokenList = (List<TokenInfo>) tokenServ.query(tknInfo, pageArg);

            //设置令牌对应的规格数据
            Map<String, Object> specMap = tokenSpecServ.queryAllSpec(new TokenSpec());
            for (TokenInfo tokenInfo : tokenList) {
                TokenSpec tokenSpec = (TokenSpec) specMap.get((String) tokenInfo.getSpecid());
                if (StrTool.objNotNull(tokenSpec)) {
                    // 需要设置令牌规格 列表上获取一、二级解锁码需要用到
                    tokenInfo.setTokenSpec(tokenSpec);
                }

                // 赋值厂商
                //                VendorInfo vendorInfo = (VendorInfo) VendorConfig.getVendorMap().get(tokenInfo.getVendorid());
                //                if (StrTool.objNotNull(vendorInfo)) {
                //                    tokenInfo.setVendorName(vendorInfo.getName());
                //                }

                //设置该用户列表中的 域对象、机构对象、域组织机构id串、域组织机构名字串 
                tokenInfo = aide.setDomainAndOrgunitName(tokenInfo);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return tokenList;
    }

    public String add() {
        return null;
    }

    /**
     * 令牌的删除
     * @Date in Apr 15, 2011,11:33:36 AM
     * @return
     */
    public String delete() {
        Set<?> keys = super.getDataKeys(request);
        try {
            if (StrTool.setNotNull(keys)) {
                tokenServ.delObj(keys);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return init();
    }

    /**
     * 设置应急口令初始化操作
     * @Date in Apr 15, 2011,11:33:36 AM
     * @return
     */
    public String find() {
        TokenInfo tkInfo = null;
        String maxtime = request.getParameter("maxtime");
        try {

            tkInfo = (TokenInfo) tokenServ.find(tokenInfo);
            if (!StrTool.objNotNull(tkInfo)) {
                return init();
            }
            //tkInfo.setEmpin(PwdEncTool.descryPwdStr(tkInfo.getEmpin()));//对应急口令解密
            //对应急口令置空
            tkInfo.setEmpin("");
            //没有设置过应急口令，过期时间默认为:应急口令有效时长+当前时间,(默认有效时长从配置中获取,获取的时间单位:小时)
            //根据认证方式、应急口令判断是否设置过应急口令
            if (!StrTool.strNotNull(tkInfo.getEmpin()) && tkInfo.getAuthmethod() == NumConstant.common_number_0) {
                String emergency_pass_validtime = getCenterConfigValue(ConfConstant.CONF_TYPE_TOKEN,
                        StrConstant.CENTER_EMERGENCY_PASS_DEF_VALIDTIME);
                tkInfo.setEmpindeathStr(DateTool.getfutureDateStr(StrTool.parseInt(emergency_pass_validtime)));
            } else {
                tkInfo.setEmpindeathStr(DateTool.dateToStr(tkInfo.getEmpindeath(), true));
            }

            //管理中心配置的最大有效时间
            String emergency_pass_max_validtime = getCenterConfigValue(ConfConstant.CONF_TYPE_TOKEN,
                    StrConstant.CENTER_EMERGENCY_PASS_MAX_VALIDTIME);

            //令牌过期的情况下，应急口令有效期不能超过一周
            if (StrTool.strNotNull(maxtime)) {
                long maxvalidtime = StrTool.parseLong(maxtime);
                tkInfo.setEmpindeathMaxStr(DateTool.dateToStr(maxvalidtime, true));
            } else {
                tkInfo.setEmpindeathMaxStr(DateTool.getfutureDateStr(StrTool.parseInt(emergency_pass_max_validtime)));
            }

            //查询令牌规格
            TokenSpec tokenSpec = new TokenSpec();
            if (StrTool.strNotNull(tkInfo.getSpecid())) {
                tokenSpec.setSpecid(tkInfo.getSpecid());
                tokenSpec = (TokenSpec) tokenSpecServ.find(tokenSpec);
                tkInfo.setTokenSpec(tokenSpec);
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        this.setTokenInfo(tkInfo);
        return SUCC_FIND;
    }

    /**
     * 根据令牌相关信息获取一个令牌
     * @Date in Apr 15, 2011,11:33:36 AM
     * @return
     */
    public String findToken() {
        try {
            String token = request.getParameter("token");
            String flag = request.getParameter("flag");
            if (!StrTool.strNotNull(token)) {
                outPutOperResult(Constant.alert_error, null);
                return null;
            }

            TokenInfo tkInfo = new TokenInfo();
            tkInfo.setToken(token);
            if (StrTool.strEquals(flag, "tknspec")) {
                //与规格表关联
                tkInfo = (TokenInfo) tokenServ.findObj(tkInfo);
            } else {
                tkInfo = (TokenInfo) tokenServ.find(tkInfo);
            }

            if (!StrTool.objNotNull(tkInfo)) {
                outPutOperResult(Constant.alert_error, null);
            } else {
                outPutOperResult(Constant.alert_succ, tkInfo);
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, null);
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 令牌修改操作
     */
    public String modify() {
        Set<?> keys = super.getTokenKey(request);//获取令牌号
        String sign = request.getParameter("sign");//令牌状态值
        //操作 1:启用、停用操作 2:挂失、解挂操作  3、锁定、解锁操作  4、作废操作
        String operType = request.getParameter("operType");
        boolean flag = false;
        try {
            if (StrTool.setNotNull(keys)) {
                //修改字段值，执行启用操作时值由0改为1，停用操作时值由1改为0
                int signValue = getTknSignValue(sign);
                if (StrTool.strEquals(operType, StrConstant.common_number_3)) {
                    int locktime = 0;
                    signValue = signValue == 1 ? 2 : signValue;
                    if (signValue != 0) {
                        locktime = StrTool.timeSecond();
                    }
                    flag = tokenServ.updateTokenLocked(keys, signValue, locktime);
                }
            }

            if (flag) {
                super.setResponseWrite("true");
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 令牌批量操作(批量修改状态、批量删除、批量取消分配)
     * 
     * @Date in Jul 15, 2013,4:41:05 PM
     * @return
     */
    public String modifyBatch() {
        try {
            Set<?> keys = super.getDelIds("tokenIds");
            int operObj = tokenQueryForm.getOperObj();
            int operType = tokenQueryForm.getOperBatch();
            TokenInfo tokenInfo = null;

            //本次查询所有记录
            if (NumConstant.batchOper_num1 == operObj) {
                tokenInfo = getTokenInfo(tokenQueryForm);
                tokenInfo = getTokenInfo(tokenInfo);

                //本次查询的所有数据封装到集合Set中
                List<?> queryList = tokenServ.query(tokenInfo, new PageArgument());
                //根据操作校验数据
                String checkResult = checkOperData(queryList, operType);
                if (StrTool.strNotNull(checkResult)) {
                    outPutOperResult(Constant.alert_error, checkResult);
                    return null;
                } else {
                    tokenServ.updateTokenState(queryList, operType);
                }
            } else {
                tokenInfo = new TokenInfo();
                String[] tempStr = new String[keys.size()];
                int i = 0;
                for (Iterator<?> it = keys.iterator(); it.hasNext();) {
                    String id = (String) it.next();
                    tempStr[i] = id;
                    i++;
                }
                tokenInfo.setBatchIds(tempStr);
                tokenInfo = TokenActionAide.getTokenInfo(tokenInfo, operType);

                if (operType == NumConstant.common_number_8) {
                    tokenServ.delObj(keys);
                } else if (operType == NumConstant.common_number_9) {
                    tokenInfo.setLogFlag(1); // 判断是否日志记录
                    tokenServ.updateTokenOrg(tokenInfo);
                } else {
                    tokenServ.updateTokenState(tokenInfo);
                }
            }

            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "tkn_batch_success"));
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_batch_failed"));
        return null;
    }

    /**
     * 批量操作，保持经过点击查询按钮之后的查询条件
     * 
     * @Date in Jul 16, 2013,9:42:05 AM
     * @param tokenInfo
     * @return
     */
    private TokenInfo getTokenInfo(TokenInfo tokenInfo) {
        String enableStr = request.getParameter("enableStr");
        String bindStr = request.getParameter("bindStr");
        String lockedStr = request.getParameter("lockedStr");
        String lostStr = request.getParameter("lostStr");
        String logoutStr = request.getParameter("logoutStr");
        String physicaltype = request.getParameter("physicaltype");
        String tokenStr = request.getParameter("tokenStr");
        String tokenStart = request.getParameter("tokenStart");
        String tokenEnd = request.getParameter("tokenEnd");
        //   String vendorId = request.getParameter("vendorId");
        String pubkeyStateStr = request.getParameter("pubkeyState");
        String orgFlagStr = request.getParameter("orgFlag");

        //直接赋值不需要判断 覆盖掉form中的映射的实时查询
        tokenInfo.setToken(tokenStr);
        tokenInfo.setTokenStart(tokenStart);
        tokenInfo.setTokenEnd(tokenEnd);
        //产品类型（c100,c200,c300,c400）
        //tokenQueryForm.getTokenInfo().setProducttype(StrTool.parseInt(producttype));
        //物理类型（手机令牌，软件令牌，硬件令牌...）
        tokenInfo.setPhysicaltype(StrTool.parseInt(physicaltype));
        //启用状态
        tokenInfo.setEnabled(StrTool.parseInt(enableStr));
        //绑定状态
        tokenInfo.setBind(StrTool.parseInt(bindStr));
        //锁定状态
        tokenInfo.setLocked(StrTool.parseInt(lockedStr));
        //挂失状态
        tokenInfo.setLost(StrTool.parseInt(lostStr));
        //作废状态
        tokenInfo.setLogout(StrTool.parseInt(logoutStr));
        //激活状态
        tokenInfo.setPubkeystate(StrTool.parseInt(pubkeyStateStr));
        //厂商
        //     tokenInfo.setVendorid(vendorId);
        //拼SQL条件
        tokenInfo.setOrgFlag(StrTool.parseInt(orgFlagStr));

        return tokenInfo;
    }

    /***
     * 令牌状态值修改
     * 
     * @Date in Apr 25, 2011,3:03:50 PM
     * @param sign
     * @return
     */
    public int getTknSignValue(String sign) {
        if (StrTool.strEquals(sign, StrConstant.common_number_0)) {
            return NumConstant.common_number_1;
        }
        return NumConstant.common_number_0;
    }

    /**
     * 检查需要操作的数据
     * 方法说明
     * @Date in Apr 7, 2013,5:28:16 PM
     * @param queryList
     * @param oper
     * @return
     */
    public String checkOperData(List<?> queryList, int oper) {
        String result = "";
        if (StrTool.listNotNull(queryList)) {
            for (int i = 0; i < queryList.size(); i++) {
                TokenInfo tokenInfo = (TokenInfo) queryList.get(i);

                // 若果令牌是作废状态，除了删除操作，其余操作都不可操作
                if (tokenInfo.getLogout() == NumConstant.common_number_1) {
                    //只允许删除操作
                    if (oper != NumConstant.common_number_8) {
                        result = Language.getLangStr(request, "tkn_warn_info_8");
                        break;
                    }
                }

                // 如果是取消分配
                if (oper == NumConstant.common_number_9) {
                    // 如果已绑定则不能取消分配
                    if (tokenInfo.getBindTag() == NumConstant.common_number_1) {
                        result = Language.getLangStr(request, "tkn_warn_info_2");
                        break;
                    }
                }
                // 如果是删除令牌
                if (oper == NumConstant.common_number_8) {
                    // 只能删除作废的令牌
                    if (tokenInfo.getLogout() != NumConstant.common_number_1) {
                        result = Language.getLangStr(request, "tkn_warn_info_3");
                        break;
                    }
                }
            }
        }

        return result;
    }

    /*
     * 管理员 初始化管理员更换令牌列表 
     * 根据条件筛选令牌列表
     * 1前提非作废的令牌
     * 2所有域下的无组织机构的令牌 
     * 
     * @return
     */
    public String initAdmTokenList() {
        if (isNeedClearForm()) {
            tokenQueryForm = null;
        }
        try {
            //设置查询条件
            TokenInfo tiQuery = new TokenInfo();
            tiQuery.setLogout(0);//未禁用的令牌
            tiQuery.setOrgunitid(null);// 组织机构为null 的
            tiQuery.setBind(-1);//全部
            if (StrTool.objNotNull(tokenQueryForm)) {//查询过来的
                tiQuery.setToken(tokenQueryForm.getToken());//令牌号
                if (tokenQueryForm.getOrgunitIds().indexOf(",") == -1) { //如果要查询的机构是 空的
                    tiQuery.setDomainid(null); //不参与条件匹配
                } else { //有域
                    int queryDomainId = StrTool.parseInt(tokenQueryForm.getOrgunitIds().split(",")[0].split(":")[0]);
                    tiQuery.setDomainid(queryDomainId);
                }
            } else {// 初始化过来的
                tiQuery.setToken("");//令牌号
                tiQuery.setDomainid(null);
            }

            int total = tokenServ.countBC(tiQuery); //统计数量
            PageArgument pageArg = getArgument(total);
            pageArg.setCurPage(getPage());
            pageArg.setPageSize(getPagesize());
            List<?> resultList = tokenServ.queryBC(tiQuery, pageArg);
            String jsonStr = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
            setResponseWrite(jsonStr);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        request.setAttribute("over", "over");
        return null;
    }

    /**
     * 分页跳转
     * @Date in Apr 18, 2011,11:48:33 AM
     * @param 
     * @return 
     */
    public String page() {
        PageArgument pArgument = getArgument(request, 0);
        query(pArgument);
        
        return SUCCESS;
    }

    /**
     * 令牌详细信息
     * @Date in Apr 18, 2011,11:48:33 AM
     * @param 
     * @return 
     */
    public String view() {
        List<?> userList = null;
        try {
            //获取令牌信息
            tokenInfo = (TokenInfo) tokenServ.find(tokenInfo);
            if (!StrTool.objNotNull(tokenInfo)) {
                //init();
                setResponseWrite(Language.getLangStr(request, "tkn_view_token_not_exist"));
                return null;
            }
            if (!StrTool.strNotNull(tokenInfo.getEmpin()) || tokenInfo.getAuthmethod() == NumConstant.common_number_0) {
                //认证动态口令不应该产生应急口令过期时间
                //String emergency_pass_validtime = getCenterConfigValue(StrConstant.CONFTYPE_CENTER,
                //        StrConstant.CENTER_EMERGENCY_PASS_DEF_VALIDTIME);
                //tokenInfo.setEmpindeathStr(DateTool.getfutureDateStr(StrTool.parseInt(emergency_pass_validtime)));
            } else {
                tokenInfo.setEmpindeathStr(DateTool.dateToStr(tokenInfo.getEmpindeath(), true));
            }
            //查询令牌规格
            TokenSpec tokenSpec = new TokenSpec();
            if (StrTool.strNotNull(tokenInfo.getSpecid())) {
                tokenSpec.setSpecid(tokenInfo.getSpecid());
                tokenSpec = (TokenSpec) tokenSpecServ.find(tokenSpec);
                tokenInfo.setTokenSpec(tokenSpec);
            }
            //取得用户数组(一个令牌可能被多个用户绑定)
            userList = getUsers(tokenInfo.getToken());
            if (StrTool.listNotNull(userList)) {
                tokenInfo.setUserIds(userList);
            }

            //查询机构名称
            tokenInfo = aide.setDomainAndOrgunitName(tokenInfo);

            //取得厂商名称
            //            VendorInfo vendorInfo = (VendorInfo) VendorConfig.getVendorMap().get(tokenInfo.getVendorid());
            //           if (StrTool.objNotNull(vendorInfo)) {
            //               tokenInfo.setVendorName(vendorInfo.getName());
            //            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        this.setTokenInfo(tokenInfo);

        return SUCC_VIEW;
    }

    /**
     * 取得用户绑定的令牌(一个或多个)
     * @Date in Apr 26, 2011,3:20:17 PM
     * @param token
     * @return List
     */
    private List<?> getUsers(String token) throws BaseException {
        UTokenQueryForm utQueryForm = new UTokenQueryForm();
        utQueryForm.setToken(token);
        PageArgument pageArg = new PageArgument();
        return userTokenServ.query(utQueryForm.getUserToken(), pageArg);
    }

    /**
     * 设置令牌应急口令
     * 
     * @Date in Jun 22, 2011,4:56:55 PM
     * @return
     * @throws BaseException
     */
    public String setTokenPin() throws BaseException {
        if (null == tokenInfo) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_warn_tkndata_isnull"));
            return null;
        }
        try {
            //应急口令认证方式
            if (tokenInfo.getAuthmethod() == NumConstant.common_number_1) {
                String empin_otp_leneq = getCenterConfigValue(ConfConstant.CONF_TYPE_TOKEN,
                        StrConstant.CORE_EMPIN_OTP_LENEQ);
                if (StrTool.strNotNull(empin_otp_leneq)
                        && StrTool.strEquals(empin_otp_leneq, StrConstant.common_number_1)) {
                    // 获取令牌规格的长度
                    TokenSpec tSpec = new TokenSpec();
                    tSpec.setSpecid(tokenInfo.getSpecid());
                    tSpec = (TokenSpec) tokenSpecServ.find(tSpec);
                    if (tSpec.getOtplen() != tokenInfo.getEmpin().length()) {
                        outPutOperResult(Constant.alert_warn, Language.getLangStr(request,
                                "tkn_warn_conf_not_eq_length"));
                        return null;
                    }
                }
                tokenInfo.setEmpin(PwdEncTool.encPwd(tokenInfo.getEmpin()));
            } else {
                tokenInfo.setEmpindeath(0);
                tokenInfo.setEmpindeathStr(null);
            }
            tokenServ.updateObj(tokenInfo);
            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "tkn_warn_set_pin_success"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_warn_set_pin_error"));
        }

        return null;
    }

    /**
     * 管理中心配置
     * 根据属性类型、类型名称获取属性值
     * @param confType
     * @param confName
     * @return confvalue
     * @throws BaseException 
     */
    public String getCenterConfigValue(String confType, String confName) throws BaseException {
        ConfigInfo configInfo = new ConfigInfo();
        configInfo.setConftype(confType);
        configInfo.setConfname(confName);
        ConfigInfo confInfo = (ConfigInfo) confInfoServ.find(configInfo);
        String confvalue = "";
        if (StrTool.objNotNull(confInfo)) {
            confvalue = confInfo.getConfvalue();
        }

        return confvalue;
    }

    /**  
     * 令牌批量查询
     * @Date in Jan 17, 2012,10:25:04 AM
     * @return
     */
    public String upBatchFile() {
        try {
            if (!StrTool.objNotNull(upFile)) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_warn_tknnum_file_failed"));
                return null;
            }

            String tokenStr = aide.getTknStrBuf(upFile);
            if (StrTool.strNotNull(tokenStr)) {
                outPutOperResult(Constant.alert_succ, tokenStr);
            } else {// 文件内容为空
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_warn_tknnum_file_empty"));
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_warn_tknnum_file_failed"));
        }

        return null;
    }

    /**  
     * 令牌分配
     * @Date in Jan 17, 2012,10:25:04 AM
     * @return
     * @throws BaseException 
     */
    public String assignTkn() throws BaseException {
    	try {
            List<Object> tokenList = new ArrayList<Object>();
            int totalCount = 0;//分配令牌数

            String tokenSel = request.getParameter("tokenSel");
            String orginId = request.getParameter("orgunit");
            int oldDomainId = Integer.parseInt(request.getParameter("oldDomain"));
            int oldOrginId = Integer.parseInt(request.getParameter("oldOrgId"));

            String oldOrginName = "";
            // 获取旧的组织机构名称
            if (oldOrginId != 0) {
                OrgunitInfo oiQuery = new OrgunitInfo();
                oiQuery.setOrgunitId(oldOrginId);
                oiQuery.setOrgunitNumber(null);
                oiQuery.setDomainId(0);
                oiQuery.setCreateTime(0);
                // 获取组织机构
                OrgunitInfo orgunitInfo = (OrgunitInfo) orgunitInfoServ.find(oiQuery);

                if (StrTool.objNotNull(orgunitInfo)) {
                    oldOrginName = orgunitInfo.getOrgunitName();
                }
            } else {
                oldOrginName = DomainConfig.getValue(oldDomainId);
            }

            if (StrTool.strEquals(tokenSel, StrConstant.common_number_0)) { // 本次选中的记录
                String tokenIdS = request.getParameter("assignTkn");

                if (StrTool.strNotNull(tokenIdS)) {
                    String tokenIdsArr[] = tokenIdS.split(",");

                    for (int i = 0; i < tokenIdsArr.length; i++) {
                        TokenInfo tokenInfo = new TokenInfo();
                        tokenInfo.setToken(tokenIdsArr[i]);
                        tokenInfo.setOrgunitid(StrTool.parseInt(orginId));
                        tokenInfo.setOldOrgunitName(oldOrginName);
                        tokenList.add(tokenInfo);
                    }
                }

                if (StrTool.listNotNull(tokenList)) {
                    tokenServ.updateTokenOrg(tokenList);
                    totalCount = tokenList.size();
                }

            } else { // 按条件查询
                TokenInfo tokenInfo = new TokenInfo();
                tokenInfo.setToken(request.getParameter("token"));
                tokenInfo.setTokenStart(request.getParameter("tokenStart"));
                tokenInfo.setTokenEnd(request.getParameter("tokenEnd"));
                String oldDomain = request.getParameter("oldDomain");
                tokenInfo.setDomainid(StrTool.parseInt(oldDomain == null ? "0" : oldDomain.trim()));
                String oldOrgId = request.getParameter("oldOrgId");
                if(StrTool.parseInt(oldOrgId) == 0){
                	tokenInfo.setOrgunitid(null);
                	tokenInfo.setQueryMark(1);
                }else{
                	tokenInfo.setOrgunitid(StrTool.parseInt(oldOrgId.trim()));
                }
                tokenInfo.setLogout(0);
                tokenInfo.setBind(0);
                String tknCountTotal = request.getParameter("tknCountTotal");
                totalCount = StrTool.parseInt(tknCountTotal == null ? "0" : tknCountTotal.trim());

                // 每次分配10000条数据 循环查询分配
                int loop = 0;
                if (totalCount % NumConstant.batchCount_10000 == 0) {
                    loop = totalCount / NumConstant.batchCount_10000;
                } else {
                    loop = totalCount / NumConstant.batchCount_10000 + 1;
                }

                List<?> queryTknList = null;
                for (int i = 0; i < loop; i++) {
                    PageArgument pageArg = new PageArgument();
                    pageArg.setCurPage(i);
                    if (i == loop - 1 && (totalCount % NumConstant.batchCount_10000 != 0)) {
                        pageArg.setPageSize(totalCount % NumConstant.batchCount_10000);
                    } else {
                        pageArg.setPageSize(NumConstant.batchCount_10000);
                    }
                    pageArg.setStartRow(i * NumConstant.batchCount_10000);

                    queryTknList = tokenServ.query(tokenInfo, pageArg);

                    for (int k = 0; k < queryTknList.size(); k++) {
                        TokenInfo tknInfo = (TokenInfo) queryTknList.get(k);
                        tknInfo.setOrgunitid(StrTool.parseInt(orginId));
                        tknInfo.setOldOrgunitName(oldOrginName);
                        tokenList.add(tknInfo);
                    }

                    // 分配一批次
                    if (StrTool.listNotNull(tokenList)) {
                        tokenServ.updateTokenOrg(tokenList);
                        tokenList.clear();
                    }
                }
            }

            super.setActionResult(true);
            outPutOperResult(Constant.alert_succ, Language.getReplaceLangValue(request, "tkn_warn_assign_result",
                    String.valueOf(totalCount)));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_warn_allot_failed"));
            super.setActionResult(false);
        }

        return null;
    }

    /**  
     * 软件令牌分发前初始化数据
     * @Date in Jan 17, 2012,10:25:04 AM
     * @return
     */
    public String prevSoftTknDist() {
        try {
            if (StrTool.objNotNull(tokenInfo)) {
                // 2 取得用户数组(一个令牌可能被多个用户绑定)
                List<?> userList = getUsers(tokenInfo.getToken());
                if (StrTool.listNotNull(userList)) {
                    tokenInfo.setUserIds(userList);
                }
                //从配置表中获取软件令牌默认分发PIN码(软件令牌已从核心配置转到管理中心配置）
                String defaultap = getCenterConfigValue(ConfConstant.CONF_TYPE_TOKEN, StrConstant.CORE_DEFAULTAP);
                if (StrTool.strNotNull(defaultap)) {
                    tokenInfo.setSofttoken_distribute_pwd(defaultap);
                }
            } else {
                return init();
            }
            this.setTokenInfo(tokenInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return "softTknDist";

    }

    /**  
     * 软件令牌分发
     * @Date in Jan 17, 2012,10:25:04 AM
     * @return
     */
    public String softTokenDist() {
        //软件令牌分发结果处理 0 页面下载   1 邮件发送
        String distResult = request.getParameter("result");
        //PIN产生方式
        String generatype = request.getParameter("generaType");
        //默认PIN码
        String defaultPass = request.getParameter("defaultap");
        super.setActionResult(true);
        try {
            if (StrTool.objNotNull(tokenInfo)) {
                //更新令牌分发时间
                tokenInfo.setDistributetime(DateTool.dateToInt(new Date()));//分发时间
                tokenServ.updateSoftTkn(tokenInfo);
                if (StrTool.strEquals(generatype, StrConstant.common_number_2)) {//随机产生
                    tokenInfo.setSofttoken_distribute_pwd(RandomPassUtil.genActivePass(8));
                }
                // 查询令牌信息（包括PUBKEY）
                TokenInfo tInfo = (TokenInfo) tokenServ.findObj(tokenInfo);

                // 取得用户数组(一个令牌可能被多个用户绑定)
                List<?> userList = getUsers(tokenInfo.getToken());
                if (StrTool.listNotNull(userList)) {
                    tInfo.setUserIds(userList);
                }
                tInfo.setSofttoken_distribute_pwd(tokenInfo.getSofttoken_distribute_pwd());
                if (null == tInfo) {
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_warn_dist_soft_failed"));
                    return null;
                }
                //生成.stf文件
                String distributeFile = "";
                String path = appPath(Constant.WEB_TEMP_FILE_STF, "");
                String fileName = tInfo.getToken() + Constant.FILE_STF;
                if (StrTool.strEquals(defaultPass, tokenInfo.getSofttoken_distribute_pwd())) {
                    distributeFile = SoftTknDistriHelp.generateDistFile(path, tInfo, true);
                } else {
                    distributeFile = SoftTknDistriHelp.generateDistFile(path, tInfo, false);
                }

                if (!StrTool.strNotNull(distributeFile)) {
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_warn_dist_soft_failed"));
                    return null;
                }
                if (StrTool.strEquals(distResult, StrConstant.common_number_0)) {//页面下载
                    outPutOperResult(Constant.alert_succ, tokenInfo.getToken());
                } else if (StrTool.strEquals(distResult, StrConstant.common_number_1)) {//邮件发送

                    //获取邮件信息
                    String emailbody = Language.getLangStr(request, "tkn_warn_distemail_soft_info");
                    emailbody += Language.getLangStr(request, "tkn_random_pin")
                            + tokenInfo.getSofttoken_distribute_pwd(); // PIN码信息
                    //获取用户邮箱集合,用户模块完后，打开此注释
                    List<?> userinList = aide.getUserEmails(tInfo.getToken(), tInfo.getUserIds(), userInfoServ,
                            adminUserServ);

                    UserToken userToken = (UserToken) tInfo.getUserIds().get(0); // 邮件发送时该令牌只能被一个用户绑定，所以tInfo.getUserIds()为1
                    MailInfo mailInfo = null;

                    if (StrTool.objNotNull(userToken.getDomainId())) { // 表示为用户绑定令牌
                        mailInfo = aide.getMailInfo(userinList,
                                Language.getLangStr(request, "tkn_warn_distemail_soft"), emailbody, distributeFile,
                                emailServ);
                    } else { // 表示为管理员绑定令牌
                        mailInfo = aide.admGetMailInfo(userinList, Language.getLangStr(request,
                                "tkn_warn_distemail_soft"), emailbody, distributeFile, emailServ);
                    }
                    if (null == mailInfo) {
                        outPutOperResult(Constant.alert_error, Language
                                .getLangStr(request, "tkn_warn_dist_soft_failed"));
                        return null;
                    }
                    if (SendMailUtil.emailSeed(mailInfo)) {
                        outPutOperResult(Constant.alert_sendSucc, Language.getLangStr(request,
                                "tkn_warn_dist_soft_success"));
                        return null;
                    } else {
                        outPutOperResult(Constant.alert_error, Language
                                .getLangStr(request, "tkn_warn_dist_mail_failed"));
                        return null;
                    }
                }
            } else {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_warn_dist_soft_failed"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_warn_dist_soft_failed"));
            super.setActionResult(false);
        }
        return null;
    }

    /**  
     * 下载软件令牌分发生成的stf文件
     * @Date in Jan 17, 2012,10:25:04 AM
     * @return
     */
    public String download() {
        try {
            String fileName = request.getParameter("fileName");
            fileName = fileName + Constant.FILE_STF;
            String filePath = appPath(Constant.WEB_TEMP_FILE_STF, fileName);
            SoftTknDistriHelp.downLoadFile(fileName, filePath, response);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 获取用户绑定令牌可以同步的令牌信息
     * @Date in Sep 3, 2013,2:55:22 PM
     * @return
     */
    public String getSyncTkns() {
        try {
            String tkns = request.getParameter("syncTkns");
            String tokens = "";
            if (StrTool.strNotNull(tkns)) {
                TokenInfo toInfo = aide.getTknBatch(tkns);
                List<?> tknList = tokenServ.query(toInfo, new PageArgument());
                if (StrTool.listNotNull(tknList)) {
                    for (int i = 0; i < tknList.size(); i++) {
                        TokenInfo tknInfo = (TokenInfo) tknList.get(i);
                        if (tknInfo.getPhysicaltype() != 6) {
                            tokens = tokens + tknInfo.getToken() + ",";
                        }
                    }
                }
            }
            if (StrTool.strNotNull(tokens)) {
                tokens = tokens.substring(0, tokens.length() - 1);
            }
            if (StrTool.strNotNull(tokens)) {
                outPutOperResult(Constant.alert_succ, tokens);
            } else {
                outPutOperResult(Constant.alert_error, "error");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * 令牌分配查询此令牌是否绑定
     * @author LXH
     * @date Sep 4, 2014 11:20:21 AM
     * @return
     */
    public String checkBindTag() {
        try {
            String tokenSN = request.getParameter("tokenSN");
            UserToken userToken = new UserToken();
            userToken.setToken(tokenSN);
            List<?> list = userTokenServ.selObjs(userToken);
            if(StrTool.listNotNull(list)){
            	outPutOperResult(Constant.alert_warn, null);
            }else{
            	outPutOperResult(Constant.alert_succ, null);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            outPutOperResult(Constant.alert_warn, null);
        }

        return null;
    }

    /**
     * @return the tknInfo
     */
    public String getTknInfo() {
        return tknInfo;
    }

    /**
     * @param tknInfo the tknInfo to set
     */
    public void setTknInfo(String tknInfo) {
        this.tknInfo = tknInfo;
    }

}
