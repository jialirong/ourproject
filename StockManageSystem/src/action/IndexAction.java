package action;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.Goods;

import org.apache.struts2.interceptor.ServletRequestAware;

import util.DbUtil;

import com.opensymphony.xwork2.ActionSupport;

import dao.GoodsDao;

public class IndexAction extends ActionSupport implements ServletRequestAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DbUtil dbUtil = new DbUtil();
	GoodsDao goodsDao = new GoodsDao();
	private HttpServletRequest request;
	private ArrayList<Goods> goodsList;
	
	

	public ArrayList<Goods> getGoodsList() {
		return goodsList;
	}


	public void setGoodsList(ArrayList<Goods> goodsList) {
		this.goodsList = goodsList;
	}


	@Override
	public String execute() throws Exception {
		HttpSession session = request.getSession();
		Connection con=null;
		con=dbUtil.getCon();
		// 商品信息
		String sql="select * from t_goods";
		goodsList = goodsDao.goodsArrayList(con,sql);
		session.setAttribute("goodsList", goodsList);	
		return "index";
	}

	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

}
