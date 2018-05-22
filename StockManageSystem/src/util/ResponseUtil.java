package util;

import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

public class ResponseUtil {
	
	public static void write(HttpServletResponse response,Object o) throws Exception{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(o.toString());
		out.flush();
		out.close();
	}
	
	public static void export(HttpServletResponse response,Workbook wb,String fileName)throws Exception{
		response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("utf-8"),"iso8859-1"));
		//����Ķ�������ֱ����������򿪵�ʱ�����Ҫ�������ͷ��
		response.setContentType("application/ynd.ms-excel;charset=UTF-8");
		//ָ��mime���͵���ΪExcel�ļ���ʽ
		OutputStream out=response.getOutputStream();
		//��ͻ�������ֽ�������Ҫ��������صĶ���һ����output�ķ�ʽ
		wb.write(out);
		out.flush();
		out.close();
	}
}
