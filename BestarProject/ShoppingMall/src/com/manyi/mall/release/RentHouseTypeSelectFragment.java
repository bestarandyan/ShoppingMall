package com.manyi.mall.release;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SelectFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.manyi.mall.R;
import com.manyi.mall.release.RentHouseTypeSelectFragment.BedroomOption;

@EFragment
public class RentHouseTypeSelectFragment extends SelectFragment<BedroomOption> {

	private ArrayList<BedroomOption> priceOptions = new ArrayList<BedroomOption>() {
		private static final long serialVersionUID = 1L;
		{
			add(new BedroomOption("不限", 0));
			add(new BedroomOption("一室", 1));
			add(new BedroomOption("二室", 2));
			add(new BedroomOption("三室", 3));
			add(new BedroomOption("四室", 4));
			add(new BedroomOption("五室", 5));
			add(new BedroomOption("五室以上", 100));
		}
	};

	public static class BedroomOption {

		private String label1;

		public BedroomOption(String label1, int bedroomSum) {
			super();
			this.label1 = label1;
			this.bedroomSum = bedroomSum;
		}

		private int bedroomSum;// 房型(几房)

		public String getLabel1() {
			return label1;
		}

		public void setLabel1(String label1) {
			this.label1 = label1;
		}

		public int getBedroomSum() {
			return bedroomSum;
		}

		public void setBedroomSum(int bedroomSum) {
			this.bedroomSum = bedroomSum;
		}

	}

	BaseAdapter adapter;
	@ViewById
	ListView list_view;

	@AfterViews
	public void init() {
		list_view.setAdapter(new BaseAdapter() {
			private LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			@Override
			public int getCount() {
				return priceOptions.size();
			}

			@Override
			public Object getItem(int pos) {
				return priceOptions.get(pos);
			}

			@Override
			public long getItemId(int pos) {
				return pos;
			}

			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {
				View view;

				if (convertView == null)
					view = mInflater.inflate(R.layout.item_select_list, parent, false);
				else
					view = convertView;

				TextView sTextView = (TextView) view.findViewById(R.id.selectshow);
				BedroomOption op = (BedroomOption) getItem(pos);
				sTextView.setText(op.getLabel1());
				return view;
			}
		});
	}

	@Override
	public View addContentView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.fragment_add_select, container, false);
		return view;
	}
	@Override
	public void onViewCreated(final View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		TranslateAnimation animation = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.dialog_animation_translate);
		animation.setFillAfter(true);
		view.findViewById(R.id.list_view).setAnimation(animation);
		final TextView backTextView = (TextView) view.findViewById(R.id.task_title_background);
		backTextView.setVisibility(View.GONE);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				backTextView.setVisibility(View.VISIBLE);
				AlphaAnimation  alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
				alphaAnimation.setDuration(500);
				backTextView.setAnimation(alphaAnimation);
			}
		});
	}
	@ItemClick(R.id.list_view)
	@UiThread
	public void onItemClick(BedroomOption bedroomOption) {
		if( CheckDoubleClick.isFastDoubleClick() )  return;
		notifySelected(bedroomOption);
	}

	@Click(R.id.task_title_background)
	void diss() {
		if( CheckDoubleClick.isFastDoubleClick() )  return;
		remove();
	}
}
