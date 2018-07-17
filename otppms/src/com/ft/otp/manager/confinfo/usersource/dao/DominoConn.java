package com.ft.otp.manager.confinfo.usersource.dao;

import java.util.ArrayList;
import java.util.List;
import lotus.domino.*;

/**
 * 类功能说明
 * 
 * @Date in Jul 15, 2010,3:18:55 PM
 * 
 * @author TBM
 */
public class DominoConn {
    private Session session = null;
    private String dbName;
    private String tbName = "People";
    private String clName;

    private String ior;
    private String dominoURL; // DOMINO服务器的IP地址
    int dominoPort;
    private String username; // 用户ID
    private String password; // 用户口令

//    public DominoConn(DominoInfo dominoInfo) {
//        //dominoInfo dominoInfo = dominoInfo;
//
//    }

    /**
     * 获取连接
     * 
     * @Date in Jul 15, 2010,3:49:24 PM
     * @return
     */
    public boolean iniDominoConn() throws NotesException {
        /*
         * //如果已将ior文件拷到本地,则直接读ior文件 try { BufferedReader iorin = new
         * BufferedReader(new FileReader( "diiop_ior.txt")); ior =
         * iorin.readLine(); iorin.close(); } catch (IOException e) { ior =
         * null; }
         * 
         * //如果读不到diiop_ior.txt文件，则需要通过网络从服务器下载这个文件 //这需要domino服务器开放http服务 if
         * (ior == null) { //下载IOR ior = NotesFactory.getIOR(dominoURL); }
         * session = NotesFactory.createSessionWithIOR(ior, username, password);
         * 
         * //如果这一行打印出来了,表示连接成功 //System.out.println(session);
         */

        session = NotesFactory.createSession(dominoURL, username, password);

        return true;
    }

    /**
     * 更新用户 根据条件取得用户列表
     * 
     * @Date in Jul 15, 2010,3:51:34 PM
     */
    public List<String> updateUsers() throws NotesException {
        List<String> list = new ArrayList<String>();
        // 取得库对象
        Database db = session.getDatabase(session.getServerName(), dbName);

        View view = db.getView(tbName);
        ViewEntryCollection viewCollection = view.getAllEntries();
        int count = viewCollection.getCount();

        for (int i = 1; i <= count; i++) {
            ViewEntry entry = viewCollection.getNthEntry(i);
            Document doc = entry.getDocument();
            String usrName = doc.getItemValueString(clName);
            list.add(usrName);
        }

        return list;
    }

    /**
     * 关闭连接
     * 
     * @Date in Jul 15, 2010,3:51:07 PM
     */
    public void close() {
        if (this.session != null) {
            try {
                this.session.recycle();
            } catch (NotesException ex) {
            }
        }
    }

}
