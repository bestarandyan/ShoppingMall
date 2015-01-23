/**
 * 
 */
package com.manyi.mall.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.SubEstateResponse;
import com.manyi.mall.cachebean.search.SubRequest;
import com.manyi.mall.cachebean.search.SubResponse;
import com.manyi.mall.cachebean.search.SearchRespose.Estate;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.CommonService;

/**
 * @author bestar
 * 
 */
@EFragment(R.layout.fragment_area_child_dialog)
public class AreaChildDialogFragment extends SuperFragment<Estate> implements OnItemClickListener, OnClickListener {
	@ViewById(R.id.dialogSelectListView)
	ListView mDialogSelectListView;

	@ViewById(R.id.dialogSelectBackBtn)
	TextView mDialogSelectBackBtn;

	CommonService mCommonService;

	public List<Map<String, String>> mDialogListViewList = new ArrayList<Map<String, String>>();
	List<SubEstateResponse> subEstateResponses = null;
	private Estate estate;
	private int estateId = 0;
	private String mEstateName = "";
	private Bundle bundle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@UiThread
	public void notifyDialogListViewAdapter() {
		if (getActivity() == null) {
			return;

		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), mDialogListViewList, R.layout.item_dialog_select,
				new String[] { "name" }, new int[] { R.id.dialog_select_text });
		mDialogSelectListView.setAdapter(simpleAdapter);
	}

	@AfterViews
	public void init() {
		bundle = getArguments();
		if (bundle != null) {
			mEstateName = bundle.getString(Constants.SELECTED_ESTATE_NAME);
			mDialogSelectBackBtn.setText(mEstateName);
			estateId = bundle.getInt(Constants.SELECTED_ESTATE_ID);
		}
		mDialogSelectListView.setOnItemClickListener(this);
		mDialogSelectListView.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.view_list_foot, null));
		mDialogSelectListView.setFooterDividersEnabled(false);
		mDialogSelectBackBtn.setOnClickListener(this);
		getdata();

		// getBackOpActivity().setGoBackListener(new GoBackListener() {
		// @Override
		// public boolean canGoBack() {
		// remove();
		// return false;
		// }
		// });
	}

	@SuppressWarnings("unchecked")
	@Background
	void getdata() {

		if (bundle != null) {
			subEstateResponses = (List<SubEstateResponse>) bundle.getSerializable(Constants.SELECTED_ESTATE_DATA);
			if (subEstateResponses == null) {
				SubRequest subRequest = new SubRequest();
				subRequest.setEstateId(estateId);
				SubResponse subResponse = mCommonService.subResponse(subRequest);
				subEstateResponses = subResponse.getSubEstateList();
			}
		}
		if (subEstateResponses == null) {
			return;
		}

		mDialogListViewList.clear();
		for (int i = 0; i < subEstateResponses.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", subEstateResponses.get(i).getSubEstateName());
			mDialogListViewList.add(map);
		}
		notifyDialogListViewAdapter();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		try {
			estate = new Estate();
			estate.setEstateId(subEstateResponses.get(position).getSubEstateId());
			estate.setEstateName(mDialogListViewList.get(position).get("name"));
			if (getSelectListener() != null) {
				notifySelected(estate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mDialogSelectBackBtn) {
			remove();
		}
	}
}
