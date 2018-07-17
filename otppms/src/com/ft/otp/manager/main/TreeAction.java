package com.ft.otp.manager.main;

import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.common.Constant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.admin_perm.entity.AdminPermCode;
import com.ft.otp.manager.admin.admin_perm.service.IAdminPermServ;
import com.ft.otp.manager.admin.role_perm.entity.RolePerm;
import com.ft.otp.manager.login.entity.LinkUser;
import com.ft.otp.manager.login.service.OnlineUsers;
import com.ft.otp.util.tool.StrTool;

/**
 * 导航菜单树加载处理
 * 
 * 此类用于加载应用中左侧菜单树数据,由于树控件采用的是LigerUI下的LigerTree控件,因此
 * 数据以JSON格式返回,在此采用的方式是将菜单项配置在XML中,访问时加载此XML文件指定模块
 * 并转换成JSON数据返回,供树控件使用.
 * 使用说明:在LigerTree构建时配置url指向本Action路径,并传一模块名代表加载该模块相关
 * Menu数据,如treeAction_loadTreeData.action?module=admin,该module需要体现在
 * XML文件中,如此时传来的是admin,则加载admin-module节点下的数据:
 * <admin-module>
        <tree-node text='用户管理' isexpand='true'>
            <url>xxxx</url>
        </tree-node>
        .....
   <admin-module>
 *
 * @Date in Apr 24, 2012,6:33:59 PM
 *
 * @version v1.0
 *
 * @author ZYH
 */
public class TreeAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = -528460341496267254L;

    private Logger logger = Logger.getLogger(TreeAction.class);
    //管理员常用功能-权限码服务接口
    public IAdminPermServ adminPermCodeServ = (IAdminPermServ) AppContextMgr.getObject("adminPermCodeServ");

    private String module; // 要加载数据所属的模块名

    public String add() {
        return null;
    }

    public String delete() {
        return null;
    }

    public String find() {
        return null;
    }

    public String init() {
        return null;
    }

    public String modify() {
        return null;
    }

    public String page() {
        return null;
    }

    public String view() {
        return null;
    }

    /**
     * 方法说明 加载菜单树数据
     * @Date in Apr 25, 2012,11:13:34 AM
     * @return LigerUI树控件LigerTree所需JSON数据
     */
    public String loadTreeData() {
        try {
            //String filePath = ServletActionContext.getServletContext().getRealPath("/") + Constant.MENU_TREE;
            Document document = createDocument(Constant.MENU_TREE);
            Element moduleElement = (Element) document.selectSingleNode("/tree-data/" + getModule() + "-module");
            if (null != moduleElement) {
                JSONArray treeData = parseXMLToJsonArray(moduleElement);
                setResponseWrite(treeData.toString());
                response.flushBuffer();
            }
        } catch (DocumentException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * 方法说明 将XML文件中children结点解析成JSONArray数据,递归处理
     * @Date in Apr 25, 2012,11:45:49 AM
     * @param children Dom4J元素,子节点元素
     * @return 子节点元素组成的Json数据
     */
    @SuppressWarnings("unchecked")
    private JSONArray parseXMLToJsonArray(Element children) throws Exception {
        JSONArray jArray = null;
        if (null != children) {
            jArray = new JSONArray();
            Iterator<Element> itChildren = children.elementIterator("tree-node");
            while (itChildren.hasNext()) {
                Element child = (Element) itChildren.next();
                JSONObject jObject = new JSONObject();
                String lanKey = child.attributeValue("text"); //显示文字
                String permkey = child.attributeValue("permcode");//权限码
                jObject.put("permcode", permkey);
                jObject.put("isexpand", child.attributeValue("isexpand"));
                jObject.put("url", child.elementTextTrim("url"));
                String lanval = "";
                if (StrTool.strEquals(getModule(), "default")) { //默认首页内容 先计算是否添加为常用功能，再判断是否有权限 

                    if (StrTool.strEquals(permkey, "001")) {//如果是首页的根节点不用校验权限和常用权限
                        lanval = Language.getLangStr(request, lanKey);
                    } else {
                        // 判断是否添加为常用功能
                        AdminPermCode adminPermCode = new AdminPermCode();
                        adminPermCode.setAdminid(super.getCurLoginUser());
                        adminPermCode.setPermcode(permkey);

                        if (adminPermCodeServ.isOftenUsed(adminPermCode)) {//如果是常用功能
                            lanval = getPermCodeStr(permkey, lanKey);// 判断是否有权限
                            lanval = lanval
                                    + "<img id='delImg_"
                                    + permkey
                                    + "' width='15px' height='15px' style='margin:0 0 -2 10;display:none' src='../../images/icon/closewin1.png' title='取消常用'/>";
                        } else {
                            lanval = "";
                        }
                    }
                } else {
                    lanval = getPermCodeStr(permkey, lanKey);
                }
                Element cElement = child.element("children");
                if (null != cElement) {
                    jObject.put("children", parseXMLToJsonArray(cElement));
                }
                if (lanval != null && !lanval.equals("")) {
                    jObject.put("text", lanval);
                    jArray.add(jObject);
                }
            }
        }
        return jArray;
    }

    /**
     * 方法说明 创建DOM4J的Document对象
     * @Date in Apr 25, 2012,10:58:39 AM
     * @param url XML文件所在路径
     * @return DOM4J的Document对象
     * @throws DocumentException
     */
    private Document createDocument(String url) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        return document;
    }

    /**
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * @param module the module to set
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * 获取在线用户
     *  permcode与权限列表中的权限code比对
     *  如果存在则权限存在，根据lankey获取多语言文字，返回多语言文字，不存在则返回""
     */
    private String getPermCodeStr(String permkey, String lankey) {
        HttpSession session = request.getSession(true);
        String sessionId = session.getId();

        LinkUser linkUser = OnlineUsers.getUser(sessionId);
        Map<String, Object> permMap = linkUser.getPermMap();
        RolePerm otpPerm = (RolePerm) permMap.get(permkey);

        String permVal = "";
        String langVal = "";
        if (!StrTool.mapNotNull(permMap) || !StrTool.objNotNull(otpPerm)) {
            return "";
        } else {
            permVal = otpPerm.getPermcode();
            if (StrTool.strNotNull(permVal)) {
                langVal = Language.getLangStr(request, lankey);
            } else {
                return "";
            }
        }
        return langVal;
    }

}
