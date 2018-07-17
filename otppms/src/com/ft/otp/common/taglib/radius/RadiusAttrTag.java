/**
 *Administrator
 */
package com.ft.otp.common.taglib.radius;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;


import org.apache.log4j.Logger;

import com.ft.otp.common.config.RadiusConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.taglib.BaseTag;
import com.ft.otp.manager.confinfo.radius.entity.RadAttrInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 封装RADIUS配置属性名称标签
 * 
 * @Date in Oct 31, 2012,12:59:12 PM
 * 
 * @version v1.0
 * 
 * @author YXS
 */
public class RadiusAttrTag extends BaseTag {

	private static final long serialVersionUID = 6099862330376280098L;
	private Logger logger = Logger.getLogger(RadiusAttrTag.class);

	public RadiusAttrTag() {
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
		RadAttrInfo obj = null;
		while (it.hasNext()) {
			Map.Entry ent = (Map.Entry) it.next();
			obj = (RadAttrInfo) (ent.getValue());
			String attrId = obj.getAttrId();
			String attrName = obj.getAttrName();
			sBuilder.append("<option value='").append(attrId).append("' ");
			if (StrTool.strEquals(dataSrc, attrId)) {
				sBuilder.append(" selected='selected' ");
			}
			sBuilder.append(">");
			sBuilder.append(attrName).append("\n");
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
