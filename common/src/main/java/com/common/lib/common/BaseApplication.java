package com.common.lib.common;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {
	public static BaseApplication mInstance;

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}


	public static Context getContext() {
		return mInstance;
	}
	public static BaseApplication getInstance() {
		if (mInstance == null) {
			mInstance=new BaseApplication();
		}
		return mInstance;
	}
}
