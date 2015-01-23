package com.manyi.mall.user;

import java.io.File;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huoqiu.framework.util.CheckDoubleClick;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.ToDaysTaskDetailsResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.provider.contract.PictureContract;
import com.manyi.mall.widget.touchview.ImageLoaderConfig;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiscCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

@EFragment(R.layout.fragment_save_photo)
public class SavePhotoFragment extends ImageLoaderFragment implements OnClickListener {
	public final static String IMAGES = "images";
	public final static String IMAGE_POSITION = "image_position";

	@ViewById(R.id.imageview)
	public ImageView imageView;
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

	@FragmentArg
	public boolean isProtrait;
	private boolean isCancel = false;
	private boolean isSave = false;
	@ViewById(R.id.landscape_layout)
	public RelativeLayout mLandscapeRelativeLayout;

	@ViewById(R.id.protrait_layout)
	public RelativeLayout mProtraitRelativeLayout;

	@ViewById(R.id.FrameLayout1)
	public RelativeLayout mRelativeLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("deprecation")
	@AfterViews
	public void init() {
		if (isProtrait) {
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			mProtraitRelativeLayout.setVisibility(View.VISIBLE);
		} else {
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			mLandscapeRelativeLayout.setVisibility(View.VISIBLE);
		}
		MemoryCacheUtils.removeFromCache("file://" + photoPath, ImageLoader.getInstance().getMemoryCache());
		DiscCacheUtils.removeFromCache("file://" + photoPath, ImageLoader.getInstance().getDiscCache());
		mImageLoader.displayImage("file://" + photoPath, imageView, ImageLoaderConfig.options);
		mRelativeLayout.setOnClickListener(this);
	}

