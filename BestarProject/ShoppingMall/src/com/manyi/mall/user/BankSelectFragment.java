/**
 * 
 */
package com.manyi.mall.user;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.provider.contract.BankListDBUtil;

/**
 * @author bestar
 *
 */
@EFragment(R.layout.fragment_bank_select)
public class BankSelectFragment extends SuperFragment<String>{
	@ViewById(R.id.bank_list)
	ListView mListView;
	
	@ViewById(R.id.bank_select_back)
	TextView mBankBackView;
	
	@ViewById(R.id.search_bank_et)
	EditText mSearchEt;
	
	@ViewById(R.id.search_clear)
	LinearLayout mClearLayout;
	
	@ViewById(R.id.voiceBtn)
	Button mVoiceBtn;
	
	private ArrayList<String> mBankList = new ArrayList<String>();
	
	private ArrayAdapter<String> mAdapter = null;

	/**
	 * Initialize fragment, show bank list.
	 */
	@AfterViews
	void initData(){
		mClearLayout.setVisibility(View.INVISIBLE);
		mBankList = BankListDBUtil.getInstance().getBankList("");
		mAdapter = new ArrayAdapter<String>(getActivity(),R.layout.item_dialog_select, R.id.dialog_select_text, mBankList);
		mListView.setAdapter(mAdapter);
		mSearchEt.requestFocus();
		ManyiUtils.showKeyBoard(getActivity(), mSearchEt);
	}
	
	/**
	 * Search bank by input.
	 * @param text
	 * @param hello
	 */
	@AfterTextChange(R.id.search_bank_et)
	void OnTextChangeListener(Editable text, TextView hello){
		String string = text.toString();
		if(string != null && string.length()>0){
			mClearLayout.setVisibility(View.VISIBLE);
			mBankList = BankListDBUtil.getInstance().getBankList(string);
		}else {
			mClearLayout.setVisibility(View.INVISIBLE);
			mBankList = BankListDBUtil.getInstance().getBankList("");
		}
		mAdapter = new ArrayAdapter<String>(getActivity(),R.layout.item_dialog_select, R.id.dialog_select_text, mBankList);
		mListView.setAdapter(mAdapter);
	}
	
	/**
	 * Back to the previous fragment.
	 */
	@Click(R.id.bank_select_back)
	void back(){
		if (CheckDoubleClick.isFastDoubleClick()) {
			return ;
		}
		remove();
	}
	
	@Click(R.id.search_clear)
	void clearSearchEt(){
		if (CheckDoubleClick.isFastDoubleClick()) {
			return ;
		}
		mSearchEt.setText("");
		mClearLayout.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Choose on bank and back to previous fragment.
	 * @param position
	 */
	@ItemClick(R.id.bank_list)
	void OnItemClickListener(int position){
		if (CheckDoubleClick.isFastDoubleClick()) {
			return ;
		}
		if (getSelectListener()!=null) {
			notifySelected(mBankList.get(position));
		}
	}

}
