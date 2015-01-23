/**
 * 
 */
package com.manyi.mall.cachebean.search;

import com.huoqiu.framework.rest.Response;

/**
 * @author bestar
 *
 */
public class SubEstateStateResponse extends Response{
	public static final int NOTLOCKED = 1; //锁定状态： 没有锁定
	public static final int LOCKED = 2; //锁定状态： 锁定不是独栋
	public static final int LOCKEDANDDUDONG = 3; //锁定状态：   锁定是独栋
    private int lockStatus; //锁定状态： 1 没有锁定，2 锁定不是独栋， 3 锁定是独栋


	public int getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(int lockStatus) {
		this.lockStatus = lockStatus;
	}
    
    
}
