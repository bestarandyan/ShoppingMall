package com.manyi.mall.common.location;

import android.location.Address;
import android.location.Location;

import com.baidu.location.BDLocation;

public abstract class LBSListener {
	/**
	 * ��λ�ɹ�
	 * 
	 * @param location
	 */
	public abstract void onLocationSuccess(Location location);

	/**
	 * ��λʧ��
	 */
	public abstract void onLocationFail();

	/**
	 * ������ַ�ɹ�
	 * 
	 * @param address
	 */
	public abstract void onAddressSuccess(Address address);

	/**
	 * ������ַʧ��
	 */
	public abstract void onAddressFail();

	/**
	 * �ٶȵ�ַ����Ԥ���ӿ�
	 * 
	 * @param bDLocation
	 */
	public abstract void onBDReceivePoi(BDLocation bDLocation);
}
