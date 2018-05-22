package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.GoodsType;
import model.PageBean;
import model.User;
import util.MD5Util;
import util.StringUtil;

public class UserDao {

	public User login(Connection con,User user) throws Exception {
		User resultUser = null;
		String sql = "select * from t_user where username=? and password=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, MD5Util.getResult(user.getUserName()));
		pstmt.setString(2, MD5Util.getResult(user.getPassword()));
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){
			resultUser = new User();
			resultUser.setUserName(rs.getString("username"));
			resultUser.setPassword(rs.getString("password"));
			resultUser.setId(rs.getInt("uid"));
			System.out.println("######"+resultUser.getId());
		}
		return resultUser;
	}
	public ResultSet usertList(Connection con){
		StringBuffer sb = new StringBuffer("SELECT * FROM t_user ");
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
	
	
	
	public int goodsTypeCount(Connection con,GoodsType goodsType) throws Exception{
		StringBuffer sb = new StringBuffer("select count(*) as total from t_goodsType");
		if(goodsType!=null && StringUtil.isNotEmpty(goodsType.getTypeName())){
			sb.append(" and typeName like '%"+goodsType.getTypeName()+"%'");
		}
		PreparedStatement pstmt=con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return rs.getInt("total");
		}else{
			return 0;
		}
	}
	
	public int goodsTypeDelete(Connection con,String delIds) throws Exception{
		String sql = "delete from t_goodsType where gtid in ("+delIds+")";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeUpdate();
	}
	
	public int goodsTypeSave(Connection con,GoodsType goodsType) throws Exception{
		String sql = "insert t_goodsType value(null,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1,goodsType.getTypeName());
		pstmt.setString(2, goodsType.getTypeDesc());
		return pstmt.executeUpdate();
	}
	public int goodsTypeModify(Connection con ,GoodsType goodsType) throws Exception{
		String sql = "update t_goodsType set id=?,typeName=?,typeDesc=? where gtid=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1,goodsType.getId());
		pstmt.setString(2, goodsType.getTypeName());
		pstmt.setString(3, goodsType.getTypeDesc());
		pstmt.setInt(4, goodsType.getId());
		return pstmt.executeUpdate();
	}
}

	

