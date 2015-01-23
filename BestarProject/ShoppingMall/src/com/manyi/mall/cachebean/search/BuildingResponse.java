/**
 * 
 */
package com.manyi.mall.cachebean.search;

/**
 * @author bestar
 *
 */
public class BuildingResponse {
	private String buildingName;//楼栋名称
	private String buildingNameStr;//本土化后显示的楼栋名称
	private String subEstateName;//子划分名称
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getBuildingNameStr() {
		return buildingNameStr;
	}
	public void setBuildingNameStr(String buildingNameStr) {
		this.buildingNameStr = buildingNameStr;
	}
	public String getSubEstateName() {
		return subEstateName;
	}
	public void setSubEstateName(String subEstateName) {
		this.subEstateName = subEstateName;
	}
}
