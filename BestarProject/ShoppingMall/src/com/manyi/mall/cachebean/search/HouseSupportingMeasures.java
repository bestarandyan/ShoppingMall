/**
 * 
 */
package com.manyi.mall.cachebean.search;

import java.io.Serializable;

/**
 * @author bestar
 * 
 */
@SuppressWarnings("serial")
public class HouseSupportingMeasures implements Serializable {
	// 基础配置 //是否含有 true 1 是； false 0 否
	private boolean hasTV; // 电视
	private boolean hasRefrigerator;// 冰箱
	private boolean hasWashingMachine;// 洗衣机
	private boolean hasAirConditioner;// 空调
	private boolean hasWaterHeater;// 热水器
	private boolean hasBed;// 床
	private boolean hasSofa;// 沙发
	private boolean hasBathtub;// 浴缸/淋浴

	// 高端配置 //是否含有 true 1 是； false 0 否
	private boolean hasCentralHeating;// 暖气/地暖
	private boolean hasCentralAirConditioning;// 中央空调
	private boolean hasCloakroom;// 衣帽间
	private boolean hasReservedParking;// 车位
	private boolean hasCourtyard;// 院落
	private boolean hasGazebo;// 露台
	private boolean hasPenthouse;// 阁楼

	public boolean isHasTV() {
		return hasTV;
	}

	public void setHasTV(boolean hasTV) {
		this.hasTV = hasTV;
	}

	public boolean isHasRefrigerator() {
		return hasRefrigerator;
	}

	public void setHasRefrigerator(boolean hasRefrigerator) {
		this.hasRefrigerator = hasRefrigerator;
	}

	public boolean isHasWashingMachine() {
		return hasWashingMachine;
	}

	public void setHasWashingMachine(boolean hasWashingMachine) {
		this.hasWashingMachine = hasWashingMachine;
	}

	public boolean isHasAirConditioner() {
		return hasAirConditioner;
	}

	public void setHasAirConditioner(boolean hasAirConditioner) {
		this.hasAirConditioner = hasAirConditioner;
	}

	public boolean isHasWaterHeater() {
		return hasWaterHeater;
	}

	public void setHasWaterHeater(boolean hasWaterHeater) {
		this.hasWaterHeater = hasWaterHeater;
	}

	public boolean isHasBed() {
		return hasBed;
	}

	public void setHasBed(boolean hasBed) {
		this.hasBed = hasBed;
	}

	public boolean isHasSofa() {
		return hasSofa;
	}

	public void setHasSofa(boolean hasSofa) {
		this.hasSofa = hasSofa;
	}

	public boolean isHasBathtub() {
		return hasBathtub;
	}

	public void setHasBathtub(boolean hasBathtub) {
		this.hasBathtub = hasBathtub;
	}

	public boolean isHasCentralHeating() {
		return hasCentralHeating;
	}

	public void setHasCentralHeating(boolean hasCentralHeating) {
		this.hasCentralHeating = hasCentralHeating;
	}

	public boolean isHasCentralAirConditioning() {
		return hasCentralAirConditioning;
	}

	public void setHasCentralAirConditioning(boolean hasCentralAirConditioning) {
		this.hasCentralAirConditioning = hasCentralAirConditioning;
	}

	public boolean isHasCloakroom() {
		return hasCloakroom;
	}

	public void setHasCloakroom(boolean hasCloakroom) {
		this.hasCloakroom = hasCloakroom;
	}

	public boolean isHasReservedParking() {
		return hasReservedParking;
	}

	public void setHasReservedParking(boolean hasReservedParking) {
		this.hasReservedParking = hasReservedParking;
	}

	public boolean isHasCourtyard() {
		return hasCourtyard;
	}

	public void setHasCourtyard(boolean hasCourtyard) {
		this.hasCourtyard = hasCourtyard;
	}

	public boolean isHasGazebo() {
		return hasGazebo;
	}

	public void setHasGazebo(boolean hasGazebo) {
		this.hasGazebo = hasGazebo;
	}

	public boolean isHasPenthouse() {
		return hasPenthouse;
	}

	public void setHasPenthouse(boolean hasPenthouse) {
		this.hasPenthouse = hasPenthouse;
	}

}
