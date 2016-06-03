package com.igeak.customwatchface;

import android.app.Application;
import android.os.Bundle;

import com.igeak.android.common.ConnectionResult;
import com.igeak.android.common.api.GeakApiClient;
import com.igeak.android.wearable.Wearable;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.LogTool;
import com.orhanobut.logger.Logger;

import org.xerrard.util.CrashHandler;

/**
 * Created by xuqiang on 16-5-19.
 */
public class MyApplication extends Application implements GeakApiClient.ConnectionCallbacks,
        GeakApiClient.OnConnectionFailedListener {

    private static final String YOUR_TAG = "CustomWatch";
    public GeakApiClient mGoogleApiclent;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        Logger.init(YOUR_TAG);



        mGoogleApiclent = new GeakApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiclent.connect();

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mGoogleApiclent.disconnect();
    }
}
