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
		Sheet sheet=wb.createSheet();//����һ��Excel��Sheet
		Row row=sheet.createRow(rowIndex++);//������
		for(int i=0;i<headers.length;i++){
			row.createCell(i).setCellValue(headers[i]);//������Ԫ�񲢸�ֵ  ��һ�е�ֵ
		}
		while(rs.next()){
			row=sheet.createRow(rowIndex++);
			for(int i=0;i<headers.length;i++){
				if(headers.length>=6){
				if(headers[6].equals("����")&&rs.getObject(i+1)==null){//�ս��˵�����Ϊ0ʱ������ȡ����
					row.createCell(i).setCellValue(0);

				}}
				if(rs.getObject(i+1)!=null){
				row.createCell(i).setCellValue(rs.getObject(i+1).toString());}
			}
		}
	}
	
	
	
	
}
