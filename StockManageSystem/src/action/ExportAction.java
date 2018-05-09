package action;

import java.sql.Connection;
import java.sql.ResultSet;

import model.Export;
import model.Goods;
import model.Import;
import model.PageBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import util.DbUtil;
import util.ExcelUtil;
import util.JsonUtil;
import util.ResponseUtil;
import util.StringUtil;
import dao.ExportDao;
import dao.ImportDao;

public class ExportAction extends ActionSupport{

	private String page;
	private String rows;
	private Export exportGoods;
	private Goods goods;
	private String s_goodsId;
	private String s_bexpoPrice;
	private String s_eexpoPrice;
	private String s_bexpoDate;
	private String s_eexpoDate;
	private String s_goodsName;
	private String delIds;
	private String id;
	
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
	public Export getExportGoods() {
		return exportGoods;
	}
	public void setExportGoods(Export exportGoods) {
		this.exportGoods = exportGoods;
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
	public String getS_bexpoPrice() {
		return s_bexpoPrice;
	}
	public void setS_bexpoPrice(String s_bexpoPrice) {
		this.s_bexpoPrice = s_bexpoPrice;
	}
	public String getS_eexpoPrice() {
		return s_eexpoPrice;
	}
	public void setS_eexpoPrice(String s_eexpoPrice) {
		this.s_eexpoPrice = s_eexpoPrice;
	}
	public String getS_bexpoDate() {
		return s_bexpoDate;
	}
	public void setS_bexpoDate(String s_bexpoDate) {
		this.s_bexpoDate = s_bexpoDate;
	}
	public String getS_eexpoDate() {
		return s_eexpoDate;
	}
	public void setS_eexpoDate(String s_eexpoDate) {
		this.s_eexpoDate = s_eexpoDate;
	}
	public String getS_goodsName() {
		return s_goodsName;
	}
	public void setS_goodsName(String s_goodsName) {
		this.s_goodsName = s_goodsName;
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
	ExportDao exportDao = new ExportDao();
	
	@Override
	public String execute() throws Exception {
		Connection con = null;
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		try {
			
			exportGoods = new Export();
			goods = new Goods();
			if(s_goodsId!=null){
				exportGoods.setGoodsId(s_goodsId);
			}
			if(s_goodsName!=null){
				goods.setGoodsName(s_goodsName);
			}
			
			
			con = dbUtil.getCon();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(exportDao.exportList(con, pageBean,goods,exportGoods,s_bexpoPrice,s_eexpoPrice,s_bexpoDate,s_eexpoDate));
			int total = exportDao.exportCount(con,goods,exportGoods,s_bexpoPrice,s_eexpoPrice,s_bexpoDate,s_eexpoDate);
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
			int delNums = exportDao.exportDelete(con,delIds);
			if(delNums>0){
				result.put("success", "true");
				result.put("delNums",delNums);
			}else{
				result.put("errorMsg", "É¾³ýÊ§°Ü");
			}
			ResponseUtil.write(ServletActionContext.getResponse(), result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String save() throws Exception{
		if(StringUtil.isNotEmpty(id)){
			exportGoods.setId(Integer.parseInt(id));
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNums = 0;
			JSONObject result = new JSONObject();
			if(StringUtil.isNotEmpty(id)){
				saveNums = exportDao.exportModify(con, exportGoods);
			}else{
				saveNums = exportDao.exportSave(con,exportGoods);				
			}
			if(saveNums>0){
				result.put("success", "true");
			}else{
				result.put("success", "true");
				result.put("errorMsg", "±£´æÊ§°Ü");
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
			Workbook wb=ExcelUtil.fillExcelDataWithTemplate(exportDao.exportData(con), "exportTemp.xls");
			ResponseUtil.export(ServletActionContext.getResponse(), wb, "µ¼³öexcel.xls");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
