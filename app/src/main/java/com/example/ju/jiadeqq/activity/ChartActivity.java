package com.example.ju.jiadeqq.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ju.jiadeqq.R;
import com.example.ju.jiadeqq.adapter.ChartMessageAdapter;
import com.example.ju.jiadeqq.core.QQConnection;
import com.example.ju.jiadeqq.domain.QQMessage;
import com.example.ju.jiadeqq.domain.QQMessageType;
import com.example.ju.jiadeqq.util.ThreadUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.listview)
    private ListView listView;
    @ViewInject(R.id.input)
    private EditText input;
    private ImApp app;
    private ChartMessageAdapter adapter;
    private String toNick;
    private long toAccount;
    private long fromAccount;
    private String inputStr;

    private List<QQMessage> messages = new ArrayList<QQMessage>();

    @OnClick(R.id.send)
    public void send(View view) {

        inputStr = input.getText().toString().trim();

        input.setText("");
        final QQMessage msg = new QQMessage();
        if ("".equals(inputStr)) {
            Toast.makeText(getApplicationContext(), "消息内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        msg.type = QQMessageType.MSG_TYPE_CHAT_P2P;
        msg.from = fromAccount;
        msg.to = toAccount;
        msg.content = inputStr;
        msg.fromAvatar = R.drawable.ic_launcher;

        messages.add(msg);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }


        if (messages.size() > 0) {
            listView.setSelection(messages.size() - 1);
        }


        ThreadUtils.runInSubThread(new Runnable() {

            public void run() {
                try {
                    app.getMyConn().sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private QQConnection.OnMessageListener listener = new QQConnection.OnMessageListener() {

        public void onReveive(final QQMessage msg) {

            ThreadUtils.runInUiThread(new Runnable() {

                public void run() {


                    System.out.println(msg.content);
                    if (QQMessageType.MSG_TYPE_CHAT_P2P.equals(msg.type)) {
                        messages.add(msg);

                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }

                        if (messages.size() > 0) {
                            listView.setSelection(messages.size() - 1);
                        }

                    }

                }
            });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ViewUtils.inject(this);
        app = (ImApp) getApplication();

        app.getMyConn().addOnMessageListener(listener);

        Intent intent = getIntent();
        toNick = intent.getStringExtra("nick");
        toAccount = intent.getLongExtra("account", 0);

        title.setText("与" + toNick + "聊天中");
        fromAccount = app.getMyAccount();

        adapter = new ChartMessageAdapter(this, messages);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        app.getMyConn().removeOnMessageListener(listener);
    }

}
