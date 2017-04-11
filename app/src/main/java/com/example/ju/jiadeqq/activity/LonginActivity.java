package com.example.ju.jiadeqq.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ju.jiadeqq.R;

import com.example.ju.jiadeqq.core.QQConnection;
import com.example.ju.jiadeqq.core.QQConnection.OnMessageListener;
import com.example.ju.jiadeqq.domain.QQMessage;
import com.example.ju.jiadeqq.domain.QQMessageType;
import com.example.ju.jiadeqq.util.ThreadUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.IOException;


public class LonginActivity extends AppCompatActivity {
    @ViewInject(R.id.account)
    private EditText account;
    @ViewInject(R.id.password)
    private EditText password;
    private String accountStr;
    private String passwordStr;
    QQConnection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_longin);
        ViewUtils.inject(this);
        ThreadUtils.runInSubThread(new Runnable() {

            public void run() {
                try {
                    conn = new QQConnection("160630ct26.iok.la", 8090);
                    conn.connect();
                    conn.addOnMessageListener(listener);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        conn.removeOnMessageListener(listener);
    }

    @OnClick(R.id.login)
    public void login(View view) {
        accountStr = account.getText().toString().trim();
        passwordStr = password.getText().toString().trim();
        ThreadUtils.runInSubThread(new Runnable() {

            public void run() {
                try {
                    QQMessage msg = new QQMessage();
                    msg.type = QQMessageType.MSG_TYPE_LOGIN;
                    msg.content = accountStr + "#" + passwordStr;
                    conn.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private OnMessageListener listener = new OnMessageListener() {

        public void onReveive(final QQMessage msg) {
            System.out.println(msg.toXML());
            ThreadUtils.runInUiThread(new Runnable() {

                public void run() {
                    if (QQMessageType.MSG_TYPE_BUDDY_LIST.equals(msg.type)) {
                        Toast.makeText(getBaseContext(), "登录成功",Toast.LENGTH_SHORT).show();
                        //ImApp app = (ImApp) getApplication();
                        ImApp app = (ImApp) getApplication();
                        app.setMyConn(conn);
                        app.setBuddyListJson(msg.content);
                        app.setMyAccount(Long.parseLong(accountStr));
                        Intent intent = new Intent();
                        intent.setClass(getBaseContext(), MainActivity.class);
                        intent.putExtra("account", accountStr);
                        startActivity(intent);

                        finish();

                    } else {
                        Toast.makeText(getBaseContext(), "登录失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    };

}
