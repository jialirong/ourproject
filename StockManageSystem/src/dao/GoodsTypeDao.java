package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.GoodsType;
import model.PageBean;
import util.StringUtil;

public class GoodsTypeDao {

	public ResultSet goodsTypeList(Connection con,PageBean pageBean,GoodsType goodsType) throws Exception{
		StringBuffer sb = new StringBuffer("select * from t_goodsType");
		if(goodsType!=null && StringUtil.isNotEmpty(goodsType.getTypeName())){
			sb.append(" and typeName like '%"+goodsType.getTypeName()+"%'");
		}
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getRows());
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		return pstmt.executeQuery();
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
		String sql = "delete from t_goodsType where id in ("+delIds+")";
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
		String sql = "update t_goodsType set id=?,typeName=?,typeDesc=? where id=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1,goodsType.getId());
		pstmt.setString(2, goodsType.getTypeName());
		pstmt.setString(3, goodsType.getTypeDesc());
		pstmt.setInt(4, goodsType.getId());
		return pstmt.executeUpdate();
	}
}
