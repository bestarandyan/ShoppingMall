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
import com.manyi.mall.release.RentHousesizeSelectFragment.SpaceAreaOption;

@EFragment
public class RentHousesizeSelectFragment extends SelectFragment<SpaceAreaOption> {
	@ViewById(R.id.list_view)
	ListView list_view;
	private ArrayList<SpaceAreaOption> spaceAreaOption = new ArrayList<SpaceAreaOption>(10) {
		private static final long serialVersionUID = 1L;

		{
			add(new SpaceAreaOption("不限", null, null));
			add(new SpaceAreaOption("50平米以下", new BigDecimal(0), new BigDecimal(50)));
			add(new SpaceAreaOption("50~70平米", new BigDecimal(50), new BigDecimal(70)));
			add(new SpaceAreaOption("70~90平米", new BigDecimal(70), new BigDecimal(90)));
			add(new SpaceAreaOption("90~110平米", new BigDecimal(90), new BigDecimal(110)));
			add(new SpaceAreaOption("110~130平米", new BigDecimal(110), new BigDecimal(130)));
			add(new SpaceAreaOption("130~150平米", new BigDecimal(130), new BigDecimal(150)));
			add(new SpaceAreaOption("150~200平米", new BigDecimal(150), new BigDecimal(200)));
			add(new SpaceAreaOption("200~300平米", new BigDecimal(200), new BigDecimal(300)));
			add(new SpaceAreaOption("300平米以上", new BigDecimal(300), null));

		}
	};

	public static class SpaceAreaOption {
		private String label2;

		public SpaceAreaOption(String label2, BigDecimal startSpaceArea, BigDecimal endSpaceArea) {
			super();
			this.label2 = label2;
			this.startSpaceArea = startSpaceArea;
			this.endSpaceArea = endSpaceArea;
		}

		private BigDecimal startSpaceArea;// 起始内空面积
		private BigDecimal endSpaceArea;// 截止内空面积

		public String getLabel2() {
			return label2;
		}

		public void setLabel2(String label2) {
			this.label2 = label2;
		}

		public BigDecimal getStartSpaceArea() {
			return startSpaceArea;
		}

		public void setStartSpaceArea(BigDecimal startSpaceArea) {
			this.startSpaceArea = startSpaceArea;
		}

		public BigDecimal getEndSpaceArea() {
			return endSpaceArea;
		}

		public void setEndSpaceArea(BigDecimal endSpaceArea) {
			this.endSpaceArea = endSpaceArea;
		}

	}

	@AfterViews
	public void init() {
		list_view.setAdapter(new BaseAdapter() {
			private LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			@Override
			public int getCount() {
				return spaceAreaOption.size();
			}

			@Override
			public Object getItem(int pos) {
				return spaceAreaOption.get(pos);
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
				SpaceAreaOption op = (SpaceAreaOption) getItem(pos);
				sTextView.setText(op.getLabel2());
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
	public void onItemClick(SpaceAreaOption so) {
		if( CheckDoubleClick.isFastDoubleClick() )  return;
		// RentSelcetInfo areaResponse = new RentSelcetInfo();
		// areaResponse.setLabel(spaceAreaOption1.getLabel());
		// areaResponse.setSpaceArea(spaceAreaOption1.getSpaceArea()) ;
		//
		notifySelected(so);

	}

	@Click(R.id.task_title_background)
	void diss() {
		if( CheckDoubleClick.isFastDoubleClick() )  return;
		remove();
	}
}
