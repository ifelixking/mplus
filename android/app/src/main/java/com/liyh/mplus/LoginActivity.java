package com.liyh.mplus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.liyh.mplus.data.Result;

public class LoginActivity extends AppCompatActivity {
	
	private EditText txtMobile;
	private EditText txtPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		txtMobile = findViewById(R.id.txtMobile);
		txtPassword = findViewById(R.id.txtPassword);
		txtMobile.addTextChangedListener(new MobileTextWatcher());
		txtMobile.setText("18610303648");
		txtPassword.setText("nintendo");
		initDeviceID();
	}
	
	private void initDeviceID() {
		TelephonyManager telephonyManager = (TelephonyManager) Application.getContext().getSystemService(Context.TELEPHONY_SERVICE);
		assert telephonyManager != null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M
				&& ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 3389);
		} else {
			@SuppressLint("HardwareIds") String deviceID = telephonyManager.getDeviceId();
			Application.initDeviceID(deviceID);
		}
	}
	
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 3389) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				TelephonyManager telephonyManager = (TelephonyManager) Application.getContext().getSystemService(Context.TELEPHONY_SERVICE);
				assert telephonyManager != null;
				@SuppressLint({"MissingPermission", "HardwareIds"}) String deviceID = telephonyManager.getDeviceId();
				Application.initDeviceID(deviceID);
			} else {
				this.finish();
			}
		}
	}
	
	@SuppressLint("HandlerLeak")
	public void onBtnLoginClick(View view) {
		String mobile = txtMobile.getText().toString();
		String deviceID = Application.getDeviceID();
		String password = txtPassword.getText().toString();
		Api.login(mobile, deviceID, password, new Handler() {
			public void handleMessage(Message msg) {
				// TODO: 改造
				if (msg.what == 0) {
					Result result = Utils.Json.deserialize((String) msg.obj, Result.class);
					if (result.isSuccess()) {
						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(intent);
					} else {
					
					}
				}
			}
		});
	}
	
	class MobileTextWatcher implements TextWatcher {
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@SuppressLint("HandlerLeak")
		@Override
		public void afterTextChanged(Editable s) {
			String mobile = s.toString();
			if (mobile.length() == 11 && Utils.isMobileNumber(mobile)) {
				Api.hasPwd(mobile, new Handler() {
					public void handleMessage(Message msg) {
						if (msg.what == 0) {
							Result result = Utils.Json.deserialize((String) msg.obj, Result.class);
							if (result.isSuccess()) {
								if (getResources().getString(R.string.had_pwd).equals(result.detail)) {
									txtPassword.setVisibility(View.VISIBLE);
								} else {
									txtPassword.setVisibility(View.INVISIBLE);
								}
							}
						}
					}
				});
			} else {
				txtPassword.setVisibility(View.INVISIBLE);
			}
		}
	}
}
