package com.ft.otp.manager.customer.action;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.common.config.DepartmentConfig;
import com.ft.otp.common.config.ProjectTypeConfig;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.customer.entity.CustomerInfo;
import com.ft.otp.manager.customer.entity.DepartmentInfo;
import com.ft.otp.manager.customer.service.ICustomerInfoServ;
import com.ft.otp.manager.project.entity.Project;
import com.ft.otp.manager.project.entity.ProjectType;
import com.ft.otp.manager.project.service.IProjectServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 客户相关业务控制Action
 */
public class CustomerInfoAction extends BaseAction implements IBaseAction {

	private static final long		serialVersionUID	= -1192376536078719256L;
	private Logger					logger				= Logger.getLogger(CustomerInfoAction.class);

	private ICustomerInfoServ		custInfoServ		= null;
	private IProjectServ			projectServ			= (IProjectServ) AppContextMgr.getObject("projectServ");
	private CustomerInfo			custInfo			= null;
	private List<DepartmentInfo>	deptList;
	private int						customerIds			= 0;
	private Project					project				= null;
	private List<ProjectType>		typeList;

	private static final String		GO_PROJ_ADD			= "goProjAdd";

	public int getCustomerIds() {
		return customerIds;
	}

	public void setCustomerIds(int customerIds) {
		this.customerIds = customerIds;
	}

	public CustomerInfo getCustInfo() {
		return custInfo;
	}

	public void setCustInfo(CustomerInfo custInfo) {
		this.custInfo = custInfo;
	}

