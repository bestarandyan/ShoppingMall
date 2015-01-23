package com.manyi.mall.user;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.huoqiu.framework.backstack.AbsOp;
import com.huoqiu.framework.backstack.BackOpFragmentActivity;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.AdvancedBitmapUtils;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.FileUtils;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.huoqiu.framework.util.StringUtil;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.AreaAndTownMessage;
import com.manyi.mall.cachebean.search.City;
import com.manyi.mall.cachebean.user.RegistNextRequest;
import com.manyi.mall.cachebean.user.RegisterAgainRequest;
import com.manyi.mall.cachebean.user.UserLocationRequest;
import com.manyi.mall.common.CommonConfig;
import com.manyi.mall.common.Constants;
import com.manyi.mall.common.location.LBSHelper;
import com.manyi.mall.common.location.ManyiLoacationMannger;
import com.manyi.mall.common.location.ManyiLoacationMannger.OnLocationReceivedListener;
import com.manyi.mall.common.location.ManyiLocation;
import com.manyi.mall.common.util.AESUtil;
import com.manyi.mall.search.CityBaseFragment;
import com.manyi.mall.service.UcService;
import com.manyi.mall.service.UserLocationService;
import com.manyi.mall.widget.touchview.ImageLoaderConfig;

@EFragment(R.layout.fragment_register_next)
public class RegisterNextFragment extends ImageLoaderFragment {
	public static final int TAKE_CODE_PHOTO = 1003;
	public static final int TAKE_CARD_PHOTO = 1004;

	@ViewById(R.id.tv_rephoto_id_card)
	public TextView mCodeCameratTextView; // 身份证拍照
	@ViewById(R.id.tv_rephoto_bs_card)
	public TextView mCardCameraTextView; // 名片拍照
	@ViewById(R.id.rl_img_id_card)
	public RelativeLayout mCodeCameraLayout;
	@ViewById(R.id.rl_img_bs_card)
	public RelativeLayout mCardCameraLayout;
	@ViewById(R.id.real_name)
	public EditText mRealNameEditText; // 真实姓名
	@ViewById(R.id.id_number)
	public EditText mIdNumberEditText; // 身份证号码
	@ViewById(R.id.invite_code)
	public EditText mInviteCodeEditText; // 邀请码

	@ViewById(R.id.img_id_card)
	// 身份证Image
	public ImageView mCodeImageView;

	@ViewById(R.id.img_bs_card)
	// 名片Image
	public ImageView mCardImageView;

	@ViewById(R.id.work_area_select)
	Button mSelectAreaButton; // 区域选择
	@ViewById(R.id.ed_local_city)
	Button mCityNameButton; // 城市选择
	@ViewById(R.id.btn_register)
	Button mRegisterButton;
	@FragmentArg
	public String mPhoneNumber; // 电话号码
	@FragmentArg
	public String mSmsCaptcha; // 验证码
	@FragmentArg
	public String mRegisterPwd; // 密码
	@FragmentArg
	public String mConfirmPwd; // 确认密码

	// 审核失败 重新注册 回填信息

	@FragmentArg
	public String mRealName;
	@FragmentArg
	public int mCityId;// 城市ID
	@FragmentArg
	public String mCityName;// 城市
	@FragmentArg
	public int mAreaId;// 区域ID
	@FragmentArg
	public String mAreaName;// 区域名称
	@FragmentArg
	public int mTownId;// 区域ID
	@FragmentArg
	public String mTownName;// 区域名称
	@FragmentArg
	public String mCode;// 身份证号码
	@FragmentArg
	public String mSpreadName;// 邀请码
	@FragmentArg
	public String mRemark;// 失败原因
	@FragmentArg
	public int mUserId; // 用户ID
	@FragmentArg
	public boolean isRegisterAgain; // 是否是重新注册

	private AreaAndTownMessage mAreaAndTownMessage;

	private UcService mUserService;

	@FragmentArg
	public File mCodeFile; // 身份证上传文件
	@FragmentArg
	public File mCardFile; // 名片上传文件

	private City mCity;
	public LocationClient mLocationClient = null;
	private boolean isFirstEnter = true;

