package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Goods;
import model.Import;
import model.PageBean;
import util.DateUtil;
import util.StringUtil;

public class ImportDao {

	public ResultSet importList(Connection con,PageBean pageBean,Goods goods,Import importGoods,String s_bimpoPrice,String s_eimpoPrice,String s_bimpoDate,String s_eimpoDate) throws Exception{
		StringBuffer sb = new StringBuffer("SELECT * FROM t_goods t2,t_import t1 WHERE t1.goodsId=t2.id");
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
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getRows());
		}
		
		System.out.println(sb.toString());
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		return pstmt.executeQuery();
	}
	
	public ResultSet exportData(Connection con) throws Exception {
		String sql = "SELECT goodsName,impoPrice,impoDate,impoNum,impoDesc FROM t_goods t2,t_import t1 WHERE t1.goodsId=t2.id";
		PreparedStatement pstmt = con.prepareStatement(sql);
		return pstmt.executeQuery();
	}
	
	public int importCount(Connection con,Goods goods,Import importGoods,String s_bimpoPrice,String s_eimpoPrice,String s_bimpoDate,String s_eimpoDate) throws Exception{
		StringBuffer sb = new StringBuffer("select count(*) as total from t_goods t2,t_import t1 where t1.goodsId=t2.id");
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
		String sql = "delete from t_import where id in ("+delIds+")";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeUpdate();
	}
	
	public int importSave(Connection con,Import importGoods) throws Exception{
		String sql = "insert t_import value(null,?,?,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1,importGoods.getGoodsId());
		pstmt.setString(2, importGoods.getImpoPrice());
		pstmt.setString(3, DateUtil.formatDate(importGoods.getImpoDate(), "yyyy-MM-dd"));
		pstmt.setString(4, importGoods.getImpoNum());
		pstmt.setString(5, importGoods.getImpoDesc());
		return pstmt.executeUpdate();
	}
	
	public int importModify(Connection con ,Import importGoods) throws Exception{
		String sql = "update t_import set goodsId=?,impoPrice=?,impoDate=?,impoNum=?,impoDesc=? where id=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1,importGoods.getGoodsId());
		pstmt.setString(2, importGoods.getImpoPrice());
		pstmt.setString(3, DateUtil.formatDate(importGoods.getImpoDate(), "yyyy-MM-dd HH:mm:ss"));
		pstmt.setString(4, importGoods.getImpoPrice());
		pstmt.setString(5, importGoods.getImpoDesc());
		pstmt.setInt(6, importGoods.getId());
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
