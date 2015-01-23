package com.manyi.mall.cachebean.user;

import java.io.File;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.huoqiu.framework.jackson.FileSerializer;

public class UploadFileRequest {
	private int houseId;
	private int taskId;
	private File file;
	private String photoFolder;// 照片所在目录的结构： HouseResourceType 的 key
	private Date takePhotoTime; // 拍摄照片时间
	private String longitude;// 坐标 经度
	private String latitude;// 坐标 纬度

	public Date getTakePhotoTime() {
		return takePhotoTime;
	}

	public void setTakePhotoTime(Date takePhotoTime) {
		this.takePhotoTime = takePhotoTime;
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

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}

	@JsonSerialize(using = FileSerializer.class)
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getPhotoFolder() {
		return photoFolder;
	}

	public void setPhotoFolder(String photoFolder) {
		this.photoFolder = photoFolder;
	}
}
