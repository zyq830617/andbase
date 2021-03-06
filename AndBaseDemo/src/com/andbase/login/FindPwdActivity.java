package com.andbase.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ab.activity.AbActivity;
import com.ab.global.AbConstant;
import com.ab.global.AbResult;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.AbTaskPool;
import com.ab.util.AbStrUtil;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;

public class FindPwdActivity extends AbActivity {
	
	private MyApplication application;
	
	private EditText userName = null;
	private EditText email = null;
	private ImageButton mClear1;
	private ImageButton mClear2;
	private String mStr_name = null;
	private String mStr_email = null;
	private AbResult mAbResult = null;
	private AbTaskPool mAbTaskPool = null;
	private AbTitleBar mAbTitleBar = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.find_pwd);
		application = (MyApplication) abApplication;
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.register_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleLayoutBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		
		mAbTaskPool = AbTaskPool.getInstance();
		userName = (EditText) this.findViewById(R.id.userName);
		email = (EditText) this.findViewById(R.id.email);
		
		mClear1 = (ImageButton) findViewById(R.id.clearName);
		mClear2 = (ImageButton) findViewById(R.id.clearEmail);
		
		userName.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String str = userName.getText().toString().trim();
				int length = str.length();
				if (length > 0) {
					mClear1.setVisibility(View.VISIBLE);
					if (!AbStrUtil.isNumberLetter(str)) {
						str = str.substring(0, length - 1);
						userName.setText(str);
						String str1 = userName.getText().toString().trim();
						userName.setSelection(str1.length());
						showToast(R.string.error_name_expr);
					}
					
					mClear1.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							mClear1.setVisibility(View.INVISIBLE);
						}
						
					}, 5000);
					
				} else {
					mClear1.setVisibility(View.INVISIBLE);
				}
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		email.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String str = email.getText().toString().trim();
				int length = str.length();
				if (length > 0) {
					mClear2.setVisibility(View.VISIBLE);
					if(AbStrUtil.isContainChinese(str)){
						str = str.substring(0, length-1);
						email.setText(str);
						String str1 = email.getText().toString().trim();
						email.setSelection(str1.length());
						showToast(R.string.error_email_expr2);
					}
					mClear2.postDelayed(new Runnable(){

						@Override
						public void run() {
							mClear2.setVisibility(View.INVISIBLE);
						}
						
					}, 5000);
				} else {
					mClear2.setVisibility(View.INVISIBLE);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			public void afterTextChanged(Editable s) {

			}
		});
		
		mClear1.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				userName.setText("");
			}
		});
		
		mClear2.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				email.setText("");
			}
		});
		
		Button findPwdBtn = (Button) findViewById(R.id.findPwdBtn);
		findPwdBtn.setOnClickListener(new FindPwdOnClickListener());
		
	}
	
	public class FindPwdOnClickListener implements View.OnClickListener {
		
		@Override
		public void onClick(View v) {
			mStr_name = userName.getText().toString().trim();
			mStr_email = email.getText().toString().trim();
			if (TextUtils.isEmpty(mStr_name)) {
				showToast(R.string.error_name);
				userName.setFocusable(true);
				userName.requestFocus();
				return;
			}
			
			if (!AbStrUtil.isNumberLetter(mStr_name)) {
				showToast(R.string.error_name_expr);
				userName.setFocusable(true);
				userName.requestFocus();
				return;
			}
			
			if (AbStrUtil.strLength(mStr_name) < 3) {
				showToast(R.string.error_name_length1);
				userName.setFocusable(true);
				userName.requestFocus();
				return;
			}
			
			if (AbStrUtil.strLength(mStr_name) > 20) {
				showToast(R.string.error_name_length2);
				userName.setFocusable(true);
				userName.requestFocus();
				return;
			}
			
			
			if (!TextUtils.isEmpty(mStr_email)) {
				if (!AbStrUtil.isEmail(mStr_email)) {
					showToast(R.string.error_email_expr);
					email.setFocusable(true);
					email.requestFocus();
					return;
				}
			}else{
				showToast(R.string.error_email);
				email.setFocusable(true);
				email.requestFocus();
			}
			
			
			showProgressDialog();
			final AbTaskItem item = new AbTaskItem();
			item.listener = new AbTaskListener() {

				@Override
				public void update() {
					removeProgressDialog();
					if(mAbResult != null){
						showToast(mAbResult.getResultMsg());
						if(mAbResult.getResultCode()==AbConstant.RESULRCODE_OK){
							finish();
			        	}
			        }
				}

				@Override
				public void get() {
		   		    try {
		   		    	mAbResult = new AbResult();
		   		    	mAbResult.setResultMsg("ok");
		   		    } catch (Exception e){
		   		    	showToastInThread(e.getMessage());
		   		    }
			  };
			};
			mAbTaskPool.execute(item);
		}
	}
	
}
