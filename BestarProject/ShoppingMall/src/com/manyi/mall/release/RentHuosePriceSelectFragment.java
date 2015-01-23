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
import com.manyi.mall.release.RentHuosePriceSelectFragment.PriceOptionRent;

@EFragment
public class RentHuosePriceSelectFragment extends SelectFragment<PriceOptionRent> {

	private ArrayList<PriceOptionRent> priceOptions = new ArrayList<PriceOptionRent>(10) {
		private static final long serialVersionUID = 1L;

		{
			add(new PriceOptionRent("不限", null, null));
			add(new PriceOptionRent("500以下", new BigDecimal(0), new BigDecimal(500)));
			add(new PriceOptionRent("500~1000元", new BigDecimal(500), new BigDecimal(1000)));
			add(new PriceOptionRent("1000~1500元", new BigDecimal(1000), new BigDecimal(1500)));
			add(new PriceOptionRent("1500~2000元", new BigDecimal(1500), new BigDecimal(2000)));
			add(new PriceOptionRent("2000~2500元", new BigDecimal(2000), new BigDecimal(2500)));
			add(new PriceOptionRent("2500~3000元", new BigDecimal(2500), new BigDecimal(3000)));
			add(new PriceOptionRent("3000~4000元", new BigDecimal(3000), new BigDecimal(4000)));
			add(new PriceOptionRent("4000~8000元", new BigDecimal(4000), new BigDecimal(8000)));
			add(new PriceOptionRent("8000元以上", new BigDecimal(8000), null));
		}
	};

	public static class PriceOptionRent {

		private String label3;
		private BigDecimal startPrice;// 起始价格
		private BigDecimal endPrice;// 截止价格

		public PriceOptionRent(String label3, BigDecimal startPrice, BigDecimal endPrice) {
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
				PriceOptionRent op = (PriceOptionRent) getItem(pos);
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
	public void onItemClick(PriceOptionRent priceOption) {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		notifySelected(priceOption);
	}

	@Click(R.id.task_title_background)
	void diss() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		remove();
	}
}
