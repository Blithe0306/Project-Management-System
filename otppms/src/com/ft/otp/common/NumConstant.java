/**
 *Administrator
 */
package com.ft.otp.common;

/**
 * 数字常量类，用于工程内静态数字常量的统一定义
 * 
 * 如：令牌是否绑定， 0表示未绑定，1表示已绑定
 *
 * @Date in Apr 25, 2011,11:35:19 AM
 *
 * @author TBM
 */
public class NumConstant {

    /**
     * 公用的数字标识符，用于数值对比等，包括阿拉伯数据0-9
     */
    public static int common_number_0 = 0;
    public static int common_number_1 = 1;
    public static int common_number_2 = 2;
    public static int common_number_3 = 3;
    public static int common_number_4 = 4;
    public static int common_number_5 = 5;
    public static int common_number_6 = 6;
    public static int common_number_7 = 7;
    public static int common_number_8 = 8;
    public static int common_number_9 = 9;

    public static int common_number_na_1 = -1;

    //单一数据库批量操作数据量限制
    public static int batchCount = 500;
    public static int batchCount_10000 = 10000;

    /**
     * 批量操作对象
     */
    //本页选中记录
    public static int batchOper_num0 = 0;
    //本次查询所有记录
    public static int batchOper_num1 = 1;

    /**
     * 批量操作状态 
     */
    //启用
    public static int batchOper_state0 = 0;
    //停用
    public static int batchOper_state1 = 1;
    //锁定
    public static int batchOper_state2 = 2;
    //解锁
    public static int batchOper_state3 = 3;
    //挂失
    public static int batchOper_state4 = 4;
    //解挂
    public static int batchOper_state5 = 5;
    //注销
    public static int batchOper_state6 = 6;
    //恢复使用
    public static int batchOper_state7 = 7;
    //删除
    public static int batchOper_state8 = 8;
    //取消分配
    public static int batchOper_state9 = 9;

    //令牌种子类型c100,c200,c300
    //c100
    public static int token_type0 = 0;
    //c200
    public static int token_type1 = 1;
    //c300
    public static int token_type2 = 2;
    //c400
    public static int token_type3 = 3;

    //手机令牌分发类型
    //在线分发
    public static int dist_online = 1;
    //离线分发
    public static int dist_offline = 0;

    //令牌物理类型常量
    public static final int HARDWARE_TOKEN = 0; //硬件令牌
    public static final int MOBILE_TOKEN = 1; //手机令牌
    public static final int SOFT_TOKEN = 2; //软件令牌
    public static final int SMS_TOKEN = 6; //短信令牌

    // 临时永久锁定边界值
    public static final int TEMP_LONG_LOCKED_NUM_65535 = 65535;

    /**
     * 设备监控预警 预警信息相隔多久发送一次 单位默认都是分钟
     */
    // 设备预警信息 2小时
    public static final int MONITOR_SB_SEND_TIMEINTERVAL = 120;
    // 应用系统预警信息 30分钟
    public static final int MONITOR_APP_SEND_TIMEINTERVAL = 30;

    // 基本预警信息 令牌还有多长时间过期信息 10天
    public static final int MONITOR_BASE_REMAINDAYS_SEND_TIMEINTERVAL = 14400;
    // 基本预警信息 未绑定的令牌比例达到多少进行预警信息 10天
    public static final int MONITOR_BASE_UNBINDLOWER_SEND_TIMEINTERVAL = 14400;
    // 基本预警信息 1小时内令牌同步达到多少比例（次数/用户数）进行预警信息 2小时
    public static final int MONITOR_BASE_SYNCUPPER_SEND_TIMEINTERVAL = 120;
    // 双机运行监控预警消息 24小时
    public static final int MONITOR_HEART_BEAT_CONF_SEND_TIMEINTERVAL = 60*24;
}
