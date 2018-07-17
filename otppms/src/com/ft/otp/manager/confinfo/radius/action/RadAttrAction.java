package com.ft.otp.manager.confinfo.radius.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.config.PubConfConfig;
import com.ft.otp.common.config.RadiusConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.confinfo.radius.entity.RadAttrInfo;
import com.ft.otp.manager.confinfo.radius.service.IRadAttrServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.StrTool;

/**
 * Radius 配置属性管理业务action
 * 
 * @Date in Oct 30, 2012,4:05:06 PM
 * 
 * @version v1.0
 * 
 * @author YXS
 */
public class RadAttrAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(RadAttrAction.class);

    private IRadAttrServ radAttrServ;
    private RadAttrInfo radAttrInfo;

    public IRadAttrServ getRadAttrServ() {
        return radAttrServ;
    }

    public void setRadAttrServ(IRadAttrServ radAttrServ) {
        this.radAttrServ = radAttrServ;
    }

    public RadAttrInfo getRadAttrInfo() {
        return radAttrInfo;
    }

    public void setRadAttrInfo(RadAttrInfo radAttrInfo) {
        this.radAttrInfo = radAttrInfo;
    }

    private RadAttrInfo getAttrInfo() {
        if (!StrTool.objNotNull(radAttrInfo)) {
            radAttrInfo = new RadAttrInfo();
        }
        return radAttrInfo;
    }

    /**
     * 添加Radius配置属性
     */
    public String add() {
        String type = request.getParameter("type");
        try {
            if (StrTool.objNotNull(radAttrInfo)) {
                // 校验自定义时配置ID
                if (StrTool.strEquals("custom", type)) {
                    Map<String, Object> attrMap = RadiusConfig.radiusMap;
                    if (StrTool.mapNotNull(attrMap) && StrTool.objNotNull(attrMap.get(radAttrInfo.getAttrId()+":"+radAttrInfo.getVendorid()))) {
                        outPutOperResult(Constant.alert_error, Language.getLangStr(request,
                                "radattr_id_already_exist_standard"));
                        return null;
                    }
                }
                RadAttrInfo attrInfo = new RadAttrInfo();
                attrInfo.setAttrId(radAttrInfo.getAttrId());
                attrInfo.setProfileId(radAttrInfo.getProfileId());
                attrInfo.setVendorid(radAttrInfo.getVendorid());
                attrInfo.setFlag(1); //SQL拼接标志
                
                int totalRow = 0;
                totalRow = radAttrServ.count(attrInfo);
                if (totalRow > NumConstant.common_number_0) {
                    if (StrTool.strEquals("custom", type)) {
                        outPutOperResult(Constant.alert_error, Language.getLangStr(request, "radattr_sel_other_attrid"));
                    } else {
                        outPutOperResult(Constant.alert_error, Language.getLangStr(request, "radattr_sel_other_name"));
                    }
                    return null;
                }
            }
            radAttrServ.addObj(radAttrInfo);
            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_save_succ_tip"));
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_save_error_tip"));
            logger.error(e.getMessage(), e);
        }
        PubConfConfig.clear();
        PubConfConfig.loadPubConfConfig();
        return null;
    }

    /**
     * 删除Radius配置属性
     */
    public String delete() {
        Set<?> keySet = super.getDelIds("delIds");
        try {
            if (StrTool.setNotNull(keySet)) {
                radAttrServ.delObj(keySet);
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_del_succ_tip"));
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_del_error_tip"));
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据attrId和profileId查找Radius配置属性
     */
    public String find() {
        String attrId = request.getParameter("attrId");
        String vendorid = request.getParameter("vendorid");
        String profileId = request.getParameter("profileId");
        RadAttrInfo attrInfo = getAttrInfo();
        try {
            if (StrTool.strNotNull(profileId) && StrTool.strNotNull(attrId)) {
                attrInfo.setProfileId(Integer.parseInt(profileId));
                attrInfo.setAttrId(attrId);
                attrInfo.setVendorid(Integer.parseInt(vendorid));
                attrInfo.setFlag(1); // SQL拼接条件
            } else {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "radattr_not_related_conf"));
                return null;
            }
            attrInfo = (RadAttrInfo) radAttrServ.find(attrInfo);
            if (StrTool.objNotNull(attrInfo)) {
            	
            	// 传参,厂商名称
            	RadAttrInfo radA = (RadAttrInfo)RadiusConfig.getValue(attrId+":"+vendorid);
            	String vendorname = radA.getVendorname();
            	if(attrInfo.getVendorid() == NumConstant.common_number_na_1){
        			vendorname = Language.getLangValue("auth_conf_radius_property", Language.getCurrLang(null), null);
        		}
            	attrInfo.setVendorname(vendorname);
                String attrHtml = pakAttrHtml(attrId, vendorid, attrInfo);
                request.setAttribute("attrHtml", attrHtml);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        this.setRadAttrInfo(attrInfo);
        return "edit";
    }
    
    /**
     * RADIUS配置属性，下拉LIST
     * @return
     */
    public String selAttrName() {
        String vendorid = request.getParameter("vendorid");
        List<RadAttrInfo> radAttrList = new ArrayList();
        try {
        	Map radMap = RadiusConfig.radiusMap;
        	Iterator<?> it = radMap.entrySet().iterator();
    		RadAttrInfo obj = null;
    		// 组装LIST
    		while (it.hasNext()) {
    			Map.Entry ent = (Map.Entry) it.next();
    			obj = (RadAttrInfo) (ent.getValue());
    			if(Integer.parseInt(vendorid) == obj.getVendorid()){
    				radAttrList.add(obj);
    			}
    		}
            //返回JSON格式查询数据
            String jsonStr = JsonTool.getJsonFromList(0, radAttrList, null);
            super.setResponseWrite(jsonStr);
        } catch (Exception ex) {
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ft.otp.base.action.IBaseAction#init()
     */
    public String init() {
        return null;
    }

    /**
     * 编辑修改Radius配置属性
     */
    public String modify() {
        try {
            if (StrTool.objNotNull(radAttrInfo)) {
                radAttrServ.updateObj(radAttrInfo);
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_edit_succ_tip"));
            } else {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_error_tip"));
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_error_tip"));
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ft.otp.base.action.IBaseAction#page()
     */
    public String page() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ft.otp.base.action.IBaseAction#view()
     */
    public String view() {
        return null;
    }

    /**
     * 获取封装分页信息
     * 
     * @Date in Oct 29, 2012,4:54:55 PM
     * @return
     * @throws Exception
     */
    private PageArgument pageArgument(RadAttrInfo atrrInfo) throws Exception {
        int totalRow = 0;
        totalRow = radAttrServ.count(atrrInfo);
        PageArgument pageArg = getArgument(totalRow);
        return pageArg;
    }

    /**
     * 查询RADIUS配置的有关属性
     * 
     * @Date in Oct 30, 2012,5:26:29 PM
     * @return
     */
    public String queryAttr() {
        String profileId = request.getParameter("profileId");
        RadAttrInfo atrrInfo = getAttrInfo();
        List<?> resultList = null;
        try {
            if (StrTool.strNotNull(profileId)) {
                atrrInfo.setProfileId(Integer.parseInt(profileId));
            }
            PageArgument pageArg = pageArgument(atrrInfo);
            pageArg.setPageSize(getPagesize());
            pageArg.setCurPage(getPage());
            resultList = radAttrServ.query(atrrInfo, pageArg);
            
            // 遍历插入厂商名称
            List list = new ArrayList();
            for(int i=0; i<resultList.size(); i++){
            	RadAttrInfo radAttrInfo = (RadAttrInfo)resultList.get(i);
            	RadAttrInfo confRad = (RadAttrInfo)RadiusConfig.getValue(radAttrInfo.getAttrId()+":"+radAttrInfo.getVendorid());
            	if(StrTool.objNotNull(confRad)){
            		 // 获取属性值
            		String attrname = radAttrInfo.getAttrValue();
            		if(StrTool.mapNotNull(confRad.getValueMap())){
            			attrname = (String) confRad.getValueMap().get(radAttrInfo.getAttrValue());
            		}
            		radAttrInfo.setAttrValueToName(attrname); // 存储属性值，下拉域存储下拉名称; 非下拉域，存储所写入的属性值;
            		String vendorname = confRad.getVendorname();
            		if(radAttrInfo.getVendorid() == NumConstant.common_number_na_1){
            			vendorname = Language.getLangValue("auth_conf_radius_property", Language.getCurrLang(null), null);
            		}
                	radAttrInfo.setVendorname(vendorname);
            	}
            	list.add(radAttrInfo);
            }

            String json = JsonTool.getJsonFromList(pageArg.getTotalRow(), list, null);
            setResponseWrite(json);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取Radius配置属性
     * 
     * @Date in Oct 31, 2012,3:02:26 PM
     * @return
     */
    public String getRadProfileAttr() {
        String attrkey = request.getParameter("attrkey");
        String vendorid = request.getParameter("vendorid");
        if (StrTool.strNotNull(attrkey)) {
            String str = pakAttrHtml(attrkey, vendorid, null);

            outPutOperResult(Constant.alert_succ, str);
        } else {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "radattr_get_conf_attr"));
        }
        return null;
    }

    /**
     * 封装返回RADIUS配置属性值类型和属性值
     * 
     * @Date in Oct 31, 2012,4:43:57 PM
     * @param attrkey
     * @param obj
     * @return
     */
    @SuppressWarnings("unused")
    private String pakAttrHtml(String attrkey, String vendorid, RadAttrInfo obj) {
        // 获取读取xml文件封装的Map
        Map<String, Object> radAttrMap = RadiusConfig.radiusMap;
        StringBuffer sbf = new StringBuffer();
        String attrValue = "";
        if (StrTool.objNotNull(obj) && StrTool.strNotNull(obj.getAttrValue())) {
            attrValue = obj.getAttrValue();
        }

        if (StrTool.mapNotNull(radAttrMap) && StrTool.objNotNull(radAttrMap.get(attrkey+":"+vendorid))) {
            RadAttrInfo attrInfo = (RadAttrInfo) radAttrMap.get(attrkey+":"+vendorid);

            sbf.append("<input type='hidden' id='attrId' name='radAttrInfo.attrId' value='" + attrInfo.getAttrId()
                    + "' />");
            sbf.append("<input type='hidden' id='attrName' name='radAttrInfo.attrName' value='"
                    + attrInfo.getAttrName() + "' />");
            sbf.append("<input type='hidden' id='attrValueType' name='radAttrInfo.attrValueType' value='"
                    + attrInfo.getAttrValueType() + "' />");
            // 获取配置属性下的子属性
            Map valueMap = attrInfo.getValueMap();
            if (StrTool.mapNotNull(valueMap) && valueMap.size() > 0) {
                Set<?> set = valueMap.entrySet();
                Iterator<?> iter = set.iterator();
                // 封装子属性，封装到下拉列表
                sbf.append("<select id='attrValue' name='radAttrInfo.attrValue' class='select100'>");
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
                    if (attrValue.length() > 0 && attrValue.equals((String) entry.getKey())) {
                        sbf.append("<option value='" + entry.getKey() + "' selected>");
                    } else {
                        sbf.append("<option value='" + entry.getKey() + "'>");
                    }
                    sbf.append((String) entry.getValue());
                    sbf.append("</option>");
                }
                sbf.append("</select>");
            } else {
                sbf.append("<input type='text' id='attrValue' name='radAttrInfo.attrValue' value='" + attrValue
                        + "' class='formCss100' />");
            }
            sbf.append("^^");
            sbf.append(attrInfo.getAttrValueType());
        } else if (StrTool.objNotNull(obj)) {
            sbf.append("<input type='hidden' id='attrId' name='radAttrInfo.attrId' value='" + obj.getAttrId() + "' />");
            sbf.append("<input type='hidden' id='attrName' name='radAttrInfo.attrName' value='" + obj.getAttrName()
                    + "' />");
            sbf.append("<input type='text' id='attrValue' name='radAttrInfo.attrValue' value='" + attrValue
                    + "' class='formCss100' />");
            sbf.append("^^");
            sbf.append("<select id='attrValueType' class='select100' name='radAttrInfo.attrValueType'>");
            if (obj.getAttrValueType().equals("string")) {
                sbf.append("<option value='string' selected>");
            } else {
                sbf.append("<option value='string'>");
            }
            sbf.append("string</option>");

            if (obj.getAttrValueType().equals("integer")) {
                sbf.append("<option value='integer' selected>");
            } else {
                sbf.append("<option value='integer'>");
            }
            sbf.append("integer</option>");

            if (obj.getAttrValueType().equals("ipaddr")) {
                sbf.append("<option value='ipaddr' selected>");
            } else {
                sbf.append("<option value='ipaddr'>");
            }
            sbf.append("ipaddr</option>");
            sbf.append("</select>");
        }
        return sbf.toString();
    }

}
