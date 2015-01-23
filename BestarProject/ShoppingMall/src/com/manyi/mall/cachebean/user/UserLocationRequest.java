/**
 * 
 */
package com.manyi.mall.cachebean.user;

/**
 * @author bestar
 *
 */
public class UserLocationRequest {
	 private Integer userId;// 用户id

     private String longitude;// 坐标 经度

     private String latitude;// 坐标 纬度

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

}
