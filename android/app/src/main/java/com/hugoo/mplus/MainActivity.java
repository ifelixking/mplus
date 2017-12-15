package com.hugoo.mplus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hugoo.mplus.data.Result;
import com.hugoo.mplus.data.User;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
		doLoadContacts();
	}
	
	private ContactsAdapter contactsAdapter() {
		return ((ContactsAdapter) ((ListView) MainActivity.this.findViewById(R.id.listView_contacts)).getAdapter());
	}
	
	private void initUI() {
		final LinearLayout layoutMessage = findViewById(R.id.layout_message);
		final LinearLayout layoutContacts = findViewById(R.id.layout_contacts);
		final LinearLayout layoutMine = findViewById(R.id.layout_mine);
		
		ListView listViewContacts = findViewById(R.id.listView_contacts);
		listViewContacts.setAdapter(new ContactsAdapter());
		listViewContacts.setOnItemClickListener(new onContactItemClick());
		
		BottomNavigationView navigation = findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				layoutMessage.setVisibility(View.INVISIBLE);
				layoutContacts.setVisibility(View.INVISIBLE);
				layoutMine.setVisibility(View.INVISIBLE);
				switch (item.getItemId()) {
					case R.id.navigation_message:
						layoutMessage.setVisibility(View.VISIBLE);
						return true;
					case R.id.navigation_contacts:
						layoutContacts.setVisibility(View.VISIBLE);
						return true;
					case R.id.navigation_mine:
						layoutMine.setVisibility(View.VISIBLE);
						return true;
				}
				return false;
			}
		});
	}
	
	class onContactItemClick implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			switch (position) {
				case 0: {
					Intent intent = new Intent(MainActivity.this, SearchActivity.class);
					startActivity(intent);
				}
				break;
				default: {
					User user = contactsAdapter().getData().get(position - 1);
					Intent intent = new Intent(MainActivity.this, MsgActivity.class);
					String json = Utils.Json.serialize(user);
					intent.putExtra("friend", json);
					startActivity(intent);
				}
				break;
			}
		}
	}
	
	
	class ContactsAdapter extends BaseAdapter {
		private List<User> m_data = Arrays.asList(new User[]{});
		
		@Override
		public int getCount() {
			return m_data.size() + 1;
		}
		
		@Override
		public Object getItem(int position) {
			return position;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		public void setData(List<User> data) {
			this.m_data = data;
			this.notifyDataSetChanged();
		}
		
		public List<User> getData() {
			return m_data;
		}
		
		class Holder {
			TextView txtName;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater layoutInflater = MainActivity.this.getLayoutInflater();
				view = layoutInflater.inflate(R.layout.item_contact, null);
				Holder holder = new Holder();
				holder.txtName = view.findViewById(R.id.name);
				view.setTag(holder);
			}
			
			Holder holder = (Holder) view.getTag();
			
			switch (position) {
				case 0:
					holder.txtName.setText("搜索");
					break;
				default:
					int index = position - 1;
					User user = m_data.get(index);
					holder.txtName.setText(user.name);
					break;
			}
			
			return view;
		}
		
		
	}
	
	@SuppressLint("HandlerLeak")
	void doLoadContacts() {
		Api.getFriends(new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					Result<User[]> result = Utils.Json.deserialize((String)msg.obj, new TypeToken<Result<User[]>>(){}.getType());
					if (result.isSuccess()) {
						contactsAdapter().setData(Arrays.asList(result.detail));
					}
				}
			}
		});
	}
	
}
