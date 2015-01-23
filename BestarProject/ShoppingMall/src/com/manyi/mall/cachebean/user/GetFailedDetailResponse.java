package com.manyi.mall.cachebean.user;

import java.io.File;

import org.androidannotations.annotations.EBean;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.huoqiu.framework.jackson.FileDeserializer;
import com.huoqiu.framework.rest.Response;

@EBean
public class GetFailedDetailResponse extends Response {
	private int userId;//用户ID 
	private String realName; 

	private int cityId;//城市ID 
	private String cityName;//城市 

	private int areaId;//区域ID 
	private String areaName;//区域名称 

	private int townId;//所属区域 下一级别 
	private String townName;//所属区域 下一级别 

	private String code;//身份证号码 
	private String spreadName;//邀请码 
	private File cardFile;//名片地址 
	private File codeFile;//身份证照片 
	private String remark;//失败原因
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public int getTownId() {
		return townId;
	}
	public void setTownId(int townId) {
		this.townId = townId;
	}
	public String getTownName() {
		return townName;
	}
	public void setTownName(String townName) {
		this.townName = townName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSpreadName() {
		return spreadName;
	}
	public void setSpreadName(String spreadName) {
		this.spreadName = spreadName;
	}
	@JsonDeserialize(using=FileDeserializer.class)
	public File getCardFile() {
		return cardFile;
	}
	public void setCardFile(File cardFile) {
		this.cardFile = cardFile;
	}
	@JsonDeserialize(using=FileDeserializer.class)
	public File getCodeFile() {
		return codeFile;
	}
	public void setCodeFile(File codeFile) {
		this.codeFile = codeFile;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
