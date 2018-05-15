package action;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;

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
import util.JsonUtil;
import util.ResponseUtil;
import util.StringUtil;

import com.opensymphony.xwork2.ActionSupport;

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



	private int s_couId;
	private int s_userId;
	
	private Goods goods;
	private String delIds;
	private String iid;
	UserDao userDao = new UserDao();
	
	
	
	
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
			if(s_goodsId!=null){
				importGoods.setGoodsId(s_goodsId);
			}
			if(s_goodsName!=null){
				goods.setGoodsName(s_goodsName);
			}
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@"+s_bimpoDate);
			
			con = dbUtil.getCon();
			JSONObject result = new JSONObject();
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
			String str[] = delIds.split(",");
			System.out.println(str.length);
			int delNums = importDao.importDelete(con,delIds);
			System.out.println(delNums);
			if(delNums>0){
				result.put("success", "true");
				result.put("delNums",delNums);
			}else{
				

				result.put("errorMsg", "删除失败");
			}
			ResponseUtil.write(ServletActionContext.getResponse(), result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	CouDao couDao = new CouDao();
	public String importCouComboList()throws Exception{
		Connection con=null;
	
		try{
			goods = new Goods();
			con=dbUtil.getCon();
			JSONArray jsonArray=new JSONArray();
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("cid", "");
			jsonObject.put("couId", "请选择...");
			
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
//			jsonObject.put("serviceId", "");
			jsonObject.put("uid", "请选择...");
			
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
	
	public String save() throws Exception{
		if(StringUtil.isNotEmpty(iid)){
			System.out.println(iid);
			importGoods.setIid(Integer.parseInt(iid));
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
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
			if(StringUtil.isNotEmpty(iid)){
				System.out.println(importGoods);
				saveNums = importDao.importModify(con, importGoods);
			}else{
				saveNums = importDao.importSave(con,importGoods);				
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
			con = dbUtil.getCon();
			Workbook wb=ExcelUtil.fillExcelDataWithTemplate(importDao.exportData(con), "importTemp.xls");
			ResponseUtil.export(ServletActionContext.getResponse(), wb, "导出excel.xls");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String upload()throws Exception{
		POIFSFileSystem fs=new POIFSFileSystem(new FileInputStream(userUploadFile));
		HSSFWorkbook wb=new HSSFWorkbook(fs);
		HSSFSheet hssfSheet=wb.getSheetAt(0);  // 获取第一个Sheet页
		if(hssfSheet!=null){
			for(int rowNum=1;rowNum<=hssfSheet.getLastRowNum();rowNum++){
				HSSFRow hssfRow=hssfSheet.getRow(rowNum);
				if(hssfRow==null){
					continue;
				}
				Import importGoods = new Import();
				
				importGoods.setGoodsId(ExcelUtil.formatCell(hssfRow.getCell(0)));
				importGoods.setImpoPrice(ExcelUtil.formatCell(hssfRow.getCell(1)));
				
				/*HSSFCell hssfCell=hssfRow.getCell(2);
				String dateFormat = ExcelUtil.getCell(hssfCell);
				System.out.println(dateFormat.toString());*/
				
				importGoods.setImpoDate(DateUtil.formatString(ExcelUtil.getCell(hssfRow.getCell(2)), "yyyy-MM-dd"));
				importGoods.setImpoNum(ExcelUtil.formatCell(hssfRow.getCell(3)));
				importGoods.setImpoDesc(ExcelUtil.formatCell(hssfRow.getCell(4)));
				
				
				Connection con=null;
				try{
					con=dbUtil.getCon();
					importDao.importSave(con, importGoods);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					dbUtil.closeCon(con);
				}
			}
		}
		JSONObject result=new JSONObject();
		result.put("success", "true");
		ResponseUtil.write(ServletActionContext.getResponse(), result);
		return null;
	}
}
