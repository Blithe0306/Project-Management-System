package com.ft.otp.core.ibatisext;

import java.io.Reader;
import com.ibatis.common.resources.Resources;
import com.ibatis.dao.client.Dao;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.DaoManagerBuilder;

/**
 * iBatis的DAO工厂，
 * 特殊情况下可直接从此处获取DAO实例来直接获取Connection
 */

public class DAOFactory {

    private static DaoManager daoManager;

    private synchronized static void initDaoManager() {
        if (daoManager == null) {
            String resource = "ibatis/sqlMapDao.xml";
            try {
                Reader reader = Resources.getResourceAsReader(resource);
                daoManager = DaoManagerBuilder.buildDaoManager(reader);
            } catch (Exception e) {
                throw new RuntimeException("failed to build ibatis DAOFactory:"
                        + resource, e);
            }
        }
    }

    /**
     * @return
     */
    public static DaoManager getDaomanager() {
        if (daoManager == null) {
            initDaoManager();
        }
        return daoManager;
    }

    public static Dao getDao(Class cls) {
        return getDaomanager().getDao(cls);
    }

    public static Dao getDao(Class cls, String arg) {
        return getDaomanager().getDao(cls, arg);
    }
}