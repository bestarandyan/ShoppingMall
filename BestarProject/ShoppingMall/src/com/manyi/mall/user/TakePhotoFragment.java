package com.manyi.mall.user;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.CameraProfile;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DeviceUtil;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.ToDaysTaskDetailsResponse;
import com.manyi.mall.common.Constants;

@EFragment(R.layout.fragment_take_photo)
public class TakePhotoFragment extends SuperFragment<Object> implements OnTouchListener, OnClickListener {

	@ViewById(R.id.surfaceView)
	public SurfaceView surfaceView;
	@ViewById(R.id.takepicture)
	public ImageView mTakePicture;
	@ViewById(R.id.buttonLayout)
	public RelativeLayout layout;
	private Camera mCamera;
	private Camera.Parameters mParameters = null;

	@FragmentArg
	public String photoPath;

	@FragmentArg
	public int sectionType;

	@FragmentArg
	public int positionType;

	@FragmentArg
	public int mTaskId;

	@FragmentArg
	public ToDaysTaskDetailsResponse mHouseInfo;

	private long lastUpdateTime;
	
	private float lastX, lastY, lastZ;

	private TouchFocusCallBack mTouchFocusCallBack;
	private SensorFocusCallBack mSensorFocusCallBack;
	private TakePhotoCallback mTakePhotoCallback;

	private ScaleAnimation mFocusAnimation, mSensorFocuAnimation;

	private ScaleAnimationListener mScaleAnimationListener;
	private SensorAnimationListener mSensorAnimationListener;

	private int restWidth, restHeight, focusWidth, focusHeight, focusingWidth, focusingHeight;

	private boolean isFocusing, isProtrait, isLandScape;

	private int mWidth;
	private int mHeight;
	private String flashModeString = Camera.Parameters.FLASH_MODE_OFF;

	private float marginX, marginY;

	private GestureDetector mFocusGestureDetector;

	private SensorManager mManager = null;
	private Sensor mSensor = null;
	private SensorEventListener mListener = null;
	private int flashModeFlag;
	@ViewById(R.id.flash_mode)
	public ImageView flashImageView;

	@ViewById(R.id.focus_area)
	public ImageView focusArea;

	private MyHandler mHandler = new MyHandler(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		mSensor = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mListener = new SensorEventListener() {
			public void onAccuracyChanged(Sensor sensor, int accuracy) {

			}

			@Override
			public void onSensorChanged(SensorEvent event) {

				// 现在检测时间
				long currentUpdateTime = System.currentTimeMillis();
				// 两次检测的时间间隔
				long timeInterval = currentUpdateTime - lastUpdateTime;
				// 判断是否达到了检测时间间隔
				if (timeInterval < Constants.UPTATE_INTERVAL_TIME)
					return;
				// 现在的时间变成last时间
				lastUpdateTime = currentUpdateTime;
				// 获得x,y,z坐标
				float x = event.values[0];
				float y = event.values[1];
				float z = event.values[2];
				if (mCamera != null) {
					if (y > 5) {
						if (!isProtrait) {
							Camera.Parameters parameters = mCamera.getParameters();
							parameters.setRotation(90);
							mCamera.setParameters(parameters);
							isProtrait = true;
							isLandScape = false;
						}
					} else {
						if (!isLandScape) {
							Camera.Parameters parameters = mCamera.getParameters();
							parameters.setRotation(0);
							mCamera.setParameters(parameters);
							isLandScape = true;
							isProtrait = false;
						}
					}
				}
				// 获得x,y,z的变化值
				float deltaX = x - lastX;
				float deltaY = y - lastY;
				float deltaZ = z - lastZ;
				// 将现在的坐标变成last坐标
				lastX = x;
				lastY = y;
				lastZ = z;
				double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
				if (length > 10) {
					if (!isFocusing) {
						mHandler.removeMessages(2016);
						mHandler.removeMessages(2015);
						try {
							if (mCamera != null && !isFocusing) {
								mCamera.autoFocus(mSensorFocusCallBack);
								isFocusing = true;
							}
						} catch (Exception e) {
							if (mCamera != null) {
								mCamera.release(); // 释放照相机
								mCamera = null;
							}
							e.printStackTrace();
						}
						mSensorFocuAnimation.cancel();
						focusArea.setBackgroundResource(R.drawable.camera_focus);
						focusArea.setVisibility(View.VISIBLE);
						LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						params.setMargins((int) surfaceView.getLeft() + surfaceView.getWidth() / 2 - focusWidth / 2,
								(int) surfaceView.getTop() + surfaceView.getHeight() / 2 - focusHeight / 2, 0, 0);
						focusArea.setLayoutParams(params);
						focusArea.startAnimation(mSensorFocuAnimation);
					}
				}
			}
		};

		mTouchFocusCallBack = new TouchFocusCallBack();
		mSensorFocusCallBack = new SensorFocusCallBack();
		mTakePhotoCallback = new TakePhotoCallback();
		initAnimation();
	}

