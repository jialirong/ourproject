package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Goods;
import model.Import;
import model.PageBean;
import util.DateUtil;
import util.StringUtil;

public class ImportDao {

	public ResultSet importList(Connection con,PageBean pageBean,Goods goods,Import importGoods,String s_bimpoPrice,String s_eimpoPrice,String s_bimpoDate,String s_eimpoDate,String s_wid) throws Exception{
		StringBuffer sb = new StringBuffer("SELECT * FROM t_goods t2,t_import t1 ,"
				+ "t_coustmer t3,t_goodstype t4 WHERE t1.goodsId=t2.gid and t3.cid=t1.couId and t4.gtid=t2.typeId");
		if(StringUtil.isNotEmpty(goods.getGoodsName())){
			sb.append(" and t2.goodsName like '%"+goods.getGoodsName()+"%'");
		}
		
		if(StringUtil.isNotEmpty(s_bimpoPrice)){
			sb.append(" and impoPrice >="+s_bimpoPrice+"");
		}
		if(StringUtil.isNotEmpty(s_eimpoPrice)){
			sb.append(" and impoPrice <="+s_eimpoPrice+"");
		}
		
		if(StringUtil.isNotEmpty(s_bimpoDate)){
			sb.append(" and TO_DAYS(t1.impoDate)>=TO_DAYS('"+s_bimpoDate+"')");
		}
		if(StringUtil.isNotEmpty(s_eimpoDate)){
			sb.append(" and TO_DAYS(t1.impoDate)<=TO_DAYS('"+s_eimpoDate+"')");
		}
		if(StringUtil.isNotEmpty(s_wid)){
			int m = Integer.parseInt(s_wid);
			sb.append(" and t4.wid="+m);
		}
		sb.append(" order by t1.iid asc");
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getRows());
		}
		
		System.out.println(sb.toString()+"..........................");
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		return pstmt.executeQuery();
	}
	
	public ResultSet exportData(Connection con) throws Exception {
		String sql = "SELECT goodsName,impoPrice,impoDate,impoNum,impoDesc,name,couId FROM t_goods t2,t_import t1,t_coustmer t3 WHERE t1.goodsId=t2.gid and t3.cid=t1.couId";
		PreparedStatement pstmt = con.prepareStatement(sql);
		return pstmt.executeQuery();
	}
	
	public int importCount(Connection con,Goods goods,Import importGoods,String s_bimpoPrice,String s_eimpoPrice,String s_bimpoDate,String s_eimpoDate) throws Exception{
		StringBuffer sb = new StringBuffer("select count(*) as total from t_goods t2,t_import t1 where t1.goodsId=t2.gid");
		if(StringUtil.isNotEmpty(goods.getGoodsName())){
			sb.append(" and t2.goodsName like '%"+goods.getGoodsName()+"%'");
		}
		
		if(StringUtil.isNotEmpty(s_bimpoPrice)){
			sb.append(" and impoPrice >="+s_bimpoPrice+"");
		}
		if(StringUtil.isNotEmpty(s_eimpoPrice)){
			sb.append(" and impoPrice <="+s_eimpoPrice+"");
		}
		
		if(StringUtil.isNotEmpty(s_bimpoDate)){
			sb.append(" and TO_DAYS(t1.impoDate)>=TO_DAYS('"+s_bimpoDate+"')");
		}
		if(StringUtil.isNotEmpty(s_eimpoDate)){
			sb.append(" and TO_DAYS(t1.impoDate)<=TO_DAYS('"+s_eimpoDate+"')");
		}
		PreparedStatement pstmt=con.prepareStatement(sb.toString());
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return rs.getInt("total");
		}else{
			return 0;
		}
	}
	
	public int importDelete(Connection con,String delIds) throws Exception{
		String sql = "delete from t_import where iid in ("+delIds+")";
		PreparedStatement pstmt=con.prepareStatement(sql);
		System.out.println(sql);
		return pstmt.executeUpdate();
	}
	public void updataStock(Connection con,Import importGoods) throws SQLException{
		if(importGoods.getBeforeNum()<Integer.parseInt(importGoods.getImpoNum())){
			String sql = "update t_stock set stockNum=stockNum+? where goodsId=?";
			PreparedStatement pstmt=con.prepareStatement(sql);
			int stockNum = Integer.parseInt(importGoods.getImpoNum())-importGoods.getBeforeNum();
			pstmt.setString(1, String.valueOf(stockNum));
			pstmt.setInt(2, Integer.parseInt(importGoods.getGoodsId()));
			pstmt.executeUpdate();
		}
		else{
			String sql = "update t_stock set stockNum=stockNum-? where goodsId=?";
			PreparedStatement pstmt=con.prepareStatement(sql);
			int stockNum = importGoods.getBeforeNum()-Integer.parseInt(importGoods.getImpoNum());
			pstmt.setString(1, String.valueOf(stockNum));
			pstmt.setInt(2, Integer.parseInt(importGoods.getGoodsId()));
			pstmt.executeUpdate();
		}
	}
	public void saveStock(Connection con,Import importGoods) throws SQLException{
		String sql = "insert t_stock value(null,?,?,?,null,?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		int goodsid = Integer.parseInt(importGoods.getGoodsId());
		pstmt.setInt(1, goodsid);
		pstmt.setString(2, importGoods.getImpoNum());
		pstmt.setString(3, importGoods.getImpoPrice());
		pstmt.setString(4, importGoods.getImpoDesc());
		pstmt.executeUpdate();
	}
	public int importSave(Connection con,Import importGoods) throws Exception{
//		importSaveGood(con,importGoods);
		saveStock(con,importGoods);
		String sql = "insert t_import value(null,?,?,?,?,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1,Integer.parseInt(importGoods.getGoodsId()));
		pstmt.setString(2, importGoods.getImpoPrice());
		pstmt.setString(3, DateUtil.formatDate(importGoods.getImpoDate(), "yyyy-MM-dd"));
		pstmt.setString(4, importGoods.getImpoNum());
		pstmt.setString(5, importGoods.getImpoDesc());
		pstmt.setInt(7, importGoods.getCouId());
		pstmt.setInt(6, importGoods.getServiceId());
		DayDao dayDao = new DayDao();
		dayDao.importToCount(con, importGoods);
		System.out.println(importGoods);
		System.out.println(sql+"sql@@@@@@@@@@@@@@@@@@@@@@22");
		return pstmt.executeUpdate();
	}

	public int importModify(Connection con ,Import importGoods) throws Exception{
		updataStock(con,importGoods);
		String sql = "update t_import set goodsId=?,impoPrice=?,impoDate=?,impoNum=?,impoDesc=? where iid=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1,importGoods.getGoodsId());
		pstmt.setString(2, importGoods.getImpoPrice());
		pstmt.setString(3, DateUtil.formatDate(importGoods.getImpoDate(), "yyyy-MM-dd HH:mm:ss"));
		pstmt.setString(4, importGoods.getImpoPrice());
		pstmt.setString(5, importGoods.getImpoDesc());
		pstmt.setInt(6, importGoods.getIid());
		return pstmt.executeUpdate();
	}
	
	public boolean getGoodsByImportId(Connection con,String delIds) throws Exception{
		String sql = "select * from t_import where goodsId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, delIds);
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return true;
		}else{
			return false;
		}
	}
}
