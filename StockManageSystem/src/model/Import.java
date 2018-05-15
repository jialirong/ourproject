package model;

import java.util.Date;

public class Import {

	private int iid;
	private String goodsId;
	private String impoPrice;
	private Date impoDate;
	private String impoNum;
	private String impoDesc;
	private int couId;
	private int serviceId;
	private int beforeNum;
	private int whid;
	public int getWhid() {
		return whid;
	}
	public void setWhid(int whid) {
		this.whid = whid;
	}
	public int getBeforeNum() {
		return beforeNum;
	}
	public void setBeforeNum(int beforeNum) {
		this.beforeNum = beforeNum;
	}
	@Override
	public String toString() {
		return "Import [id=" + iid + ", goodsId=" + goodsId + ", impoPrice=" + impoPrice + ", impoDate=" + impoDate
				+ ", impoNum=" + impoNum + ", impoDesc=" + impoDesc + ", couId=" + couId + ", serviceId=" + serviceId
				+ ", proId=" + proId + ", typeId=" + typeId + ", goodsName=" + goodsName + "]";
	}
	private int proId;
	private int typeId;
	private String goodsName;
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getProId() {
		return proId;
	}
	public void setProId(int proId) {
		this.proId = proId;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public int getCouId() {
		return couId;
	}
	public void setCouId(int couId) {
		this.couId = couId;
	}
	
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	
	

	public int getIid() {
		return iid;
	}
	public void setIid(int iid) {
		this.iid = iid;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getImpoPrice() {
		return impoPrice;
	}
	public void setImpoPrice(String impoPrice) {
		this.impoPrice = impoPrice;
	}
	public Date getImpoDate() {
		return impoDate;
	}
	public void setImpoDate(Date impoDate) {
		this.impoDate = impoDate;
	}
	public String getImpoNum() {
		return impoNum;
	}
	public void setImpoNum(String impoNum) {
		this.impoNum = impoNum;
	}
	public String getImpoDesc() {
		return impoDesc;
	}
	public void setImpoDesc(String impoDesc) {
		this.impoDesc = impoDesc;
	}
	
	
}
