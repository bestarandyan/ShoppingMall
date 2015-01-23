package com.manyi.mall.cachebean.user;


import java.io.File;

import org.androidannotations.annotations.EBean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.huoqiu.framework.jackson.FileSerializer;


/**
 * 从新注册信息 填充数据依赖对象
 * @author huxin
 * 
 */
@EBean
public class RegisterAgainRequest {
	private int userId; 
	private String spreadName;//推广人推广码 
	private String realName;//身份证姓名 
	private String code;//身份证号码 
	private File cardFile; 
	private File codeFile; 
	private int areaId ;//所属区域 
	private int cityId;//城市ID
	private int townId;// 所属区域 下一级别 
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getSpreadName() {
		return spreadName;
	}
	public void setSpreadName(String spreadName) {
		this.spreadName = spreadName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@JsonSerialize(using=FileSerializer.class)
	public File getCardFile() {
		return cardFile;
	}
	public void setCardFile(File cardFile) {
		this.cardFile = cardFile;
	}
	
	@JsonSerialize(using=FileSerializer.class)
	public File getCodeFile() {
		return codeFile;
	}
	public void setCodeFile(File codeFile) {
		this.codeFile = codeFile;
	}
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getTownId() {
		return townId;
	}
	public void setTownId(int townId) {
		this.townId = townId;
	}
}