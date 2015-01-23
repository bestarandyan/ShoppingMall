/**
 * 
 */
package com.manyi.mall.release;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SelectFragment;
import com.manyi.mall.R;
import com.manyi.mall.release.ShortcutsSelectFragment.ReleaseShortcuts;

/**
 * @author ryan2014
 * 
 */
@EFragment
public class ShortcutsSelectFragment extends SelectFragment<ReleaseShortcuts> {

	private ArrayList<ReleaseShortcuts> arrayList = new ArrayList<ReleaseShortcuts>() {

		private static final long serialVersionUID = 1L;

		{
			add(new ReleaseShortcuts("发布出售"));
			add(new ReleaseShortcuts("发布出租"));

		}

	};

	public static class ReleaseShortcuts {
		private String shortcutstitle;

		public ReleaseShortcuts(String shortcutstitle) {
			super();
			this.shortcutstitle = shortcutstitle;
		}

		public String getShortcutstitle() {
			return shortcutstitle;
		}

		public void setShortcutstitle(String shortcutstitle) {
			this.shortcutstitle = shortcutstitle;
		}

	}

	@ViewById
	ListView short_list_view;

	@AfterViews
	public void init() {
		short_list_view.setAdapter(new BaseAdapter() {
			class ViewHolder {
				public TextView sTextView;
			}

			private LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			@Override
			public int getCount() {
				return arrayList.size();
			}

			@Override
			public Object getItem(int pos) {
				return arrayList.get(pos);
			}

			@Override
			public long getItemId(int pos) {
				return pos;
			}

			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {
				ViewHolder holder;

				if (convertView == null) {
					holder = new ViewHolder();
					convertView = mInflater.inflate(R.layout.item_shortcuts_select, parent, false);
					holder.sTextView = (TextView) convertView.findViewById(R.id.shortcuts_tv);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				ReleaseShortcuts op = (ReleaseShortcuts) getItem(pos);
				holder.sTextView.setText(op.getShortcutstitle());
				return convertView;
			}
		});

	}

	//
	@Override
	public View addContentView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.fragment_shortcuts_select, container, false);

		return view;
	}
	@Override
	public void onViewCreated(final View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		AnimationSet animation = (AnimationSet) AnimationUtils.loadAnimation(getActivity(), R.anim.dialog_animation_set);
		animation.setFillAfter(true);
		view.findViewById(R.id.short_list_view).setAnimation(animation);


	}
	@ItemClick(R.id.short_list_view)
	public void onItemClick(ReleaseShortcuts priceOption) {
		notifySelected(priceOption);
	}

}
