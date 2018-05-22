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
		//传输的东西不能直接在浏览器打开的时候就需要设置这个头。
		response.setContentType("application/ynd.ms-excel;charset=UTF-8");
		//指定mime类型导出为Excel文件格式
		OutputStream out=response.getOutputStream();
		//向客户端输出字节流，需要浏览器下载的东西一般用output的方式
		wb.write(out);
		out.flush();
		out.close();
	}
}
