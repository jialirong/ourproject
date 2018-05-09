package model;

import java.util.Date;

public class Export {

	private int id;
	private String goodsId;
	private String expoPrice;
	private Date expoDate;
	private String expoNum;
	private String expoDesc;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getExpoPrice() {
		return expoPrice;
	}
	public void setExpoPrice(String expoPrice) {
		this.expoPrice = expoPrice;
	}
	public Date getExpoDate() {
		return expoDate;
	}
	public void setExpoDate(Date expoDate) {
		this.expoDate = expoDate;
	}
	public String getExpoNum() {
		return expoNum;
	}
	public void setExpoNum(String expoNum) {
		this.expoNum = expoNum;
	}
	public String getExpoDesc() {
		return expoDesc;
	}
	public void setExpoDesc(String expoDesc) {
		this.expoDesc = expoDesc;
	}
	
	
	
}
