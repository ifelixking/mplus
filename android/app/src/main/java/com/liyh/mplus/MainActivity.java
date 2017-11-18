package com.liyh.mplus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.liyh.mplus.data.User;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout m_layoutMessage;
    private LinearLayout m_layoutContacts;
    private LinearLayout m_layoutMine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        doLoadContacts();
    }

    private void initUI() {
        m_layoutMessage = findViewById(R.id.layout_message);
        m_layoutContacts = findViewById(R.id.layout_contacts);
        m_layoutMine = findViewById(R.id.layout_mine);

        ListView listViewContacts = findViewById(R.id.listView_contacts);
        listViewContacts.setAdapter(new ContactsAdapter());
        listViewContacts.setOnItemClickListener(new onContactItemClick());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                m_layoutMessage.setVisibility(View.INVISIBLE);
                m_layoutContacts.setVisibility(View.INVISIBLE);
                m_layoutMine.setVisibility(View.INVISIBLE);
                switch (item.getItemId()) {
                    case R.id.navigation_message:
                        m_layoutMessage.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.navigation_contacts:
                        m_layoutContacts.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.navigation_mine:
                        m_layoutMine.setVisibility(View.VISIBLE);
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
                case 0:
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
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
                    if (Utils.Json.isSuccessResult((JSONObject) msg.obj)) {
                        User[] users = Utils.Json.mappingFromJsonArray(Utils.Json.jsArray((JSONObject) msg.obj, "detail"), User.class);
                        ((ContactsAdapter) ((ListView) MainActivity.this.findViewById(R.id.listView_contacts)).getAdapter()).setData(Arrays.asList(users));
                    }
                }
            }
        });
    }

}
