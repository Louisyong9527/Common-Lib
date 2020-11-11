package com.common.lib.common;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import com.common.lib.common.etoast2.EToastUtils;

public class BaseApplication extends Application {
	public static BaseApplication mInstance;
	public static AppManager mAppManager;
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		mAppManager = AppManager.getAppManager();
		EToastUtils.init(this);
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
