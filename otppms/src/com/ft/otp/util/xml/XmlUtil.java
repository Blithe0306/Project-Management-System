package com.ft.otp.util.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.ft.otp.util.tool.StrTool;

/**
 * 用来解析XML文件
 *
 * @Date in May 5, 2010,4:32:40 PM
 *
 * @author TBM
 */
public class XmlUtil {

    private static Logger logger = Logger.getLogger(XmlUtil.class);

    //存放递归读取的XML节点数据
    private static Map<String, String> elemMap = new HashMap<String, String>();

    /**
     * 载入XML文件，取得XML根节点
     * @Date in May 6, 2010,11:22:08 AM
     * @param xmlPath
     * @return
     */

    public Element getElement(File file) {
        Document document = null;
        SAXReader saxReader = new SAXReader();
        InputStream iStream = null;
        try {
            iStream = new FileInputStream(file);
            document = saxReader.read(iStream);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        Element eleRoot = document.getRootElement();
        return eleRoot;
    }

    /**
     * 加载XML文件，返回Document对象
     * @Date in Feb 24, 2011,5:02:41 PM
     * @param xmlPath
     * @return
     */
    public static synchronized Document getDocument(String xmlPath) {
        SAXReader saxReader = new SAXReader();
        Document document = null;
        InputStream iStream = null;
        try {
            iStream = new FileInputStream(xmlPath);
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
     * 递归读取XML指定节点下的子节点数据
     * @Date in May 6, 2010,11:22:31 AM
     * @param element
     * @return
     */
    public static Map<String, String> recursionXML(Element element) {
        Iterator<?> iterator = element.elementIterator();
        List<?> list = null;
        while (iterator.hasNext()) {
            Element node = (Element) iterator.next();
            list = node.elements();
            int count = node.attributeCount();
            for (int i = 0; i < count; i++) {
                Attribute attr = node.attribute(i);
                elemMap.put(attr.getName(), attr.getText());
            }
            if (StrTool.listNotNull(list)) {
                recursionXML(node);
            } else {
                elemMap.put(node.getName(), node.getText());
            }
        }
        return elemMap;
    }

    /**
     * 读取(加载)管理员权限
     * @Date in Jul 3, 2011,2:38:13 PM
     * @param element
     * @return
     */
    public static Map<String, String> readPermXML(Element element) {
        Map<String, String> permMap = new HashMap<String, String>();
        Iterator<?> iter = element.elementIterator();
        while (iter.hasNext()) {
            Element elem = (Element) iter.next();
            Iterator<?> iter1 = elem.elementIterator();
            while (iter1.hasNext()) {
                Element elem1 = (Element) iter1.next();
                permMap.put(elem.attributeValue("id"), elem1.getText());
            }
        }
        return permMap;
    }

    /**
     * 数据写入XML
     * @Date in Jan 13, 2012,11:03:29 AM
     * @param document
     * @param path
     * @throws Exception
     */
    public static boolean creatXML(String xml, String path) {
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();

            format.setEncoding("GBK"); // 指定XML编码
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
            out.write(xml);
            out.flush();

            if (out != null) {
                out.close();
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 删除文件
     */
    public static void delXml(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void creatTestXML(Document document, String path) throws Exception {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8"); // 指定XML编码
        XMLWriter writer = new XMLWriter(new FileWriter(path), format);
        writer.write(document);
        writer.close();
    }
}