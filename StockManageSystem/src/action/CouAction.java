package action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

import dao.CouDao;
import model.Cou;
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

public class CouAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String page;
	private String rows;
	private String s_typeName;
	private String Name;



	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}


	private Cou cou;
	private String delIds;
	private String cid;
	
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
	public String getS_typeName() {
		return s_typeName;
	}
	public void setS_typeName(String s_typeName) {
		this.s_typeName = s_typeName;
	}
	
	
	public Cou getCou() {
		return cou;
	}
	public void setCou(Cou cou) {
		this.cou = cou;
	}
	public String getDelIds() {
		return delIds;
	}
	public void setDelIds(String delIds) {
		this.delIds = delIds;
	}

	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	
	
	DbUtil dbUtil = new DbUtil();
	CouDao couDao = new CouDao();
	
	@Override
	public String execute() throws Exception {
		Connection con = null;
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		try {
			if(cou==null){
				cou = new Cou();
			}
			cou.setName(s_typeName);
			con = dbUtil.getCon();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(couDao.couList(con, null, cou));
			int total = couDao.couCount(con, cou);
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
		Connection con = null;

		try {
			con = dbUtil.getCon();
			JSONObject result = new JSONObject();
			String str[] = delIds.split(",");
			int delNums = couDao.couDelete(con,delIds);
			if(delNums>0){
				result.put("success", "true");
				result.put("delNums",delNums);
				
				
				LogUtil.log("ɾ���ͻ���Ϣ�ɹ�");

			}else{
				
				LogUtil.log("ɾ���ͻ���Ϣʧ��");

				result.put("errorMsg", "ɾ��ʧ��");
			}
			ResponseUtil.write(ServletActionContext.getResponse(), result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String save() throws Exception{

		if(StringUtil.isNotEmpty(cid)){
			cou.setCid(Integer.parseInt(cid));
		}
		Connection con = null;

		try {
			con = dbUtil.getCon();
			int saveNums = 0;
			JSONObject result = new JSONObject();
			if(StringUtil.isNotEmpty(cid)){
				System.out.println(cou);
				saveNums = couDao.couModify(con, cou);	
				if(saveNums>0){
					
					LogUtil.log("�޸Ŀͻ���Ϣ�ɹ�");
				}else{
			
					LogUtil.log("�޸Ŀͻ���Ϣʧ��");
				}
				

			}else{
				
				saveNums = couDao.couSave(con,cou);	
				if(saveNums>0){
				
					LogUtil.log("���ӿͻ���Ϣ�ɹ�");
				}else{
			
					LogUtil.log("���ӿͻ���Ϣʧ��");
				}
			}
			if(saveNums>0){
				
				result.put("success", "true");
			}else{
				
				result.put("errorMsg", "����ʧ��");
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
			Workbook wb=new HSSFWorkbook();
			String headers[]={"���","�ͻ�����"};
			ResultSet rs=couDao.couList(con, null,null);
			ExcelUtil.fillExcelData(rs, wb, headers);
			ResponseUtil.export(ServletActionContext.getResponse(), wb, "excel.xls");
				LogUtil.log("�����ͻ���Ϣ�ɹ�");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.log("�����ͻ���Ϣʧ��");
			e.printStackTrace();
		}
		return null;
	}
}
