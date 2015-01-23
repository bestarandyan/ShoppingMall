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
import android.widget.TextView;

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
import com.manyi.mall.cachebean.release.ReleasedSellRequest;
import com.manyi.mall.cachebean.release.RentAndSellReleaseRecordInfoResponse;
import com.manyi.mall.cachebean.user.UserLocationRequest;
import com.manyi.mall.cachebean.user.UserLocationResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.common.location.ManyiLoacationMannger;
import com.manyi.mall.common.location.ManyiLoacationMannger.OnLocationReceivedListener;
import com.manyi.mall.common.location.ManyiLocation;
import com.manyi.mall.service.SellService;
import com.manyi.mall.service.UserLocationService;

@EFragment(R.layout.fragment_released_sell_next)
public class ReleasedSellNextFragment extends SuperFragment<Object>{

	@ViewById(R.id.sell_add)
	TextView mSellOwnerAdd;
	@ViewById(R.id.sell_add_layout)
	public LinearLayout mLayInclude;
	@ViewById(R.id.released_sell_price)
	public EditText mSellPrice;
	@ViewById(R.id.released_sell_area)
	public EditText mSellArea;
	@ViewById(R.id.firstview)
	public View view;
	@ViewById(R.id.bedroomsum)
	Spinner mBedroomSum; 
	@ViewById(R.id.livingroomSum)
	Spinner mLivingroomSum; 
	@ViewById(R.id.wcsum)
	Spinner mWcSum; 
	@FragmentArg
	RentAndSellReleaseRecordInfoResponse mRecordInfoResponse;
	@FragmentArg
	int subEstateId;
	@FragmentArg
	String building;
	@FragmentArg
	String room;
	@FragmentArg
	int houseId;
	@FragmentArg
	String token;
	private List<View> mListIncludeList = new ArrayList<View>();// 储存所有的值
	private ArrayAdapter<String> mBedAdapter, mLivingAdapter, mwcAdapter;
	private static final String[] BED_ROOM_SUM_TYPE = { "    室", "   1室", "   2室", "   3室", "   4室", "   5室", "   6室" };
	private static final String[] LIVING_ROOM_SUM_TYPE = {"    厅","   0厅","   1厅", "   2厅", "   3厅", "   4厅", "   5厅", "   6厅" };
	private static final String[] WC_SUM_TYPE = { "    卫", "   1卫", "   2卫", "   3卫", "   4卫", "   5卫", "   6卫" };
	private int bedroom = 0, livingRoom = 0, wc = 0;
	private EditText mSellOwnerName;
	private PhoneEditTextView mSellOwnerPhoneNumber;
	private SellService mSellService;
	private StringBuffer mOwnerName;
	private StringBuffer mOwnerNumber;
	private UserLocationService userLocationService;
	
