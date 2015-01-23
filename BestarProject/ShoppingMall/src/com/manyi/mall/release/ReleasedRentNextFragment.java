package com.manyi.mall.release;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.ManyiUtils;
import com.huoqiu.framework.util.StringUtil;
import com.huoqiu.widget.PhoneEditTextView;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.release.ReleasedRentRequest;
import com.manyi.mall.cachebean.release.RentAndSellReleaseRecordInfoResponse;
import com.manyi.mall.cachebean.user.ReleasedSaleRollInfo;
import com.manyi.mall.cachebean.user.UserLocationRequest;
import com.manyi.mall.cachebean.user.UserLocationResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.common.location.ManyiLoacationMannger;
import com.manyi.mall.common.location.ManyiLoacationMannger.OnLocationReceivedListener;
import com.manyi.mall.common.location.ManyiLocation;
import com.manyi.mall.service.RentService;
import com.manyi.mall.service.UserLocationService;

@EFragment(R.layout.fragment_released_rent_next)
public class ReleasedRentNextFragment extends SuperFragment<Object> {

//	@ViewById(R.id.rent_add)
//	TextView mRentOwnerAdd;

	@ViewById(R.id.rent_add_layout)
	public LinearLayout mLayInclude;

	@ViewById(R.id.rent_view)
	public View view;
	
	@ViewById(R.id.released_rent_price)
	public EditText mRentPice;

	@ViewById(R.id.released_rent_area)
	public EditText mRentArea;
	
	private ArrayAdapter<String> mBedAdapter, mLivingAdapter, mWcAdapter;
	private static final String[] BEDROOMSUMTYPE = { "    室", "   1室", "   2室", "   3室", "   4室", "   5室", "   6室" };
	private static final String[] LIVINGROOMSUMTYPE = { "    厅", "   0厅", "   1厅", "   2厅", "   3厅", "   4厅", "   5厅", "   6厅" };
	private static final String[] WCSUMTYPE = { "    卫", "   1卫", "   2卫", "   3卫", "   4卫", "   5卫", "   6卫" };
	private int mBedroom, mLivingRoom, mWc;

	public ReleasedSaleRollInfo mReleasedSaleRollInfo;

	@ViewById(R.id.bedroomsum)
	Spinner mBedroomSum;

	@ViewById(R.id.livingroomSum)
	Spinner mLivingroomSum;

	@ViewById(R.id.wcsum)
	Spinner mWcSum;

	public EditText mRentOwnerName;
	public PhoneEditTextView mRentOwnerPhoneNumber;
	private List<View> mListIncludeList = new ArrayList<View>();// 储存所有的值
	private RentService mRentService;
	private StringBuffer mOwnerName;
	private StringBuffer mOwnerNumber;
	private UserLocationService userLocationService;
	
	@FragmentArg
	RentAndSellReleaseRecordInfoResponse mRecordInfoResponse;
	@FragmentArg
	String token;
	@FragmentArg
	int houseId;