	public List<DepartmentInfo> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<DepartmentInfo> deptList) {
		this.deptList = deptList;
	}

	public ICustomerInfoServ getCustInfoServ() {
		return custInfoServ;
	}

	public void setCustInfoServ(ICustomerInfoServ custInfoServ) {
		this.custInfoServ = custInfoServ;
	}

	/**
	 * 初始化用户列表
	 */
	public String init() {
		if (isNeedClearForm()) {
			//queryForm = null;
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

		return null;
	}

	/**
	 * 添加定制项目
	 * @return
	 */
	public String goProjectAdd() {
		try {
			custInfo = new CustomerInfo();
			String custId = request.getParameter("custId"); //客户ID
			custInfo.setId(Integer.parseInt(custId));
			custInfo = (CustomerInfo) custInfoServ.find(custInfo);
			custInfo.getCustid();
			if (StrTool.objNotNull(custInfo)) {
				project = new Project();
				project.setCustid("" + custInfo.getId());
				project.setCustname(custInfo.getCustname());
			}
			setProject(project);
			setTypeList(ProjectTypeConfig.getPrjTypeList());
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return GO_PROJ_ADD;
	}

	/**
	 * 获取所属部门信息
	 */
	public String getDepts() {
		setResponseWrite(getDeptString(DepartmentConfig.getDeptList()));
		return null;
	}

	public String getDeptString(List<DepartmentInfo> depts) {
		StringBuffer sb = new StringBuffer();

		if (StrTool.objNotNull(depts)) {
			for (int i = 0; i < depts.size(); i++) {
				DepartmentInfo departmentInfo = depts.get(i);
				sb.append(departmentInfo.getDeptid() + ";" + departmentInfo.getDeptName());

				if (i != depts.size() - 1) {
					sb.append(",");
				}
			}
		}
		return sb.toString();
	}

	public String toAddCustomer() {
		setDeptList(DepartmentConfig.getDeptList());
		try {
			//自动生成客户编号
			String curYear = DateTool.getCurDate("yyyy");

			CustomerInfo condition = new CustomerInfo();
			condition.setCustid(curYear);
			CustomerInfo customer;

			customer = (CustomerInfo) custInfoServ.findMaxCustid(condition);
			//如果没有初始编号不存在的话
			if (!StrTool.objNotNull(customer) || !StrTool.strNotNull(customer.getCustid())) {
				condition = new CustomerInfo();
				condition.setCustid(curYear + "0001");
			} else {
				String custid = customer.getCustid();
				custid = custid.substring(4);
				int nums = Integer.parseInt(custid);
				nums += 1;
				if (nums < 10) {
					custid = curYear + "000" + nums;
				} else if (nums < 100) {
					custid = curYear + "00" + nums;
				} else if (nums < 1000) {
					custid = curYear + "0" + nums;
				} else if (nums < 10000) {
					custid = "" + nums;
				}
				condition.setCustid(custid);
			}
			setCustInfo(condition);

		} catch (BaseException ex) {
			ex.printStackTrace();
		}

		return SUCC_FIND;
	}

	/**
	 * 特殊查询，异步查询客户名称是否已经存在
	 */
	public String queryConditionName() {
		//projectServ.findExceptself(project);
		try {
			CustomerInfo conditionName = new CustomerInfo();
			//除本身外的是否存在其他
			if (StrTool.strIsNotNull(custInfo.getCustname())) {
				String[] custname = StrTool.strToUTF8(custInfo.getCustname()).split(",");
				conditionName.setCustname(custname[0]);
				CustomerInfo customer = (CustomerInfo) custInfoServ.findObj(conditionName);
				if (StrTool.objNotNull(customer)) {
					System.out.println("1.存在");
					setResponseWrite("exist");
				}
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 添加用户
	 */
	public String add() {
		try {
			if (StrTool.objNotNull(custInfo)) {

				if (StrTool.strIsNotNull(custInfo.getCustid())) {
					CustomerInfo condition = new CustomerInfo();
					condition.setCustid(custInfo.getCustid());
					CustomerInfo customer = (CustomerInfo) custInfoServ.findObj(condition);
					if (StrTool.objNotNull(customer)) {
						outPutOperResult(Constant.alert_error, "此客户编号已存在，请更换客户编号后重试!");
						return null;
					}
				}

				CustomerInfo conditionName = new CustomerInfo();
				conditionName.setCustname(custInfo.getCustname());
				CustomerInfo customer = (CustomerInfo) custInfoServ.findObj(conditionName);
				if (StrTool.objNotNull(customer)) {
					outPutOperResult(Constant.alert_error, "此客户名称已存在，请更换客户名称后重试!");
					return null;
				}

				custInfo.setOperator(getCurLoginUser());
				custInfo.setCreatetime(DateTool.currentUTC());
				custInfoServ.addObj(custInfo);
				setActionResult(true);
				outPutOperResult(Constant.alert_succ, "添加成功!");
				return null;

			} else {
				outPutOperResult(Constant.alert_error, "客户信息不能为空!");
				return null;
			}
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
			outPutOperResult(Constant.alert_error, "添加失败，出现异常信息!");
		}
		return null;
	}

	/**
	 * 删除用户操作
	 */
	public String delete() {
		Set<?> keys = super.getDelIds("delCustIds");
		try {
			if (StrTool.setNotNull(keys)) {
				Iterator it = keys.iterator();
				while (it.hasNext()) {
					Project prj = new Project();
					prj.setCustid(it.next().toString());
					int counts = (int) projectServ.count(prj);
					if (counts != 0) {
						outPutOperResult(Constant.alert_error, "客户已存在定制项目，不能删除!");
						return null;
					}
				}

				custInfoServ.delObj(keys);
				setCustomerIds(keys.size());
				setActionResult(true);
				outPutOperResult(Constant.alert_succ, "删除成功!");
			}
		} catch (BaseException e) {
			outPutOperResult(Constant.alert_error, "删除失败!");
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 用户编辑初始化，查询一条用户信息
	 */
	public String find() {
		try {
			setCustInfo((CustomerInfo) custInfoServ.find(custInfo));
			setDeptList(DepartmentConfig.getDeptList());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return SUCC_FIND;
	}

	/**
	 * 编辑用户信息
	 */
	public String modify() {

		try {
			if (StrTool.objNotNull(custInfo)) {//不是空的

				CustomerInfo conditionName = new CustomerInfo();
				conditionName.setCustname(custInfo.getCustname());
				conditionName.setId(custInfo.getId());
				CustomerInfo customer = (CustomerInfo) custInfoServ.findObj(conditionName);
				if (StrTool.objNotNull(customer)) {
					outPutOperResult(Constant.alert_error, "此客户名称已存在，请更换客户名称后重试!");
					return null;
				}

				if (StrTool.strIsNotNull(custInfo.getCustid())) {
					CustomerInfo condition = new CustomerInfo();
					condition.setCustid(custInfo.getCustid());
					condition.setId(custInfo.getId());
					customer = (CustomerInfo) custInfoServ.findObj(condition);
					if (StrTool.objNotNull(customer)) {
						outPutOperResult(Constant.alert_error, "此客户编号已存在，请更换客户编号后重试!");
						return null;
					}
				}
				custInfoServ.updateObj(custInfo);
				outPutOperResult(Constant.alert_succ, "修改成功!");
				return null;
			} else {
				outPutOperResult(Constant.alert_error, "修改内容不能为空!");
				return null;
			}
		} catch (Exception e) {
			outPutOperResult(Constant.alert_error, "修改失败，出现异常!");
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	//编辑保存时
	public String findCustnameExistById() {
		try {
			if (StrTool.objNotNull(custInfo)) {
				CustomerInfo condition = new CustomerInfo();
				condition.setCustname(StrTool.strToUTF8(custInfo.getCustname()));
				condition.setCustid(StrTool.strToUTF8(custInfo.getCustid()));
				condition.setId(custInfo.getId());
				CustomerInfo customer = (CustomerInfo) custInfoServ.findObj(condition);
				if (StrTool.objNotNull(customer)) {
					setResponseWrite("false");
				}
			}
		} catch (Exception e) {
			setResponseWrite("false");
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	//新增保存时
	public String findCustnameExist() {
		try {
			if (StrTool.objNotNull(custInfo)) {
				CustomerInfo condition = new CustomerInfo();
				condition.setCustname(StrTool.strToUTF8(custInfo.getCustname()));
				CustomerInfo customer = (CustomerInfo) custInfoServ.findObj(condition);
				if (StrTool.objNotNull(customer)) {
					setResponseWrite("false");
				}
			}
		} catch (Exception e) {
			setResponseWrite("false");
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	//编辑客户编号时
	public String findCustidExistByCustid() {
		try {
			if (StrTool.objNotNull(custInfo)) {
				CustomerInfo condition = new CustomerInfo();
				condition.setCustid(StrTool.strToUTF8(custInfo.getCustid()));
				condition.setId(custInfo.getId());
				CustomerInfo customer = (CustomerInfo) custInfoServ.findObj(condition);
				if (StrTool.objNotNull(customer)) {
					setResponseWrite("false");
				}
			}
		} catch (Exception e) {
			setResponseWrite("false");
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	//新增客户编号时
	public String findCustidExist() {
		try {
			if (StrTool.objNotNull(custInfo)) {
				CustomerInfo condition = new CustomerInfo();
				condition.setCustid(StrTool.strToUTF8(custInfo.getCustid()));
				CustomerInfo customer = (CustomerInfo) custInfoServ.findObj(condition);
				if (StrTool.objNotNull(customer)) {
					setResponseWrite("false");
				}
			}
		} catch (Exception e) {
			setResponseWrite("false");
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 用户信息分页处理
	 */
	public String page() {
		PageArgument pageArg = getArgument(request, 0);
		query(pageArg);

		return SUCCESS;
	}

	/**
	 * 显示用户详细信息
	 */
	public String view() {
		try {
			setCustInfo((CustomerInfo) custInfoServ.find(custInfo));
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCC_VIEW;
	}

	/**
	 * 处理数据查询
	 * @Date in Apr 20, 2011,12:04:36 PM
	 * @param pageArg
	 */
	private List<?> query(PageArgument pageArg) {
		List<?> customerList = null;
		try {
			customerList = custInfoServ.query(custInfo, pageArg);
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}

		return customerList;
	}

	/**
	 * 行数统计 分页处理
	 * @Date in Apr 15, 2011,11:33:36 AM
	 * @return
	 */
	private PageArgument pageArgument() throws BaseException {
		int totalRow = custInfoServ.count(custInfo);
		PageArgument pageArg = getArgument(totalRow);

		return pageArg;
	}

	/**
	 * 用户批量操作（批量删除、批量解绑）
	 * @throws Exception
	 */
	public String batchOper() throws Exception {
		//      Set<?> keys = super.getDelIds("userIds");
		//      String operobj = request.getParameter("operObj");
		//      String oper = request.getParameter("oper");
		//      String userIdStr = request.getParameter("userId");
		//      userIdStr = aide.replaceUserId(userIdStr);
		//      String realNameStr = request.getParameter("realName");
		//      String dOrgunitId = request.getParameter("dOrgunitId");
		//      String tokenStr = request.getParameter("token");
		//      String radprofileid = request.getParameter("radprofileid");
		//      String backEndAuth = request.getParameter("backEndAuth");
		//      String localAuth = request.getParameter("localAuth");
		//      String backEndAuthStr = request.getParameter("backEndAuthStr");
		//      String localAuthStr = request.getParameter("localAuthStr");
		//      String pwd = request.getParameter("pwd");
		//      String locked = request.getParameter("locked");
		//      String enabled = request.getParameter("enabled");
		//      String bindState = request.getParameter("bindState");

		//      boolean flag = false;
		//      if (StrTool.strEquals("1", oper)) { // 解绑操作特殊处理
		//      Map<String, Object> map = null;
		//      if (StrTool.strEquals(operobj, StrConstant.common_number_0)) {//本页
		//      map = modifyData(keys);
		//      } else if (StrTool.strEquals(operobj, StrConstant.common_number_1)) {//本次查询
		//      queryForm.setUserId(userIdStr);
		//      queryForm.setRealName(realNameStr);
		//      queryForm.setDOrgunitId(dOrgunitId);
		//      queryForm.setToken(tokenStr);
		//      queryForm.setLocked(StrTool.parseInt(locked));
		//      queryForm.setEnabled(StrTool.parseInt(enabled));
		//      queryForm.setBackEndAuth(StrTool.parseInt(backEndAuthStr));
		//      queryForm.setLocalAuth(StrTool.parseInt(localAuthStr));
		//      queryForm.setBindState(StrTool.parseInt(bindState));
		//      keys = convertLitToSets(queryForm);
		//      map = modifyData(keys);
		//      }
		//      if (!StrTool.mapNotNull(map)) {
		//      outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_opera_error_tip"));
		//      return null;
		//      }
		//      int bindTotal = ((Integer) map.get("bindTotal")).intValue();
		//      int noBindTotal = ((Integer) map.get("noBindTotal")).intValue();
		//      int allTotal = ((Integer) map.get("allTotal")).intValue();
		//      outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "user_vd_unbind_1") + allTotal
		//      + Language.getLangStr(request, "page_records") + Language.getLangStr(request, "user_vd_user_data")
		//      + Language.getLangStr(request, "user_vd_unbind_2") + bindTotal
		//      + Language.getLangStr(request, "page_records") + Language.getLangStr(request, "user_vd_user_total")
		//      + noBindTotal + Language.getLangStr(request, "page_records")
		//      + Language.getLangStr(request, "user_vd_user_data")
		//      + Language.getLangStr(request, "user_vd_unbind_3") + Language.getLangStr(request, "plaint"));
		//      } else {
		//      if (StrTool.strEquals(operobj, StrConstant.common_number_0)) {//本页
		//      flag = modifyData(oper, keys, radprofileid, backEndAuth, localAuth, pwd);
		//      } else if (StrTool.strEquals(operobj, StrConstant.common_number_1)) {//本次查询
		//      queryForm.setUserId(userIdStr);
		//      queryForm.setRealName(realNameStr);
		//      queryForm.setDOrgunitId(dOrgunitId);
		//      queryForm.setToken(tokenStr);
		//      queryForm.setLocked(StrTool.parseInt(locked));
		//      queryForm.setEnabled(StrTool.parseInt(enabled));
		//      queryForm.setBackEndAuth(StrTool.parseInt(backEndAuthStr));
		//      queryForm.setLocalAuth(StrTool.parseInt(localAuthStr));
		//      queryForm.setBindState(StrTool.parseInt(bindState));
		//      keys = convertLitToSets(queryForm);
		//      flag = modifyData(oper, keys, radprofileid, backEndAuth, localAuth, pwd);
		//      }
		//      if (flag) {
		//      outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_opera_succ_tip"));
		//      } else {
		//      outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_opera_error_tip"));
		//      }
		//      }
		return null;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<ProjectType> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<ProjectType> typeList) {
		this.typeList = typeList;
	}

	/**
	 * 将LIST数据封装到set中 方法说明
	 * @Date in Feb 8, 2012,10:23:37 AM
	 * @param queryForm
	 * @param total
	 * @return
	 */
	//  public Set<?> convertLitToSets(UInfoQueryForm queryForm) {
	//  Set<String> keys = new HashSet<String>();
	//  try {
	//  PageArgument pageArg = new PageArgument();
	//  pageArg.setCurPage(getPage());
	//  pageArg.setPageSize(getPagesize());
	//  UserInfo uiQuery = getUserInfo(queryForm);
	//  uiQuery.setUpPageTag(NumConstant.common_number_0);//不分页
	//  List<?> queryList = userInfoServ.query(uiQuery, pageArg);
	//  int size = queryList.size();
	//  if (StrTool.listNotNull(queryList)) {
	//  for (int i = 0; i < size; i++) {
	//  UserInfo userInfo = (UserInfo) queryList.get(i);
	//  keys.add(userInfo.getUserId() + ":" + userInfo.getDomainId());
	//  }
	//  }
	//  } catch (BaseException e) {
	//  logger.error(e.getMessage(), e);
	//  }
	//  return keys;
	//  }
}
