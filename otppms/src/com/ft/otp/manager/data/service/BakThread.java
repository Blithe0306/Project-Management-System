package com.ft.otp.manager.data.service;

import java.util.Map;

import org.apache.log4j.Logger;

import com.ft.otp.common.language.Language;
import com.ft.otp.manager.data.entity.DBBakConfInfo;
import com.ft.otp.manager.data.entity.TableInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 备份数据线程
 * 
 */

public class BakThread extends Thread {
    private Logger logger = Logger.getLogger(BakThread.class);

    //单列
    private static BakThread instance = null;

    private DBBakConfInfo dbBakConfInfo = null;

    public static synchronized BakThread getInstance(DBBakConfInfo bakConfInfo) {
        return StrTool.objNotNull(instance) ? instance : (new BakThread(bakConfInfo));
    }

    public BakThread(DBBakConfInfo bakConfInfo) {
        this.dbBakConfInfo = bakConfInfo;
    }

    /**
     * 数据备份线程run
     * 该方法完成如下操作
     * 1、得到要备份的（表及字段）列表
     * 2、依据数据生成数据文件
     * （可选）3、远程备份 需要FTPclient上传到远程服务器
     *        4、删除本地临时目录下的备份文件
     */
    public synchronized void run() {

        Map<String, TableInfo> tableInfos;
        try {
            tableInfos = DataBakServ.getTablesAndCols(dbBakConfInfo.getIsbaklog());//获得配置的表信息
            DataBakServ.createBakFile(tableInfos, dbBakConfInfo, null);//备份
        } catch (Exception e) {
            logger.error(Language.getLangValue("databak_vd_exception", Language.getCurrLang(null), null), e);
            stop();
        }
    }

}