	private void initAnimation() {
		try {
			mScaleAnimationListener = new ScaleAnimationListener();
			mFocusAnimation = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			mFocusAnimation.setAnimationListener(mScaleAnimationListener);
			mFocusAnimation.setDuration(500);
			Drawable focusDrawable = getResources().getDrawable(R.drawable.camera_focus);
			Drawable focusingDrawable = getResources().getDrawable(R.drawable.camera_focusing);
			focusWidth = focusDrawable.getIntrinsicWidth();
			focusHeight = focusDrawable.getIntrinsicHeight();
			focusingWidth = focusingDrawable.getIntrinsicWidth();
			focusingHeight = focusingDrawable.getIntrinsicHeight();
			restWidth = focusWidth - focusingWidth;
			restHeight = focusHeight - focusingHeight;

			mSensorAnimationListener = new SensorAnimationListener();
			mSensorFocuAnimation = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
					0.5f);
			mSensorFocuAnimation.setAnimationListener(mSensorAnimationListener);
			mSensorFocuAnimation.setDuration(250);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class MyHandler extends Handler {
		WeakReference<TakePhotoFragment> mFragment;

		MyHandler(TakePhotoFragment takePhotoFragment) {
			mFragment = new WeakReference<TakePhotoFragment>(takePhotoFragment);
		}

		@Override
		public void handleMessage(Message msg) {
			try {
				TakePhotoFragment takePhotoFragment = mFragment.get();
				switch (msg.what) {
				case 2014:
					takePhotoFragment.focusArea.clearAnimation();
					takePhotoFragment.focusArea.setBackgroundResource(R.drawable.camera_focused);
					LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.setMargins((int) takePhotoFragment.marginX + takePhotoFragment.restWidth / 2, (int) takePhotoFragment.marginY
							+ takePhotoFragment.restHeight / 2, 0, 0);
					takePhotoFragment.focusArea.setLayoutParams(params);
					break;
				case 2015:
					if (takePhotoFragment.getActivity() == null || takePhotoFragment.getActivity().isFinishing()) {
						return;
					}
					takePhotoFragment.focusArea.setVisibility(View.INVISIBLE);
					break;
				case 2016:
					takePhotoFragment.focusArea.clearAnimation();
					takePhotoFragment.focusArea.setBackgroundResource(R.drawable.camera_focused);
					LayoutParams sensorParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					sensorParams.setMargins((int) takePhotoFragment.surfaceView.getLeft() + takePhotoFragment.surfaceView.getWidth() / 2
							- takePhotoFragment.focusingWidth / 2, (int) takePhotoFragment.surfaceView.getTop()
							+ takePhotoFragment.surfaceView.getHeight() / 2 - takePhotoFragment.focusingHeight / 2, 0, 0);
					takePhotoFragment.focusArea.setLayoutParams(sensorParams);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@SuppressWarnings("deprecation")
	@AfterViews
	public void init() {
		try {
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			initPreviewLayout();
			if (mWidth > 0 && mHeight > 0) {
				LayoutParams rlp = new RelativeLayout.LayoutParams(mWidth, mHeight);
				rlp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
				surfaceView.setLayoutParams(rlp);
				surfaceView.getHolder().setSizeFromLayout();
			}
			surfaceView.getHolder().setKeepScreenOn(true);
			surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			surfaceView.getHolder().addCallback(new SurfaceCallback());
			mFocusGestureDetector = new GestureDetector(getActivity(), new MyGesture(), null);
			surfaceView.setOnTouchListener(this);
			surfaceView.setOnClickListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mManager.registerListener(mListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
		isFocusing = false;
		isProtrait = false;
		isLandScape = false;
	}

	@Override
	public void onPause() {
		super.onPause();
		mManager.unregisterListener(mListener);
		focusArea.setVisibility(View.GONE);
		surfaceView.setOnTouchListener(null);
	}

	/**
	 * 初始化View展示大小
	 */

	public void initPreviewLayout() {
		try {
			if (mCamera == null) {
				mCamera = Camera.open(0);
			}
			mParameters = mCamera.getParameters();
			List<Camera.Size> previewSizes = mParameters.getSupportedPreviewSizes();
			if (previewSizes != null && previewSizes.size() > 0) {
				Camera.Size previewSize = getOptimalPicSize(previewSizes);
				DisplayMetrics dm = new DisplayMetrics();
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
				mHeight = dm.heightPixels;
				mWidth = mHeight * previewSize.width / previewSize.height;
				Log.i(Constants.TAKE_PHOTO_TAG, "previewSize.height:" + previewSize.height + ",previewSize.width:" + previewSize.width + ",mWidth:" + mWidth
						+ ",mHeight:" + mHeight);
			}
			if (DeviceUtil.getDeviceModel().equals("MI 3")) {
				// flashImageView.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			if (mCamera != null) {
				mCamera.release();
				mCamera = null;
			}
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroyView() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release(); // 释放照相机
			mCamera = null;
		}
		super.onDestroyView();
	}

	@Click(R.id.takepicture)
	public void onTakePhoto() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}

		if (!isFocusing) {
			setUnable();
			takePhotoFocus();
		}
	}

	@UiThread
	public void setEnable() {
		mTakePicture.setEnabled(true);
		surfaceView.setEnabled(true);
		surfaceView.setOnTouchListener(this);
	}

	@UiThread
	public void setUnable() {
		mTakePicture.setEnabled(false);
		surfaceView.setEnabled(false);
		surfaceView.setOnTouchListener(null);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	private final class SurfaceCallback implements Callback {

		// 拍照状态变化时调用该方法
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			initCamera(holder, width, height);
		}

		// 开始拍照时调用该方法
		@Override
		public void surfaceCreated(SurfaceHolder holder) {

		}

		// 停止拍照时调用该方法
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if (mCamera != null) {
				mCamera.release(); // 释放照相机
				mCamera = null;
			}
		}
	}

	// 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
	public static int getPreviewDegree(Activity activity) {
		// 获得手机的方向
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(0, info);
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	private void initCamera(SurfaceHolder holder, int width, int height) {
		try {
			if (mCamera == null) {
				mCamera = Camera.open(0);
			}
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			Camera.Parameters parameters = mCamera.getParameters();
			if (flashModeString != null) {
				parameters.setFlashMode(flashModeString);
			}
			if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			}
			// flashModeString = Camera.Parameters.FLASH_MODE_OFF;
			parameters.setPictureFormat(ImageFormat.JPEG);
			int jpegQuality = CameraProfile.getJpegEncodingQualityParameter(0, CameraProfile.QUALITY_MEDIUM);
			parameters.setJpegQuality(jpegQuality);
			List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
			// Camera.Size preSize = getOptimalPicSize(previewSizes);
			Camera.Size preSize = getOptimalPicSize(previewSizes);
			if (preSize != null) {
				parameters.setPreviewSize(preSize.width, preSize.height);
			}
			List<Camera.Size> picSizes = parameters.getSupportedPictureSizes();
			Camera.Size picSize = getOptimalPicSize(picSizes);
			if (picSize != null) {
				parameters.setPictureSize(picSize.width, picSize.height);
			}
			mCamera.setParameters(parameters);
			mCamera.setDisplayOrientation(getPreviewDegree(getBackOpActivity()));
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();			
			mCamera.autoFocus(new AutoFocusCallback() {
				@Override
				public void onAutoFocus(boolean success, Camera camera) {
					isFocusing = false;
				}
			});
			isFocusing = true;
		} catch (Exception e) {
			if (mCamera != null) {
				mCamera.release();
				mCamera = null;
			}
			e.printStackTrace();
		}
	}

	/**
	 * 获取最佳图片大小
	 * 
	 * @param previewSizes
	 * @return
	 */
	private Size getOptimalPicSize(List<Size> picSizes) {
		if (picSizes != null && picSizes.size() > 0) {
			int size = picSizes.size();
			Size temp = picSizes.get(0);
			float muti = getMuti(Math.max(temp.width, temp.height), 1920);
			for (int i = 0; i < size; i++) {
				Log.i(Constants.TAKE_PHOTO_TAG, "height:" + (((Camera.Size) picSizes.get(i)).height) + "/width:" + (((Camera.Size) picSizes.get(i)).width));
				float mutiTemp = getMuti(Math.max(picSizes.get(i).width, picSizes.get(i).height), 1920);
				if (mutiTemp > muti) {
					muti = mutiTemp;
					temp = picSizes.get(i);
				}
			}
			return temp;
		}
		return null;
	}

	private float getMuti(float listHeight, float defaultHeight) {
		return Math.min(listHeight, defaultHeight) / Math.max(listHeight, defaultHeight);
	}

	private final class MyPictureCallback implements PictureCallback {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				camera.stopPreview();
				hidden();
				String photoPathString = saveToSDCard(data, photoPath); // 保存图片到sd卡中
				SavePhotoFragment savePhotoFragment = GeneratedClassUtils.getInstance(SavePhotoFragment.class);
				Bundle argsBundle = new Bundle();
				argsBundle.putString("photoPath", photoPathString);
				argsBundle.putInt("sectionType", sectionType);
				argsBundle.putInt("mTaskId", mTaskId);
				argsBundle.putInt("positionType", positionType);
				argsBundle.putBoolean("isProtrait", isProtrait);
				argsBundle.putSerializable("mHouseInfo", mHouseInfo);
				savePhotoFragment.setArguments(argsBundle);
				savePhotoFragment.tag = SavePhotoFragment.class.getName();
				savePhotoFragment.setManager(getActivity().getSupportFragmentManager());
				savePhotoFragment.setSelectListener(new SelectListener<Object>() {

					@Override
					public void onSelected(Object t) {
						remove();
					}

					@Override
					public void onCanceled() {
						isFocusing = false;
						if (flashModeString != null) {
							Camera.Parameters parameters = mCamera.getParameters();
							parameters.setFlashMode(flashModeString);
							mCamera.setParameters(parameters);
						}
						getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					}
				});
				savePhotoFragment.show(SHOW_ADD);
				setEnable();
				Camera.Parameters parameters = mCamera.getParameters();
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				mCamera.setParameters(parameters);
				mCamera.startPreview(); // 拍完照后，重新开始预览
			} catch (Exception e) {
				if (mCamera != null) {
					mCamera.release(); // 释放照相机
					mCamera = null;
				}
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将拍下来的照片存放在SD卡中
	 * 
	 * @param data
	 * @throws IOException
	 */
	public String saveToSDCard(byte[] data, String filePath) throws IOException {
		try {
			String photoName = null;
			if (filePath.endsWith(".jpg")) {
				photoName = filePath;
			} else {
				File filePathFile = new File(filePath);
				if (!filePathFile.exists()) {
					filePathFile.mkdirs();
				}
				photoName = filePath + File.separator + DateFormat.format("yyyy-MM-dd-hh-mm-ss", Calendar.getInstance(Locale.CHINA))
						+ ".jpg";
			}

			File photoFile = new File(photoName);
			FileOutputStream outputStream = new FileOutputStream(photoFile); // 文件输出流
			outputStream.write(data); // 写入sd卡中
			outputStream.close(); // 关闭输出流
			return photoName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void focusOnTouch(MotionEvent event) {
		try {
			int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			if (currentapiVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				Rect focusRect = calculateTapArea((int) event.getX(), (int) event.getY());
				Rect meteringRect = calculateMeterArea((int) event.getX(), (int) event.getY());

				Camera.Parameters parameters = mCamera.getParameters();
				if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
					parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
				}
/*				if (DeviceUtil.getDeviceModel().equals("MI 3")) {
					parameters.setFlashMode(Parameters.FLASH_MODE_ON);
				} else {
					parameters.setFlashMode(Parameters.FLASH_MODE_AUTO);
				}*/
				if (parameters.getMaxNumFocusAreas() > 0) {
					List<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
					focusAreas.add(new Camera.Area(focusRect, 1000));
					parameters.setFocusAreas(focusAreas);
				}
				if (parameters.getMaxNumMeteringAreas() > 0) {
					List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
					meteringAreas.add(new Camera.Area(meteringRect, 1000));
					parameters.setMeteringAreas(meteringAreas);
				}

				mCamera.setParameters(parameters);
			}
			try {
				if (mCamera != null && !isFocusing) {
					mCamera.autoFocus(mTouchFocusCallBack);
					isFocusing = true;
				}
			} catch (Exception e) {
				if (mCamera != null) {
					mCamera.release(); // 释放照相机
					mCamera = null;
				}
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Rect calculateTapArea(float x, float y) {
		float x1 = (x / mWidth) * 2000 - 1000;
		float y1 = (y / mHeight) * 2000 - 1000;
		if (x1 < -980) {
			x1 = -980;
		} else if (x1 > 980) {
			x1 = 980;
		}

		if (y1 < -980) {
			y1 = -980;
		} else if (y1 > 980) {
			y1 = 980;
		}

		return new Rect((int) x1 - 20, (int) y1 - 20, (int) x1 + 20, (int) y1 + 20);
	}

	private Rect calculateMeterArea(float x, float y) {
		float x1 = (x / mWidth) * 2000 - 1000;
		float y1 = (y / mHeight) * 2000 - 1000;
		if (x1 < -960) {
			x1 = -960;
		} else if (x1 > 960) {
			x1 = 960;
		}

		if (y1 < -960) {
			y1 = -960;
		} else if (y1 > 960) {
			y1 = 960;
		}
		return new Rect((int) x1 - 40, (int) y1 - 40, (int) x1 + 40, (int) y1 + 40);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		flashModeFlag = 0;
		flashModeString = null;
		try {
			if (getActivity() == null || getActivity().isFinishing()) {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouch(View v, final MotionEvent event) {
		return mFocusGestureDetector.onTouchEvent(event);
	}

	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
					drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}

	@UiThread
	public void focusSuccess() {
		focusArea.setBackgroundResource(R.drawable.camera_focused);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins((int) marginX, (int) marginY, 0, 0);
		focusArea.setLayoutParams(params);
	}

	@UiThread
	public void hidden() {
		if (getActivity() == null || getActivity().isFinishing()) {
			return;
		}
		focusArea.setVisibility(View.INVISIBLE);
	}

	@UiThread
	public void changeFocusing() {
		if (getActivity() == null || getActivity().isFinishing()) {
			return;
		}
		focusArea.setBackgroundResource(R.drawable.camera_focusing);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins((int) marginX + restWidth / 2, (int) marginY + restHeight / 2, 0, 0);
		focusArea.setLayoutParams(params);
	}

	@UiThread
	public void sensorChangeFocusing() {
		if (getActivity() == null || getActivity().isFinishing()) {
			return;
		}
		focusArea.setBackgroundResource(R.drawable.camera_focusing);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		marginX = surfaceView.getLeft() + surfaceView.getWidth() / 2 - focusingWidth / 2;
		marginY = surfaceView.getTop() + surfaceView.getHeight() / 2 - focusingHeight / 2;
		params.setMargins((int) marginX, (int) marginY, 0, 0);
		focusArea.setLayoutParams(params);
	}

	public void takePhotoFocus() {
		if (mCamera != null) {
			try {
				if (mCamera != null) {
					mCamera.autoFocus(mTakePhotoCallback);
					isFocusing = true;
				}
				mSensorFocuAnimation.cancel();
				focusArea.setBackgroundResource(R.drawable.camera_focus);
				focusArea.setVisibility(View.VISIBLE);
				LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.setMargins((int) surfaceView.getLeft() + surfaceView.getWidth() / 2 - focusWidth / 2,
						(int) surfaceView.getTop() + surfaceView.getHeight() / 2 - focusHeight / 2, 0, 0);
				focusArea.setLayoutParams(params);
				focusArea.startAnimation(mSensorFocuAnimation);
				
				// MI 3
				if (DeviceUtil.getDeviceModel().equals("MI 3") && flashModeString.equals(Camera.Parameters.FLASH_MODE_ON)) {
					Camera.Parameters parameters = mCamera.getParameters();
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
					mCamera.setParameters(parameters);
				}
			} catch (Exception e) {
				if (mCamera != null) {
					mCamera.release(); // 释放照相机
					mCamera = null;
				}
				e.printStackTrace();
			}
		}
	}
	

	@UiThread
	public void takePhoto() {
		try {
			if (mCamera != null) {
				mCamera.takePicture(null, null, new MyPictureCallback());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class TakePicture implements Runnable{

		@Override
		public void run() {
			try {
				if (mCamera != null) {
					mCamera.takePicture(null, null, new MyPictureCallback());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class MyGesture implements OnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			if (!isFocusing) {
				mFocusAnimation.cancel();
				mHandler.removeMessages(2014);
				mHandler.removeMessages(2015);
				if (mCamera != null) {
					focusOnTouch(e);
				}
				focusArea.setBackgroundResource(R.drawable.camera_focus);
				focusArea.setVisibility(View.VISIBLE);
				LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				marginX = e.getX() - focusArea.getWidth() / 2 + surfaceView.getLeft();
				marginY = e.getY() - focusArea.getHeight() / 2 + surfaceView.getTop();

				if ((e.getX() + focusWidth / 2) > surfaceView.getWidth()) {
					marginX = surfaceView.getRight() - focusWidth;
				} else if ((e.getX() - focusWidth / 2) < 0) {
					marginX = surfaceView.getLeft();
				}

				if ((e.getY() + focusHeight / 2) > surfaceView.getHeight()) {
					marginY = surfaceView.getBottom() - focusHeight;
				} else if ((e.getY() - focusArea.getHeight() / 2) < 0) {
					marginY = surfaceView.getTop();
				}

				params.setMargins((int) marginX, (int) marginY, 0, 0);
				focusArea.setLayoutParams(params);
				focusArea.startAnimation(mFocusAnimation);
			}
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {

		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			return true;
		}

	}

	class ScaleAnimationListener implements AnimationListener {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			changeFocusing();

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

	class SensorAnimationListener implements AnimationListener {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			sensorChangeFocusing();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

	class TouchFocusCallBack implements AutoFocusCallback {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if (success) {
				Message successMessage = mHandler.obtainMessage(2014);
				mHandler.sendMessage(successMessage);
				Message hiddenMessage = mHandler.obtainMessage(2015);
				mHandler.sendMessageDelayed(hiddenMessage, 2000);
			} else {
				hidden();
			}
			isFocusing = false;
		}
	}

	class SensorFocusCallBack implements AutoFocusCallback {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if (success) {
				Message successMessage = mHandler.obtainMessage(2016);
				mHandler.sendMessage(successMessage);
				Message hiddenMessage = mHandler.obtainMessage(2015);
				mHandler.sendMessageDelayed(hiddenMessage, 500);
			} else {
				hidden();
			}
			isFocusing = false;
		}
	}
	
	class TakePhotoCallback implements AutoFocusCallback {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if (success) {
				Message successMessage = mHandler.obtainMessage(2016);
				mHandler.sendMessage(successMessage);
				Message hiddenMessage = mHandler.obtainMessage(2015);
				mHandler.sendMessageDelayed(hiddenMessage, 500);
			} else {
				hidden();
			}
			takePhoto();
			isFocusing = false;
		}
	}

	@Click(R.id.flash_mode)
	public void setFlashMode() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		flashModeFlag++;
		flashModeFlag = flashModeFlag % 3;
		if (mCamera != null) {
			switch (flashModeFlag) {
			case 0:
				flashModeString = Camera.Parameters.FLASH_MODE_OFF;
				setFalshModeOff();
				break;
			case 1:
				flashModeString = Camera.Parameters.FLASH_MODE_ON;
				setFalshModeOn();
				break;
			case 2:
				if (DeviceUtil.getDeviceModel().equals("MI 3")) {
					flashModeString = Camera.Parameters.FLASH_MODE_OFF ;
				} else {
					flashModeString = Camera.Parameters.FLASH_MODE_AUTO;
				}
				setFalshModeAuto();
				break;
			default:
				flashModeString = Camera.Parameters.FLASH_MODE_OFF;
				setFalshModeOff();
				break;
			}
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setFlashMode(flashModeString);
			mCamera.setParameters(parameters);
		}
	}

	@UiThread
	public void setFalshModeOff() {
		flashImageView.setBackgroundResource(R.drawable.ic_action_flash_off);
	}

	@UiThread
	public void setFalshModeOn() {
		flashImageView.setBackgroundResource(R.drawable.ic_action_flash_on);
	}

	@UiThread
	public void setFalshModeAuto() {
		flashImageView.setBackgroundResource(R.drawable.ic_action_flash_automatic);
	}

	@Override
	public void onClick(View v) {

	}

}