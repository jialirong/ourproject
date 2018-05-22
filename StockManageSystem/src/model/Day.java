package model;

import java.util.Date;

public class Day {
	private int dayid;
	public int getDayid() {
		return dayid;
	}
	public void setDayid(int dayid) {
		this.dayid = dayid;
	}
	private Date date;
	private int innum;
	private int outnum;
	private int sum;
	private int badnum;
	private int wid;
	private int goodsId;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getInnum() {
		return innum;
	}
	public void setInnum(int innum) {
		this.innum = innum;
	}
	public int getOutnum() {
		return outnum;
	}
	public void setOutnum(int outnum) {
		this.outnum = outnum;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	public int getBadnum() {
		return badnum;
	}
	public void setBadnum(int badnum) {
		this.badnum = badnum;
	}
	public int getWid() {
		return wid;
	}
	public void setWid(int wid) {
		this.wid = wid;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	@Override
	public String toString() {
		return "Day [date=" + date + ", innum=" + innum + ", outnum=" + outnum + ", sum=" + sum + ", badnum=" + badnum
				+ ", wid=" + wid + ", goodsId=" + goodsId + "]";
	}
	
}
