package com.ft.otp.base.action;

/**
 * 基础Action，提供常用控制流程处理接口，供实现。
 *
 * @Date in Apr 2, 2011,4:01:45 PM
 *
 * @author TBM
 */
public interface IBaseAction {

    /**
     * 初始化操作
     * @Date in Apr 11, 2011,1:49:54 PM
     * @return
     */
    String init();

    /**
     * 添加操作
     * @Date in Apr 2, 2011,4:42:25 PM
     */
    String add();

    /**
     * 删除操作
     * @Date in Apr 2, 2011,4:42:38 PM
     */
    String delete();

    /**
     * 修改操作
     * @Date in Apr 2, 2011,4:42:47 PM
     */
    String modify();

    /**
     * 分页处理
     * @Date in Apr 11, 2011,1:48:33 PM
     * @return
     */
    String page();

    /**
     * 查询一条数据
     * @Date in Apr 2, 2011,4:43:02 PM
     * @return
     */
    String find();

    /**
     * 显示详细信息
     * @Date in Apr 12, 2011,9:58:09 AM
     * @return
     */
    String view();

}
