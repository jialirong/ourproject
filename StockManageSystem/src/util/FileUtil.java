package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
	
	
	public static void writeToLog(String mes){
    BufferedWriter bw = null;  
    System.out.println(mes);
    try {  
      File file = new File("d:\\log.txt");
      if (!file.exists()) {  
        file.createNewFile();  
      }  
      FileWriter fw = new FileWriter(file,true);  //trueʹ���ļ����ݿ�׷��
      bw = new BufferedWriter(fw);  
      String str = mes+System.getProperty("line.separator");  //���ݲ�ͬϵͳ�ӻ��з�
      bw.write(str);  
      bw.flush();
    } catch (IOException e) {  
      e.printStackTrace();  
    } finally {  
      try {  
        if (bw != null) {  
          bw.close();  
        }  
      } catch (IOException e) {  
        System.out.println("Error in closing the BufferedWritter" + e);  
      }  
    } }
}
