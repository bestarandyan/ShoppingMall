package com.manyi.mall.search;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.widget.db.SearchDB;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.SearchRequest;
import com.manyi.mall.cachebean.search.SearchRespose;
import com.manyi.mall.cachebean.search.SearchRespose.Estate;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.CommonService;
import com.manyi.mall.user.AddEstateFragment;

@EFragment(R.layout.fragement_search_info)
public class SearchInfoFragment extends SuperFragment<Estate> {
	

	@ViewById(R.id.lv_search)
	public ListView mSearchListView;
	@ViewById(R.id.lv_record)
	public ListView mRecordListView;

	@ViewById(R.id.search_edtext)
	public EditText mSearchEdtext;

	@ViewById(R.id.clickfiltrate)
	public TextView mClickFiltrate;

	private SearchAdapter mSearchAdapter;
	private ArrayList<Estate> mSearchList;
	private ArrayList<SpannableStringBuilder> mSearchShowList;

	private ArrayList<SearchDB> mSearchDB;

	// 历史记录布局
	@ViewById(R.id.ll_record)
	public LinearLayout mRecordLayout;
	@ViewById(R.id.tv_record)
	public TextView mRecordTextView;
	@ViewById(R.id.add_layout)
	public LinearLayout mAddLayout; // 新增小区Layout
	@ViewById(R.id.serch_add)
	public Button mAddSerchButton; // 新增小区Button

	@FragmentArg
	String targetClass;

	private Estate mEstate;

	private int uid;

	/**
	 * 请求网络数据，刷新列表
	 */
	public void search() {
		if (!mSearchEdtext.getText().toString().equals("")) {
			// 有内容就搜索
			dealStr(mSearchEdtext.getText().toString());
		}
	}

	public CommonService commonService;

	private SearchRecordAdapter mRecordAdapter;

	@AfterViews
	public void initSearchInfo() {

		uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);

		// 历史记录列表
		mSearchShowList = new ArrayList<SpannableStringBuilder>();
		mSearchDB = new ArrayList<SearchDB>();
		SearchDB.getAll(getActivity(), String.valueOf(uid), mSearchDB);
		mRecordAdapter = new SearchRecordAdapter(getActivity(), mSearchDB);
		mRecordListView.setAdapter(mRecordAdapter);
		if (mSearchDB.size() > 0) { // 如果有数据 就添加 到 表里去
			mRecordTextView.setVisibility(View.GONE);// 暂无历史记录
			mSearchDB.add(new SearchDB());
		} else {
			mRecordTextView.setVisibility(View.VISIBLE);
		}

		mSearchList = new ArrayList<Estate>();
		mSearchAdapter = new SearchAdapter(getActivity(), mSearchShowList);

		mSearchListView.setAdapter(mSearchAdapter);

		mRecordListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (mSearchDB.get(position).getSearch_content() == null) {

					ManyiAnalysis.onEvent(SearchInfoFragment.this.getActivity(), "ClearRecordsClick");

					// 点击的是清除搜索记录操作
					SearchDB.clearAll(getActivity(), String.valueOf(uid));
					mSearchDB.clear();
					mRecordAdapter.notifyDataSetChanged();
					mRecordListView.setVisibility(View.GONE);
					mRecordTextView.setVisibility(View.VISIBLE);
					closeKeyBoard(getActivity(), mSearchEdtext);
				}

				else {
					try {
						ManyiAnalysis.onEvent(SearchInfoFragment.this.getActivity(), "SearchHistoyOnSellPageClick");
						mEstate = new Estate();
						mEstate.setEstateId(Integer.parseInt(mSearchDB.get(position).getSearch_date()));
						mEstate.setEstateName(mSearchDB.get(position).getSearch_content());

						if (getSelectListener() != null) {
							notifySelected(mEstate);
						} else {
							SearchResultFragment targetFragment = SearchResultFragment.class.cast(Fragment.instantiate(getBackOpActivity(),
									targetClass));
							targetFragment.tag = targetFragment.getClass().getName();
							Bundle bundle = new Bundle();
							bundle.putSerializable("estate", mEstate);
							targetFragment.setArguments(bundle);
							targetFragment.setManager(getFragmentManager());
							targetFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
									R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);
							targetFragment.show();
							closeKeyBoard(getActivity(), mSearchEdtext);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		mSearchListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case SCROLL_STATE_FLING:
					closeKeyBoard(getActivity(), mSearchEdtext);
					break;
				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});

		mRecordListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case SCROLL_STATE_FLING:
					closeKeyBoard(getActivity(), mSearchEdtext);
					break;
				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});

		mSearchListView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("Recycle")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// 保存历史记录
				SpannableStringBuilder searchInfo = mSearchShowList.get(position);
				if (mSearchShowList.size() > 0) {
					// 显示历史记录列表
					mRecordTextView.setVisibility(View.GONE);
					mRecordListView.setVisibility(View.VISIBLE);

					// 保存一条选中历史记录
					SearchDB searchDB = new SearchDB();
					searchDB.setSearch_content(searchInfo.toString());
					searchDB.setSearch_date(String.valueOf(mSearchList.get(position).getEstateId()));
					searchDB.setUser_id(String.valueOf(uid));
					if (mSearchDB.size() > 0) {
						searchDB.save(getActivity(), mSearchDB.size(), mSearchDB.get(0).getSearch_id());
					} else {
						searchDB.save(getActivity(), mSearchDB.size(), "");
					}
					// 刷新历史记录列表（需要先删除历史记录列表，然后再从数据库中从新查询一次数据）
					mSearchDB.clear();
					SearchDB.getAll(getActivity(), String.valueOf(uid), mSearchDB);
					mSearchDB.add(new SearchDB());
					mRecordAdapter.notifyDataSetChanged();

					Estate estate = new Estate();
					estate.setEstateName(((Estate)mSearchList.get(position)).getEstateName());
					estate.setAliasName(((Estate)mSearchList.get(position)).getAliasName());
					estate.setCityName(((Estate)mSearchList.get(position)).getCityName());
					estate.setTownName(((Estate)mSearchList.get(position)).getTownName());
					estate.setEstateId(mSearchList.get(position).getEstateId());
					if (getSelectListener() != null) {
						notifySelected(estate);
						closeKeyBoard(getActivity(), mSearchEdtext);
					} else {
						SearchResultFragment targetFragment = SearchResultFragment.class.cast(Fragment.instantiate(getBackOpActivity(),
								targetClass));
						targetFragment.tag = targetFragment.getClass().getName();

						Bundle bundle = new Bundle();
						bundle.putSerializable("estate", estate);
						targetFragment.setArguments(bundle);

						targetFragment.setManager(getFragmentManager());
						targetFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
								R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);
						targetFragment.show();
						closeKeyBoard(getActivity(), mSearchEdtext);
					}
				}
			}
		});

		loadDate();

		mSearchEdtext.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

				if (mSearchEdtext.getText().toString().equals("")) {
					mRecordLayout.setVisibility(View.VISIBLE);
					mSearchListView.setVisibility(View.GONE);
					mAddLayout.setVisibility(View.GONE);
				}

				loadDate();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Background
	public void loadDate() {
		try {

			if (mSearchEdtext.getText().toString().length() != 0) {
				int cityId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("cityId", 0);
				SearchRequest searchRequest = new SearchRequest();
				searchRequest.setName(mSearchEdtext.getText().toString());
				searchRequest.setCityId(cityId);
				SearchRespose searchRespose = commonService.search(searchRequest);
				mSearchList.clear();
				mSearchList.addAll((ArrayList<Estate>) searchRespose.getEstateList());
				refresh();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@UiThread
	public void refresh() {
		search();
	}

	/**
	 * 处理关键字显示变色
	 * 
	 * @param str
	 */
	public void dealStr(String str) {
		mSearchShowList.clear();
		if (str != null && !str.trim().equals("")) {
			for (int i = 0; i < mSearchList.size(); i++) {
				Log.i("syso", "匹配的内容：" + mSearchList.get(i).getEstateName());
				// 重新构建新的字符串类
				SpannableStringBuilder builder = new SpannableStringBuilder(mSearchList.get(i).getEstateName());
				// int startPosition = searchList.get(i).getName().indexOf(str);
				// int endPosition = startPosition + str.length();
				// 参数一：指定改变内容的字体的颜色
				// 参数二：从哪里开始
				// 参数二：到哪里结束
				// 参数四：数学里面的区间 例(1,5)--不包括1并且不包括5，去他们的中间内容
				// builder.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.red)), startPosition, endPosition,
				// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				mSearchShowList.add(builder);
			}
		}
		// 判断是否显示搜索列表还是显示历史列表
		if (mSearchShowList.size() > 0) {
			// searchShowList.add(new SpannableStringBuilder(""));
			mSearchListView.setVisibility(View.VISIBLE);
			mRecordLayout.setVisibility(View.GONE);
		} else {
			if (str.equals("")) {
				mAddLayout.setVisibility(View.GONE);
				mSearchListView.setVisibility(View.GONE);
				mRecordLayout.setVisibility(View.VISIBLE);
			} else {
				mAddLayout.setVisibility(View.VISIBLE);
				mSearchListView.setVisibility(View.GONE);
				mRecordLayout.setVisibility(View.GONE);
			}

		}
		mSearchAdapter.notifyDataSetChanged();
	}

	public class SearchAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<SpannableStringBuilder> searchInfos;

		public SearchAdapter(Context context, ArrayList<SpannableStringBuilder> searchInfos) {
			this.context = context;
			this.searchInfos = searchInfos;
		}

		@Override
		public int getCount() {
			return searchInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return searchInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViweHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
				holder = new ViweHolder();
				holder.searchTextView = (TextView) convertView.findViewById(R.id.tv_search);
				holder.titleTextView = (TextView) convertView.findViewById(R.id.tv_title);
				holder.titleLayout = (RelativeLayout) convertView.findViewById(R.id.title_layout);
				holder.contentLayout = (LinearLayout) convertView.findViewById(R.id.content_layout);
				convertView.setTag(holder);
			} else {
				holder = (ViweHolder) convertView.getTag();
			}
			if (searchInfos.get(position).toString().equals("")) {
				holder.titleLayout.setVisibility(View.VISIBLE);
				holder.contentLayout.setVisibility(View.GONE);
				holder.searchTextView.setText("搜索\"".concat(searchInfos.get(position).toString()).concat("\""));
			} else {
				holder.titleLayout.setVisibility(View.GONE);
				holder.contentLayout.setVisibility(View.VISIBLE);
				SpannableStringBuilder style = new SpannableStringBuilder((CharSequence) searchInfos.get(position));

				holder.titleTextView.setText(style);
			}

			return convertView;
		}
	}

	class ViweHolder {
		TextView titleTextView;
		TextView searchTextView;
		LinearLayout contentLayout;
		RelativeLayout titleLayout;
	}

	public class SearchRecordAdapter extends BaseAdapter {
		private Context context;
		private ArrayList<SearchDB> searchDBs;

		public SearchRecordAdapter(Context context, ArrayList<SearchDB> searchDBs) {
			this.context = context;
			this.searchDBs = searchDBs;
		}

		@Override
		public int getCount() {
			return searchDBs.size();
		}

		@Override
		public Object getItem(int position) {
			return searchDBs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			class ViewHolder {
				TextView searchContentTextView;
			}

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_search_record, parent, false);
				holder.searchContentTextView = (TextView) convertView.findViewById(R.id.tv_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (searchDBs.get(position).getSearch_content() == null) {
				holder.searchContentTextView.setGravity(Gravity.CENTER);
				holder.searchContentTextView.setText("清除搜索记录");
			} else {
				holder.searchContentTextView.setGravity(Gravity.CENTER_VERTICAL);
				holder.searchContentTextView.setText(searchDBs.get(position).getSearch_content());
			}
			return convertView;
		}

	}

	@Click(R.id.clickfiltrate)
	void clickfiltrate() {
		AreaSelectFragment searchInfoFragment = GeneratedClassUtils.getInstance(AreaSelectFragment.class);
		Bundle args = new Bundle();
		args.putString("targetClass", targetClass);
		searchInfoFragment.setArguments(args);

		searchInfoFragment.tag = AreaSelectFragment.class.getName();
		searchInfoFragment.setManager(getFragmentManager());
		searchInfoFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		searchInfoFragment.show();
		closeKeyBoard(getActivity(), mSearchEdtext);
	}

	public static void closeKeyBoard(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (((Activity) context).getCurrentFocus() != null && ((Activity) context).getCurrentFocus().getWindowToken() != null) {
			imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public void closeKeyBoard(Context context, EditText editText) {
		InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		// 强制隐藏键盘
		inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	// 点击Button　隐藏软键盘
	@Click(R.id.serch_add)
	public void serchAdd() {

		closeKeyBoard(getActivity());

		AddEstateFragment newHEFragment = GeneratedClassUtils.getInstance(AddEstateFragment.class);
		Bundle args = new Bundle();
		args.putString("label", mSearchEdtext.getText().toString());
		newHEFragment.setArguments(args);
		newHEFragment.tag = AddEstateFragment.class.getName();

		newHEFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		newHEFragment.setManager(getFragmentManager());
		newHEFragment.show();
	}

	@Click(R.id.bt_clear)
	public void serachClear() {
		mSearchEdtext.setText("");
	}

	@Click(R.id.serchback)
	void serchback() {
		closeKeyBoard(getActivity(), mSearchEdtext);
		remove();
	}
}
