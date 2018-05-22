package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Goods;
import model.Import;
import model.PageBean;
import model.Stock;
import util.DateUtil;
import util.StringUtil;

public class StockDao {

	public ResultSet stockList(Connection con,PageBean pageBean,Goods goods,String s_bimpoPrice,String s_eimpoPrice,String s_bexpoPrice,String s_eexpoPrice) throws Exception{
		StringBuffer sb = new StringBuffer("select * from t_goods t1, t_stock t2 where t1.gid=t2.goodsId");
		if(StringUtil.isNotEmpty(goods.getGoodsName())){
			sb.append(" and t1.goodsName like '%"+goods.getGoodsName()+"%'");
		}
		if(StringUtil.isNotEmpty(s_bimpoPrice)){
			sb.append(" and impoPrice >="+s_bimpoPrice+"");
		}
		if(StringUtil.isNotEmpty(s_eimpoPrice)){
			sb.append(" and impoPrice <="+s_eimpoPrice+"");
		}
		if(StringUtil.isNotEmpty(s_bexpoPrice)){
			sb.append(" and expoPrice >="+s_bexpoPrice+"");
		}
		if(StringUtil.isNotEmpty(s_eexpoPrice)){
			sb.append(" and expoPrice <="+s_eexpoPrice+"");
		}
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getRows());
		}
		//System.out.println(sb.toString());
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		return pstmt.executeQuery();
	}
	
	public int stockCount(Connection con,Goods goods,String s_bimpoPrice,String s_eimpoPrice,String s_bexpoPrice,String s_eexpoPrice) throws Exception{
		StringBuffer sb = new StringBuffer("select count(*) as total from t_goods t1, t_stock t2 where t1.gid=t2.goodsId");
		if(StringUtil.isNotEmpty(goods.getGoodsName())){
			sb.append(" and t1.goodsName like '%"+goods.getGoodsName()+"%'");
		}
		if(StringUtil.isNotEmpty(s_bimpoPrice)){
			sb.append(" and impoPrice >="+s_bimpoPrice+"");
		}
		if(StringUtil.isNotEmpty(s_eimpoPrice)){
			sb.append(" and impoPrice <="+s_eimpoPrice+"");
		}
		if(StringUtil.isNotEmpty(s_bexpoPrice)){
			sb.append(" and expoPrice >="+s_bexpoPrice+"");
		}
		if(StringUtil.isNotEmpty(s_eexpoPrice)){
			sb.append(" and expoPrice <="+s_eexpoPrice+"");
		}
		PreparedStatement pstmt=con.prepareStatement(sb.toString());
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return rs.getInt("total");
		}else{
			return 0;
		}
	}
	
	public int stockDelete(Connection con,String delIds) throws Exception{
		String sql = "delete from t_stock where sid in ("+delIds+")";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeUpdate();
	}
	
	public int stockSave(Connection con,Stock stock) throws Exception{
		String sql = "insert t_stock value(null,?,?,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1,stock.getGoodsId());
		pstmt.setString(2, stock.getStockNum());
		pstmt.setString(3, stock.getImpoPrice());
		pstmt.setString(4, stock.getExpoPrice());
		pstmt.setString(5, stock.getStockDesc());
		return pstmt.executeUpdate();
	}
	
	public int stockModify(Connection con ,Stock stock) throws Exception{
		String sql = "update t_stock set goodsId=?,stockNum=?,impoPrice=?,expoPrice=?,stockDesc=? where sid=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1,stock.getGoodsId());
		pstmt.setString(2, stock.getStockNum());
		pstmt.setString(3, stock.getImpoPrice());
		pstmt.setString(4, stock.getExpoPrice());
		pstmt.setString(5, stock.getStockDesc());
		pstmt.setInt(6, stock.getId());
		return pstmt.executeUpdate();
	}
	
	public int stockModify2(Connection con ,int sum,int goodsid,int num) throws Exception{
		String sql = "update t_stock set stockNum=? where goodsId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, String.valueOf(sum-num));
		pstmt.setInt(2,goodsid);
		return pstmt.executeUpdate();
	}
	//用于删除商品时判断商品时否还有，能不能删除
	public boolean getGoodsByStockId(Connection con,String delIds) throws Exception{
		String sql = "select * from t_stock where goodsId=?";
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
