package com.manyi.mall.user;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.AreasResponse.AreaResponse;
import com.manyi.mall.cachebean.user.AddEstateRequest;
import com.manyi.mall.common.Constants;
import com.manyi.mall.search.AddEstateAreaFragment;
import com.manyi.mall.search.SearchAreaFragment;
import com.manyi.mall.service.UcService;

@EFragment(R.layout.fragment_add_estate)
public class AddEstateFragment extends SuperFragment<Object> {
	private boolean mHasInit = false;

	
	private UcService service;
	private String aeraName;
	private int estateId;
	
	@ViewById
	EditText get_search_name, address;
	@ViewById
	TextView areapartname;

	@FragmentArg
	public String label;

	@Click(R.id.addnew)
	void cheknull() {
		if (get_search_name.getText().toString().length() != 0 && address.getText().toString().length() != 0) {
			submit();

		} else if (get_search_name.getText().toString().length() == 0) {
			adderror("请输入小区名");
		} else if (address.getText().toString().length() == 0) {
			adderror("请输入地址");
		}
	}

	@Background
	void submit() {
		try {
			AddEstateRequest req = new AddEstateRequest();
			int uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
			req.setAddress(address.getText().toString());
			req.setEsateName(get_search_name.getText().toString());
			req.setEstateId(estateId);
			req.setUid(uid);
			service.addEstate(req);
			addnewSucess();
		} catch (RestException e) {
			adderror(e.getMessage());
		}
	}

	@UiThread
	void addnewSucess() {
		LinearLayout layout = new LinearLayout(getActivity());

		TextView tv = new TextView(getActivity());
		TextView tv2 = new TextView(getActivity());
		tv2.setText("");
		tv.setText("添加成功正在审核中");
		tv.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams pm = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		pm.gravity = Gravity.CENTER | Gravity.CENTER_VERTICAL;
		// pm.topMargin = tv.getCompoundPaddingTop();.
		layout.addView(tv2, pm);
		layout.addView(tv, pm);

		layout.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
		new AlertDialog.Builder(getActivity())

		.setView(layout).setPositiveButton("我知道了", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				remove();
				ManyiUtils.closeKeyBoard(getActivity(), get_search_name);

			}
		})

		.show();

	}

	@UiThread
	void adderror(String e) {
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());
	}

	@AfterViews
	public void init() {
		get_search_name.setText(label);
		if (!mHasInit) {
			get_search_name.requestFocus();
			ManyiUtils.showKeyBoard(getActivity(), get_search_name);
			mHasInit = true;
		} else {
			ManyiUtils.closeKeyBoard(getActivity(), get_search_name);
			get_search_name.clearFocus();
		}
	}

	@UiThread
	@Click(R.id.areapartname)
	void areapartname() {
		final AddEstateAreaFragment areaFragment3 = GeneratedClassUtils.getInstance(AddEstateAreaFragment.class);
		areaFragment3.tag = SearchAreaFragment.class.getName();
		Bundle bundle = new Bundle();
		bundle.putBoolean("NewHEF", true);
		areaFragment3.setArguments(bundle);
		areaFragment3.setManager(getFragmentManager());
		areaFragment3.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);
		areaFragment3.setSelectListener(new SelectListener<AreaResponse>() {

			@Override
			public void onSelected(AreaResponse t) {
				aeraName = t.getName();
				estateId = t.getAreaId();
				// areaFragment3.remove();
			}

			@Override
			public void onCanceled() {
				Log.i("----onCanceled----->", "onCanceled");
			}

		});
		areaFragment3.show();
		areapartname.requestFocus();

		ManyiUtils.closeKeyBoard(getActivity(), get_search_name);
	}

	@Click(R.id.newhefback)
	void back() {
		ManyiUtils.closeKeyBoard(getActivity(), get_search_name);
		remove();
	}

	@Override
	public void onResume() {
		super.onResume();
		areapartname.setText(aeraName);
	}

	@UiThread
	void finish() {
		remove();

	}
}
