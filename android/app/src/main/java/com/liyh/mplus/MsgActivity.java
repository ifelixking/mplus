package com.liyh.mplus;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.liyh.mplus.data.Msg;
import com.liyh.mplus.data.Result;
import com.liyh.mplus.data.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MsgActivity extends AppCompatActivity {

    User m_friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);

        String jsonFriend = getIntent().getStringExtra("friend");
        m_friend = Utils.Json.deserialize(jsonFriend, User.class);

        ((ListView) findViewById(R.id.listView_main)).setAdapter(new Adapter());

        initData();
    }

    @SuppressLint("HandlerLeak")
    private void initData() {
        Api.getMessage(m_friend.id, new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what != 0) return;
                Result<Msg[]> result = Utils.Json.deserialize((String) msg.obj, new TypeToken<Result<Msg[]>>() {
                }.getType());
                if (!result.isSuccess()) return;
                ((Adapter) ((ListView) findViewById(R.id.listView_main)).getAdapter()).add(Arrays.asList(result.detail));
            }
        });
    }

    public void onBtnSendClick(View view) {
    }

    class Adapter extends BaseAdapter {

        private List<Msg> m_data = new ArrayList<>();

        void add(Collection<Msg> msgList) {
            m_data.addAll(msgList);
        }

        @Override
        public int getCount() {
            return m_data.size();
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
            Msg msg = m_data.get(position);
            View view = convertView;
            if (view == null) {
                view = MsgActivity.this.getLayoutInflater().inflate(R.layout.item_msg, null);
            }
            TextView txt = view.findViewById(R.id.msg);
            txt.setGravity(msg.isSend ? Gravity.RIGHT : Gravity.LEFT);
            txt.setText(msg.content);
            return view;
        }
    }
}
