package com.example.ju.jiadeqq.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.TextView;

import com.example.ju.jiadeqq.R;
import com.example.ju.jiadeqq.util.ThreadUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView textView = (TextView) findViewById(R.id.spbt);
        TextPaint paint = textView.getPaint();
        paint.setFakeBoldText(true);
        ThreadUtils.runInSubThread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                startActivity(new Intent(getApplicationContext(),LonginActivity.class));
                finish();
            }
        });
    }
}
