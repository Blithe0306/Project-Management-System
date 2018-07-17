/**
 *Administrator
 */
package com.ft.otp.manager.task.service;

import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;

/**
 * 用户来源定时时间信息业务接口说明
 *
 * @Date in Mar 9, 2012,9:42:45 AM
 *
 * @version v1.0
 *
 * @author ZJY
 */
public interface ITaskInfoServ extends IBaseService  {
 
 void updateList(List<Object> list) throws BaseException;
}
