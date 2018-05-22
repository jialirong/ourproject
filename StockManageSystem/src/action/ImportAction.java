package action;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.Goods;
import model.Import;
import model.PageBean;
import model.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;

import util.DateUtil;
import util.DbUtil;
import util.ExcelUtil;
import util.FileUtil;
import util.JsonUtil;
import util.LogUtil;
import util.ResponseUtil;
import util.StringUtil;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

import dao.CouDao;
import dao.GoodsTypeDao;
import dao.ImportDao;
import dao.ProviderDao;
import dao.UserDao;

public class ImportAction extends ActionSupport{

	private String page;
	private String rows;
	private String s_goodsId;
	private String s_bimpoPrice;
	private String s_eimpoPrice;
	private String s_bimpoDate;
	private String s_eimpoDate;
	private Import importGoods;
	private String s_goodsName;
	private String s_wid;
	private String beforeNum;
	private String whid;



	private int s_couId;
	private int s_userId;
	
	private Goods goods;
	private String delIds;
	private String iid;
	UserDao userDao = new UserDao();
	public String getWhid() {
		return whid;
	}
	public void setWhid(String whid) {
		this.whid = whid;
	}
	public String getBeforeNum() {
		return beforeNum;
	}
	public void setBeforeNum(String beforeNum) {
		this.beforeNum = beforeNum;
	}
	public String getS_wid() {
		return s_wid;
	}
	public void setS_wid(String s_wid) {
		this.s_wid = s_wid;
	}



	
	
	
	
	public int getS_couId() {
		return s_couId;
	}
	public void setS_couId(int s_couId) {
		this.s_couId = s_couId;
	}
	public int getS_userId() {
		return s_userId;
	}
	public void setS_userId(int s_userId) {
		this.s_userId = s_userId;
	}


	
	private File userUploadFile;
	
	
	public File getUserUploadFile() {
		return userUploadFile;
	}
	public void setUserUploadFile(File userUploadFile) {
		this.userUploadFile = userUploadFile;
	}
	
	
	public String getIid() {
		return iid;
	}
	public void setIid(String iid) {
		this.iid = iid;
	}
	public String getDelIds() {
		return delIds;
	}
	public void setDelIds(String delIds) {
		this.delIds = delIds;
	}
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public String getS_goodsName() {
		return s_goodsName;
	}
	public void setS_goodsName(String s_goodsName) {
		this.s_goodsName = s_goodsName;
	}
	public Import getImportGoods() {
		return importGoods;
	}
	public void setImportGoods(Import importGoods) {
		this.importGoods = importGoods;
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
	
	public String getS_goodsId() {
		return s_goodsId;
	}
	public void setS_goodsId(String s_goodsId) {
		this.s_goodsId = s_goodsId;
	}
	public String getS_bimpoPrice() {
		return s_bimpoPrice;
	}
	public void setS_bimpoPrice(String s_bimpoPrice) {
		this.s_bimpoPrice = s_bimpoPrice;
	}
	public String getS_eimpoPrice() {
		return s_eimpoPrice;
	}
	public void setS_eimpoPrice(String s_eimpoPrice) {
		this.s_eimpoPrice = s_eimpoPrice;
	}
	public String getS_bimpoDate() {
		return s_bimpoDate;
	}
	public void setS_bimpoDate(String s_bimpoDate) {
		this.s_bimpoDate = s_bimpoDate;
	}
	public String getS_eimpoDate() {
		return s_eimpoDate;
	}
	public void setS_eimpoDate(String s_eimpoDate) {
		this.s_eimpoDate = s_eimpoDate;
	}


	DbUtil dbUtil = new DbUtil();
	ImportDao importDao = new ImportDao();
	
	@Override
	public String execute() throws Exception {
		Connection con = null;
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		try {
			
			importGoods = new Import();
			goods = new Goods();
		
			
			con = dbUtil.getCon();
			JSONObject result = new JSONObject();
			//把查询出的数据以json的格式存好
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(importDao.importList(con, pageBean,goods,importGoods,s_bimpoPrice,s_eimpoPrice,s_bimpoDate,s_eimpoDate,s_wid));
			int total = importDao.importCount(con,goods,importGoods,s_bimpoPrice,s_eimpoPrice,s_bimpoDate,s_eimpoDate);
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
			String str[] = delIds.split(",");//前台调用该函数的时候传递过来的
			int delNums = importDao.importDelete(con,delIds);
			if(delNums>0){
				result.put("success", "true");
				result.put("delNums",delNums);
				LogUtil.log("删除入库信息成功");
				
			}else{
			
				result.put("errorMsg", "删除失败");

				LogUtil.log("删除入库信息失败");
			}
			ResponseUtil.write(ServletActionContext.getResponse(), result);//将信息返回给页面
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	CouDao couDao = new CouDao();
	
	
	public String save() throws Exception{
		if(StringUtil.isNotEmpty(iid)){
			System.out.println(iid);
			importGoods.setIid(Integer.parseInt(iid));
		}
		if(StringUtil.isNotEmpty(beforeNum)){
			importGoods.setBeforeNum(Integer.parseInt(beforeNum));
		}
		if(StringUtil.isNotEmpty(whid)){
			importGoods.setWhid(Integer.parseInt(whid));
		}
		Connection con = null;
		try {
			
			con = dbUtil.getCon();
			int saveNums = 0;
			JSONObject result = new JSONObject();
			if(StringUtil.isNotEmpty(iid)){//若前台传入了这个参数代表前台想要的是修改入库信息
				saveNums = importDao.importModify(con, importGoods);
				if(saveNums>0){
				
					LogUtil.log("修改入库信息成功");
				}else{
					
				LogUtil.log("修改入库信息失败");
		

				}
			}else{
				saveNums = importDao.importSave(con,importGoods);	
				if(saveNums>0){
				
					LogUtil.log("增加入库信息成功");
				}else{
			
					LogUtil.log("增加入库信息失败");
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
			Workbook wb=new HSSFWorkbook();
			String headers[]={"入库编号","商品编号","入库价格","入库日期","入库数量","入库描述","业务员编号","客户编号"};
			ResultSet rs=importDao.importList(con, null, null, null, null, null, null, null, null);
			ExcelUtil.fillExcelData(rs, wb, headers);
			ResponseUtil.export(ServletActionContext.getResponse(), wb, "excel.xls");
			LogUtil.log("导出入库表成功");
		
		} catch (Exception e) {
			// TODO Auto-generated catch block

			LogUtil.log("导出入库表失败");

			e.printStackTrace();
		}
		return null;
	}
	

	
	public String importCouComboList()throws Exception{
		Connection con=null;
	
		try{
			goods = new Goods();
			con=dbUtil.getCon();
			JSONArray jsonArray=new JSONArray();
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("ct", "");
			
			jsonArray.add(jsonObject);
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(couDao.coutList(con)));
			System.out.println(jsonArray.toString());
			ResponseUtil.write(ServletActionContext.getResponse(), jsonArray);
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
		
	public String importServiceComboList()throws Exception{
		Connection con=null;
	
		try{
			goods = new Goods();
			con=dbUtil.getCon();
			JSONArray jsonArray=new JSONArray();
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("ut", "请选择...");
			
			jsonArray.add(jsonObject);
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(userDao.usertList(con)));
			System.out.println(jsonArray.toString());
			ResponseUtil.write(ServletActionContext.getResponse(), jsonArray);
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
}
