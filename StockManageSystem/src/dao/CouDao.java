package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import model.Cou;
import model.GoodsType;
import model.PageBean;
import util.StringUtil;

public class CouDao {
	public ResultSet coutList(Connection con){
		StringBuffer sb = new StringBuffer("SELECT * FROM t_coustmer ");
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(sb.toString());
			System.out.println(sb.toString());
			return pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
	}
	

	
	
	public ResultSet couList(Connection con,PageBean pageBean,Cou cou) throws Exception{
		StringBuffer sb = new StringBuffer("select * from t_coustmer");
		if(cou!=null && StringUtil.isNotEmpty(cou.getName())){
			sb.append(" and name like '%"+cou.getName()+"%'");
		}
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getRows());
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		return pstmt.executeQuery();
	}
	
	public int couCount(Connection con,Cou cou) throws Exception{
		StringBuffer sb = new StringBuffer("select count(*) as total from t_coustmer");
		if(cou!=null && StringUtil.isNotEmpty(cou.getName())){
			sb.append(" and name like '%"+cou.getName()+"%'");
		}
		PreparedStatement pstmt=con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return rs.getInt("total");
		}else{
			return 0;
		}
	}

	public int couDelete(Connection con,String delIds) throws Exception{
		String sql = "delete from t_coustmer where cid in ("+delIds+")";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeUpdate();
	}
	
	public int couSave(Connection con,Cou cou) throws Exception{
		String sql = "insert t_coustmer value(null,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		System.out.println("getName()"+cou.getName());
		pstmt.setString(1,cou.getName());
		pstmt.setString(2, cou.getCouId());
		return pstmt.executeUpdate();
	}
	public int couModify(Connection con ,Cou cou) throws Exception{
		String sql = "update t_coustmer set name=?,couId=? where cid=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1,cou.getName());
		pstmt.setString(2, cou.getCouId());
		pstmt.setInt(3, cou.getCid());
		return pstmt.executeUpdate();
	}
}