	private UserLocationService userLocationService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	void init() {
		ManyiLoacationMannger mLocation = new ManyiLoacationMannger(getActivity(), 0);
		mLocation.setOnLocationReceivedListener(new OnLocationReceivedListener() {

			@Override
			public void onReceiveLocation(ManyiLocation location) {

				if (location == null)
					return;
				String cityName = location.getCity();
				if (!StringUtil.isEmptyOrNull(cityName)) {
					City city = LBSHelper.getCityFromCityName(getActivity(), cityName);
					if (city != null && mCity == null && !isRegisterAgain) {
						mCity = city;
						refreshCityInfobar(city);
					}
				}

				sendLocation(location);
			}

			@Override
			public void onFailedLocation(String msg) {
				// TODO Auto-generated method stub
				
			}
		});
		mLocation.start();
		createFolders();
		codeTextChange();
	}

	@Background
	void sendLocation(ManyiLocation location) {
		SharedPreferences sharedPreferences = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE);
		Integer userId = sharedPreferences.getInt("uid", 1);
		UserLocationRequest request = new UserLocationRequest();
		request.setLatitude(String.valueOf(location.getLatitude()));
		request.setLongitude(String.valueOf(location.getLongitude()));
		request.setUserId(userId);
		try {
			 userLocationService.sendUserLocation(request);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void createFolders() {
		if (isFirstEnter) {
			addAnimationListener(new Animation.AnimationListener() {
			            @Override
			            public void onAnimationStart(Animation animation) {
			 
			            }
			 
			            @Override
			            public void onAnimationEnd(Animation animation) {
			                ManyiUtils.showKeyBoard(getActivity(), mRealNameEditText);
			            }
			 
			            @Override
			            public void onAnimationRepeat(Animation animation) {
			 
			            }
			        });

			try {

				File codeFile = new File(Constants.CODE_FILE_PATH);
				if (!codeFile.exists()) {
					codeFile.mkdirs();
				}

				if (mCodeFile != null) {
					mImageLoader.displayImage("file://" + mCodeFile, mCodeImageView, ImageLoaderConfig.options);
				}

				File cardFile = new File(Constants.CARD_FILE_PATH);
				if (!cardFile.exists()) {
					cardFile.mkdirs();
				}

				if (mCardFile != null) {
					mImageLoader.displayImage("file://" + mCardFile, mCardImageView, ImageLoaderConfig.options);
				}

				showFailedItem();
				updateCommitText();
			} catch (Exception e) {
				e.printStackTrace();
			}
			isFirstEnter = false;
		}
	}

	private void showFailedItem() {
		// 真实姓名
		if (mRealName != null) {
			mRealNameEditText.setText(mRealName);
		}
		// 身份证号码
		if (mCode != null) {
			mIdNumberEditText.setText(mCode);
		}
		// 城市
		if (mCityName != null) {
			mCityNameButton.setText(mCityName);
		}
		// 区域
		if (mAreaName != null || mTownName != null) {
			mSelectAreaButton.setText(mAreaName + " " + mTownName);
		}
	}

	private void updateCommitText() {
		if (isRegisterAgain) {
			mCodeCameratTextView.setText(getResources().getString(R.string.re_upload_id_card));
			mCardCameraTextView.setText(getResources().getString(R.string.re_upload_bs_card));
			mRegisterButton.setText(getResources().getString(R.string.commit_info_again));
		}
	}

	@UiThread
	void setLocationCityText(String msg) {
		mCityNameButton.setText(msg);
	}

	public void codeTextChange() {
		mIdNumberEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				if (!s.equals("")) {
					int length = mIdNumberEditText.getText().toString().length();
					if (length == 18) {
						if (Character.isLowerCase(s.toString().charAt(17))) {
							mIdNumberEditText.setText(s.toString().toUpperCase(Locale.getDefault()));
							mIdNumberEditText.setSelection(mIdNumberEditText.getText().length());
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
	}

	@Click(R.id.rl_img_id_card)
	public void codeCamera() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		if (!checkEnable(Constants.CODE_FILE_PATH))
			return;
		takePhoto(Constants.CODE_FILE_PATH);
	}

	@Click(R.id.rl_img_bs_card)
	public void cardCamera() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		if (!checkEnable(Constants.CARD_FILE_PATH))
			return;
		takePhoto(Constants.CARD_FILE_PATH);
	}

	private boolean checkEnable(String path) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(getActivity(), R.string.storage_unable, Toast.LENGTH_SHORT).show();
			return false;
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
			if (!file.exists()) {
				Toast.makeText(getActivity(), R.string.storage_unable, Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return true;
	}

	@UiThread
	public void takePhoto(String path) {
		ManyiUtils.closeKeyBoard(getBackOpActivity(), mRealNameEditText);
		TakePhotoFragment takePhotoFragment = GeneratedClassUtils.getInstance(TakePhotoFragment.class);
		Bundle argsBundle = new Bundle();
		argsBundle.putString("photoPath", path);
		takePhotoFragment.setArguments(argsBundle);
		takePhotoFragment.tag = TakePhotoFragment.class.getName();
		takePhotoFragment.setManager(getFragmentManager());
		takePhotoFragment.show(SHOW_ADD_HIDE);
		takePhotoFragment.setSelectListener(new SelectListener<Object>() {

			@Override
			public void onSelected(Object t) {

			}

			@Override
			public void onCanceled() {
				updateCodePhotos();
				updateCardPhotos();
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();

		updateCityName();

		updateAreaName();
		
		if (isRegisterAgain) {
			mRegisterButton.setText(getResources().getString(R.string.commit_info_again));
		}

		// 邀请码
		if (mSpreadName != null && !TextUtils.isEmpty(mSpreadName.trim())) {
			mInviteCodeEditText.setText(mSpreadName);
		}
	}
	
	@UiThread
	public void updateCityName(){
		if (mCity != null || mCityName != null) {
			mCityNameButton.setText(mCity == null ? mCityName : mCity.getName());
		}
	}
	
	@UiThread
	public void updateAreaName(){
		if (mCity != null) {
			if (mAreaAndTownMessage != null) {
				mSelectAreaButton.setText(mAreaAndTownMessage.getAreaName() + " " + mAreaAndTownMessage.getTownName());
			}
		} else {
			mSelectAreaButton.setText(mAreaAndTownMessage == null ? (mTownName == null ? mAreaName : mAreaName + " " + mTownName)
					: mAreaAndTownMessage.getAreaName() + " " + mAreaAndTownMessage.getTownName());
		}
	}

	@Click(R.id.work_area_select)
	@UiThread
	public void isworkAreaSelect() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		ManyiUtils.closeKeyBoard(getBackOpActivity(), mRealNameEditText);
		clearAreaSelect();
		if (mCity != null || mCityId != 0) {
			final RegisterSelectAreaFragment areaFragment = GeneratedClassUtils.getInstance(RegisterSelectAreaFragment.class);
			Bundle argsBundle = new Bundle();
			argsBundle.putInt("areaId", mCity == null ? mCityId : mCity.getAreaId());
			areaFragment.setArguments(argsBundle);
			areaFragment.tag = areaFragment.getClass().getName();
			areaFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
					R.anim.anim_fragment_close_out);
			areaFragment.setManager(getFragmentManager());
			areaFragment.setSelectListener(new SelectListener<AreaAndTownMessage>() {

				@Override
				public void onSelected(AreaAndTownMessage t) {
					refreshAreaInfobar(t);
					mAreaAndTownMessage = t;
				}

				@Override
				public void onCanceled() {
					updateAreaName();
				}
			});
			areaFragment.show(SHOW_ADD_HIDE);
		} else {
			onSendSMSError("请先选择城市");
		}
	}

	// 跳转城市选择页
	@Click(R.id.ed_local_city)
	@UiThread
	public void showCityListFragment() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		ManyiUtils.closeKeyBoard(getBackOpActivity(), mRealNameEditText);
		clearCitySelect();
		final CityBaseFragment citySelectFragment = GeneratedClassUtils.getInstance(CityBaseFragment.class);
		citySelectFragment.tag = citySelectFragment.getClass().getName();
		citySelectFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		citySelectFragment.setManager(getFragmentManager());
		citySelectFragment.setSelectListener(new SelectListener<City>() {

			@Override
			public void onSelected(City city) {
				refreshCityInfobar(city);
				mCity = city;
			}

			@Override
			public void onCanceled() {
				updateCityName();
			}

		});
		citySelectFragment.show(SHOW_ADD_HIDE);
	}

	private void clearCitySelect() {
		mCity = null;
		mCityNameButton.setText(null);
		mAreaAndTownMessage = null;
		mSelectAreaButton.setText(null);
	}

	private void clearAreaSelect() {
		mAreaAndTownMessage = null;
		mSelectAreaButton.setText(null);
	}

	/**
	 * 刷新城市栏
	 */
	@UiThread
	void refreshCityInfobar(City city) {
		mCityNameButton.setText(city.getName());
	}

	/**
	 * 刷新区域栏
	 */
	@UiThread
	void refreshAreaInfobar(AreaAndTownMessage msg) {
		mSelectAreaButton.setText(msg.getAreaName() + " " + msg.getTownName());
	}

	@Click(R.id.btn_register)
	@Background
	public void toRegister() {

		if (CheckDoubleClick.isFastDoubleClick())
			return;
		if (isRegisterAgain) {
			commitRegisterAgainRequest();
		} else {
			commitRegisterRequest();
		}
		ManyiUtils.closeKeyBoard(getBackOpActivity(), mRealNameEditText);
		onRegisterSeccess();
	}

	private void getUploadFile() {
		// get code file and card file
		if (getLastestFileInDirectory(Constants.CODE_FILE_PATH) != null) {
			AdvancedBitmapUtils.processImage(getLastestFileInDirectory(Constants.CODE_FILE_PATH));
			mCodeFile = new File(getLastestFileInDirectory(Constants.CODE_FILE_PATH));
		}
		if (getLastestFileInDirectory(Constants.CARD_FILE_PATH) != null) {
			AdvancedBitmapUtils.processImage(getLastestFileInDirectory(Constants.CARD_FILE_PATH));
			mCardFile = new File(getLastestFileInDirectory(Constants.CARD_FILE_PATH));
		}
	}

	private void commitRegisterRequest() {
		RegistNextRequest nextRequest = new RegistNextRequest();
		nextRequest.setMobile(mPhoneNumber);
		nextRequest.setVilidate(mSmsCaptcha);
		nextRequest.setPassword(mRegisterPwd);
		nextRequest.setRealName(mRealNameEditText.getText().toString().trim());
		if (mCity != null) {
			nextRequest.setCityId(mCity.getAreaId());
		}
		nextRequest.setAreaId(mAreaAndTownMessage == null ? 0 : mAreaAndTownMessage.getAreaId());
		nextRequest.setTownId(mAreaAndTownMessage == null ? 0 : mAreaAndTownMessage.getTownId());
		nextRequest.setCode(mIdNumberEditText.getText().toString().trim());
		nextRequest.setSpreadName(mInviteCodeEditText.getText().toString().trim());
		getUploadFile();
		nextRequest.setCodeFile(mCodeFile);
		nextRequest.setCardFile(mCardFile);

		mUserService.register(nextRequest);
	}

	private void commitRegisterAgainRequest() {
		RegisterAgainRequest againRequest = new RegisterAgainRequest();
		againRequest.setRealName(mRealNameEditText.getText().toString().trim());

		againRequest.setCityId(mCity == null ? mCityId : mCity.getAreaId());
		if (mCity != null) {
			againRequest.setAreaId(mAreaAndTownMessage == null ? 0 : mAreaAndTownMessage.getAreaId());
			againRequest.setTownId(mAreaAndTownMessage == null ? 0 : mAreaAndTownMessage.getTownId());
		} else {
			againRequest.setAreaId(mAreaAndTownMessage == null ? mAreaId : mAreaAndTownMessage.getAreaId());
			againRequest.setTownId(mAreaAndTownMessage == null ? mTownId : mAreaAndTownMessage.getTownId());
		}
		againRequest.setCode(mIdNumberEditText.getText().toString().trim());
		againRequest.setSpreadName(mInviteCodeEditText.getText().toString().trim());
		getUploadFile();
		againRequest.setCodeFile(mCodeFile);
		againRequest.setCardFile(mCardFile);
		againRequest.setUserId(mUserId);
		mUserService.againRegist(againRequest);
	}

	@UiThread
	public void onSendSMSError(String e) {
		DialogBuilder.showSimpleDialog(e, getActivity());
	}

	@UiThread
	public void onRegisterSeccess() {
		try {
			if (!isRegisterAgain) {
				SharedPreferences sharedPreferences = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0);
				Editor editor = sharedPreferences.edit();
				editor.putString("name", AESUtil.encrypt(mPhoneNumber, CommonConfig.AES_KEY));
				editor.putString("password", AESUtil.encrypt(mRegisterPwd, CommonConfig.AES_KEY));
				editor.commit();
			}
			ReviewFragment reviewFragment = GeneratedClassUtils.getInstance(ReviewFragment.class);
			reviewFragment.tag = ReviewFragment.class.getName();
			reviewFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, 0, 0);
			reviewFragment.setManager(getFragmentManager());
			reviewFragment.setBackOp(new AbsOp(ReviewFragment.class.getName()) {

				@Override
				public void perform(BackOpFragmentActivity activity) {
					getActivity().finish();
				}
			});
			reviewFragment.show(SHOW_ADD_HIDE);

			// delete file

			File registerFile = new File(Constants.IMAGE_REGISTER_ROOT_PATH);
			if (registerFile.exists()) {
				File renameFile = new File(Constants.IMAGE_ROOT_PATH + System.currentTimeMillis());
				registerFile.renameTo(renameFile);
				FileUtils.deleteFile(renameFile);
			}
		} catch (RestException e) {
			showError(e.getMessage());
		} catch (Exception e) {
			Thread.getDefaultUncaughtExceptionHandler().uncaughtException(null, e);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		ManyiUtils.closeKeyBoard(getBackOpActivity(), mRealNameEditText);
	}

	@Click({ R.id.service_agreement })
	@UiThread
	public void serviceAgreement() {

		if (CheckDoubleClick.isFastDoubleClick())
			return;

		HtmlLoadFragment htmlLoadFragment = GeneratedClassUtils.getInstance(HtmlLoadFragment.class);
		htmlLoadFragment.tag = HtmlLoadFragment.class.getName();
		Bundle bundle = new Bundle();
		bundle.putString("mHtmLTitle", "服务协议");
		bundle.putString("mHtmlUrl", "agreement.html");
		htmlLoadFragment.setArguments(bundle);
		htmlLoadFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		htmlLoadFragment.setManager(getFragmentManager());
		htmlLoadFragment.show(SHOW_ADD_HIDE);

	}

	@Click({ R.id.register_next_back })
	public void registerBack() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;

		ManyiUtils.closeKeyBoard(getBackOpActivity(), mRealNameEditText);
		remove();
	}

	@UiThread
	void showError(String e) {
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());

	}

