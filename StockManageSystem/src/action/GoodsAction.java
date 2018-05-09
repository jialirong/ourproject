package action;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;

import model.Goods;
import model.Import;
import model.PageBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;

import util.DbUtil;
import util.ExcelUtil;
import util.JsonUtil;
import util.ResponseUtil;
import util.StringUtil;

import com.opensymphony.xwork2.ActionSupport;

import dao.ExportDao;
import dao.GoodsDao;
import dao.ImportDao;
import dao.StockDao;

public class GoodsAction extends ActionSupport {
	
	private String page;
	private String rows;
	private Goods goods;
	private String s_goodsId;
	private String s_goodsName;
	private String s_proId;
	private String s_typeId;
	private String delIds;
	private String id;
	
	private File userUploadFile;
	
	public File getUserUploadFile() {
		return userUploadFile;
	}
	public void setUserUploadFile(File userUploadFile) {
		this.userUploadFile = userUploadFile;
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
	
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	
	

	public String getS_goodsId() {
		return s_goodsId;
	}
	public void setS_goodsId(String s_goodsId) {
		this.s_goodsId = s_goodsId;
	}
	public String getS_goodsName() {
		return s_goodsName;
	}
	public void setS_goodsName(String s_goodsName) {
		this.s_goodsName = s_goodsName;
	}
	public String getS_proId() {
		return s_proId;
	}
	public void setS_proId(String s_proId) {
		this.s_proId = s_proId;
	}
	public String getS_typeId() {
		return s_typeId;
	}
	public void setS_typeId(String s_typeId) {
		this.s_typeId = s_typeId;
	}
	


	public String getDelIds() {
		return delIds;
	}
	public void setDelIds(String delIds) {
		this.delIds = delIds;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	DbUtil dbUtil = new DbUtil();
	GoodsDao goodsDao = new GoodsDao();
	ImportDao importDao = new ImportDao();
	ExportDao exportDao = new ExportDao();
	StockDao stockDao = new StockDao();
	
	@Override
	public String execute() throws Exception {
		Connection con = null;
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		try {
			goods = new Goods();
			if(s_goodsId!=null){
				goods.setGoodsId(s_goodsId);
				goods.setGoodsName(s_goodsName);
				goods.setProId(s_proId);
				goods.setTypeId(s_typeId);
			}
			con = dbUtil.getCon();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(goodsDao.goodsList(con, pageBean,goods));
			int total = goodsDao.goodsCount(con,goods);
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
	
	public String delete()throws Exception{
		Connection con=null;
		try{
			con=dbUtil.getCon();
			JSONObject result=new JSONObject();
			
			String str[] = delIds.split(",");
			for(int i=0;i<str.length;i++){
				boolean f1 = importDao.getGoodsByImportId(con,str[i]);
				boolean f2 = exportDao.getGoodsByExportId(con,str[i]);
				boolean f3 = stockDao.getGoodsByStockId(con,str[i]);
				if(f1 || f2 || f3){
					result.put("errorIndex", i);
					result.put("errorMsg", "商品库存量大于0，不能删除");
					ResponseUtil.write(ServletActionContext.getResponse(), result);
					return null;
				}
			}
			
			int delNums=goodsDao.goodsDelete(con, delIds);
			if(delNums>0){
				result.put("success", "true");
				result.put("delNums", delNums);
			}else{
				result.put("errorMsg", "删除失败");
			}
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
	
	public String save()throws Exception{
		if(StringUtil.isNotEmpty(id)){
			goods.setId(Integer.parseInt(id));
		}
		Connection con=null;
		try{
			con=dbUtil.getCon();
			int saveNums=0;
			JSONObject result=new JSONObject();
			if(StringUtil.isNotEmpty(id)){
				saveNums=goodsDao.goodsModify(con, goods);
			}else{
				saveNums=goodsDao.goodsAdd(con, goods);
			}
			if(saveNums>0){
				result.put("success", "true");
			}else{
				result.put("success", "true");
				result.put("errorMsg", "保存失败");
			}
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
	
	public String goodsComboList()throws Exception{
		Connection con=null;
		try{
			goods = new Goods();
			con=dbUtil.getCon();
			JSONArray jsonArray=new JSONArray();
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("id", "");
			jsonObject.put("goodsName", "请选择...");
			jsonArray.add(jsonObject);
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(goodsDao.goodsList(con, null,goods)));
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
	
	public String export() throws Exception{
		Connection con = null;
		try {
			con = dbUtil.getCon();
			Workbook wb=ExcelUtil.fillExcelDataWithTemplate(goodsDao.exportData(con), "goodsTemp.xls");
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
				Goods goods = new Goods();
				
				goods.setGoodsId(ExcelUtil.formatCell(hssfRow.getCell(0)));
				goods.setGoodsName(ExcelUtil.formatCell(hssfRow.getCell(1)));
				goods.setProId(ExcelUtil.formatCell(hssfRow.getCell(2)));
				goods.setTypeId(ExcelUtil.formatCell(hssfRow.getCell(3)));
				goods.setGoodsDesc(ExcelUtil.formatCell(hssfRow.getCell(4)));
								
				Connection con=null;
				try{
					con=dbUtil.getCon();
					goodsDao.goodsAdd(con, goods);
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
