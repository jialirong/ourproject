package model;

public class Cou {
	private int cid;
	private String name;
	private String couId;
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCouId() {
		return couId;
	}
	public void setCouId(String couId) {
		this.couId = couId;
	}
	@Override
	public String toString() {
		return "Cou [cid=" + cid + ", name=" + name + ", couId=" + couId + "]";
	}
	
	

	
	
}