	public String getLastestFileInDirectory(String filePath) {
		if (filePath == null || filePath.length() == 0) {
			return null;
		}
		File file = new File(filePath);
		File[] items = file.listFiles();
		if (items == null || items.length == 0) {
			return null;
		}
		Arrays.sort(items, new Comparator<File>() {
			@Override
			public int compare(File lhs, File rhs) {
				if (lhs.lastModified() < rhs.lastModified()) {
					return 1;
				} else {
					return -1;
				}
			}
		});

		for (int i = 0; i < items.length; i++) {
			if (i < 1) {
				continue;
			} else {
				items[i].delete();
			}
		}

		if (items.length > 0) {
			return items[0].getAbsolutePath();
		}

		return null;
	}

	public void deleteRestFileInDirectory(String filePath) {
		if (filePath == null || filePath.length() == 0) {
			return;
		}
		File file = new File(filePath);
		File[] items = file.listFiles();
		if (items == null || items.length == 0) {
			return;
		}
		Arrays.sort(items, new Comparator<File>() {
			@Override
			public int compare(File lhs, File rhs) {
				if (lhs.lastModified() < rhs.lastModified()) {
					return 1;
				} else {
					return -1;
				}
			}
		});

		for (int i = 0; i < items.length; i++) {
			if (i < 1) {
				continue;
			} else {
				items[i].delete();
			}
		}
	}

