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
import util.LogUtil;
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
	private String s_wid;
	private String s_goodsName;
	private String s_proId;
	private String s_typeId;
	private String delIds;
	private String gid;

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
	
	public String getS_wid() {
		return s_wid;
	}
	public void setS_wid(String s_wid) {
		this.s_wid = s_wid;
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
	
	
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
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
			if(s_wid!=null){
				goods.setWid(s_wid);
			}
			if(s_goodsName!=null){
				goods.setGoodsName(s_goodsName);
				
			}
			if(s_proId!=null){
				goods.setProId(s_proId);
				
			}
			if(s_typeId!=null){
				goods.setTypeId(s_typeId);
			}
			con = dbUtil.getCon();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(goodsDao.goodsList(con, pageBean,goods));
			int total = goodsDao.goodsCount(con,goods);
			result.put("rows", jsonArray);//必须以这个格式封装好传回去datagrid才会识别
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
				
				boolean f3 = stockDao.getGoodsByStockId(con,str[i]);
				if(f3){
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
		if(StringUtil.isNotEmpty(gid)){
			goods.setGid(Integer.parseInt(gid));
		}
		Connection con=null;
		try{
			con=dbUtil.getCon();
			int saveNums=0;
			JSONObject result=new JSONObject();
			if(StringUtil.isNotEmpty(gid)){
				saveNums=goodsDao.goodsModify(con, goods);
			}else{
				saveNums=goodsDao.goodsAdd(con, goods);
			}
			if(saveNums>0){
				result.put("success", "true");
			}else{
			
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
	

	
	public String export() throws Exception{
		Connection con = null;
		try {
			con=dbUtil.getCon();
			Workbook wb=new HSSFWorkbook();//创建一个Excel文件 
			String headers[]={"编号","外部编号","商品名","供应商编号","商品类型","商品描述","出库日期"};
			ResultSet rs=goodsDao.goodsList(con, null, null);
			ExcelUtil.fillExcelData(rs, wb, headers);
			ResponseUtil.export(ServletActionContext.getResponse(), wb, "excel.xls");
			LogUtil.log("导出商品信息成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.log("导出商品信息失败");

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
			jsonObject.put("ggid", "请选择...");
			jsonArray.add(jsonObject);
			//把所有的结果以json的格式返回
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(goodsDao.goodsList(con, null,goods)));
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
