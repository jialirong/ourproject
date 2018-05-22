package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.GoodsType;
import model.Provider;
import util.StringUtil;

public class ProviderDao {
	public ResultSet providerList(Connection con){
		StringBuffer sb = new StringBuffer("SELECT * FROM t_provider ");
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
	
	
	public int providerCount(Connection con,Provider provider) throws Exception{
		StringBuffer sb = new StringBuffer("select count(*) as total from t_provider");
		if(provider!=null && StringUtil.isNotEmpty(provider.getProName())){
			sb.append(" and typeName like '%"+provider.getProName()+"%'");
		}
		PreparedStatement pstmt=con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return rs.getInt("total");
		}else{
			return 0;
		}
	}
	
	public int providerDelete(Connection con,String delIds) throws Exception{
		String sql = "delete from t_provider where proId in ("+delIds+")";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeUpdate();
	}
	
	public int providerSave(Connection con,Provider provider) throws Exception{
		String sql = "insert t_provider value(null,?,?,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1,provider.getProId());
		pstmt.setString(2, provider.getProName());
		pstmt.setString(3, provider.getLinkMan());
		pstmt.setString(4, provider.getProPhone());
		pstmt.setString(5, provider.getProDesc());

		return pstmt.executeUpdate();
	}
	public int providerModify(Connection con ,Provider provider) throws Exception{
		String sql = "update t_goodsType set proId=?,proName=?,linkMan=?,proPhone=?,proDesc=? where pid=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1,provider.getProId());
		pstmt.setString(2, provider.getProName());
		pstmt.setString(3, provider.getLinkMan());
		pstmt.setString(4, provider.getProPhone());
		pstmt.setString(5, provider.getProDesc());
		pstmt.setInt(6, provider.getPid());
		return pstmt.executeUpdate();
	}
}
