package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Cou;
import model.Day;
import model.Export;
import model.Import;
import model.PageBean;
import util.DateUtil;
import util.StringUtil;

public class DayDao {
	public ResultSet dayList(Connection con){
		StringBuffer sb = new StringBuffer("SELECT * FROM t_daycount ");
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
	
	
	public ResultSet importCount(Connection con,Import impo) throws SQLException{
		StringBuffer sb = new StringBuffer("SELECT * FROM t_daycount ");
		if(impo.getImpoDate()!=null){
			sb.append(" and date='"+ DateUtil.formatDate(impo.getImpoDate(), "yyyy-MM-dd")+"'");
		}
		if(impo.getGoodsId()!=null){
			sb.append(" and goodsid="+Integer.parseInt(impo.getGoodsId()));
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		System.out.println("importCount"+sb);
		return pstmt.executeQuery();
	}
	
	public ResultSet exportCount(Connection con,Export export) throws SQLException{
		StringBuffer sb = new StringBuffer("SELECT * FROM t_daycount ");
		if(export.getExpoDate()!=null){
			sb.append(" and date='"+ DateUtil.formatDate(export.getExpoDate(), "yyyy-MM-dd")+"'");
		}
		if(export.getGoodsId()!=null){
			sb.append(" and goodsid="+Integer.parseInt(export.getGoodsId()));
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		return pstmt.executeQuery();
	}
	
	
	public ResultSet sum(Connection con,int goodsid) throws SQLException{
		StringBuffer sb = new StringBuffer("SELECT * FROM t_stock where goodsId="+goodsid);
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		System.out.println("sum"+sb);
		return pstmt.executeQuery();
	}
	
	
	public void importToCount(Connection con,Import impo) throws SQLException{
		ResultSet rs = importCount(con,impo);//查找日结账表是否有对应信息
		if(rs.next()){//有对应信息就修改
			ResultSet rs2 = sum(con,Integer.parseInt(impo.getGoodsId()));
			if(rs2.next()){
			int sum = Integer.parseInt(rs2.getString("stockNum"));//得到库存表里面的总的库存数量
			String sql = "update t_daycount set whid=?,innum=?,sum=? where date=? and goodsId=?";
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setString(4, DateUtil.formatDate(impo.getImpoDate(), "yyyy-MM-dd"));
			pstmt.setInt(1, impo.getWhid());
			pstmt.setInt(2, Integer.parseInt(impo.getImpoNum()));
			pstmt.setInt(3, sum);
			pstmt.setInt(5,Integer.parseInt(impo.getGoodsId()));
			 pstmt.executeUpdate();
			 System.out.println("importToCount   "+sql);
			}
		}
		else{
			ResultSet rs2 = sum(con,Integer.parseInt(impo.getGoodsId()));
			if(rs2.next()){
			int sum = Integer.parseInt(rs2.getString("stockNum"));
			String sql = "insert t_daycount(whid,innum,sum,date,goodsid) value(?,?,?,?,?)";
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, impo.getWhid());
			pstmt.setInt(2, Integer.parseInt(impo.getImpoNum()));
			pstmt.setInt(3, sum);
			pstmt.setString(4, DateUtil.formatDate(impo.getImpoDate(), "yyyy-MM-dd"));
			pstmt.setInt(5,Integer.parseInt(impo.getGoodsId()));
			
			pstmt.executeUpdate();
			}
		}
	}
	
	public void exportToCount(Connection con,Export export) throws Exception{
		ResultSet rs = exportCount(con,export);//对应消息在日结账中是否存在如果存在则修改，不存在就添加
		if(rs.next()){//有则修改
			ResultSet rs2 = sum(con,Integer.parseInt(export.getGoodsId()));
			if(rs2.next()){
			int sum = Integer.parseInt(rs2.getString("stockNum"));//得到库存中的该商品数量
			//更新对应的日结账信息，增加出库数
			String sql = "update t_daycount set whid=?,outnum=?,sum=? where date=? and goodsId=? ";
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(export.getWhid()));
			if(rs.getInt("outNum")>0){
				pstmt.setInt(2, Integer.parseInt(export.getExpoNum())+rs.getInt("outNum"));
			}
			else{//日结账中原先只有入库信息
				pstmt.setInt(2, Integer.parseInt(export.getExpoNum()));
			}
			pstmt.setInt(3, sum);
			pstmt.setString(4, DateUtil.formatDate(export.getExpoDate(), "yyyy-MM-dd"));

			pstmt.setInt(5,Integer.parseInt( export.getGoodsId()));

			 pstmt.executeUpdate();
			}
		}
		else{//没有对应的日结账信息，新建一个
			ResultSet rs2 = sum(con,Integer.parseInt(export.getGoodsId()));
			if(rs2.next()){
			int sum = Integer.parseInt(rs2.getString("stockNum"));//得到库存中该商品的数量
			String sql = "insert t_daycount(whid,outnum,sum,date,goodsid) value(?,?,?,?,?)";
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(export.getWhid()));
			pstmt.setInt(2, Integer.parseInt(export.getExpoNum()));
			pstmt.setInt(3, sum);
			pstmt.setString(4, DateUtil.formatDate(export.getExpoDate(), "yyyy-MM-dd"));
			pstmt.setInt(5,Integer.parseInt(export.getGoodsId()));
			pstmt.executeUpdate();
			}
		}
	}
	

	
	
