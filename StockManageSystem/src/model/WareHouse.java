package model;

public class WareHouse {
	@Override
	public String toString() {
		return "WareHouse [wId=" + wid + ", wName=" + Name + ", description=" + description + "]";
	}
	private int wid;
	private String Name;
	private String description;
	private String wareid;
	
	public String getWareid() {
		return wareid;
	}
	public void setWareid(String wareid) {
		this.wareid = wareid;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getWid() {
		return wid;
	}
	public void setWid(int wId) {
		this.wid = wId;
	}
	public String getName() {
		return Name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
