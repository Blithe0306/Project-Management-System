/**
 *Administrator
 */
package com.ft.otp.manager.report.form;

import java.util.Calendar;

import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 统计管理查询form类功能说明
 *
 * @Date in Jan 29, 2013,10:54:52 AM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class ReportQueryForm {
    private String domain = null; // 域
    private String deptNames = null; // 组织机构
    private String beginDateStr = "";// 开始时间串
    private long beginDateLong = 0L;// 开始时间值
    private String endDateStr = "";// 结束时间串
    private long endDateLong = 0L;// 结束时间值

    // 令牌报表 过期时间统计时所用属性
    private long weekExpireTime = 0L; // 一周过期时间
    private long monthExpireTime = 0L; // 一月过期时间
    private long quarterExpireTime = 0L; // 一季度过期时间
    private long moreExpireTime = 0L; // 大于三个月过期时间
    private long nowtime = 0L; // 当前时间

    private int filterType = 0;// 判断组织机构的过滤类型 如果是0 域和组织机构是or  如果是1 域和组织机构是and

    private int operType = 0;// 业务类型
    private String domainAndOrgName = "";// 组织机构名称查询条件

    public long getNowtime() {
        if (nowtime == 0) {
            Calendar calendar = Calendar.getInstance();
            nowtime = DateTool.dateToInt(calendar.getTime());
        }
        return nowtime;
    }

    public void setNowtime(long nowtime) {
        this.nowtime = nowtime;
    }

    /**
     * @return the deptNames
     */
    public String getDeptNames() {
        return strToSqlInFormat(deptNames);
    }

    /**
     * @param deptNames the deptNames to set
     */
    public void setDeptNames(String deptNames) {
        this.deptNames = deptNames;
    }

    /**
     * @return the beginDateStr
     */
    public String getBeginDateStr() {
        return beginDateStr;
    }

    /**
     * @param beginDateStr the beginDateStr to set
     */
    public void setBeginDateStr(String beginDateStr) {
        this.beginDateStr = beginDateStr;
    }

    /**
     * @return the beginDateLong
     */
    public long getBeginDateLong() {
        if (beginDateLong == 0) {
            if (StrTool.strNotNull(beginDateStr)) {
                beginDateLong = DateTool.dateToInt(DateTool.strToDateFull(beginDateStr));
            }
        }
        return beginDateLong;
    }

    /**
     * @param beginDateLong the beginDateLong to set
     */
    public void setBeginDateLong(long beginDateLong) {
        this.beginDateLong = beginDateLong;
    }

    /**
     * @return the endDateStr
     */
    public String getEndDateStr() {
        return endDateStr;
    }

    /**
     * @param endDateStr the endDateStr to set
     */
    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    /**
     * @return the endDateLong
     */
    public long getEndDateLong() {
        if (endDateLong == 0) {
            if (StrTool.strNotNull(endDateStr)) {
                endDateLong = DateTool.dateToInt(DateTool.strToDateFull(endDateStr));
            } else {
                endDateLong = Long.MAX_VALUE;
            }
        }
        return endDateLong;
    }

    /**
     * @param endDateLong the endDateLong to set
     */
    public void setEndDateLong(long endDateLong) {
        this.endDateLong = endDateLong;
    }

    /**
     * @return the domain
     */
    public String getDomain() {
        return strToSqlInFormat(domain);
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * @return the weekExpireTime
     */
    public long getWeekExpireTime() {
        if (weekExpireTime == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.WEEK_OF_MONTH, 1);
            weekExpireTime = DateTool.dateToInt(calendar.getTime());
        }

        return weekExpireTime;
    }

    /**
     * @param weekExpireTime the weekExpireTime to set
     */
    public void setWeekExpireTime(long weekExpireTime) {
        this.weekExpireTime = weekExpireTime;
    }

    /**
     * @return the monthExpireTime
     */
    public long getMonthExpireTime() {
        if (monthExpireTime == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 1);
            monthExpireTime = DateTool.dateToInt(calendar.getTime());
        }

        return monthExpireTime;
    }

    /**
     * @param monthExpireTime the monthExpireTime to set
     */
    public void setMonthExpireTime(long monthExpireTime) {
        this.monthExpireTime = monthExpireTime;
    }

    /**
     * @return the quarterExpireTime
     */
    public long getQuarterExpireTime() {
        if (quarterExpireTime == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 3);
            quarterExpireTime = DateTool.dateToInt(calendar.getTime());
        }

        return quarterExpireTime;
    }

    /**
     * @param quarterExpireTime the quarterExpireTime to set
     */
    public void setQuarterExpireTime(long quarterExpireTime) {
        this.quarterExpireTime = quarterExpireTime;
    }

    /**
     * @return the moreExpireTime
     */
    public long getMoreExpireTime() {
        if (moreExpireTime == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 3);
            moreExpireTime = DateTool.dateToInt(calendar.getTime());
        }

        return moreExpireTime;
    }

    /**
     * @param moreExpireTime the moreExpireTime to set
     */
    public void setMoreExpireTime(long moreExpireTime) {
        this.moreExpireTime = moreExpireTime;
    }

    /**
     * 将aa,bb,cc 格式的字符串转换为'aa','bb','cc' 格式的字符串 
     * 如果传入的是null或"" 返回''
     * 方法说明
     * @Date in Feb 20, 2013,4:54:49 PM
     * @param str
     * @return
     */
    private String strToSqlInFormat(String strs) {
        String result = "";
        if (StrTool.strNotNull(strs)) {
            String[] arrStr = strs.split(",");
            StringBuilder sb = new StringBuilder();
            if (StrTool.arrNotNull(arrStr)) {
                for (String str : arrStr) {
                    str = str.replace("'", "");
                    // 如果不存在则添加上
                    if (sb.indexOf("'" + str + "',") == -1) {
                        sb.append("'" + str + "',");
                    }
                }

                result = sb.toString();
                if (StrTool.strNotNull(result)) {
                    result = result.substring(0, result.length() - 1);
                }
            } else {
                result = null;
            }
        } else {
            result = null;
        }

        return result;
    }

    /**
     * @return the filterType
     */
    public int getFilterType() {
        return filterType;
    }

    /**
     * @param filterType the filterType to set
     */
    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }

    /**
     * @return the operType
     */
    public int getOperType() {
        return operType;
    }

    /**
     * @param operType the operType to set
     */
    public void setOperType(int operType) {
        this.operType = operType;
    }

    /**
     * @return the domainAndOrgName
     */
    public String getDomainAndOrgName() {
        return domainAndOrgName;
    }

    /**
     * @param domainAndOrgName the domainAndOrgName to set
     */
    public void setDomainAndOrgName(String domainAndOrgName) {
        this.domainAndOrgName = domainAndOrgName;
    }
}
