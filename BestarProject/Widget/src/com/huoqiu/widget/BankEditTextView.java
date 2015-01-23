/**
 * 
 */
package com.huoqiu.widget;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * @author bestar
 * 
 */
public class BankEditTextView extends EditText {

	private TextWatcher watcher;

	@Override
	public void addTextChangedListener(TextWatcher watcher) {
		this.watcher = watcher;
	}

	public BankEditTextView(Context context) {
		super(context);
		bankCardNumAddSpace();
	}

	public BankEditTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		bankCardNumAddSpace();
	}

	public BankEditTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		bankCardNumAddSpace();
	}

	public String getTextString() {
		String text = getText().toString();
		text = text.replace(" ", "");
		return text;
	}

	/**
	 * 四位卡号格式
	 * 
	 * @param mEditText
	 */
	private void bankCardNumAddSpace() {
		setInputType(EditorInfo.TYPE_CLASS_NUMBER);
		super.addTextChangedListener(new TextWatcher() {
			int beforeTextLength = 0;
			int onTextLength = 0;
			boolean isChanged = false;

			int location = 0;// 下标从0开始
			private char[] tempChar;
			private StringBuffer buffer = new StringBuffer();
			int konggeNumberB = 0;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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
				// konggeNumberB = s.toString().split(" ").length-1;
				if (watcher != null) {
					watcher.beforeTextChanged(s, start, count, after);
				}
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				onTextLength = s.length();
				buffer.append(s.toString());
				if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
					isChanged = false;
					return;
				}
				isChanged = true;
				if (watcher != null) {
					watcher.onTextChanged(s, start, before, count);
				}
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
					while (index < buffer.length()) {
						 if ((index == 4 || index == 9 || index == 14 || index == 19)) {
//						if (index >= 4 && ((index - 4) % 5 == 0)) {
							buffer.insert(index, ' ');
							konggeNumberC++;
						}
						index++;
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
					isChanged = false;
				}
				if (watcher != null) {
					watcher.afterTextChanged(s);
				}
			}
		});
	}

}
