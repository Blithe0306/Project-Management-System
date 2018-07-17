/**
 *Administrator
 */
package com.ft.otp.common.config;

import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.PropertyConfigurator;
import com.ft.otp.common.Constant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.XmlConstant;
import com.ft.otp.util.properties.BaseProperties;
import com.ft.otp.util.tool.StrTool;

/**
 * 系统运行日志加载
 * 
 * @Date in Apr 8, 2010,3:08:54 PM
 * 
 * @author TBM
 */
public class RunLogConfig {

    private static RunLogConfig config = null;
    private static BaseProperties properties;
    private static String log4j = "/log4j.properties";
    private static String logFile = "/logs/otpcenter.log";

    private static final String THRESHOLD_OFF = "OFF";

    private RunLogConfig() {
        properties = new BaseProperties();
        InputStream iStream = null;
        try {
            iStream = this.getClass().getResourceAsStream(log4j);
            properties.load(iStream);

            setLogProperties();//log4j配置属性重新设置

            PropertyConfigurator.configure(properties);//装入log4j配置信息
        } catch (Exception ex) {
            StrTool.print("log4j configuration file failed to load!");
            ex.printStackTrace();//主动打印异常
        } finally {
            if (null != iStream) {
                try {
                    iStream.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    public static RunLogConfig loadRunLogConfig() {
        if (config != null) {
            return config;
        }

        synchronized (RunLogConfig.class) {
            if (config == null) {
                config = new RunLogConfig();
            }
            return config;
        }
    }

    /**
     * log4j配置属性重新设置
     * 
     * @Date in Aug 9, 2013,5:57:19 PM
     */
    private void setLogProperties() throws Exception {
        //日志记录级别
        String threshold = SystemConfig.getValue(XmlConstant.XML_LOG_THRESHOLD);
        //日志记录模式
        String model = SystemConfig.getValue(XmlConstant.XML_LOG_MODEL);
        //日志文件产生周期
        String period = SystemConfig.getValue(XmlConstant.XML_LOG_TIME_PERIOD);
        //日志文件的最大容量(单位:KB)
        String fileSize = SystemConfig.getValue(XmlConstant.XML_LOG_FILE_SIZE);
        //最多产生日志文件的个数(单位:个)
        String backupIndex = SystemConfig.getValue(XmlConstant.XML_LOG_BACKUP_INDEX);

        properties.put("log4j.appender.logFile.Threshold", threshold);
        properties.put("log4j.appender.logFile.File", Constant.WEB_APP_PATH + logFile);

        if (StrTool.strEqualsIgnoreCase(THRESHOLD_OFF, threshold)) {
            properties.put("log4j.appender.logFile.ImmediateFlush", "false");
        }

        if (StrTool.strEquals(model, StrConstant.common_number_2)) {
            properties.put("log4j.appender.logFile", "org.apache.log4j.DailyRollingFileAppender");
            properties.put("log4j.appender.logFile.DatePattern", period);
        } else {
            properties.put("log4j.appender.logFile", "org.apache.log4j.RollingFileAppender");
            if (!fileSize.endsWith("KB") && !fileSize.endsWith("kb")) {
                fileSize += "KB";
            }
            properties.put("log4j.appender.logFile.MaxFileSize", fileSize);
            properties.put("log4j.appender.logFile.MaxBackupIndex", backupIndex);
        }
    }
}
