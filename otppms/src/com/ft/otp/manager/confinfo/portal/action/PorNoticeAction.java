/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.portal.action;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.common.config.PubConfConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.confinfo.portal.entity.ProNoticeInfo;
import com.ft.otp.manager.confinfo.portal.form.NoticeQueryForm;
import com.ft.otp.manager.confinfo.portal.service.IProNoticeServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户门户系统公告通知业务action
 *
 * @Date in Apr 27, 2013,1:40:29 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class PorNoticeAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = -7617535565708294949L;
    // 公共配置服务接口
    private Logger logger = Logger.getLogger(PorNoticeAction.class);
    
    private IProNoticeServ noticeServ;
    private ProNoticeInfo noticeInfo;
    private NoticeQueryForm noticeQueryForm = null;
    
    public IProNoticeServ getNoticeServ() {
        return noticeServ;
    }

    public void setNoticeServ(IProNoticeServ noticeServ) {
        this.noticeServ = noticeServ;
    }

    public ProNoticeInfo getNoticeInfo() {
        return noticeInfo;
    }

    public void setNoticeInfo(ProNoticeInfo noticeInfo) {
        this.noticeInfo = noticeInfo;
    }

    public NoticeQueryForm getNoticeQueryForm() {
        return noticeQueryForm;
    }

    public void setNoticeQueryForm(NoticeQueryForm noticeQueryForm) {
        this.noticeQueryForm = noticeQueryForm;
    }
    
    /**
     * 取出QueryForm中的实体
     * @Date in Apr 27, 2013,4:11:13 PM
     * @param noticeForm
     * @return
     */
    private ProNoticeInfo getNoticeInfo(NoticeQueryForm noticeForm) {
        ProNoticeInfo noticeInfo = new ProNoticeInfo();
        if (StrTool.objNotNull(noticeForm)) {
            noticeInfo = noticeForm.getNoticeInfo();
        }
        return noticeInfo;
    }
    
    /**
     * 添加系统通知公告
     */
    public String add() {
        if (StrTool.objNotNull(noticeInfo)) {
            try {
                Date expTime = DateTool.stringToDate(noticeInfo.getTempDeathTime());
                noticeInfo.setExpiretime(DateTool.dateToInt(expTime));
                noticeInfo.setCreatetime(DateTool.dateToInt(DateTool.stringToDate(DateTool.dateToStr(new Date()))));
                noticeInfo.setCreateuser(getLinkUser().getUserId());
                noticeServ.addObj(noticeInfo);
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_save_succ_tip"));
            } catch (Exception e) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_save_error_tip"));
                logger.error(e.getMessage(), e);
            }
            PubConfConfig.clear();
            PubConfConfig.loadPubConfConfig();
        }
        return null;
    }

    /**
     * 删除系统通知
     */
    public String delete() {
        String delIdStr = request.getParameter("delIds");
        if (!StrTool.strNotNull(delIdStr)) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_del_error_tip"));
        }
        String[] delIds = delIdStr.split(",");
 
        try {
            if (delIds.length > 0) {
                int[] ids = new int[delIds.length];
                for (int i = 0; i < delIds.length; i++) {
                    ids[i] = Integer.parseInt(delIds[i]);
                }
                ProNoticeInfo nInfo = new ProNoticeInfo();
                nInfo.setBatchIdsInt(ids);
                noticeServ.delObj(nInfo);
            }
            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_del_succ_tip"));
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_del_error_tip"));
            logger.error(e.getMessage(), e);
        }
        PubConfConfig.clear();
        PubConfConfig.loadPubConfConfig();
        return null;
    }

    /**
     * 查询系统通知
     */
    public String find() {
        String noticeId = request.getParameter("noticeId");
        ProNoticeInfo nInfo = new ProNoticeInfo();
        nInfo.setId(Integer.parseInt(noticeId));
        try {
            nInfo = (ProNoticeInfo) noticeServ.find(nInfo);
            nInfo.setTempDeathTime(DateTool.dateToStr(nInfo.getExpiretime(), true));
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }
        request.setAttribute("noticeInfo", nInfo);
        return SUCC_ADD;
    }
    
    /**
     * 通知公告初始化
     */
    public String init() {
        try {
            if (isNeedClearForm()) {
                setNoticeQueryForm(null);
            }
            
            PageArgument pageArg = pageArgument();
            pageArg.setPageSize(getPagesize());
            pageArg.setCurPage(getPage());
            
            List<?> resultList = query(pageArg);
            String jsonStr = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
            setResponseWrite(jsonStr);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * 查询系统通知列表
     * @Date in Apr 27, 2013,3:52:32 PM
     * @param pageArg
     * @return
     */
    private List<?> query(PageArgument pageArg) {
        ProNoticeInfo nInfo = getNoticeInfo(noticeQueryForm);
        List<?> noticeList = null;
        try {
            noticeList = noticeServ.query(nInfo, pageArg);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return noticeList;
    }
    
    /**
     * 行数统计 分页处理
     * @Date in Apr 27, 2013,4:13:53 PM
     * @return
     * @throws BaseException
     */
    private PageArgument pageArgument() throws BaseException {
        ProNoticeInfo nInfo = getNoticeInfo(noticeQueryForm);
        int totalRow = 0;
        totalRow = noticeServ.count(nInfo);
        PageArgument pageArg = getArgument(totalRow);
        return pageArg;
    }

    /**
     * 编辑系统通知
     */
    public String modify() {
        try {
            if (StrTool.objNotNull(noticeInfo)) {
                Date expTime = DateTool.stringToDate(noticeInfo.getTempDeathTime());
                noticeInfo.setExpiretime(DateTool.dateToInt(expTime));
                noticeInfo.setCreatetime(DateTool.dateToInt(DateTool.stringToDate(DateTool.dateToStr(new Date()))));
                noticeServ.updateObj(noticeInfo);
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_save_succ_tip"));
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_succ_tip"));
            logger.error(e.getMessage(), e);
        }
        PubConfConfig.clear();
        PubConfConfig.loadPubConfConfig();
        return null;
    }


    public String page() {
        return null;
    }

    /**
     * 通知查看
     */
    public String view() {
        String noticeId = request.getParameter("noticeId");
        ProNoticeInfo nInfo = new ProNoticeInfo();
        nInfo.setId(Integer.parseInt(noticeId));
        try {
            nInfo = (ProNoticeInfo) noticeServ.find(nInfo);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }
        request.setAttribute("noticeInfo", nInfo);
        return "view";
    }


}
