package com.igeak.customwatchface.view.view.watchfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by tangyi on 15-8-17.
 */
public class BackGround extends   BaseElement{


    public BackGround(Context context,Bitmap bitmap) {
        super(context,bitmap);
        window_level = DisPlayLevel.LEVEL_BACKGROUND;
        mType = WatchPreviewView.Type.BACKGROUND;

    }

    public BackGround(Context context,Drawable drawable) {
        super(context,drawable);
        window_level = DisPlayLevel.LEVEL_BACKGROUND;
        mType = WatchPreviewView.Type.BACKGROUND;

    }

}