	@AfterViews
	public void initSellIncludes() {
//		mListIncludeList.add(view);
		addSpinner();
		// 精确到小数点后两位
		mRentArea.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (!s.equals("") && s.length() > 0) {
					if (String.valueOf(mRentArea.getText().toString().charAt(0)).equals(".")) {
						mRentArea.setText("");
					} else {
						if (s.toString().indexOf(".") > 0) {
							if (s.toString().indexOf(".") != s.toString().lastIndexOf(".")) {
								mRentArea.setText(s.toString().substring(0, s.length() - 1));
								mRentArea.setSelection(mRentArea.length());
							} else {
								String substring = s.toString().substring(s.toString().indexOf(".") + 1, s.toString().length());
								if (substring.length() > 2) {
									mRentArea.setText(s.toString().substring(0, s.length() - 1));
									mRentArea.setSelection(mRentArea.length());
								}
							}
						}
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		
	
		if(mRecordInfoResponse != null){//重新发布的逻辑入口
			if (mRecordInfoResponse.getHoustConcatList()!= null) {
				for (int i = 0; i < mRecordInfoResponse.getHoustConcatList().size(); i++) {
					View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_released_rent_include, null);
					mRentOwnerName = (EditText) view.findViewById(R.id.include_rent_name);
					mRentOwnerPhoneNumber = (PhoneEditTextView) view.findViewById(R.id.include_rent_phone);
					String name = mRecordInfoResponse.getHoustConcatList().get(i).getHostName();
					String mobile = mRecordInfoResponse.getHoustConcatList().get(i).getHostMobile();
					mRentOwnerName.setText(name.toString());
					mRentOwnerPhoneNumber.setText(mobile.toString());
					mListIncludeList.add(view);
					mLayInclude.addView(view);	
				}
				
				if (mRecordInfoResponse.getHoustConcatList().size() == 3) {//联系人最多只容许添加3个
//					mRentOwnerAdd.setVisibility(View.GONE);
				}else if(mRecordInfoResponse.getHoustConcatList().size() == 0){
					view.setVisibility(View.VISIBLE);
					mListIncludeList = new ArrayList<View>();
					mListIncludeList.add(view);
				}
			}
			BigDecimal rentStr = mRecordInfoResponse.getPrice();
			double rentPriceStr = 0.00;
			if(rentStr != null){
				rentPriceStr = Double.parseDouble(rentStr+"");
			}
			String rentPrice = StringUtil.fromatString(rentPriceStr);
			mRentPice.setText(rentPrice.trim());
			
			BigDecimal areaStr = mRecordInfoResponse.getSpaceArea();
			double spaceAreaStr = 0.00;
			if(rentStr != null){
				spaceAreaStr = Double.parseDouble(areaStr+"");
			}
			String spaceArea = StringUtil.fromatString(spaceAreaStr);
			mRentArea.setText(spaceArea.trim());
		}else{
			view.setVisibility(View.VISIBLE);
			mRentOwnerName = (EditText) view.findViewById(R.id.include_rent_name);
			mRentOwnerPhoneNumber = (PhoneEditTextView) view.findViewById(R.id.include_rent_phone);
			mListIncludeList = new ArrayList<View>();
			mListIncludeList.add(view);
			
		}
		ManyiLoacationMannger manager = new ManyiLoacationMannger(getActivity(), 0);
		manager.setOnLocationReceivedListener(new OnLocationReceivedListener() {
			
			@Override
			public void onReceiveLocation(ManyiLocation location) {
				if(location!=null){
					SharedPreferences sharedPreferences = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE);
					Integer userId = sharedPreferences.getInt("uid", 1);
					UserLocationRequest request = new UserLocationRequest();
					request.setLatitude(String.valueOf(location.getLatitude()));
					request.setLongitude(String.valueOf(location.getLongitude()));
					request.setUserId(userId);
					sendLocation(request);
				}
			}

			@Override
			public void onFailedLocation(String msg) {
				// TODO Auto-generated method stub
				
			}
		});
		manager.start();
		
		addAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
 
            }
 
            @Override
            public void onAnimationEnd(Animation animation) {
                ManyiUtils.showKeyBoard(getActivity(),mRentOwnerName);
            }
 
