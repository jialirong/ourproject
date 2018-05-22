package action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

import util.DateUtil;
import util.DbUtil;
import util.ExcelUtil;
import util.FileUtil;
import util.JsonUtil;
import util.LogUtil;
import util.ResponseUtil;
import util.StringUtil;
import dao.ExportDao;
import dao.ImportDao;

public class ExportAction extends ActionSupport{

	private String page;
	private String rows;
	private Export exportGoods;
	private Goods goods;
	private String s_wid;
	private String whid;
	public String getWhid() {
		return whid;
	}
	public void setWhid(String whid) {
		this.whid = whid;
	}
	public String getS_wid() {
		return s_wid;
	}
	public void setS_wid(String s_wid) {
		this.s_wid = s_wid;
	}


	private String s_bexpoPrice;
	private String s_eexpoPrice;
	private String s_bexpoDate;
	private String s_eexpoDate;
	private String delIds;
	private String eid;
	private String brforeNum;

	public String getBrforeNum() {
		return brforeNum;
	}
	public void setBrforeNum(String brforeNum) {
		this.brforeNum = brforeNum;
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

	public String getDelIds() {
		return delIds;
	}
	public void setDelIds(String delIds) {
		this.delIds = delIds;
	}


	public String getEid() {
		return eid;
	}
	public void setEid(String eid) {
		this.eid = eid;
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
			con = dbUtil.getCon();
			JSONObject result = new JSONObject();
			//get rs
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(exportDao.exportList(con, pageBean,goods,exportGoods,s_bexpoPrice,s_eexpoPrice,s_bexpoDate,s_eexpoDate,s_wid));
			int total = exportDao.exportCount(con,goods,exportGoods,s_bexpoPrice,s_eexpoPrice,s_bexpoDate,s_eexpoDate);
			result.put("rows", jsonArray);//
			result.put("total", total);//
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
				LogUtil.log("删除出库信息成功");
			}else{
				LogUtil.log("删除出库信息失败");
				result.put("errorMsg", "删除失败");
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

	public String save() throws Exception{

		if(StringUtil.isNotEmpty(eid)){

			exportGoods.setEid(Integer.parseInt(eid));
		}
		if(StringUtil.isNotEmpty(brforeNum)){
			exportGoods.setBrforeNum(Integer.parseInt(brforeNum));
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNums = 0;
			JSONObject result = new JSONObject();
			if(StringUtil.isNotEmpty(eid)){
				saveNums = exportDao.exportModify(con, exportGoods);
				
				if(saveNums>0){
					LogUtil.log("修改出库信息成功");
				}else{
					LogUtil.log("修改出库信息失败");
				}
			}else{
				saveNums = exportDao.exportSave(con,exportGoods);	
				if(saveNums>0){
					LogUtil.log("增加出库信息成功");

				}else{
					LogUtil.log("增加出库信息失败");

				}
			}
			if(saveNums>0){
				result.put("success", "true");
			}
			else {
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
			ResultSet rs=exportDao.exportList(con,null , null, null, null, null, null, null, null);
			ExcelUtil.fillExcelData(rs, wb, headers);
			ResponseUtil.export(ServletActionContext.getResponse(), wb, "excel.xls");
			LogUtil.log("导出出库信息成功");

		} catch (Exception e) {
			// TODO Auto-generated catch block

			LogUtil.log("修改出库信息失败");

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
