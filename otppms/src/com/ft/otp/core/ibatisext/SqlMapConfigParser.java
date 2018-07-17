package com.ft.otp.core.ibatisext;

import com.ibatis.sqlmap.engine.builder.xml.XmlConverter;

/**
 * 可改变ibatis的执行代理
 *
 * @Date in Apr 2, 2011,7:08:24 PM
 *
 * @author TBM
 */

public class SqlMapConfigParser extends
        com.ibatis.sqlmap.engine.builder.xml.SqlMapConfigParser {

    public SqlMapConfigParser() {
        this(null, null);
    }

    public SqlMapConfigParser(XmlConverter sqlMapConfigConv,
            XmlConverter sqlMapConv) {
        super(sqlMapConfigConv, sqlMapConv);
        //必要时在此处改变IBATIS的执行代理,实现自己的执行逻辑,可用于某些性能的优化
        //vars.delegate = null;
        //vars.client = new SqlMapClientImpl(vars.delegate);
    }
}
