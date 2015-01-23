package com.manyi.mall.cachebean.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.huoqiu.framework.rest.Response;
import com.manyi.mall.common.util.ItemContent;

public class AreasResponse extends Response implements Serializable {
	// errorcode
	/*
	 * 0:获取列表成功 areaList:小区列表集合 areaId:区域ID parentId:区域父ID name:区域名称
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<AreaResponse> areaList = new ArrayList<AreaResponse>();

	public List<AreaResponse> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<AreaResponse> areaList) {
		this.areaList = areaList;
	}

	public static class AreaResponse implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int areaId;
		private String name;
		private boolean flag;// 是否子节点 ，true,是；false否
        private int hot;// 热点： 1 是，0 不是

        public int getHot() {
            return hot;
        }

        public void setHot(int hot) {
            this.hot = hot;
        }

        public int getAreaId() {
			return areaId;
		}

		public void setAreaId(int areaId) {
			this.areaId = areaId;
		}
		
		@ItemContent
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isFlag() {
			return flag;
		}

		public void setFlag(boolean flag) {
			this.flag = flag;
		}

	}

}
