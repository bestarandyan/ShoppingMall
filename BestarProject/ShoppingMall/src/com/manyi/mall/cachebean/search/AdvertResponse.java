/**
 * 
 */
package com.manyi.mall.cachebean.search;

import java.util.List;

import com.huoqiu.framework.rest.Response;

/**
 * @author bestar
 *
 */
public class AdvertResponse extends Response{
	private List<AdvertisingResponse> list;
	
	
	
    public List<AdvertisingResponse> getList() {
		return list;
	}



	public void setList(List<AdvertisingResponse> list) {
		this.list = list;
	}



	public static class AdvertisingResponse extends Response {
        private int id;
        private String title;
        private String content;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
    }
}
