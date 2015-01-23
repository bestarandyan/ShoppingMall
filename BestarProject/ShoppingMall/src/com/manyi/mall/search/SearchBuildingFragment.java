package com.manyi.mall.search;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.BuildingResponse;
import com.manyi.mall.cachebean.search.GetBuildingListRequest;
import com.manyi.mall.cachebean.search.GetBuildingListResponse;
import com.manyi.mall.cachebean.search.SearchBuildingListResponse;
import com.manyi.mall.cachebean.search.SearchBuildingListResquest;
import com.manyi.mall.service.CommonService;

@EFragment(R.layout.fragment_search_building)
public class SearchBuildingFragment extends SuperFragment<Object>{
	public CommonService mCommonService;
	
	@ViewById(R.id.search_living_area_layout)
	public RelativeLayout mFragmentLayout;

	@ViewById(R.id.search_living_area_name)
	public EditText mSearchKey;

	@ViewById(R.id.search_living_area_clear)
	public LinearLayout mClear;

	@ViewById(R.id.search_living_area_list)
	public ListView mList;

	@ViewById(R.id.search_living_area_nodata)
	public TextView mNoData;

	@ViewById(R.id.search_living_progressbar)
	public ProgressBar mProgressBar;

	@FragmentArg
	int subEstateId;
	
	GetBuildingListResponse mResponse;

	private TextWatcher mSearchTextWatcher;
	private SearchBuildingRemoteAdapter mRemoteAdapter;

	@Click(R.id.search_by_condition_back)
	void back() {
		remove();
	}
	private long lastSeatchTime = 0;
	
	private String lastSearchString = "";
	@AfterViews
	public void init() {
		getBuildingList();

		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				processItemClick(parent, position, id);
			}
		});

		mClear.setVisibility(View.GONE);
		mSearchTextWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (mSearchKey.getText().toString().trim().length() > 0) {
					Log.d("bestar", s.toString());
					mClear.setVisibility(View.VISIBLE);
					lastSeatchTime = System.currentTimeMillis();
					mSearchKey.postDelayed(runnable,300);
					mProgressBar.setVisibility(View.VISIBLE);
				} else {
					mClear.setVisibility(View.GONE);
					mProgressBar.setVisibility(View.VISIBLE);
//					updateDataFromRemote(mResponse.getBuildingList());
					getBuildingList();
					
				}
			}
		};
		mSearchKey.addTextChangedListener(mSearchTextWatcher);
        addAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ManyiUtils.showKeyBoard(getActivity(),mSearchKey);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
	}

	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastSeatchTime >= 300) {
				searchBuildingList(mSearchKey.getText().toString());
			}
		}
	}; 

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(lastSearchString.equals(mSearchKey.getText().toString())){
				searchBuildingList(mSearchKey.getText().toString());
				mProgressBar.setVisibility(View.VISIBLE);
			}
		};
	};

	@UiThread
	public void updateDataFromRemote(List<BuildingResponse> buildings) {
		mProgressBar.setVisibility(View.GONE);
		if (getActivity() == null || buildings == null) {
			return;
		}
		if (mList.getFooterViewsCount() == 0 && buildings.size() >= 25) {
			TextView footView = new TextView(getActivity());
			footView.setText("最多显示25条楼栋号，请输入楼栋号查找");
			footView.setTextColor(Color.parseColor("#8a000000"));
			footView.setTextSize(14);
			footView.setGravity(Gravity.CENTER);
			footView.setPadding(10, 10, 10, 10);
			mList.addFooterView(footView);
			mList.setFooterDividersEnabled(false);
		}
		mRemoteAdapter = new SearchBuildingRemoteAdapter(getActivity(), R.layout.item_search_building, buildings,mSearchKey.getText().toString());
		mList.setAdapter(mRemoteAdapter);
		
		
		if (buildings != null && buildings.size() > 0) {
			updateViews(true, true);
		} else {
			updateViews(false, true);
		}
	}
	@Background
	void getBuildingList(){
		GetBuildingListRequest request = new GetBuildingListRequest();
		request.setSubEstateId(subEstateId);
		mResponse =mCommonService.getBuildingList(request);
		if(mResponse!=null && mResponse.getErrorCode() == 0){
			updateDataFromRemote(mResponse.getBuildingList());
		}
	}
	
	@Background
	void searchBuildingList(String searchString){
		try {
			SearchBuildingListResquest request = new SearchBuildingListResquest();
			request.setBuildingName(searchString);
			request.setSubEstateId(subEstateId);
			SearchBuildingListResponse response =mCommonService.searchBuildingList(request);
			if(response!=null && response.getErrorCode() == 0){
				updateDataFromRemote(response.getBuildingList());
			}else {
				updateDataFromRemote(null);
			}
		} catch (Exception e) {
			Log.d("bestar", e.getMessage());
			if(!e.getMessage().contains("Repeated request")){
				setProgressState(View.GONE);
			}
			throw e;
		}
		
	}
	
	@UiThread
	void setProgressState(int state){
		if(mProgressBar!=null){
			mProgressBar.setVisibility(state);
		}
	}

	@Click(R.id.search_living_area_clear)
	public void clearSearchText() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		mSearchKey.setText("");
	}


	private void processItemClick(AdapterView<?> parent, int position, long id) {
			BuildingResponse buildingResponse = (BuildingResponse) parent.getAdapter().getItem(position);
			if(getSelectListener()!=null){
				notifySelected(buildingResponse);
			}
	}

	private void updateViews(boolean remoteHasData, boolean remoteFirst) {
		if (remoteHasData) {
			mList.setVisibility(View.VISIBLE);
			mNoData.setVisibility(View.GONE);
		} else {
			mNoData.setVisibility(View.VISIBLE);
			mList.setVisibility(View.GONE);
			if (!remoteHasData) {
				mNoData.setText(R.string.search_build_remote_nohistory);
			}
			if (remoteFirst)
				mNoData.setText(R.string.search_build_remote_nohistory);
		}
	}


}
