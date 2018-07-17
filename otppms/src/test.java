import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.MessageFormat;


public class test {
	public static void main(String[] args) {
        try {
            // 创建将要导入数据库的数据File
            String dataFile = "C:/sqldata.csv";
            File file = new File(dataFile);
            if(file.exists()){
                file.delete();
            }else{
                file.createNewFile();
            }
            OutputStream output = new FileOutputStream(file);
            String dataStr = "{0},{0},1,1405428375\n";
            for (int i = 1; i < 1000000; i++) {
                String tokenNum = String.format("%014d", i);
                // 第一种写入文件方式
               output.write(MessageFormat.format(dataStr, tokenNum).getBytes());
                // 第二种写入文件方式
               //IOUtils.write(MessageFormat.format(dataStr, tokenNum), output, "utf-8");
            }
            if(output!=null){
                output.flush();
                output.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
