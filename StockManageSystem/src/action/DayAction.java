package action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.sun.org.apache.regexp.internal.recompile;

import dao.DayDao;
import model.Day;

import model.PageBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.DateUtil;
import util.DbUtil;
import util.ExcelUtil;
import util.FileUtil;
import util.JsonUtil;
import util.LogUtil;
import util.ResponseUtil;
import util.StringUtil;

public class DayAction extends ActionSupport {
//	private static final long serialVersionUID = 1L;
	private String page;
	private String rows;
	private String s_bexpoDatee;
	private String s_eexpoDatee;
	private Day day;
	private String delIds;
	private String dayid;
	private String s_widd;
	HttpServletRequest request;

	private Logger logger = LoggerFactory.getLogger(LoginAction.class);


	public String getS_bexpoDatee() {
		return s_bexpoDatee;
	}
	public void setS_bexpoDatee(String s_bexpoDatee) {
		this.s_bexpoDatee = s_bexpoDatee;
	}
	public String getS_eexpoDatee() {
		return s_eexpoDatee;
	}
	public void setS_eexpoDatee(String s_eexpoDatee) {
		this.s_eexpoDatee = s_eexpoDatee;
	}
	public String getS_widd() {
		return s_widd;
	}
	public void setS_widd(String s_widd) {
		this.s_widd = s_widd;
	}

	DbUtil dbUtil = new DbUtil();
	DayDao dayDao = new DayDao();
	
	public Day getDay() {
		return day;
	}
	public void setDay(Day day) {
		this.day = day;
	}
	public String getDayid() {
		return dayid;
	}
	public void setDayid(String dayid) {
		this.dayid = dayid;
	}


	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getDelIds() {
		return delIds;
	}
	public void setDelIds(String delIds) {
		this.delIds = delIds;
	}

	
	@Override
	public String execute() throws Exception {
		System.out.println("page"+page);
		Connection con = null;
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		try {
			
				day = new Day();
				con = dbUtil.getCon();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(dayDao.dayList(con, pageBean, day,s_bexpoDatee,s_eexpoDatee,s_widd));
			System.out.println("JSONArray"+jsonArray);
			int total = dayDao.dayCount(con, day,s_bexpoDatee,s_eexpoDatee);
			result.put("rows", jsonArray);
			result.put("total", total);
			ResponseUtil.write(ServletActionContext.getResponse(), result);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String delete() throws Exception{
		HttpSession session = request.getSession();

		Connection con = null;
		try {
			con = dbUtil.getCon();
			JSONObject result = new JSONObject();
			String str[] = delIds.split(",");
			int delNums = dayDao.dayDelete(con,delIds);
			if(delNums>0){
				Date day=new Date();    
				String mes = "   用户于"+DateUtil.formatDate(day, "yyyy-MM-dd HH:mm:ss")+"删除日结账信息成功";
				FileUtil.writeToLog(mes);
				logger.info(mes);
				result.put("success", "true");
				result.put("delNums",delNums);
			}else{
				Date day=new Date();    
				String mes = "   用户于"+DateUtil.formatDate(day, "yyyy-MM-dd HH:mm:ss")+"删除日结账信息失败";
				FileUtil.writeToLog(mes);
				logger.info(mes);
				result.put("errorMsg", "删除失败");
			}
			ResponseUtil.write(ServletActionContext.getResponse(), result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String save() throws Exception{
		HttpSession session = request.getSession();

		if(StringUtil.isNotEmpty(dayid)){
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~``"+dayid);
			day.setDayid(Integer.parseInt(dayid));
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNums = 0;
			JSONObject result = new JSONObject();
			if(StringUtil.isNotEmpty(dayid)){
				System.out.println(day);
				saveNums = dayDao.dayModify(con, day);
				if(saveNums>0){
				
					String mes = "修改结账成功";
				
					LogUtil.log(mes);
			
				}
				else{
					
					
					String mes ="修改结账失败";
					LogUtil.log(mes);
				}
			}else{
				saveNums = dayDao.daySave(con,day);	
				if(saveNums>0){
					  
					String mes = "增加结账信息成功";
					LogUtil.log(mes);
				

				}else{
					String mes = "增加结账信息失败";
					LogUtil.log(mes);
				

				}
			}
			if(saveNums>0){
				
				result.put("success", "true");
			}else{
				
				result.put("errorMsg", "保存失败");
			}
			ResponseUtil.write(ServletActionContext.getResponse(), result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	
	
	public String export() throws Exception{
	
		Connection con = null;
		try {
			con=dbUtil.getCon();
			Workbook wb=new HSSFWorkbook();//创建一个Excel文件 
			String headers[]={"编号","日期","仓库号","商品编号","入库数","出库数","损坏数","剩余总数"};
			ResultSet rs=dayDao.dayList(con, null,null,null,null,null);
		
			ExcelUtil.fillExcelData(rs, wb, headers);
			ResponseUtil.export(ServletActionContext.getResponse(), wb, "excel.xls");
	
			String mes = "导出结账表成功";
			
			LogUtil.log(mes);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			String mes = "导出结账表失败";
			
			LogUtil.log(mes);


			e.printStackTrace();
		}
		return null;
	}
}
