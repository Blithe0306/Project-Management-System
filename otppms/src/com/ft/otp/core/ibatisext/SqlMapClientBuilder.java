package com.ft.otp.core.ibatisext;

import java.io.Reader;
import java.util.Properties;
import com.ibatis.sqlmap.client.SqlMapClient;

public class SqlMapClientBuilder {

    private static String dbType;

    private SqlMapClientBuilder() {
    }

    public static String getDbType() {
        return dbType;
    }

    public static void setDbType(String dbType) {
        SqlMapClientBuilder.dbType = dbType;
    }

    public static SqlMapClient buildSqlMapClient(Reader reader) {
        return new SqlMapConfigParser().parse(reader);
    }

    public static SqlMapClient buildSqlMapClient(Reader reader, Properties props) {
        return new SqlMapConfigParser().parse(reader, props);
    }

}
