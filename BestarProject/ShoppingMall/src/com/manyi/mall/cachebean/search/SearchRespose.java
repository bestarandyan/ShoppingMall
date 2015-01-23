package com.manyi.mall.cachebean.search;

import java.io.Serializable;
import java.util.List;

import com.huoqiu.framework.rest.Response;

public class SearchRespose extends Response {

	private List<Estate> estateList;

	public List<Estate> getEstateList() {
		return estateList;
	}

	public void setEstateList(List<Estate> estateList) {
		this.estateList = estateList;
	}

	public static class Estate implements Serializable {

		private static final long serialVersionUID = 1L;
		private int estateId;
		private String estateName;// 小区名称

		private String[] aliasName;// 别名

		private String cityName;// 区域 名称
		private String estateNameStr;//拼接后的小区名称  1.8新增参数

		private String townName;// 板块 名称
        private int hot;// 热点： 1 是，0 不是

        /**
		 * @return the estateNameStr
		 */
		public String getEstateNameStr() {
			return estateNameStr;
		}

		/**
		 * @param estateNameStr the estateNameStr to set
		 */
		public void setEstateNameStr(String estateNameStr) {
			this.estateNameStr = estateNameStr;
		}


		public int getHot() {
            return hot;
        }

        public void setHot(int hot) {
            this.hot = hot;
        }

        public String getEstateName() {
			return estateName;
		}

		public void setEstateName(String estateName) {
			this.estateName = estateName;
		}

		public String[] getAliasName() {
			return aliasName;
		}

		public void setAliasName(String[] aliasName) {
			this.aliasName = aliasName;
		}

		public String getCityName() {
			return cityName;
		}

		public void setCityName(String cityName) {
			this.cityName = cityName;
		}

		public String getTownName() {
			return townName;
		}

		public void setTownName(String townName) {
			this.townName = townName;
		}

		private List<SubEstateResponse> subEstatelList;

		public List<SubEstateResponse> getSubEstatelList() {
			return subEstatelList;
		}

		public void setSubEstatelList(List<SubEstateResponse> subEstatelList) {
			this.subEstatelList = subEstatelList;
		}

		public int getEstateId() {
			return estateId;
		}

		public void setEstateId(int estateId) {
			this.estateId = estateId;
		}

	}
}