            @Override
            public void onAnimationRepeat(Animation animation) {
 
            }
        });
	}
	
	@SuppressWarnings("unused")
	@Background
	void sendLocation(UserLocationRequest request){
		try {
			UserLocationResponse response = userLocationService.sendUserLocation(request);
			int error = response.getErrorCode();
		} catch (Exception e) {
			
		}

	}
	

	@UiThread
	void addSpinner() {
		mBedAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, BEDROOMSUMTYPE);
		mBedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mBedroomSum.setAdapter(mBedAdapter);
		if(mRecordInfoResponse!=null){
			mBedroomSum.setSelection(mRecordInfoResponse.getBedroomSum());
		}
		mBedroomSum.setOnItemSelectedListener(new bedroomSumListener());
		mLivingAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, LIVINGROOMSUMTYPE);
		mLivingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mLivingroomSum.setAdapter(mLivingAdapter);
		if(mRecordInfoResponse!=null){
			mLivingroomSum.setSelection(mRecordInfoResponse.getLivingRoomSum()+1);
		}
		mLivingroomSum.setOnItemSelectedListener(new livingroomSumListener());
		mWcAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, WCSUMTYPE);
		mWcAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mWcSum.setAdapter(mWcAdapter);
		if(mRecordInfoResponse!=null){
			mWcSum.setSelection(mRecordInfoResponse.getWcSum());
		}
		mWcSum.setOnItemSelectedListener(new wcSumListener());
	}

	/**
	 * 暂时关闭 增加联系人
	@Click(R.id.rent_add)
	public void addSellOwner() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}

		ManyiAnalysis.onEvent(this.getActivity(), "AddOwnerOnRentPublishClick");

		View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_released_rent_include, null);
		mListIncludeList.add(view);
		mLayInclude.addView(view);
		if (mListIncludeList.size() == 3) {
			mRentOwnerAdd.setVisibility(View.GONE);
		}
	} */

	@Click(R.id.released_rent_sumbit)
	public void sellSubmitClick() {
		ManyiAnalysis.onEvent(getActivity(), "SubmmitOnRentPublishClick");

		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
			mOwnerName = new StringBuffer();
			mOwnerNumber = new StringBuffer();

			int i = 0;
			for (View view : mListIncludeList) {
				mListIncludeList.size();
				mRentOwnerName = (EditText) view.findViewById(R.id.include_rent_name);
				mRentOwnerPhoneNumber = (PhoneEditTextView) view.findViewById(R.id.include_rent_phone);
				String name = mRentOwnerName.getText().toString();
				String phone = mRentOwnerPhoneNumber.getTextString();

				if (i == 0) {
					mOwnerName.append(name);
					mOwnerName.append(",");

					mOwnerNumber.append(phone);
					mOwnerNumber.append(",");

				} else {
					if(!"".equals(name) && !"".equals(phone)){
						mOwnerName.append(name);
						mOwnerName.append(",");
						
						mOwnerNumber.append(phone);
						mOwnerNumber.append(",");
					}else if("".equals(name) && "".equals(phone)){
						
					}else {
						onReleasedError(getString(R.string.release_error));
						return;
					}
				}

				++i;
			}

			if (mOwnerName.length() > 1) {
				mOwnerName.deleteCharAt(mOwnerName.length() - 1);// 删除最后一个多了的,
			}
			if (mOwnerNumber.length() > 1) {
				mOwnerNumber.deleteCharAt(mOwnerNumber.length() - 1);
			}
			
			submitRelease();//提交发布请求
	}
	
	@UiThread
	public void onReleasedError(String e) {
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());
	}

	
	@Background
	void submitRelease(){
		try{
			int uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
			ReleasedRentRequest releasedRentRequest = new ReleasedRentRequest();
			releasedRentRequest.setHouseId(houseId);
			releasedRentRequest.setUserId(uid);
			releasedRentRequest.setHostName(mOwnerName.toString());
			releasedRentRequest.setHoustMobile(mOwnerNumber.toString());
	
		
			BigDecimal bigDecimalPrice = new BigDecimal("".equals(mRentPice.getText().toString().trim()) ? "0" : mRentPice.getText().toString().trim());
			releasedRentRequest.setPrice(bigDecimalPrice);
			
			releasedRentRequest.setBedroomSum(mBedroom);
			releasedRentRequest.setLivingRoomSum(mLivingRoom);
			releasedRentRequest.setWcSum(mWc);
			releasedRentRequest.setSource(1);
			releasedRentRequest.setToken(token);
			
			BigDecimal db = new BigDecimal("".equals(mRentArea.getText().toString().trim()) ? "0" : mRentArea.getText().toString().trim());
			releasedRentRequest.setSpaceArea(db);
			Response releasedRentResponse = mRentService.releasedRent(releasedRentRequest);
			ManyiUtils.closeKeyBoard(getActivity(), mRentArea);
			rentSuccess(releasedRentResponse.getMessage());

	} catch (RestException e) {
		onReleasedRentNextError(e.getMessage());
	}
	}

	@UiThread
	public void rentSuccess(String message) {
		ManyiUtils.closeKeyBoard(getActivity(), mRentArea);
		DialogBuilder.showSimpleDialog(message, getBackOpActivity(), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (CheckDoubleClick.isFastDoubleClick()) {
					return;
				}
				if (getSelectListener() != null) {
					notifySelected(null);
				}else{
					remove();
				}
			}
		});
	}

	class bedroomSumListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			mBedroom = arg2;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	class livingroomSumListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			mLivingRoom = arg2 - 1;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class wcSumListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			mWc = arg2;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	@UiThread
	public void onReleasedRentNextError(String e) {
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());
	}

	@Click(R.id.released_next_rent_back)
	public void releasedSellBack() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		ManyiUtils.closeKeyBoard(getActivity(), mRentArea);
		remove();
	}
}
