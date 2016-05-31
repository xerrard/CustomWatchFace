package com.igeak.customwatchface.view.watchfaceview;

import android.content.Context;
import android.graphics.Bitmap;


/**
 * Created by tangyi on 15-8-17.
 */
public class Hour extends   BaseElement {


    public Hour(Context context,Bitmap bitmap) {
        super(context,bitmap);
        window_level = DisPlayLevel.LEVEL_HOUR;
        mType = WatchPreviewView.Type.HOUR;
    }


}
