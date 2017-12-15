package com.hugoo.mplus;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hugoo.mplus.data.Result;
import com.hugoo.mplus.data.User;

import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
	
	ListView m_listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		m_listView = findViewById(R.id.listView_main);
		m_listView.setAdapter(new UserAdapter());
		m_listView.setOnItemClickListener(new onListViewItemClick());
		flushData();
	}
	
	class onListViewItemClick implements AdapterView.OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			List<User> listUser = ((UserAdapter) m_listView.getAdapter()).getData();
			final User user = listUser.get(position);
			final AlertDialog.Builder dlg = new AlertDialog.Builder(SearchActivity.this);
			dlg.setMessage("添加" + user.name + "为好友");
			dlg.setTitle("添加好友");
			dlg.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@SuppressLint("HandlerLeak")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					final DialogInterface dlg = dialog;
					Api.addFriend(user.id, new Handler() {
						public void handleMessage(Message msg) {
							dlg.dismiss();
						}
					});
				}
			});
			dlg.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dlg.show();
		}
	}
	
	@SuppressLint("HandlerLeak")
	void flushData() {
		Api.search(new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					Result<User[]> result = Utils.Json.deserialize((String) msg.obj, new TypeToken<Result<User[]>>() {
					}.getType());
					if (result.isSuccess()) {
						((UserAdapter) m_listView.getAdapter()).setData(Arrays.asList(result.detail));
					}
				}
			}
		});
	}
	
	class UserAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return m_list.size();
		}
		
		@Override
		public Object getItem(int position) {
			return position;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			User user = m_list.get(position);
			View view = convertView;
			if (view == null) {
				view = SearchActivity.this.getLayoutInflater().inflate(R.layout.item_user, null);
				Holder holder = new Holder();
				holder.txtName = view.findViewById(R.id.name);
				view.setTag(holder);
			}
			Holder holder = (Holder) view.getTag();
			holder.txtName.setText(user.name);
			return view;
		}
		
		class Holder {
			TextView txtName;
		}
		
		private List<User> m_list = Arrays.asList(new User[]{});
		
		void setData(List<User> list) {
			m_list = list;
			this.notifyDataSetChanged();
		}
		
		List<User> getData() {
			return m_list;
		}
	}
}
