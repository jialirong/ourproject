package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.Goods;
import model.PageBean;
import model.Stock;
import util.StringUtil;

public class GoodsDao {

	public ResultSet goodsList(Connection con,PageBean pageBean,Goods goods) throws Exception{
		StringBuffer sb = new StringBuffer("SELECT * FROM t_goodsType t1,t_provider t2,t_goods t3 WHERE t3.proId=t2.id and t1.id=t3.typeId");
		if(StringUtil.isNotEmpty(goods.getGoodsId())){
			sb.append(" and t3.goodsId like '%"+goods.getGoodsId()+"%'");
		}
		if(StringUtil.isNotEmpty(goods.getGoodsName())){
			sb.append(" and t3.goodsName like '%"+goods.getGoodsName()+"%'");
		}
		if(StringUtil.isNotEmpty(goods.getProId())){
			sb.append(" and t3.proId='"+goods.getProId()+"'");
		}
		if(StringUtil.isNotEmpty(goods.getTypeId())){
			sb.append(" and t3.typeId='"+goods.getTypeId()+"'");
		}
		sb.append(" order by t3.id asc");
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getRows());
		}
		System.out.println(sb.toString());
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		return pstmt.executeQuery();
	}
	
	public ResultSet exportData(Connection con) throws Exception {
		String sql = "SELECT goodsId,goodsName,proName,typeName,goodsDesc FROM t_goodsType t1,t_provider t2,t_goods t3 WHERE t3.proId=t2.id AND t1.id=t3.typeId";
		PreparedStatement pstmt = con.prepareStatement(sql);
		return pstmt.executeQuery();
	}
	
	public int goodsCount(Connection con,Goods goods) throws Exception{
	StringBuffer sb = new StringBuffer("select count(*) as total from t_goodsType t1,t_provider t2,t_goods t3 WHERE t3.proId=t2.id and t1.id=t3.typeId");
		if(StringUtil.isNotEmpty(goods.getGoodsId())){
			sb.append(" and t3.goodsId like '%"+goods.getGoodsId()+"%'");
		}
		if(StringUtil.isNotEmpty(goods.getGoodsName())){
			sb.append(" and t3.goodsName like '%"+goods.getGoodsName()+"%'");
		}
		if(StringUtil.isNotEmpty(goods.getProId())){
			sb.append(" and t3.proId='"+goods.getProId()+"'");
		}
		if(StringUtil.isNotEmpty(goods.getTypeId())){
			sb.append(" and t3.typeId='"+goods.getTypeId()+"'");
		}
		sb.append(" order by t3.id asc");
		PreparedStatement pstmt=con.prepareStatement(sb.toString());
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return rs.getInt("total");
		}else{
			return 0;
		}
	}
	
	public int goodsDelete(Connection con,String delIds)throws Exception{
		String sql="delete from t_goods where id in("+delIds+")";
		PreparedStatement pstmt=con.prepareStatement(sql);
		System.out.println(sql);
		return pstmt.executeUpdate();
	}
	
	public int goodsAdd(Connection con,Goods goods)throws Exception{
		String sql="insert into t_goods values(null,?,?,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, goods.getGoodsId());
		pstmt.setString(2, goods.getGoodsName());
		pstmt.setString(3, goods.getProId());
		pstmt.setString(4, goods.getTypeId());
		pstmt.setString(5, goods.getGoodsDesc());
		return pstmt.executeUpdate();
	}

	public int goodsModify(Connection con,Goods goods)throws Exception{
		String sql="update t_goods set goodsId=?,goodsName=?,proId=?,typeId=?,goodsDesc=? where id=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, goods.getGoodsId());
		pstmt.setString(2, goods.getGoodsName());
		pstmt.setString(3, goods.getProId());
		pstmt.setString(4, goods.getTypeId());
		pstmt.setString(5, goods.getGoodsDesc());
		pstmt.setInt(6, goods.getId());
		return pstmt.executeUpdate();
	}
	
	public ArrayList<Goods> goodsArrayList(Connection con,String sql) throws Exception{
		ArrayList<Goods> goodsList=new ArrayList<Goods>();
		PreparedStatement pstmt=con.prepareStatement(sql);
		ResultSet rs=pstmt.executeQuery();
		while(rs.next()){
			Goods goods = new Goods();
			goods.setId(rs.getInt("id"));
			goods.setGoodsId(rs.getString("goodsId"));
			goods.setGoodsName(rs.getString("goodsName"));
			goods.setProId(rs.getString("proId"));
			goods.setTypeId(rs.getString("typeId"));
			goods.setGoodsDesc(rs.getString("goodsDesc"));
			goodsList.add(goods);
		}
		return goodsList;
	}
	
	
	public Goods getGoodsById(Connection con,String id)throws Exception{
		String sql="SELECT * from t_goods;";
		PreparedStatement pstmt=con.prepareStatement(sql);
		ResultSet rs=pstmt.executeQuery();
		Goods goods = new Goods();
		while(rs.next()){
			goods.setId(rs.getInt("id"));
			goods.setGoodsId(rs.getString("goodsId"));
			goods.setGoodsName(rs.getString("goodsName"));
			goods.setProId(rs.getString("proId"));
			goods.setTypeId(rs.getString("typeId"));
			goods.setGoodsDesc(rs.getString("goodsDesc"));
		}
		return goods;
	}
}
