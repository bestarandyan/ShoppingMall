/**
 * 
 */
package com.manyi.mall.common.location;


/**
 * @author bestar
 *
 */
public class ManyiLocation{
	
	public static ManyiLocation application = null;
	public ManyiLocation() {
	}
	
	public static ManyiLocation getInstant(){
		if(application == null){
			application = new ManyiLocation();
		}
		return application;
	}
	
	private double latitude;
	private double longitude;
	private String province;
	private String city;
	private String district;
	private String address;
	public Long currentTime;//
	public float radius;//精度半径
	

	public Long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Long currentTime) {
		this.currentTime = currentTime;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
