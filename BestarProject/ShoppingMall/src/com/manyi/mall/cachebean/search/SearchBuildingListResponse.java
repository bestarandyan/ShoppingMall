/**
 * 
 */
package com.manyi.mall.cachebean.search;

import java.util.List;

import com.huoqiu.framework.rest.Response;

/**
 * @author bestar
 *
 */
public class SearchBuildingListResponse extends Response{
	private List<BuildingResponse> buildingList;

	public List<BuildingResponse> getBuildingList() {
		return buildingList;
	}

	public void setBuildingList(List<BuildingResponse> buildingList) {
		this.buildingList = buildingList;
	}

	
	
}
