package com.igeak.customwatchface;

/**
 * Created by xuqiang on 16-5-25.
 */

import android.content.Context;

public class CustomManager {
    private static CustomManager sInstance;

    public static CustomManager getInstance(Context context) {
        if (sInstance == null) {

            // This class will hold a reference to the context
            // until it's unloaded. The context could be an Activity or Service.
            sInstance = new CustomManager(context);
        }

        return sInstance;
    }

    public static CustomManager getInstance() {

        return sInstance;
    }
    private Context mContext;

    private CustomManager(Context context) {
        mContext = context;
    }

    public Context getContext(){
        return mContext;
    }
}