	@AfterViews
	public void initSellIncludes() {
		initSpinnerData();

		// 精确到小数点后两位
		mSellArea.addTextChangedListener(SellAreaTextChangedListener);
		if(mRecordInfoResponse != null){
			if (mRecordInfoResponse.getHoustConcatList()!= null) {
				for (int i = 0; i < mRecordInfoResponse.getHoustConcatList().size(); i++) {
					View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_released_sell_include, null);
					mSellOwnerName = (EditText) view.findViewById(R.id.sell_include_name);
					mSellOwnerPhoneNumber = (PhoneEditTextView) view.findViewById(R.id.sell_include_phone);
					String name = mRecordInfoResponse.getHoustConcatList().get(i).getHostName();
					String mobile = mRecordInfoResponse.getHoustConcatList().get(i).getHostMobile();
					mSellOwnerName.setText(name.toString());
					mSellOwnerPhoneNumber.setText(mobile.toString());
					mListIncludeList.add(view);
					mLayInclude.addView(view);	
				}
				
				if (mRecordInfoResponse.getHoustConcatList().size() == 3) {//联系人最多只容许添加3个
					mSellOwnerAdd.setVisibility(View.GONE);
				}else if(mRecordInfoResponse.getHoustConcatList().size() == 0){
					mSellOwnerAdd.setVisibility(View.VISIBLE);
					view.setVisibility(View.VISIBLE);
					mListIncludeList = new ArrayList<View>();
					mListIncludeList.add(view);
				}
			}
			
			BigDecimal sellStr = mRecordInfoResponse.getPrice();
			double sellPriceStr = 0.00;
			if(sellStr != null){
				sellPriceStr = Double.parseDouble(sellStr+"");
			}
			sellPrice = StringUtil.fromatString(sellPriceStr);
			mSellPrice.setText(sellPrice.trim());
			
			BigDecimal areaStr = mRecordInfoResponse.getSpaceArea();
			double spaceAreaStr = 0.00;
			if(sellStr != null){
				spaceAreaStr = Double.parseDouble(areaStr+"");
			}
			String spaceArea = StringUtil.fromatString(spaceAreaStr);
			mSellArea.setText(spaceArea.trim());
		}else{
			view.setVisibility(View.VISIBLE);
			mSellOwnerName = (EditText) view.findViewById(R.id.sell_include_name);
			mSellOwnerPhoneNumber = (PhoneEditTextView) view.findViewById(R.id.sell_include_phone);
			mListIncludeList = new ArrayList<View>();
			mListIncludeList.add(view);
		}
		ManyiLoacationMannger manager = new ManyiLoacationMannger(getActivity(), 0);
		manager.setOnLocationReceivedListener(new OnLocationReceivedListener() {
			
			@Override
			public void onReceiveLocation(ManyiLocation location) {
				if(location!=null){
					try {
						SharedPreferences sharedPreferences = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE);
						Integer userId = sharedPreferences.getInt("uid", 1);
						UserLocationRequest request = new UserLocationRequest();
						request.setLatitude(String.valueOf(location.getLatitude()));
						request.setLongitude(String.valueOf(location.getLongitude()));
						request.setUserId(userId);
						sendLocation(request);
					} catch (Exception e) {
					}
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
                ManyiUtils.showKeyBoard(getActivity(),mSellOwnerName);
            }
 
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
	}
	
	@Background
	void sendLocation(UserLocationRequest request){
		
		try {
			UserLocationResponse response = userLocationService.sendUserLocation(request);
			@SuppressWarnings("unused")
			int error = response.getErrorCode();
		} catch (Exception e) {
			
		}
		
	}
	
	/**
	 * 文本框时间监听
	 * @author bestar
	 */
	TextWatcher SellAreaTextChangedListener = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (!s.equals("") && s.length() > 0) {
				if (String.valueOf(mSellArea.getText().toString().charAt(0)).equals(".")) {
					mSellArea.setText("");
				} else {
					if (s.toString().indexOf(".") > 0) {
						if (s.toString().indexOf(".") != s.toString().lastIndexOf(".")) {
							mSellArea.setText(s.toString().substring(0, s.length() - 1));
							mSellArea.setSelection(mSellArea.length());
						} else {
							String substring = s.toString().substring(s.toString().indexOf(".") + 1, s.toString().length());
							if (substring.length() > 2) {
								mSellArea.setText(s.toString().substring(0, s.length() - 1));
								mSellArea.setSelection(mSellArea.length());
							}
						}
					}
				}
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	private String sellPrice;
	
	/**
	 * 控件初始化
	 * @author bestar
	 */
	@UiThread
	void initSpinnerData() {
		mBedAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, BED_ROOM_SUM_TYPE);
		mBedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mBedroomSum.setAdapter(mBedAdapter);
		if(mRecordInfoResponse!=null){
			mBedroomSum.setSelection(mRecordInfoResponse.getBedroomSum());
		}
		mBedroomSum.setOnItemSelectedListener(new BedroomSumListener());
		mLivingAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, LIVING_ROOM_SUM_TYPE);
		mLivingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mLivingroomSum.setAdapter(mLivingAdapter);
		if(mRecordInfoResponse!=null){
			mLivingroomSum.setSelection(mRecordInfoResponse.getLivingRoomSum()+1);
		}
		mLivingroomSum.setOnItemSelectedListener(new LivingroomSumListener());
		mwcAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, WC_SUM_TYPE);
		mwcAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mWcSum.setAdapter(mwcAdapter);
		if(mRecordInfoResponse!=null){
			mWcSum.setSelection(mRecordInfoResponse.getWcSum());
		}
		mWcSum.setOnItemSelectedListener(new WcSumListener());
	}

	/**
	 * 室的控件监听
	 * @author bestar
	 *
	 */
	class BedroomSumListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			bedroom = arg2;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	
	/**
	 * 厅的事件监听
	 * @author bestar
	 *
	 */
	class LivingroomSumListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				livingRoom = arg2 - 1;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
		
	}
	/**
	 * 卫的控件事件监听
	 * @author bestar
	 *
	 */
	class WcSumListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			wc = arg2;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}

	}
	/**
	 * 添加业主和联系电话
	 * @author bestar
	 */
	@Click(R.id.sell_add)
	public void addSellOwner() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		
		ManyiAnalysis.onEvent(this.getActivity(), "AddOwnerOnSellPublishClick");
		
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_released_sell_include, null);
		mListIncludeList.add(view);
		mLayInclude.addView(view);
		if (mListIncludeList.size() == 3) {//联系人最多只容许添加3个
			mSellOwnerAdd.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 提交发布
	 * @author bestare
	 */
	@Click(R.id.released_submit)
	@Background
	public void sellSubmitClick() {
		
		ManyiAnalysis.onEvent(getActivity(), "SubmmitOnSellPublishClick");
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}

		mOwnerName = new StringBuffer();
		mOwnerNumber = new StringBuffer();
		int i =0;
		for (View view : mListIncludeList) {//拼接联系人
			mListIncludeList.size();
			mSellOwnerName = (EditText) view.findViewById(R.id.sell_include_name);
			mSellOwnerPhoneNumber = (PhoneEditTextView) view.findViewById(R.id.sell_include_phone);
			String name = mSellOwnerName.getText().toString();
			String phone = mSellOwnerPhoneNumber.getTextString();
			if(i==0){
				mOwnerName.append(name);
				mOwnerName.append(",");
				mOwnerNumber.append(phone);
				mOwnerNumber.append(",");
			}else{
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
		if (mOwnerName.length() > 1 ) {
			mOwnerName.deleteCharAt(mOwnerName.length() - 1);// 删除最后一个多了的,
		}
		if (mOwnerNumber.length() > 1 ) {
			mOwnerNumber.deleteCharAt(mOwnerNumber.length() - 1);
		}
		submitRelease(); //发布请求
	}
	
	@UiThread
	public void onReleasedError(String e) {
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());
	}
	
	
	@Background
	void submitRelease(){
		try {
			int uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
			ReleasedSellRequest releasedSellRequest = new ReleasedSellRequest();
			releasedSellRequest.setSubEstateId(subEstateId);
			releasedSellRequest.setBuilding(building);
			releasedSellRequest.setRoom(room);
			releasedSellRequest.setUserId(uid);
			releasedSellRequest.setHostName(mOwnerName.toString());
			releasedSellRequest.setHoustMobile(mOwnerNumber.toString());
			releasedSellRequest.setToken(token);
			BigDecimal bigDecimalPrice = new BigDecimal("".equals(mSellPrice.getText().toString().trim())? "0" : mSellPrice.getText().toString().trim());
			releasedSellRequest.setPrice(bigDecimalPrice);
			releasedSellRequest.setBedroomSum(bedroom);
			releasedSellRequest.setLivingRoomSum(livingRoom);
			releasedSellRequest.setWcSum(wc);
			releasedSellRequest.setSource(1);
			BigDecimal db = new BigDecimal("".equals(mSellArea.getText().toString().trim()) ? "0" : mSellArea.getText().toString().trim());
			db.setScale(2, BigDecimal.ROUND_HALF_UP);
			releasedSellRequest.setSpaceArea(db);
			Response releasedSellResponse = mSellService.releasedSell(releasedSellRequest);
			ManyiUtils.closeKeyBoard(getActivity(), mSellArea);
			releaseSuccess(releasedSellResponse.getMessage());
		} catch (RestException e) {
			onReleasedSellNextError(e.getMessage());
		}
	}

	/**
	 * 发布成功之后的处理
	 * @param message5
	 * @author bestar
	 */
	@UiThread
	public void releaseSuccess(String message) {
		if (this.getActivity() == null || this.getActivity().isFinishing()) {
			return;
		}
		DialogBuilder.showSimpleDialog(message, getBackOpActivity(),new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (CheckDoubleClick.isFastDoubleClick()) {
					return;
				}
				if(getSelectListener()!=null){
					notifySelected(null);
				}else{
					remove();
				}
			}
		});
	}

	/**
	 * 发布失败的返回值
	 * @param e
	 * @author bestar
	 */
	@UiThread
	public void onReleasedSellNextError(String e) {
		if (this.getActivity() == null || this.getActivity().isFinishing()) {
			return;
		}
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());
	}
	
	/**
	 *返回按钮事件
	 *@author bestar
	 */
	@Click(R.id.released_next_sell_back)
	public void releasedSellBack() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		ManyiUtils.closeKeyBoard(getActivity(), mSellArea);
		remove();
	}

}
