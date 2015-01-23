/**
 * 
 */
package com.huoqiu.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * @author bestar
 * 
 */
public class PhoneEditTextView extends EditText {
	
	private InputFilter [] phoneFilters = new InputFilter[]{new InputFilter.LengthFilter(100)};
	
	private InputFilter [] telFilters = new InputFilter[]{new InputFilter.LengthFilter(100)};

	public PhoneEditTextView(Context context) {
		super(context);
		phoneNumAddSpace();
	}

	public PhoneEditTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		phoneNumAddSpace();
	}

	public PhoneEditTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		phoneNumAddSpace();
	}
	
	public String getTextString(){
		String text = getText().toString();
		text = text.replace(" ", "");
		return text;
	}

	/**
	 * 
	 * 手机号码格式设置
	 * 如 ： 138 8888 8888
	 * @param mEditText
	 */
	private void phoneNumAddSpace() {
		setInputType(EditorInfo.TYPE_CLASS_NUMBER);
		addTextChangedListener(new TextWatcher() {
			int beforeTextLength = 0;
			int onTextLength = 0;
			boolean isChanged = false;

			int location = 0;// 
			private char[] tempChar;
			private StringBuffer buffer = new StringBuffer();
			int konggeNumberB = 0;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				beforeTextLength = s.length();
				if (buffer.length() > 0) {
					buffer.delete(0, buffer.length());
				}
				konggeNumberB = 0;
				for (int i = 0; i < s.length(); i++) {
					if (s.charAt(i) == ' ') {
						konggeNumberB++;
					}
				}
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				onTextLength = s.length();
				buffer.append(s.toString());
				if (onTextLength == beforeTextLength || onTextLength <= 3
						|| isChanged) {
					isChanged = false;
					return;
				}
				isChanged = true;
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (isChanged) {
					location = getSelectionEnd();
					int index = 0;
					while (index < buffer.length()) {
						if (buffer.charAt(index) == ' ') {
							buffer.deleteCharAt(index);
						} else {
							index++;
						}
					}

					index = 0;
					int konggeNumberC = 0;
					if(buffer.toString().startsWith("1")){
						while (index < buffer.length()) {
							if ((index == 3 || index == 8)) {
								buffer.insert(index, ' ');
								konggeNumberC++;
							}
							index++;
						}
					}else {
						while (index < buffer.length()) {
							if ((index == 4)) {
								buffer.insert(index, ' ');
								konggeNumberC++;
							}
							index++;
						}
					}

					if (konggeNumberC > konggeNumberB) {
						location += (konggeNumberC - konggeNumberB);
					}

					tempChar = new char[buffer.length()];
					buffer.getChars(0, buffer.length(), tempChar, 0);
					String str = buffer.toString();
					if (location > str.length()) {
						location = str.length();
					} else if (location < 0) {
						location = 0;
					}

					setText(str);
					Editable etable = getText();
					Selection.setSelection(etable, location);
					if (buffer.toString().startsWith("1")) {
						setFilters(phoneFilters);
					} else {
						setFilters(telFilters);
					}
					isChanged = false;
				}
			}
		});
	}

}
