package com.example.ju.jiadeqq.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ju.jiadeqq.R;
import com.example.ju.jiadeqq.adapter.ContactInfoAdapter;
import com.example.ju.jiadeqq.core.QQConnection;
import com.example.ju.jiadeqq.domain.ContactInfo;
import com.example.ju.jiadeqq.domain.ContactInfoList;
import com.example.ju.jiadeqq.domain.QQMessage;
import com.example.ju.jiadeqq.domain.QQMessageType;
import com.example.ju.jiadeqq.util.ThreadUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @ViewInject(R.id.listview)
    ListView listView;
    private List<ContactInfo> infos = new ArrayList<ContactInfo>();
    private ImApp app;
    private ContactInfoAdapter adapter;

    private QQConnection.OnMessageListener listener = new QQConnection.OnMessageListener() {

        public void onReveive(final QQMessage msg) {

            ThreadUtils.runInUiThread(new Runnable() {

                public void run() {


                    if (QQMessageType.MSG_TYPE_BUDDY_LIST.equals(msg.type)) {

                        String newBuddyListJson = msg.content;

                        Gson gson = new Gson();
                        ContactInfoList newList = gson.fromJson(
                                newBuddyListJson, ContactInfoList.class);
                        infos.clear();
                        infos.addAll(newList.buddyList);
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }

                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        app = (ImApp) getApplication();
        app.getMyConn().addOnMessageListener(listener);


        String buddyListJson = app.getBuddyListJson();
        System.out.println(buddyListJson);
        Gson gson = new Gson();
        ContactInfoList list = gson.fromJson(buddyListJson,
                ContactInfoList.class);
        infos.addAll(list.buddyList);
        adapter = new ContactInfoAdapter(getBaseContext(), infos);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ContactInfo info = infos.get(position);

                if (info.account != app.getMyAccount()) {
                    Intent intent = new Intent(getBaseContext(),
                            ChartActivity.class);

                    intent.putExtra("account", info.account);
                    intent.putExtra("nick", info.nick);

                    startActivity(intent);

                } else {
                    Toast.makeText(getBaseContext(), "不能跟自己聊天", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.getMyConn().removeOnMessageListener(listener);

    }
}
