/**
 *Administrator
 */
package com.ft.otp.manager.token.distmanager.service;

import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;

/**
 * 令牌分发service接口类功能说明
 *
 * @Date in Apr 18, 2011,11:21:50 AM
 *
 * @author ZJY
 */
public interface IDistManagerServ extends IBaseService {

    int importExtToken(List<Object> tokeniList) throws BaseException;

}
