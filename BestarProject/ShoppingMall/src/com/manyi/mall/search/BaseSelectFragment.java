/**
 * 
 */
package com.manyi.mall.search;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.R;
import com.manyi.mall.common.util.ItemContent;
import com.manyi.mall.service.CommonService;

/**
 * @author bestar
 *
 */
@EFragment(R.layout.fragment_select_base)
public abstract class BaseSelectFragment<T> extends SuperFragment<T>{
	@ViewById(R.id.district_select_back)
	Button mSelectBackBtn;
	
	@ViewById(R.id.select_listview)
	ListView mSelectListView;
	
	protected ArrayList<T> mSelectArrayList = new ArrayList<T>();
	
	protected ArrayList<Map<String,String>> mArrayList = new ArrayList<Map<String,String>>();
	
	private final String CONTENT_TAG = "content";
	
	public CommonService mCommonService;
	
	@AfterViews
	public void init(){
		String titleString = getTitle();
		if(titleString!=null && titleString.trim().length()>0){
			mSelectBackBtn.setText(titleString);
		}
		getData();
	}
	
	@UiThread
	protected void notifyListView(){
		mSelectArrayList = setDataList();
		if(mSelectArrayList!=null){
			ListAdapter adapter = setAdapter();
			if(adapter == null){
				adapter = getAdapter();
			}
			mSelectListView.setAdapter(adapter);
		}
	}
	
	@ItemClick(R.id.select_listview)
	public void onItemClick(int position){
		setOnItemClick(mSelectArrayList.get(position));
	}
	
	public abstract void getData();
	
	public abstract void setOnItemClick(Object response);
	
	public abstract ArrayList<T> setDataList();
	
	public abstract BaseAdapter setAdapter();
	
	public abstract String getTitle();
	
	
	/**
	 * 设置title
	 * @param resid
	 */
	protected void setTitle(int resid){
		mSelectBackBtn.setText(resid);
	}
	
	private ListAdapter getAdapter(){
		mArrayList.clear();
		for (T response : mSelectArrayList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put(CONTENT_TAG, getContent(response));
			mArrayList.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), mArrayList,
				R.layout.fragment_district_parent_item, new String[]{CONTENT_TAG}, new int[]{R.id.district_name});
		return adapter;
	}
	
	private String getContent(Object response){
		String contentString = null;
		Method[] methods = response.getClass().getDeclaredMethods();
		for (Method method : methods) {
			ItemContent itemContent = method.getAnnotation(ItemContent.class);
			if (itemContent != null) {
				try {
					contentString = (String) method.invoke(response, new Object[]{});
				} catch (Exception e) {
					String TAG = BaseSelectFragment.class.getSimpleName();
					Log.e(TAG, e.toString());
				}
				return contentString;
			}
		}
		return contentString;
	}
	
	@Click(R.id.district_select_back)
	void goBack(){
		remove();
	}
}
