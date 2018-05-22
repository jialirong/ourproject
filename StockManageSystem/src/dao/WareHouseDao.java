package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.GoodsType;
import model.PageBean;
import model.WareHouse;
import util.StringUtil;

public class WareHouseDao {
	public ResultSet wareHouseList(Connection con,PageBean pageBean,WareHouse wareHouse) throws Exception{
		StringBuffer sb = new StringBuffer("select * from t_warehouse");
		if(wareHouse!=null && StringUtil.isNotEmpty(wareHouse.getName())){
			sb.append(" and wName like '%"+wareHouse.getName()+"%'");
		}
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getRows());
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		return pstmt.executeQuery();
	}
	
	public int wareHouseCount(Connection con,WareHouse wareHouse) throws Exception{
		StringBuffer sb = new StringBuffer("select count(*) as total from t_warehouse");
		if(wareHouse!=null && StringUtil.isNotEmpty(wareHouse.getName())){
			sb.append(" and wName like '%"+wareHouse.getName()+"%'");
		}
		PreparedStatement pstmt=con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return rs.getInt("total");
		}else{
			return 0;
		}
	}
	
	public int wareHouseDelete(Connection con,String delIds) throws Exception{
		String sql = "delete from t_warehouse where wareid in ("+delIds+")";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeUpdate();
	}
	
	public int wareHouseSave(Connection con,WareHouse wareHouse) throws Exception{
		
		String sql = "insert t_warehouse value(null,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1,wareHouse.getName());
		pstmt.setString(2, wareHouse.getDescription());
		pstmt.setString(3, wareHouse.getWareid());
		return pstmt.executeUpdate();
	}
	
	public int wareHouseModify(Connection con ,WareHouse wareHouse) throws Exception{
		String sql = "update t_warehouse set wName=?,description=?,wareid=? where wid=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1,wareHouse.getName());
		pstmt.setString(2, wareHouse.getDescription());
		pstmt.setString(3, wareHouse.getWareid());
		pstmt.setInt(4,wareHouse.getWid());
		System.out.println(wareHouse.getWareid());
		return pstmt.executeUpdate();
	}
}
