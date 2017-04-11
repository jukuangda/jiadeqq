package com.example.ju.jiadeqq.activity;

import com.example.ju.jiadeqq.core.QQConnection;

import android.app.Application;

public class ImApp extends Application {
	private QQConnection myConn;
	private long myAccount;
	private String buddyListJson;

	public QQConnection getMyConn() {
		return myConn;
	}

	public void setMyConn(QQConnection myConn) {
		this.myConn = myConn;
	}

	public long getMyAccount() {
		return myAccount;
	}

	public void setMyAccount(long myAccount) {
		this.myAccount = myAccount;
	}

	public String getBuddyListJson() {
		return buddyListJson;
	}

	public void setBuddyListJson(String buddyListJson) {
		this.buddyListJson = buddyListJson;
	}

}
