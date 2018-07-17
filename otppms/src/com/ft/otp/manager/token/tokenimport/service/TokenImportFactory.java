/**
 *Administrator
 */
package com.ft.otp.manager.token.tokenimport.service;

import java.io.File;
import org.apache.log4j.Logger;

import com.ft.otp.common.Constant;
import com.ft.otp.manager.token.tokenimport.entity.VendorInfo;
import com.ft.otp.util.dyloader.DynaClassLoader;
import com.ft.otp.util.tool.StrTool;

/**
 * 令牌导入工厂，动态加载不同厂商
 *
 * @Date in Mar 27, 2013,2:50:24 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class TokenImportFactory {

    private static Logger logger = Logger.getLogger(TokenImportFactory.class);

    public static ITokenImport geTokenImportServ(VendorInfo vendorInfo) {
        ITokenImport tokenImportServ = null;
        String jarPath = null;
        String classPath = null;

        try {
            jarPath = Constant.BASE_CLASS_PATH + vendorInfo.getJarPath();
            classPath = vendorInfo.getClassPath();
            if (!StrTool.strNotNull(classPath)) {
                classPath = "com.vendor" + vendorInfo.getVendorId() + ".TokenImportImpl";
            }

            DynaClassLoader cld = new DynaClassLoader(Thread.currentThread().getContextClassLoader());
            //令牌生产的时候 动态加载的jar包
            File[] files = new File[] { new File(jarPath) };
            cld.addEtries(files);
            Class<?> instance = cld.getDynaClass(classPath, false);
            tokenImportServ = (ITokenImport) instance.newInstance();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

        return tokenImportServ;
    }

}
