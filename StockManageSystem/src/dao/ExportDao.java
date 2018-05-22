package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Export;
import model.Goods;
import model.PageBean;
import util.DateUtil;
import util.StringUtil;

public class ExportDao {

	public ResultSet exportList(Connection con,PageBean pageBean,Goods goods,Export exportGoods,String s_bexpoPrice,String s_eexpoPrice,String s_bexpoDate,String s_eexpoDate,String s_wid) throws Exception{
		//4 g---gname   e---
		StringBuffer sb = new StringBuffer("SELECT * FROM t_goods t2,t_export t1 WHERE t1.goodsId=t2.gid ");

		if(goods!=null &&StringUtil.isNotEmpty(goods.getGoodsName())){
			sb.append(" and t2.goodsName like '%"+goods.getGoodsName()+"%'");
		}
		
		if(StringUtil.isNotEmpty(s_bexpoPrice)){
			sb.append(" and expoPrice >="+s_bexpoPrice+"");
		}
		if(StringUtil.isNotEmpty(s_eexpoPrice)){
			sb.append(" and expoPrice <="+s_eexpoPrice+"");
		}
		
		if(StringUtil.isNotEmpty(s_bexpoDate)){
			sb.append(" and TO_DAYS(t1.expoDate)>=TO_DAYS('"+s_bexpoDate+"')");
		}
		if(StringUtil.isNotEmpty(s_eexpoDate)){
			sb.append(" and TO_DAYS(t1.expoDate)<=TO_DAYS('"+s_eexpoDate+"')");
		}
		if(StringUtil.isNotEmpty(s_wid)){
			System.out.println(s_wid+"wid...............................");

			int wid = Integer.parseInt(s_wid);
			sb.append(" and wareid="+wid);
		}
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getRows());
		}
		
		System.out.println(sb.toString()+"...............................");
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		return pstmt.executeQuery();
	}
	
	public ResultSet exportData(Connection con) throws Exception {
		String sql = "SELECT t2.gid,goodsName,expoPrice,expoDate,expoNum,expoDesc FROM t_goods t2,t_export t1 WHERE t1.goodsId=t2.gid";
		PreparedStatement pstmt = con.prepareStatement(sql);
		return pstmt.executeQuery();
	}
	
	
	
	public int exportCount(Connection con,Goods goods,Export exportGoods,String s_bexpoPrice,String s_eexpoPrice,String s_bexpoDate,String s_eexpoDate) throws Exception{
		StringBuffer sb = new StringBuffer("select count(*) as total from t_goods t2,t_export t1 where t1.goodsId=t2.gid");
		if(StringUtil.isNotEmpty(goods.getGoodsName())){
			sb.append(" and t2.goodsName like '%"+goods.getGoodsName()+"%'");
		}
		
		if(StringUtil.isNotEmpty(s_bexpoPrice)){
			sb.append(" and expoPrice >="+s_bexpoPrice+"");
		}
		if(StringUtil.isNotEmpty(s_eexpoPrice)){
			sb.append(" and expoPrice <="+s_eexpoPrice+"");
		}
		
		if(StringUtil.isNotEmpty(s_bexpoDate)){
			//给定日期返回天数
			sb.append(" and TO_DAYS(t1.expoDate)>=TO_DAYS('"+s_bexpoDate+"')");
		}
		if(StringUtil.isNotEmpty(s_eexpoDate)){
			sb.append(" and TO_DAYS(t1.expoDate)<=TO_DAYS('"+s_eexpoDate+"')");
		}
		PreparedStatement pstmt=con.prepareStatement(sb.toString());
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return rs.getInt("total");
		}else{
			return 0;
		}
	}
	
	public int exportDelete(Connection con,String delIds) throws Exception{
		String sql = "delete from t_export where eid in ("+delIds+")";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeUpdate();
	}
	
	public ResultSet exportMaxNum(Connection con,Export exportGoods) throws NumberFormatException, SQLException{
		String sql = "select stockNum from t_stock where goodsId=? ";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1, Integer.parseInt(exportGoods.getGoodsId()));
		return pstmt.executeQuery();
	}
	
	public void upDataStock(Connection con ,Export exportGoods,int num,int bnum) throws NumberFormatException, SQLException{
		if(num>bnum){
		String sql = "update t_stock set stockNum=stockNum-? where goodsId=? ";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1, num-bnum);
		pstmt.setInt(2,Integer.parseInt(exportGoods.getGoodsId()));
		pstmt.executeUpdate();
		}
		else{
			String sql = "update t_stock set stockNum=stockNum+? where goodsId=? ";
			PreparedStatement pstmt=con.prepareStatement(sql);
			
			pstmt.setInt(1, bnum-num);
			pstmt.setInt(2,Integer.parseInt(exportGoods.getGoodsId()));
			pstmt.executeUpdate();
		}
	}
	
	
	
	public int exportSave(Connection con,Export exportGoods) throws Exception{
		int maxNum = 0;
		ResultSet rs = exportMaxNum(con,exportGoods);//当前商品库存
	
		if(rs.next()){
			maxNum = Integer.parseInt(rs.getString("stockNum"));
		
		}
		
		int exNum = Integer.parseInt(exportGoods.getExpoNum());
		int beforeNum = exportGoods.getBrforeNum();
		if(exNum<maxNum){
			upDataStock(con,exportGoods,exNum,beforeNum);//更新商品库存数量
			

		String sql = "insert t_export value(null,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1,Integer.parseInt(exportGoods.getGoodsId()));
		pstmt.setString(2, exportGoods.getExpoPrice());
		pstmt.setInt(3, Integer.valueOf(exportGoods.getCouId()));
		pstmt.setString(4, DateUtil.formatDate(exportGoods.getExpoDate(), "yyyy-MM-dd HH:mm:ss"));
		pstmt.setString(5, exportGoods.getExpoNum());
		pstmt.setString(6, exportGoods.getExpoDesc());
		pstmt.setInt(7, Integer.parseInt(exportGoods.getSerId()));
		pstmt.setInt(8, Integer.parseInt(exportGoods.getWhid()));
		int nums = pstmt.executeUpdate();//先执行之后再写入日结账中的总数量才是对的
		 
		DayDao dayDao = new DayDao();//写入日结账表
		dayDao.exportToCount(con, exportGoods);
		return nums;
		}
		else{
			return 0;
		}
		
	}

	public int exportModify(Connection con ,Export exportGoods) throws Exception{
		
		int maxNum = 0;
		ResultSet rs = exportMaxNum(con,exportGoods);
	
		if(rs.next()){
			maxNum = rs.getInt("stockNum");

		}
		
		int exNum = Integer.parseInt(exportGoods.getExpoNum());
		int brforeNum = exportGoods.getBrforeNum();//得到修改之前的出库数
		
		if(exNum<maxNum){
		String sql = "update t_export set goodsId=?,expoPrice=?,expoDate=?,expoNum=?,serId=?,expoDesc=?,wareid=?,couid=? where eid=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1,Integer.parseInt(exportGoods.getGoodsId()));
		pstmt.setString(2, exportGoods.getExpoPrice());
		pstmt.setString(3, DateUtil.formatDate(exportGoods.getExpoDate(), "yyyy-MM-dd HH:mm:ss"));
		pstmt.setString(4, exportGoods.getExpoNum());
		pstmt.setString(5, exportGoods.getExpoDesc());
		pstmt.setInt(6, Integer.parseInt(exportGoods.getSerId()));
		
		pstmt.setInt(7, Integer.valueOf(exportGoods.getCouId()));
		pstmt.setInt(8, Integer.valueOf(exportGoods.getWhid()));
		pstmt.setInt(9, exportGoods.getEid());
		upDataStock(con,exportGoods,exNum,brforeNum);//根据beforeNum与修改后的值对比来确定是增加库存数还是减少库存数
		int nums = pstmt.executeUpdate();
		

		return nums;
		
		}
		else{
			return 0;
		}
	}
	
	public boolean getGoodsByExportId(Connection con,String delIds) throws Exception{
		String sql = "select * from t_export where goodsId=?";
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
