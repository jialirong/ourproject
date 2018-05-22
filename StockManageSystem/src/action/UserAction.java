package action;

import java.sql.Connection;

import org.apache.struts2.ServletActionContext;

import dao.UserDao;
import model.Goods;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.DbUtil;
import util.JsonUtil;
import util.ResponseUtil;

public class UserAction {
	DbUtil dbUtil = new DbUtil();
	UserDao userDao = new UserDao();
	public String userComboList()throws Exception{
		Connection con=null;
		
		try{
			
			con=dbUtil.getCon();
			JSONArray jsonArray=new JSONArray();
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("iid", "");
			jsonObject.put("uid", "«Î—°‘Ò");
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
