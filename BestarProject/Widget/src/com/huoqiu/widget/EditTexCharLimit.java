package com.huoqiu.widget;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTexCharLimit extends EditText {

	public EditTexCharLimit(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.addTextChangedListener(new onEditTextCharLimit());
	}

	public EditTexCharLimit(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.addTextChangedListener(new onEditTextCharLimit());
	}

	public EditTexCharLimit(Context context) {
		super(context);
		this.addTextChangedListener(new onEditTextCharLimit());
	}
	
	
	public class onEditTextCharLimit implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
//			if(!s.toString().equals("")){
//				String string = PinYinUtil.isStringChineseCharacters(s.toString());
//				if(string!=null){
//					String replace = EditTexCharLimit.this.getText().toString().replace(string, "");
//					EditTexCharLimit.this.setText(replace);
//					EditTexCharLimit.this.setSelection(EditTexCharLimit.this.getText().toString().length());
//				}
//			}
		}
		
	}

}
