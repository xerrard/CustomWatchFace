package com.igeak.customwatchface.view.view.watchfaceview;

import android.content.Context;
import android.graphics.Bitmap;


/**
 * Created by tangyi on 15-8-17.
 */
public class Minute extends   BaseElement {



    public Minute(Context context,Bitmap bitmap) {
        super(context,bitmap);
        window_level = DisPlayLevel.LEVEL_MINUTE;
        mType = WatchPreviewView.Type.MINUTE;
    }


}