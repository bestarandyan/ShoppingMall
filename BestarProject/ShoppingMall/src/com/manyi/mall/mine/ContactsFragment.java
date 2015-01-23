package com.manyi.mall.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.springframework.util.StringUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.RecommendRequest;
import com.manyi.mall.common.CommonConfig;
import com.manyi.mall.common.Constants;
import com.manyi.mall.common.util.AESUtil;
import com.manyi.mall.mine.ContactsLetterListView.OnTouchingLetterChangedListener;
import com.manyi.mall.service.UcService;

/**
 * 联系人列表
 */

@SuppressLint("DefaultLocale")
@EFragment(R.layout.fragment_contacts_list)
public class ContactsFragment extends SuperFragment<Integer> {

	private BaseAdapter adapter;
	@ViewById(R.id.list_view)
	public ListView personList;
	private TextView overlay;
	@ViewById(R.id.MyLetterListView01)
	public ContactsLetterListView letterListView;
	private AsyncQueryHandler asyncQuery;
	private static final String NAME = "name", NUMBER = "number", SORT_KEY = "sort_key";
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	private String[] sections;// 存放存在的汉语拼音首字母
	private Handler handler;
	private OverlayThread overlayThread;
	@ViewById(R.id.non_contact)
	public TextView mNullContacts;
	@ViewById(R.id.next_commit)
	public Button mNextBtn;
	
	@ViewById(R.id.progressView)
	ProgressBar mProgressBar;
	private List<ContentValues> list = null;
	private int checkNum = 0;
	private UcService mUserService;


	/* 建立两个mServiceReceiver对象，作为类成员变量 */
	// private mSMSServiceReceiver mReceiver01, mReceiver02;

	private String mRecommendMsg;

	@SuppressWarnings("unused")
	private ArrayList<String> mSuccessNum;

