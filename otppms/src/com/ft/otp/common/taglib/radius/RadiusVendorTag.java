/**
 *Administrator
 */
package com.ft.otp.common.taglib.radius;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ft.otp.common.config.RadiusConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.taglib.BaseTag;
import com.ft.otp.manager.confinfo.radius.entity.RadAttrInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 封装RADIUS，厂商名称标签
 * 
 * @Date in Oct 31, 2012,12:59:12 PM
 * 
 * @version v1.0
 * 
 * @author YXS
 */
public class RadiusVendorTag extends BaseTag {

	private static final long serialVersionUID = 6099862330376280098L;
	private Logger logger = Logger.getLogger(RadiusVendorTag.class);

	public RadiusVendorTag() {
		super();
	}

	private String dataSrc;
	private String index1Lang;
	private String index1Val;

	public int doEndTag() {
		try {
			pageContext.getOut().print(this.optionStr());
		} catch (Exception e) {
		    logger.error(e.getMessage(), e);
		}
		return EVAL_PAGE;
	}

	@SuppressWarnings("unchecked")
	private String optionStr() throws Exception {
	    HttpSession session = pageContext.getSession();
		StringBuilder sBuilder = new StringBuilder();
		Map radMap = RadiusConfig.radiusMap;
		if (!StrTool.mapNotNull(radMap)) {
			return sBuilder.toString();
		}
		sBuilder.append("<option value='-2' selected='selected'>---"+Language.getLangValue("common_syntax_please_sel", Language.getCurrLang(session), null)+"---</option>");
		Iterator<?> it = radMap.entrySet().iterator();
		List list = new ArrayList();
		RadAttrInfo obj = null;
		// 组装LIST
		while (it.hasNext()) {
			Map.Entry ent = (Map.Entry) it.next();
			obj = (RadAttrInfo) (ent.getValue());
			String vendorId = String.valueOf(obj.getVendorid());
			String vendorName = obj.getVendorname();
			if(StrTool.strEquals("-1", vendorId)){
				vendorName = Language.getLangValue("auth_conf_radius_property", Language.getCurrLang(session), null);
			}
			list.add(vendorId+":"+vendorName);
		}
		// 去重复
		HashSet set = new HashSet(list);
		list.clear();
		list.addAll(set);        
		// 封装下拉数据
		for(int i=0; i<list.size(); i++){
			String data = (String)list.get(i);
			String vendorId = data.split(":")[0];
			String vendorName = data.split(":")[1];
			sBuilder.append("<option value='").append(vendorId).append("' ");
			if (StrTool.strEquals(dataSrc, vendorId)) {
				sBuilder.append(" selected='selected' ");
			}
			sBuilder.append(">");
			sBuilder.append(vendorName).append("\n");
			sBuilder.append("</option>");
		}
		String result = sBuilder.toString();
		return result;
	}

	public String getDataSrc() {
		return dataSrc;
	}

	public void setDataSrc(String dataSrc) {
		this.dataSrc = dataSrc;
	}

	public String getIndex1Lang() {
		return index1Lang;
	}

	public void setIndex1Lang(String index1Lang) {
		this.index1Lang = index1Lang;
	}

	public String getIndex1Val() {
		return index1Val;
	}

	public void setIndex1Val(String index1Val) {
		this.index1Val = index1Val;
	}
}