	private void updateCodePhotos() {
		// 身份证照片
		String lastCodeFilePath = getLastestFileInDirectory(Constants.CODE_FILE_PATH);
		if (lastCodeFilePath != null) {
			File lastCodeFile = new File(lastCodeFilePath);
			mImageLoader.displayImage("file://" + lastCodeFile, mCodeImageView, ImageLoaderConfig.options);
		} else if (mCodeFile != null) {
			mImageLoader.displayImage("file://" + mCodeFile, mCodeImageView, ImageLoaderConfig.options);
		}

		if (lastCodeFilePath != null || mCodeFile != null) {
			mCodeCameratTextView.setText(getResources().getString(R.string.re_upload_id_card));
		}
	}

	private void updateCardPhotos() {
		// 名片照片
		String lastCardFilePath = getLastestFileInDirectory(Constants.CARD_FILE_PATH);
		if (lastCardFilePath != null) {
			File lastCardFile = new File(lastCardFilePath);
			mImageLoader.displayImage("file://" + lastCardFile, mCardImageView, ImageLoaderConfig.options);
		} else if (mCardFile != null) {
			mImageLoader.displayImage("file://" + mCardFile, mCardImageView, ImageLoaderConfig.options);
		}

		if (lastCardFilePath != null || mCardFile != null) {
			mCardCameraTextView.setText(getResources().getString(R.string.re_upload_bs_card));
		}
	}

}
