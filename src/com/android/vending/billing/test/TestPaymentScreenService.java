package com.android.vending.billing.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TestPaymentScreenService extends Service {

	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
