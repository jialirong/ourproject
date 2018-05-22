package action;

import java.sql.Connection;
import java.sql.ResultSet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import dao.GoodsTypeDao;
import dao.ProviderDao;
import model.GoodsType;
import model.PageBean;
import model.Provider;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.DbUtil;
import util.ExcelUtil;
import util.JsonUtil;
import util.LogUtil;
import util.ResponseUtil;
import util.StringUtil;

public class ProviderAction extends ActionSupport{
	DbUtil dbUtil = new DbUtil();
	ProviderDao providerDao = new ProviderDao();


	private static final long serialVersionUID = 1L;

	private String page;
	private String rows;
	private String s_typeName;
	private GoodsType goodsType;
	private String delIds;
	private String pid;
	private Provider provider;

	public Provider getProvider() {
		return provider;
	}
	public void setProvider(Provider provider) {
		this.provider = provider;
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

	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}


	@Override
	public String execute() throws Exception {
		Connection con = null;
		PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		try {
			if(goodsType==null){
				goodsType = new GoodsType();
			}
			goodsType.setTypeName(s_typeName);
			con = dbUtil.getCon();
			JSONObject result = new JSONObject();
			JSONArray jsonArray = JsonUtil.formatRsToJsonArray(providerDao.providerList(con));
			int total = providerDao.providerCount(con, provider);
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
			int delNums = providerDao.providerDelete(con, delIds);
			if(delNums>0){
				result.put("success", "true");
				result.put("delNums",delNums);
				LogUtil.log("ɾ����Ӧ����Ϣ�ɹ�");

			}else{
				LogUtil.log("ɾ����Ӧ����Ϣʧ��");

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
		if(StringUtil.isNotEmpty(pid)){
			goodsType.setId(Integer.parseInt(pid));
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNums = 0;
			JSONObject result = new JSONObject();
			if(StringUtil.isNotEmpty(pid)){
				saveNums = providerDao.providerModify(con, provider);
				if(saveNums>0){
					LogUtil.log("�޸Ĺ�Ӧ����Ϣ�ɹ�");

				}else{
					LogUtil.log("�޸Ĺ�Ӧ����Ϣʧ��");

				}
			}else{
				saveNums = providerDao.providerSave(con, provider);		
				if(saveNums>0){
					LogUtil.log("���ӹ�Ӧ����Ϣ�ɹ�");

				}else{
					LogUtil.log("���ӹ�Ӧ����Ϣʧ��");

				}
			}
			if(saveNums>0){
				result.put("success", "true");
			}else{
				result.put("success", "true");
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
			String headers[]={"���","��Ӧ������","��Ӧ����ϵ��","��Ӧ����ϵ�绰","��Ӧ������"};
			ResultSet rs=providerDao.providerList(con);
			ExcelUtil.fillExcelData(rs, wb, headers);
			ResponseUtil.export(ServletActionContext.getResponse(), wb, "excel.xls");
			LogUtil.log("������Ӧ����Ϣ�ɹ�");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.log("������Ӧ����Ϣʧ��");

			e.printStackTrace();
		}
		return null;
	}
	
	public String providerComboList()throws Exception{
		Connection con=null;
		try{
			con=dbUtil.getCon();
			JSONArray jsonArray=new JSONArray();
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("proName", "��ѡ��...");
			jsonArray.add(jsonObject);
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(providerDao.providerList(con)));
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