	public ResultSet dayList(Connection con,PageBean pageBean,Day day,String s_daydate1,String s_daydate2,String s_wid) throws Exception{
		StringBuffer sb = new StringBuffer("select * from t_daycount");
		if(StringUtil.isNotEmpty(s_daydate1)){
			sb.append(" and TO_DAYS(date)>=TO_DAYS('"+s_daydate1+"')");
		}
		if(StringUtil.isNotEmpty(s_daydate2)){
			sb.append(" and TO_DAYS(date)<=TO_DAYS('"+s_daydate2+"')");
		}
		if(StringUtil.isNotEmpty(s_wid)){
			System.out.println(s_wid+"wid...............................");

			int wid = Integer.parseInt(s_wid);
			sb.append(" and whid="+wid);
		}
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getRows());
		}
		
		System.out.println("..............."+sb+"...................");
		PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		return pstmt.executeQuery();
	}
	
	public int dayCount(Connection con,Day day,String s_daydate1,String s_daydate2) throws Exception{
		StringBuffer sb = new StringBuffer("select count(*) as total from t_daycount");

		if(StringUtil.isNotEmpty(s_daydate1)){
			sb.append(" and TO_DAYS(date)>=TO_DAYS('"+s_daydate1+"')");
		}
		if(StringUtil.isNotEmpty(s_daydate2)){
			sb.append(" and TO_DAYS(date)<=TO_DAYS('"+s_daydate2+"')");
		}
		PreparedStatement pstmt=con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return rs.getInt("total");
		}else{
			return 0;
		}
	}
	
	public int dayDelete(Connection con,String delIds) throws Exception{
		String sql = "delete from t_daycount where dayid in ("+delIds+")";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeUpdate();
	}
	
	public int daySave(Connection con,Day day) throws Exception{
		String sql = "insert t_daycount value(null,?,?,?,?,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);

		pstmt.setString(1, DateUtil.formatDate(day.getDate(), "yyyy-MM-dd"));
		pstmt.setInt(2, day.getWid());
		pstmt.setInt(3, day.getGoodsId());
		pstmt.setInt(4, day.getInnum());
		pstmt.setInt(5, day.getOutnum());
		pstmt.setInt(6, day.getBadnum());
		pstmt.setInt(7, day.getSum());
		return pstmt.executeUpdate();
	}
	public int dayModify(Connection con ,Day day) throws Exception{
		String sql = "update t_daycount set date=?,whid=?,goodsId=?,innum=?,outnum=?,badnum=?,sum=? where dayid=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, DateUtil.formatDate(day.getDate(), "yyyy-MM-dd"));
		pstmt.setInt(2, day.getWid());
		pstmt.setInt(3, day.getGoodsId());
		pstmt.setInt(4, day.getInnum());
		pstmt.setInt(5, day.getOutnum());
		pstmt.setInt(6, day.getBadnum());
		if(day.getBadnum()!=0){
			StockDao stoc = new StockDao();
			stoc.stockModify2(con, day.getSum(),day.getGoodsId(),day.getBadnum());
			pstmt.setInt(7, day.getSum()-day.getBadnum());
		}else{
			pstmt.setInt(7, day.getSum());
		}
		
		pstmt.setInt(8, day.getDayid());
		return pstmt.executeUpdate();
	}
}
