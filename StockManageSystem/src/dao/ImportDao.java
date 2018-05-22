package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Goods;
import model.Import;
import model.PageBean;
import util.DateUtil;
import util.StringUtil;

public class ImportDao {

	public ResultSet importList(Connection con,PageBean pageBean,Goods goods,Import importGoods,String s_bimpoPrice,String s_eimpoPrice,String s_bimpoDate,String s_eimpoDate,String s_wid) throws Exception{
		StringBuffer sb = new StringBuffer("SELECT * FROM t_goods t2,t_import t1 ,"
				+ "t_coustmer t3,t_goodstype t4 WHERE t1.goodsId=t2.gid and t3.cid=t1.couId and t4.gtid=t2.typeId");
		if(goods!=null&&StringUtil.isNotEmpty(goods.getGoodsName())){
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
		if(StringUtil.isNotEmpty(s_wid)){
			int m = Integer.parseInt(s_wid);
			sb.append(" and t4.wid="+m);
		}
		sb.append(" order by t1.iid asc");
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getRows());
		}

		System.out.println(sb.toString()+"..........................");
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		return pstmt.executeQuery();
	}

	public ResultSet exportData(Connection con) throws Exception {
		String sql = "SELECT goodsName,impoPrice,impoDate,impoNum,impoDesc,name,couId FROM t_goods t2,t_import t1,t_coustmer t3 WHERE t1.goodsId=t2.gid and t3.cid=t1.couId";
		PreparedStatement pstmt = con.prepareStatement(sql);
		return pstmt.executeQuery();
	}

	public int importCount(Connection con,Goods goods,Import importGoods,String s_bimpoPrice,String s_eimpoPrice,String s_bimpoDate,String s_eimpoDate) throws Exception{
		StringBuffer sb = new StringBuffer("select count(*) as total from t_goods t2,t_import t1 where t1.goodsId=t2.gid");
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
		String sql = "delete from t_import where iid in ("+delIds+")";
		PreparedStatement pstmt=con.prepareStatement(sql);
		System.out.println(sql);
		return pstmt.executeUpdate();
	}

	public void updataStock(Connection con,Import importGoods) throws SQLException{
		//修改的数量大于之前的数量时，对应的库存要加上对应的差，小于之前的数量时要减去对应的差值（之前的数和修改之后的数）
		if(importGoods.getBeforeNum()<Integer.parseInt(importGoods.getImpoNum())){
			String sql = "update t_stock set stockNum=stockNum+? where goodsId=?";
			PreparedStatement pstmt=con.prepareStatement(sql);
											// 加 现在的入库数-之前的入库数
			int stockNum = Integer.parseInt(importGoods.getImpoNum())-importGoods.getBeforeNum();
			pstmt.setString(1, String.valueOf(stockNum));
			pstmt.setInt(2, Integer.parseInt(importGoods.getGoodsId()));
			pstmt.executeUpdate();
		}
		else{
			String sql = "update t_stock set stockNum=stockNum-? where goodsId=?";
			PreparedStatement pstmt=con.prepareStatement(sql);
											//减   现在的入库数-之前的入库数
			int stockNum = importGoods.getBeforeNum()-Integer.parseInt(importGoods.getImpoNum());
			pstmt.setString(1, String.valueOf(stockNum));
			pstmt.setInt(2, Integer.parseInt(importGoods.getGoodsId()));
			pstmt.executeUpdate();
		}
	}
	
	//增加一条入库信息时调用
	public void saveStock(Connection con,Import importGoods) throws SQLException{
		String sqlif = "select * from t_stock where goodsId=?";
		PreparedStatement pstmt1 = con.prepareStatement(sqlif);
		pstmt1.setInt(1, Integer.valueOf(importGoods.getGoodsId()));
		ResultSet rs = pstmt1.executeQuery();
		if(!rs.next())//对应的商品不存在则在库存信息中增加一条
		{
			String sql = "insert t_stock value(null,?,?,?,null,?)";
			PreparedStatement pstmt = con.prepareStatement(sql);
			int goodsid = Integer.parseInt(importGoods.getGoodsId());
			pstmt.setInt(1, goodsid);
			pstmt.setString(2, importGoods.getImpoNum());
			pstmt.setString(3, importGoods.getImpoPrice());
			pstmt.setString(4, importGoods.getImpoDesc());
			pstmt.executeUpdate();
		}else{//存在则更新对应的库存信息
			String sql = "update t_stock set stockNum=stockNum+? where goodsId=?";
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setString(1, String.valueOf(importGoods.getImpoNum()));
			pstmt.setInt(2, Integer.parseInt(importGoods.getGoodsId()));
			pstmt.executeUpdate();
		}


	}
	
	public int importSave(Connection con,Import importGoods) throws Exception{
		saveStock(con,importGoods);//修改对应商品的库存信息
		String sql = "insert t_import value(null,?,?,?,?,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1,Integer.parseInt(importGoods.getGoodsId()));
		pstmt.setString(2, importGoods.getImpoPrice());
		pstmt.setString(3, DateUtil.formatDate(importGoods.getImpoDate(), "yyyy-MM-dd"));
		pstmt.setString(4, importGoods.getImpoNum());
		pstmt.setString(5, importGoods.getImpoDesc());
		pstmt.setInt(7, importGoods.getCouId());
		pstmt.setInt(6, importGoods.getServiceId());
		int nums = pstmt.executeUpdate();//先执行然后再修改日结账表
		DayDao dayDao = new DayDao();
		dayDao.importToCount(con, importGoods);

		return nums;
	}

	public int importModify(Connection con ,Import importGoods) throws Exception{
		updataStock(con,importGoods);//修改对应的商品库存
		String sql = "update t_import set goodsId=?,impoPrice=?,impoDate=?,impoNum=?,impoDesc=?,couId=?,serviceId=? where iid=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1,importGoods.getGoodsId());
		pstmt.setString(2, importGoods.getImpoPrice());
		pstmt.setString(3, DateUtil.formatDate(importGoods.getImpoDate(), "yyyy-MM-dd HH:mm:ss"));
		pstmt.setString(4, importGoods.getImpoNum());
		pstmt.setString(5, importGoods.getImpoDesc());
		pstmt.setInt(6, importGoods.getCouId());
		pstmt.setInt(7, importGoods.getServiceId());
		pstmt.setInt(8, importGoods.getIid());

		int nums = pstmt.executeUpdate();
	
		return nums;
	
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
