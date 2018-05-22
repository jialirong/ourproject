package util;

import java.io.InputStream;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtil {
	
	public static void fillExcelData(ResultSet rs,Workbook wb,String[] headers)throws Exception{
		int rowIndex=0;
		Sheet sheet=wb.createSheet();//创建一个Excel的Sheet
		Row row=sheet.createRow(rowIndex++);//创建行
		for(int i=0;i<headers.length;i++){
			row.createCell(i).setCellValue(headers[i]);//创建单元格并赋值  第一行的值
		}
		while(rs.next()){
			row=sheet.createRow(rowIndex++);
			for(int i=0;i<headers.length;i++){
				if(headers.length>=6){
				if(headers[6].equals("损坏数")&&rs.getObject(i+1)==null){//日结账的损坏数为0时这样读取出来
					row.createCell(i).setCellValue(0);

				}}
				if(rs.getObject(i+1)!=null){
				row.createCell(i).setCellValue(rs.getObject(i+1).toString());}
			}
		}
	}
	
	
	
	
}
