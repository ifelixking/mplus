package com.liyh.mplus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liyh.mplus.data.User;

public class MsgActivity extends AppCompatActivity {
	
	User m_friend;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg);
		
		String jsonFriend = getIntent().getStringExtra("friend");
		m_friend = Utils.Json.deserialize(jsonFriend, User.class);
	}
	
	public void onBtnSendClick(View view) {
	}
}