	@Click(R.id.save_picture_landscape)
	public void landscapeTakePhoto() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		savePhoto();
	}

	private void savePhotoInDB() {
		String housePath = Constants.HOUSE_ROOT_PATH + mTaskId + File.separator;

		// 客厅
		String livingRoomPath = housePath + Constants.LIVING_ROOM + File.separator;

		// 卧室
		String bedRoomPath = housePath + Constants.BED_ROOM + File.separator;

		String othersPath = housePath + Constants.OTHERS + File.separator;
		String savePath = null;
		ContentValues values = new ContentValues();
		values.put(PictureContract.PICTURE_TIME, System.currentTimeMillis());
		if (sectionType == Constants.FACILITY_TYPE_LIVING_ROOM || sectionType == Constants.FACILITY_TYPE_BEDROOM) {
			values.put(PictureContract.PICTURE_EXSIT_STATUS, 1);
			if (sectionType == Constants.FACILITY_TYPE_LIVING_ROOM) {
				if (positionType == 0) {
					savePath = livingRoomPath + mHouseInfo.getName() + "-" + mHouseInfo.getBuilding() + "号-" + mHouseInfo.getRoom() + "室-"
							+ "客厅.jpg";
				} else if (positionType == 1) {
					savePath = livingRoomPath + mHouseInfo.getName() + "-" + mHouseInfo.getBuilding() + "号-" + mHouseInfo.getRoom() + "室-"
							+ "餐厅.jpg";
				} else {
					savePath = livingRoomPath + mHouseInfo.getName() + "-" + mHouseInfo.getBuilding() + "号-" + mHouseInfo.getRoom() + "室-"
							+ "客厅" + positionType + ".jpg";
				}
			} else if (sectionType == Constants.FACILITY_TYPE_BEDROOM) {
				if (positionType == 0) {
					savePath = bedRoomPath + mHouseInfo.getName() + "-" + mHouseInfo.getBuilding() + "号-" + mHouseInfo.getRoom() + "室-"
							+ "主卧.jpg";
				} else if (positionType == 1) {
					savePath = bedRoomPath + mHouseInfo.getName() + "-" + mHouseInfo.getBuilding() + "号-" + mHouseInfo.getRoom() + "室-"
							+ "次卧.jpg";
				} else {
					savePath = bedRoomPath + mHouseInfo.getName() + "-" + mHouseInfo.getBuilding() + "号-" + mHouseInfo.getRoom() + "室-"
							+ "次卧" + positionType + ".jpg";
				}
			}
			values.put(PictureContract.PICTURE_PATH, savePath);
			getActivity().getContentResolver().update(
					PictureContract.CONTENT_URI,
					values,
					PictureContract.PICTURE_TASKID + "=?" + " AND " + PictureContract.PICTURE_SECTION_TYPE + "=?" + " AND "
							+ PictureContract.PICTURE_POSITION_TYPE + "=?",
					new String[] { String.valueOf(mTaskId), String.valueOf(sectionType), String.valueOf(positionType) });
		} else {
			switch (sectionType) {
			case Constants.FACILITY_TYPE_DOOR_PLATE:
				savePath = housePath + mHouseInfo.getName() + "-" + mHouseInfo.getBuilding() + "号-" + mHouseInfo.getRoom() + "室-"
						+ "房门.jpg";
				values.put(PictureContract.PICTURE_PATH, savePath);
				break;
			case Constants.FACILITY_TYPE_KITCHEN:
				savePath = housePath + mHouseInfo.getName() + "-" + mHouseInfo.getBuilding() + "号-" + mHouseInfo.getRoom() + "室-"
						+ "厨房.jpg";
				values.put(PictureContract.PICTURE_PATH, savePath);
				break;
			case Constants.FACILITY_TYPE_WCROOM:
				savePath = housePath + mHouseInfo.getName() + "-" + mHouseInfo.getBuilding() + "号-" + mHouseInfo.getRoom() + "室-"
						+ "卫生间.jpg";
				values.put(PictureContract.PICTURE_PATH, savePath);
				break;
			case Constants.FACILITY_TYPE_OTHERS:
				savePath = othersPath;
				values.put(PictureContract.PICTURE_PATH, savePath);
				break;
			}
			if (sectionType == Constants.FACILITY_TYPE_OTHERS) {
				File othersFile = new File(othersPath);
				File[] othersFiles = othersFile.listFiles();
				if (othersFiles.length >= 5) {
					values.put(PictureContract.PICTURE_EXSIT_STATUS, 1);
				}
				getActivity().getContentResolver().update(PictureContract.CONTENT_URI, values,
						PictureContract.PICTURE_TASKID + "=?" + " AND " + PictureContract.PICTURE_SECTION_TYPE + "=?",
						new String[] { String.valueOf(mTaskId), String.valueOf(sectionType) });
			} else {
				values.put(PictureContract.PICTURE_EXSIT_STATUS, 1);
				getActivity().getContentResolver().update(PictureContract.CONTENT_URI, values,
						PictureContract.PICTURE_TASKID + "=?" + " AND " + PictureContract.PICTURE_SECTION_TYPE + "=?",
						new String[] { String.valueOf(mTaskId), String.valueOf(sectionType) });
			}
		}

		if (mHouseInfo != null && sectionType != Constants.FACILITY_TYPE_OTHERS) {
			File oriFile = new File(photoPath);
			File saveFile = new File(savePath);
			if (saveFile.exists()) {
				saveFile.delete();
			}
			if (oriFile.exists()) {
				oriFile.renameTo(new File(savePath));
			}
		}
	}

	private void cancelPhotoInDB() {
		ContentValues values = new ContentValues();
		values.put(PictureContract.PICTURE_EXSIT_STATUS, 0);
		values.put(PictureContract.PICTURE_TIME, System.currentTimeMillis());
		if (sectionType == Constants.FACILITY_TYPE_LIVING_ROOM || sectionType == Constants.FACILITY_TYPE_BEDROOM) {
			getActivity().getContentResolver().update(
					PictureContract.CONTENT_URI,
					values,
					PictureContract.PICTURE_TASKID + "=?" + " AND " + PictureContract.PICTURE_SECTION_TYPE + "=?" + " AND "
							+ PictureContract.PICTURE_POSITION_TYPE + "=?",
					new String[] { String.valueOf(mTaskId), String.valueOf(sectionType), String.valueOf(positionType) });
		} else {
			if (sectionType == Constants.FACILITY_TYPE_OTHERS) {
				String housePath = Constants.HOUSE_ROOT_PATH + mTaskId + File.separator;
				String othersPath = housePath + Constants.OTHERS + File.separator;
				File othersFile = new File(othersPath);
				File[] othersFiles = othersFile.listFiles();
				if (othersFiles.length == 0) {
					getActivity().getContentResolver().update(PictureContract.CONTENT_URI, values,
							PictureContract.PICTURE_TASKID + "=?" + " AND " + PictureContract.PICTURE_SECTION_TYPE + "=?",
							new String[] { String.valueOf(mTaskId), String.valueOf(sectionType) });
				}
			} else {
				getActivity().getContentResolver().update(PictureContract.CONTENT_URI, values,
						PictureContract.PICTURE_TASKID + "=?" + " AND " + PictureContract.PICTURE_SECTION_TYPE + "=?",
						new String[] { String.valueOf(mTaskId), String.valueOf(sectionType) });
			}
		}
	}

	@Click(R.id.cancel_picture_landscape)
	public void landscapeCancelPhoto() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		cancelPhoto();
	}

	@Click(R.id.save_picture_protrait)
	public void protraitTakePhoto() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		savePhoto();
	}

	@Click(R.id.cancel_picture_protrait)
	public void protraitCancelPhoto() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		cancelPhoto();
	}

	private void savePhoto() {
		savePhotoInDB();
		remove();
		isSave = true;
	}

	private void cancelPhoto() {
		cancelPhotoInDB();
		remove();
		isCancel = true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	// 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
	public static int getPreviewDegree(Activity activity) {
		// 获得手机的方向
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		int degree = 0;
		// 根据手机的方向计算相机预览画面应该选择的角度
		switch (rotation) {
		case Surface.ROTATION_0:
			degree = 90;
			break;
		case Surface.ROTATION_90:
			degree = 0;
			break;
		case Surface.ROTATION_180:
			degree = 270;
			break;
		case Surface.ROTATION_270:
			degree = 180;
			break;
		}
		return degree;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			if (!isSave) {
				File file = new File(photoPath);
				if (file.exists()) {
					file.delete();
				}
			}
			if (isCancel) {
				return;
			}
			if (getSelectListener() != null)
				getSelectListener().onSelected(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {

	}
}