	@AfterViews
	public void init() {
		if (getArguments() != null) {
			mRecommendMsg = getArguments().getString("recommendmsg");
		}
		letterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
		asyncQuery = new MyAsyncQueryHandler(getActivity().getContentResolver());
		alphaIndexer = new HashMap<String, Integer>();
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what == 1){
					if (list == null || list.size() == 0) {
						mProgressBar.setVisibility(View.GONE);
					}else {
						personList.setVisibility(View.VISIBLE);
					}
				}
			}
			
		};
		overlayThread = new OverlayThread();
		initOverlay();
		
		handler.postDelayed(getDataRunnable, 500);

	}
	
	Runnable getDataRunnable = new Runnable() {
		
		@Override
		public void run() {
			Uri uri = Uri.parse("content://com.android.contacts/data/phones");
			String[] projection = { "_id", "display_name", "data1", "sort_key" };
			asyncQuery.startQuery(0, null, uri, projection, null, null, "sort_key COLLATE LOCALIZED asc");
			handler.sendEmptyMessage(1);
		}
	};
	
	

	@Click({ R.id.contact_back })
	public void registerBack() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		remove();
	}

	@Click(R.id.next_commit)
	public void nextClick() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		if (checkNum <= 0) {
			return;
		}
		final ArrayList<String> array = new ArrayList<String>();
		String tempStr = "";
		for (int i = 0; i < adapter.getCount(); i++) {
			if (((ListAdapter) adapter).getSelectList().get(i)) {
				ContentValues cv = list.get(i);
				tempStr = cv.getAsString(NUMBER);
				if (tempStr != null && StringUtils.hasLength(tempStr)) {
					array.add(tempStr);
				}
			}
		}
		if (array.size() <= 0) {
			return;
		}
		confirm(array);
	}

	public void confirm(final ArrayList<String> array) {
		Dialog dialog = new AlertDialog.Builder(getActivity()).setTitle("确认").setMessage("确定发送推荐短信吗？").setPositiveButton(("确定"), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						sendRecommendService(array);
						sendMessage((mRecommendMsg != null && StringUtils.hasLength(mRecommendMsg)) ? mRecommendMsg : getResources().getString(R.string.share_app_content), array);
						registerBack();
					}
				}).start();

			}
		}).setNegativeButton(("取消"), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create();
		dialog.show();
	}

	private void sendRecommendService(ArrayList<String> array) {
		StringBuilder builder = new StringBuilder();
		for (String num : array) {
			builder.append(num + ",");
		}
		RecommendRequest request = new RecommendRequest();
		int uid = getActivity().getSharedPreferences("LOGIN_times", 0).getInt("uid", 0);
		request.setUid(uid);
		request.setRecommendTel(builder.toString().trim());
		String phone = getActivity().getSharedPreferences("LOGIN_times", 0).getString("name", "");
		try {
			request.setUserTel(AESUtil.decrypt(phone, CommonConfig.AES_KEY));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Response response = mUserService.saveRecommendNum(request);
		int responseCode = response.getErrorCode();
		if (responseCode == 0) {
			tip("推荐成功");
		} else {
			tip("推荐失败");
		}
	}

	private void tip(final String message) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void sendMessage(String message, ArrayList<String> array) {
		// 移动运营商允许每次发送的字节数据有限，我们可以使用Android给我们提供 的短信工具。
		SmsManager sms = SmsManager.getDefault();
		// 如果短信没有超过限制长度，则返回一个长度的List。
		List<String> texts = sms.divideMessage(message);

		/* 建立自定义Action常数的Intent(给PendingIntent参数之用) */
		Intent itSend = new Intent(Constants.SMS_SEND_ACTIOIN);
		Intent itDeliver = new Intent(Constants.SMS_DELIVERED_ACTION);

		/* sentIntent参数为传送后接受的广播信息PendingIntent */
		PendingIntent mSendPI = PendingIntent.getBroadcast(getActivity(), 0, itSend, 0);

		/* deliveryIntent参数为送达后接受的广播信息PendingIntent */
		PendingIntent mDeliverPI = PendingIntent.getBroadcast(getActivity(), 0, itDeliver, 0);

		/* 发送SMS短信，注意倒数的两个PendingIntent参数 */
		if (message.length() > 70) {
			for (String number : array) {
				for (String text : texts) {
					sms.sendTextMessage(number, null, text, mSendPI, mDeliverPI);
				}
			}
		} else {
			for (String number : array) {
				sms.sendTextMessage(number, null, message, mSendPI, mDeliverPI);
			}
		}
	}

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (CheckDoubleClick.isFastDoubleClick())
				return;
			// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
			ListAdapter.ViewHolder holder = (ListAdapter.ViewHolder) view.getTag();
			String num = list.get(position).getAsString(NUMBER).replace(" ", "");
			num = num.replace("-", "");
			// if (!checkMobile(num.trim())) {
			// Toast.makeText(getActivity(), "你选择的号码不正确:" + num, Toast.LENGTH_SHORT).show();
			// return;
			// }
			// 改变CheckBox的状态
			holder.cb.toggle();
			// 将CheckBox的选中状况记录下来
			((ListAdapter) adapter).getSelectList().put(position, holder.cb.isChecked());
			if (holder.cb.isChecked()) {
				checkNum++;
			} else {
				checkNum--;
			}
			if (checkNum >= 0) {
				mNextBtn.setText("发送短信(" + checkNum + ")");
			} else {
				mNextBtn.setText("发送短信(0)");
			}
		}
	};

	// /**
	// * 正则 检查 手机号的合法性 电话号码的验证 13[0-9] , 15[0-9] , 18[0-9], 11位数字
	// *
	// * @param mobile
	// * @return true 通过; false 不通过
	// */
	// public static boolean checkMobile(String mobile) {
	// // 电话号码的验证 13[0-9] , 15[0-9] , 18[0-9], 11位数字
	// Pattern patternTel = Pattern.compile("1[3,5,8,7,4]\\d{9}");
	// Matcher matcherTel = patternTel.matcher(mobile);
	// return matcherTel.matches();
	// }

	// 查询联系人
	@SuppressLint("HandlerLeak")
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);

		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				list = new ArrayList<ContentValues>();
				Map<String, ArrayList<ContentValues>> contentMap = new HashMap<String, ArrayList<ContentValues>>();
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					ContentValues cv = new ContentValues();
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					String number = cursor.getString(2);
					String sortKey = cursor.getString(3);// Cn2Spell.converterToSpell(name);
					if (number.startsWith("+86")) {
						cv.put(NAME, name);
						cv.put(NUMBER, number.substring(3)); // 去掉+86
						cv.put(SORT_KEY, sortKey);
					} else {
						cv.put(NAME, name);
						cv.put(NUMBER, number);
						cv.put(SORT_KEY, sortKey);
					}

					// 获得汉语拼音首字母
					String indexChar = getAlpha(cv.getAsString(SORT_KEY));
					if (contentMap.get(indexChar) != null) {
						contentMap.get(indexChar).add(cv);
					} else {
						ArrayList<ContentValues> values = new ArrayList<ContentValues>();
						values.add(cv);
						contentMap.put(indexChar, values);
					}
				}
				for (char j = 'A'; j <= 'Z'; j++) {
					if (contentMap.get(j + "") != null) {
						list.addAll(contentMap.get(j + ""));
					}
				}
				if (contentMap.get("#") != null) {
					list.addAll(contentMap.get("#"));
				}

				if (list.size() > 0) {
					setAdapter(list);
				}
			} else {
				personList.setVisibility(View.GONE);
				letterListView.setVisibility(View.GONE);
				mNextBtn.setVisibility(View.GONE);
				mNullContacts.setVisibility(View.VISIBLE);
			}
		}
	}

	private void setAdapter(List<ContentValues> list) {
		adapter = new ListAdapter(getActivity(), list);
		personList.setAdapter(adapter);
		personList.setOnItemClickListener(mItemClickListener);
	}

	private class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<ContentValues> list;
		// 用来控制CheckBox的选中状况
		private SparseBooleanArray mSelectList;

		public ListAdapter(Context context, List<ContentValues> list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[list.size()];
			mSelectList = new SparseBooleanArray(list.size());
			for (int i = 0; i < list.size(); i++) {
				// 当前汉语拼音首字母
				String currentStr = getAlpha(list.get(i).getAsString(SORT_KEY));
				// 上一个汉语拼音首字母，如果不存在为“ ”
				String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1).getAsString(SORT_KEY)) : " ";
				if (!previewStr.equals(currentStr)) {
					String name = getAlpha(list.get(i).getAsString(SORT_KEY));
					alphaIndexer.put(name, i);
					sections[i] = name;
				}
			}

		}

		public SparseBooleanArray getSelectList() {
			return mSelectList;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_contact_list, null);
				holder = new ViewHolder();
				holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.number = (TextView) convertView.findViewById(R.id.number);
				holder.cb = (CheckBox) convertView.findViewById(R.id.checkbox);
				holder.line_i = (View) convertView.findViewById(R.id.view_i);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ContentValues cv = list.get(position);
			holder.name.setText(cv.getAsString(NAME));
			holder.number.setText(cv.getAsString(NUMBER));
			holder.cb.setChecked(mSelectList.get(position));
			String currentStr = getAlpha(list.get(position).getAsString(SORT_KEY));
			String previewStr = (position - 1) >= 0 ? getAlpha(list.get(position - 1).getAsString(SORT_KEY)) : " ";
			if (!previewStr.equals(currentStr)) {
				holder.alpha.setVisibility(View.VISIBLE);
				holder.line_i.setVisibility(View.VISIBLE);
				holder.alpha.setText(currentStr);
			} else {
				holder.alpha.setVisibility(View.GONE);
				holder.line_i.setVisibility(View.GONE);
			}
			return convertView;
		}

		private class ViewHolder {
			TextView alpha;
			TextView name;
			TextView number;
			CheckBox cb;
			View line_i;
		}

	}

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		overlay = (TextView) inflater.inflate(R.layout.view_contact_letter, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	private class LetterListViewListener implements OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				personList.setSelection(position);
				overlay.setText(sections[position]);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// 延迟一秒后执行，让overlay为不可见
				handler.postDelayed(overlayThread, 1500);
			}
		}

	}

	// 设置overlay不可见
	private class OverlayThread implements Runnable {

		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}

	}

	// 获得汉语拼音首字母
	private String getAlpha(String str) {
		if (str == null || str.trim().length() == 0) {
			return "#";
		}

		String c = str.trim().substring(0, 1);
		// 判断首字母是否为简体或者繁体
		Pattern word = Pattern.compile("^[\u4E00-\u9FFF]+$");
		if (word.matcher(c).matches()) {
			c = Cn2Spell.converterToFirstSpell(c);
		}
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c).matches()) {
			return (c).toUpperCase();
		} else {
			return "#";
		}
	}

}
