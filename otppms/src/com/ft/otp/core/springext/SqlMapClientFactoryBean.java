package com.ft.otp.core.springext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.support.lob.LobHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import com.ft.otp.common.Constant;
import com.ft.otp.common.database.pool.DbEnv;
import com.ft.otp.core.ibatisext.SqlMapClientBuilder;
import com.ft.otp.util.tool.StrTool;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.engine.transaction.TransactionConfig;
import com.ibatis.sqlmap.engine.transaction.TransactionManager;
import com.ibatis.sqlmap.engine.transaction.external.ExternalTransactionConfig;

/**
 * 使用自定义的SqlMapClientBuilder创建SqlMapClient
 */
public class SqlMapClientFactoryBean extends org.springframework.orm.ibatis.SqlMapClientFactoryBean {

    private Logger logger = Logger.getLogger(SqlMapClientFactoryBean.class);

    private Resource configLocation;

    private Properties sqlMapClientProperties;

    private DataSource dataSource;

    private boolean useTransactionAwareDataSource = true;

    private Class transactionConfigClass = ExternalTransactionConfig.class;

    private Properties transactionConfigProperties;

    private LobHandler lobHandler;

    private SqlMapClient sqlMapClient;

    public SqlMapClientFactoryBean() {
        this.transactionConfigProperties = new Properties();
        this.transactionConfigProperties.setProperty("SetAutoCommitAllowed", "false");
    }

    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }

    public void setSqlMapClientProperties(Properties sqlMapClientProperties) {
        this.sqlMapClientProperties = sqlMapClientProperties;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setUseTransactionAwareDataSource(boolean useTransactionAwareDataSource) {
        this.useTransactionAwareDataSource = useTransactionAwareDataSource;
    }

    public void setTransactionConfigClass(Class transactionConfigClass) {
        if (transactionConfigClass == null || !TransactionConfig.class.isAssignableFrom(transactionConfigClass)) {
            throw new IllegalArgumentException("Invalid transactionConfigClass: does not implement "
                    + "com.ibatis.sqlmap.engine.transaction.TransactionConfig");
        }
        this.transactionConfigClass = transactionConfigClass;
    }

    public void setTransactionConfigProperties(Properties transactionConfigProperties) {
        this.transactionConfigProperties = transactionConfigProperties;
    }

    public void setLobHandler(LobHandler lobHandler) {
        this.lobHandler = lobHandler;
    }

    public void afterPropertiesSet() {
        if (this.configLocation == null) {
            throw new IllegalArgumentException("configLocation is required");
        }

        InputStream inStream = null;
        try {
            // Build the SqlMapClient.
            inStream = this.configLocation.getInputStream();
            SqlMapClientBuilder.setDbType(DbEnv.getDbtype());
            //Dynamic loading SqlMap

            Document document = getDocument(inStream);
            Reader reader = addElement(document, DbEnv.getDbtype());

            this.sqlMapClient = (this.sqlMapClientProperties != null) ? SqlMapClientBuilder.buildSqlMapClient(reader,
                    this.sqlMapClientProperties) : SqlMapClientBuilder.buildSqlMapClient(reader);

            // Tell the SqlMapClient to use the given DataSource, if any.
            if (this.dataSource != null) {
                TransactionConfig transactionConfig = (TransactionConfig) this.transactionConfigClass.newInstance();
                DataSource dataSourceToUse = this.dataSource;
                if (this.useTransactionAwareDataSource && !(this.dataSource instanceof TransactionAwareDataSourceProxy)) {
                    dataSourceToUse = new TransactionAwareDataSourceProxy(this.dataSource);
                }
                transactionConfig.setDataSource(dataSourceToUse);
                transactionConfig.initialize(this.transactionConfigProperties);
                applyTransactionConfig(this.sqlMapClient, transactionConfig);
            }
        } catch (Exception e) {
            //记录异常日志
            logger.error(e.getMessage(), e);
        } finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    protected void applyTransactionConfig(SqlMapClient sqlMapClient, TransactionConfig transactionConfig) {
        if (!(this.sqlMapClient instanceof ExtendedSqlMapClient)) {
            throw new IllegalArgumentException(
                    "Cannot set TransactionConfig with DataSource for SqlMapClient if not of type "
                            + "ExtendedSqlMapClient: " + this.sqlMapClient);
        }
        ExtendedSqlMapClient extendedClient = (ExtendedSqlMapClient) this.sqlMapClient;
        transactionConfig.setMaximumConcurrentTransactions(extendedClient.getDelegate().getMaxTransactions());
        extendedClient.getDelegate().setTxManager(new TransactionManager(transactionConfig));
    }

    public Object getObject() {
        return this.sqlMapClient;
    }

    public Class getObjectType() {
        return (this.sqlMapClient != null ? this.sqlMapClient.getClass() : SqlMapClient.class);
    }

    public boolean isSingleton() {
        return true;
    }

    class NoOpEntityResolver implements EntityResolver {
        @SuppressWarnings("deprecation")
        public InputSource resolveEntity(String publicId, String systemId) {
            return new InputSource(new StringBufferInputStream(""));
        }
    }

    private Document getDocument(InputStream iStream) {
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            saxReader.setValidation(false);
            saxReader.setEntityResolver(new NoOpEntityResolver());
            document = saxReader.read(iStream);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (iStream != null) {
                try {
                    iStream.close();
                } catch (IOException ex) {
                }
            }
        }

        return document;
    }

    /**
     * 返回一个ibatis/sqlmap/数据脚本文件名组合而成的XML文件
     * 
     * @Date in Jun 9, 2011,3:01:41 PM
     * @param dbtype
     * @param srcIs
     * @return
     * @throws Exception
     */
    private Reader addElement(Document document, String dbtype) throws Exception {
        String sqlMapPath = "ibatis/sqlmap";
        Element rootElement = document.getRootElement();

        File file = new File(Constant.IBATIS_SQLMAP);
        List<String> flieNameList = new ArrayList<String>();
        orgSqlMap("/", file, flieNameList, dbtype, true);

        //排序flieNameList
        flieNameList = sequenceList(flieNameList);
        for (int x = 0; x < flieNameList.size(); x++) {
            String fileName = flieNameList.get(x);
            Element element = rootElement.addElement("sqlMap");
            element.addAttribute("resource", sqlMapPath + fileName);
        }
        String xml = document.asXML();

        return new StringReader(xml);
    }

    /**
     * 递归读取ibatis/sqlmap/数据脚本文件名放入list
     * @Date in Jun 9, 2011,3:01:00 PM
     * @param prefix
     * @param file
     * @param fnlist
     * @param dbtype
     * @param isa
     */
    private void orgSqlMap(String prefix, File file, List<String> fnlist, String dbtype, boolean isa) {
        if (file.isFile()) {
            String tempfp = prefix + file.getName();
            // 只允许加载XML文件
            if(StrTool.strEquals(file.getName().substring(file.getName().
            		lastIndexOf(".") + 1).toLowerCase(), "xml")){
            	fnlist.add(tempfp);
            }
            fnlist.add(tempfp);
        } else {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file2 = files[i];
                if (file2.isFile()) {
                    String tempfp = prefix + file2.getName();
                    // 只允许加载XML文件
                    if(StrTool.strEquals(file2.getName().substring(file2.getName().
                    		lastIndexOf(".") + 1).toLowerCase(), "xml")){
                    	fnlist.add(tempfp);
                    }
                } else {
                    if (isa) {
                        if (!file2.getName().equalsIgnoreCase(dbtype)) {
                            continue;
                        }
                    }
                    //递归
                    orgSqlMap("/" + file2.getName() + "/", file2, fnlist, dbtype, false);
                }
            }
        }
    }

    /**
     * 排序ibatis/sqlmap/数据脚本文件名顺序
     * @Date in Jun 9, 2011,4:07:37 PM
     * @param list
     * @return
     */
    private List<String> sequenceList(List<String> list) {
        List<String> fileList = new ArrayList<String>();
        List<String> directoryList = new ArrayList<String>();
        Iterator<String> iter = list.iterator();
        while (iter.hasNext()) {
            String name = iter.next();
            String tmpStr = name.substring(1, name.length());
            if (tmpStr.indexOf("/") != -1 || tmpStr.indexOf("\\") != -1) {
                directoryList.add(name);
            } else {
                fileList.add(name);
            }
        }

        fileList.addAll(directoryList);
        return fileList;
    }

}
