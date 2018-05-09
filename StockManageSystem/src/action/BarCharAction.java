package action;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import model.Goods;
import model.Stock;
import util.DbUtil;

import com.opensymphony.xwork2.ActionSupport;

import dao.GoodsDao;

public class BarCharAction extends ActionSupport{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private String id;
	private Goods goods;
	private JFreeChart chart;
	
	public JFreeChart getChart() {
		return chart;
	}
	public void setChart(JFreeChart chart) {
		this.chart = chart;
	}
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	DbUtil dbUtil = new DbUtil();
	GoodsDao goodsDao = new GoodsDao();
	
	public String show() throws Exception{
		//这是实现动态数据报表
		//request.setAttribute("mainPage", "/view/show.jsp");
		/*Connection con=dbUtil.getCon();
		goods = goodsDao.getGoodsById(con, id);//获取点击商品的信息
*/				
		//创建报表
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(510, "库存", "java01");
		dataset.addValue(320, "销售量", "java01");
		chart=ChartFactory.createBarChart3D("商品库存销售表", "库存", "", dataset,
				PlotOrientation.VERTICAL, true, true, true);
		CategoryPlot plot=chart.getCategoryPlot();
		// 显示每个柱的数值，并修改该数值的字体属性
		BarRenderer3D renderer=new BarRenderer3D();
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
		renderer.setItemLabelAnchorOffset(10D);  
		// 设置平行柱的之间距离
		renderer.setItemMargin(0.4);
		
		plot.setRenderer(renderer);
				
		return "success";
	}
	
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}

}
