package com.manyi.mall.cachebean.user;

import java.math.BigDecimal;

public class HouseRequest {
	private String serialCode;// 区域serialCode

	private int areaId;// 区域areaId

	private int bedroomSum;// 房型(几房)
	private BigDecimal startPrice;// 起始价格
	private BigDecimal endPrice;// 截止价格
	private BigDecimal startSpaceArea;// 起始内空面积
	private BigDecimal endSpaceArea;// 截止内空面积

	private int start;// 数据起始下标
	private int max;// 返回的数据量(返回几条记录)

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	public int getBedroomSum() {
		return bedroomSum;
	}

	public void setBedroomSum(int bedroomSum) {
		this.bedroomSum = bedroomSum;
	}

	public BigDecimal getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(BigDecimal startPrice) {
		this.startPrice = startPrice;
	}

	public BigDecimal getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(BigDecimal endPrice) {
		this.endPrice = endPrice;
	}

	public BigDecimal getStartSpaceArea() {
		return startSpaceArea;
	}

	public void setStartSpaceArea(BigDecimal startSpaceArea) {
		this.startSpaceArea = startSpaceArea;
	}

	public BigDecimal getEndSpaceArea() {
		return endSpaceArea;
	}

	public void setEndSpaceArea(BigDecimal endSpaceArea) {
		this.endSpaceArea = endSpaceArea;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public String getSerialCode() {
		return serialCode;
	}

	public void setSerialCode(String serialCode) {
		this.serialCode = serialCode;
	}
}
