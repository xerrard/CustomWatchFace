package com.igeak.customwatchface.view.watchfaceview;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by tangyi on 15-8-26.
 */
public class DialScale extends BaseElement {


    public DialScale(Context context,Bitmap bitmap) {
        super(context,bitmap);
        window_level = DisPlayLevel.LEVEL_DIALSCALE;
        mType = WatchPreviewView.Type.DIALSCALE;

    }



}
