package com.manyi.mall.cachebean.search;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HouseDetailImage  implements Serializable{
	  private String type;// 类型：卧室bedroom、客厅livingRoom、卫生间wc、厨房kitchen、阳台balcony、厨房kitchen、邮箱mailBox、邮箱或电梯mailBox_or_elevatorRoom、电梯间elevatorRoom、门牌doorPlate、外景exterior
	  private String description;// 描述 主卧、次卧1、次卧2、门牌1、阳台、外景、、、EntityUtils.HouseResourceType
	  private String imgKey_fyb;// 房源宝水印大图
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImgKey_fyb() {
		return imgKey_fyb;
	}
	public void setImgKey_fyb(String imgKey_fyb) {
		this.imgKey_fyb = imgKey_fyb;
	}

}
