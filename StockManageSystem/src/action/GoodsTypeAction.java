package action;

import java.sql.Connection;
import java.sql.ResultSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;

import util.DbUtil;
import util.ExcelUtil;
import util.JsonUtil;
import util.LogUtil;
import util.ResponseUtil;
import util.StringUtil;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

import dao.GoodsTypeDao;
import model.GoodsType;
import model.PageBean;


public class GoodsTypeAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String page;
	private String rows;
	private String s_typeName;
	private String s_wid;

	public String getS_wid() {
		return s_wid;
	}
	public void setS_wid(String s_wid) {
		this.s_wid = s_wid;
	}


	private GoodsType goodsType;
	private String delIds;
	private String gtid;
	
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
	public GoodsType getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(GoodsType goodsType) {
		this.goodsType = goodsType;
	}
	public String getDelIds() {
		return delIds;
	}
	public void setDelIds(String delIds) {
		this.delIds = delIds;
	}

	
	public String getGtid() {
		return gtid;
	}
	public void setGtid(String gtid) {
		this.gtid = gtid;
	}


	DbUtil dbUtil = new DbUtil();
	GoodsTypeDao goodsTypeDao = new GoodsTypeDao();
	
	@Override
	public String execute() throws Exception {
		System.out.println("excute.....................");
		Connection con = null;
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		try {
			if(goodsType==null){
			goodsType = new GoodsType();
			}
			if(s_typeName!=null){
			goodsType.setTypeName(s_typeName);
			
			}
			if(s_wid!=null&&s_wid!=""){
				goodsType.setWid(Integer.parseInt(s_wid));
				System.out.println(s_wid);
			}
			con = dbUtil.getCon();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(goodsTypeDao.goodsTypeList(con, pageBean, goodsType));
			int total = goodsTypeDao.goodsTypeCount(con, goodsType);
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
			int delNums = goodsTypeDao.goodsTypeDelete(con,delIds);
			if(delNums>0){
				result.put("success", "true");
				result.put("delNums",delNums);
				LogUtil.log("删除商品类型成功");
			}else{
				LogUtil.log("删除商品类型失败");

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
		if(StringUtil.isNotEmpty(gtid)){
			goodsType.setId(Integer.parseInt(gtid));
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNums = 0;
			JSONObject result = new JSONObject();
			if(StringUtil.isNotEmpty(gtid)){
				saveNums = goodsTypeDao.goodsTypeModify(con, goodsType);
				if(saveNums>0){
					LogUtil.log("修改商品类型成功");

				}else{
					LogUtil.log("修改商品类型失败");

				}
			}else{
				saveNums = goodsTypeDao.goodsTypeSave(con,goodsType);
				if(saveNums>0){
					LogUtil.log("增加商品类型成功");

				}else{
					LogUtil.log("增加商品类型失败");

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
			String headers[]={"编号","仓库编号","商品类别名称","商品类别描述"};
			ResultSet rs=goodsTypeDao.goodsTypeList(con, null,null);
			ExcelUtil.fillExcelData(rs, wb, headers);
			ResponseUtil.export(ServletActionContext.getResponse(), wb, "excel.xls");
			LogUtil.log("导出商品类型表成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.log("导出商品类型表失败");

			e.printStackTrace();
		}
		return null;
	}
	
	public String goodsTypeComboList()throws Exception{
		Connection con=null;
		try{
			con=dbUtil.getCon();
			JSONArray jsonArray=new JSONArray();
			JSONObject jsonObject=new JSONObject();
			
			jsonObject.put("typeName", "请选择...");
			jsonArray.add(jsonObject);
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(goodsTypeDao.goodsTypeList(con, null,null)));
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
