package com.manyi.mall.release;

import java.math.BigDecimal;
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
import com.manyi.mall.release.SellHuosePriceSelectFragment.PriceOption;

@EFragment
public class SellHuosePriceSelectFragment extends SelectFragment<PriceOption> {
	BaseAdapter adapter;
	@ViewById
	ListView list_view;

	private ArrayList<PriceOption> priceOptions = new ArrayList<PriceOption>(10) {
		private static final long serialVersionUID = 1L;

		{
			add(new PriceOption("不限", null, null));
			add(new PriceOption("50万以下", new BigDecimal(0), new BigDecimal(50)));
			add(new PriceOption("50~100万", new BigDecimal(50), new BigDecimal(100)));
			add(new PriceOption("100~150万", new BigDecimal(100), new BigDecimal(150)));
			add(new PriceOption("150~200万", new BigDecimal(150), new BigDecimal(200)));
			add(new PriceOption("200~250万", new BigDecimal(200), new BigDecimal(250)));
			add(new PriceOption("250~300万", new BigDecimal(250), new BigDecimal(300)));
			add(new PriceOption("300~500万", new BigDecimal(350), new BigDecimal(500)));
			add(new PriceOption("500~1000万", new BigDecimal(500), new BigDecimal(1000)));
			add(new PriceOption("1000万以上", new BigDecimal(1000), null));
		}
	};

	public static class PriceOption {

		private String label3;
		private BigDecimal startPrice;// 起始价格
		private BigDecimal endPrice;// 截止价格

		public PriceOption(String label3, BigDecimal startPrice, BigDecimal endPrice) {
			super();
			this.label3 = label3;
			this.startPrice = startPrice;
			this.endPrice = endPrice;
		}

		public String getLabel3() {
			return label3;
		}

		public void setLabel3(String label3) {
			this.label3 = label3;
		}

		public BigDecimal getStartPrice() {
			return startPrice;
		}

		public void setStartPrice(BigDecimal startPrice) {
			this.startPrice = startPrice;
		}

		public BigDecimal getEndPrice() {
			return endPrice;
		}

		public void setEndPrice(BigDecimal endPrice) {
			this.endPrice = endPrice;
		}

	}

	
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
				PriceOption op = (PriceOption) getItem(pos);
				sTextView.setText(op.getLabel3());
				return view;
			}
		});
	}

	@Override
	public View addContentView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.fragment_add_select, container, false);
		return view;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
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
	public void onItemClick(PriceOption priceOption) {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		notifySelected(priceOption);
	}

	@Click(R.id.task_title_background)
	void diss() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		remove();
	}
}
