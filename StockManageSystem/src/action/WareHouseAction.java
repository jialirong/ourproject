package action;

import java.sql.Connection;
import java.sql.ResultSet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import dao.WareHouseDao;
import model.GoodsType;
import model.PageBean;
import model.WareHouse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.DbUtil;
import util.ExcelUtil;
import util.JsonUtil;
import util.LogUtil;
import util.ResponseUtil;
import util.StringUtil;

public class WareHouseAction extends ActionSupport{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private String page;
	private String rows;
	private String s_typeName;
	private WareHouse ware;
	private String delIds;
	private String wid;


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

	public WareHouse getWare() {
		return ware;
	}
	public void setWare(WareHouse ware) {
		this.ware = ware;
	}

	
	public String getDelIds() {
		return delIds;
	}
	public void setDelIds(String delIds) {
		this.delIds = delIds;
	}
	



	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}




	DbUtil dbUtil = new DbUtil();
	WareHouseDao wareHouseDao = new WareHouseDao();
	@Override
	public String execute() throws Exception {
		Connection con = null;
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		try {
			if(ware==null){
				ware = new WareHouse();
			}
			ware.setName(s_typeName);
			con = dbUtil.getCon();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(wareHouseDao.wareHouseList(con, pageBean, ware));
			int total = wareHouseDao.wareHouseCount(con, ware);
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
			int delNums = wareHouseDao.wareHouseDelete(con,delIds);
			if(delNums>0){
				result.put("success", "true");
				result.put("delNums",delNums);
				LogUtil.log("删除仓库信息成功");

			}else{
				LogUtil.log("删除仓库信息失败");

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
		if(StringUtil.isNotEmpty(wid)){
		
			ware.setWid(Integer.parseInt(wid));
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNums = 0;
			JSONObject result = new JSONObject();
			
			
			if(StringUtil.isNotEmpty(wid)){
				saveNums = wareHouseDao.wareHouseModify(con, ware);
				if(saveNums>0){
					
					LogUtil.log( "修改仓库信息成功");

				}else{
					
					LogUtil.log( "修改仓库信息失败");

				}
			}else{
				saveNums = wareHouseDao.wareHouseSave(con,ware);
				if(saveNums>0){
					
					LogUtil.log( "增加库存信息成功");

				}else{
					
					LogUtil.log( "增加仓存信息失败");

				}
			}
			if(saveNums>0){
				result.put("success", "true");
			}else{
				result.put("success", "true");
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
			String headers[]={"仓库编号","仓库名称","仓库描述"};
			ResultSet rs=wareHouseDao.wareHouseList(con, null,null);
			ExcelUtil.fillExcelData(rs, wb, headers);
			ResponseUtil.export(ServletActionContext.getResponse(), wb, "excel.xls");
			
				
				LogUtil.log( "导出库存信息成功");

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.log( "导出库存信息失败");

			e.printStackTrace();
		}
		return null;
	}
	
	
	public String wareHouseComboList()throws Exception{
		Connection con=null;
		try{
			con=dbUtil.getCon();
			JSONArray jsonArray=new JSONArray();
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("wt", "请选择...");
			jsonArray.add(jsonObject);
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(wareHouseDao.wareHouseList(con, null,null)));
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
