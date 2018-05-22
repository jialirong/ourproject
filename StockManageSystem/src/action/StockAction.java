package action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.Goods;
import model.PageBean;
import model.Stock;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

import dao.GoodsDao;
import dao.StockDao;

public class StockAction extends ActionSupport{

	private String page;
	private String rows;
	private Stock stock;
	private Goods goods;
	private String s_bimpoPrice;
	private String s_eimpoPrice;
	private String s_bexpoPrice;
	private String s_eexpoPrice;
	private String delIds;
	private String sid;
	private String s_goodsName;

	
	
	public String getS_goodsName() {
		return s_goodsName;
	}
	public void setS_goodsName(String s_goodsName) {
		this.s_goodsName = s_goodsName;
	}
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getDelIds() {
		return delIds;
	}
	public void setDelIds(String delIds) {
		this.delIds = delIds;
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
	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
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


	DbUtil dbUtil = new DbUtil();
	StockDao stockDao = new StockDao();
	GoodsDao goodsDao = new GoodsDao();
	
	@Override
	public String execute() throws Exception{
		Connection con = null;
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		try {
			goods = new Goods();
			if(stock==null){//防止传递参数去dao的时候有空指针异常
				stock = new Stock();
			}
			if(s_goodsName!=null){
				goods.setGoodsName(s_goodsName);
			}
			con = dbUtil.getCon();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(stockDao.stockList(con, pageBean,goods,s_bimpoPrice,s_eimpoPrice,s_bexpoPrice,s_eexpoPrice));
			int total = stockDao.stockCount(con,goods,s_bimpoPrice,s_eimpoPrice,s_bexpoPrice,s_eexpoPrice);
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
			int delNums=stockDao.stockDelete(con, delIds);
			if(delNums>0){
				result.put("success", "true");
				result.put("delNums",delNums);
				LogUtil.log( "删除库存信息成功");

			}else{
				
				LogUtil.log( "删除库存信息失败");

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
		if(StringUtil.isNotEmpty(sid)){
			stock.setId(Integer.parseInt(sid));
		}
		Connection con=null;
		try{
			con=dbUtil.getCon();
			int saveNums=0;
			JSONObject result=new JSONObject();
			if(StringUtil.isNotEmpty(sid)){
				saveNums=stockDao.stockModify(con, stock);
				if(saveNums>0){	
					LogUtil.log( "修改库存信息成功");

				}else{
					LogUtil.log( "修改库存信息失败");
				}
			}else{
				saveNums=stockDao.stockSave(con, stock);
				if(saveNums>0){
					LogUtil.log( "增加库存信息成功");
				}else{	
					LogUtil.log( "增加库存信息失败");
				}
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
	
	
	public String export() throws Exception{
		Connection con = null;

		try {
			con=dbUtil.getCon();
			Workbook wb=new HSSFWorkbook();
			String headers[]={"编号","仓库名称","描述","仓库外部号"};
			ResultSet rs=stockDao.stockList(con, null, null, null, null, null, null);
			ExcelUtil.fillExcelData(rs, wb, headers);
			ResponseUtil.export(ServletActionContext.getResponse(), wb, "excel.xls");
			LogUtil.log("导出仓库信息成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.log("导出仓库信息失败");
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
