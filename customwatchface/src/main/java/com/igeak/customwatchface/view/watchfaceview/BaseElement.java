package com.igeak.customwatchface.view.watchfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.Transformation;

import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.util.PicUtil;


/**
 * Created by tangyi on 15-8-17.
 */
public abstract class BaseElement {

    public WatchPreviewView.Type mType = WatchPreviewView.Type.NULL;

    public String ResourcePath = "";

    public BitmapDrawable mDrawable;

    public float width = 0;
    public float height = 0;

    public float mRotate = 0.0f;

    public float scale = 1.0f;

    public int alpha = 255;

    public float start_x = 0;
    public float start_y = 0;

    public boolean isEnable = false;

    public int window_level = 0;

    public Context mContext;

    private Matrix matrix = new Matrix();

    private Transformation mTransformation = new Transformation();

    private String pngName = "";


    public BaseElement(Context context, Drawable drawable) {
        mContext = context;
        isEnable = true;
        mDrawable = (BitmapDrawable) drawable;
    }

    public BaseElement(Context context, Bitmap bitmap) {
        mContext = context;
        isEnable = true;
        mDrawable = PicUtil.bitmap2Drawable(bitmap);
    }

    //public abstract BitmapDrawable loadDefaultDrawable(Context context);


    public static class DisPlayLevel {

        public static int LEVEL_BACKGROUND = 10;
        public static int LEVEL_DIALSCALE = 15;
        public static int LEVEL_HOUR = 45;
        public static int LEVEL_MINUTE = 50;
        public static int LEVEL_SECOND = 55;

    }


    public void layout(float view_width, float view_height) {

//起始坐标为中心点
        start_x = view_width / 2;
        start_y = view_height / 2;

        if (mType == WatchPreviewView.Type.HOUR) {
            mRotate = 320.0f;
        } else if (mType == WatchPreviewView.Type.MINUTE) {
            mRotate = 40.0f;
        } else if (mType == WatchPreviewView.Type.SECOND) {
            mRotate = 180.0f;
        } else {
            mRotate = 0.0f;
        }
    }

    public void setDrawable(BitmapDrawable drawable) {
        mDrawable = drawable;
    }


    public void setBitmap(Bitmap bitmap) {
        mDrawable = PicUtil.bitmap2Drawable(bitmap);
    }

    public String getPngName() {
        return mType.toString().toLowerCase() + ".png";
    }

    public void onDraw(Canvas canvas) {
        Log.d(Const.TAG, "onDraw Type = " + mType);
        canvas.save();

        float x = start_x;
        float y = start_y;

        canvas.translate(x, y);

        matrix.setRotate(mRotate);

        canvas.concat(matrix);
        canvas.translate(-x, -y);

        final Drawable drawable = mDrawable;

        if (drawable != null) {

            //BitmapDrawable
            mDrawable.getBitmap().getDensity();
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            Log.d(Const.TAG, " w = " + w + " h = " + h);
            Log.d(Const.TAG, " mDrawable.getBitmap().getDensity(); = " + mDrawable.getBitmap()
                    .getDensity());
            Log.d(Const.TAG, " w = " + mDrawable.getBitmap().getWidth() + " h = " + mDrawable
                    .getBitmap().getHeight());
            Log.d(Const.TAG, " mDrawable.getBitmap().getScaledWidth() = " + mDrawable.getBitmap()
                    .getScaledWidth(canvas));
            drawable.setBounds((int) (x - (w / 2)), (int) (y - (h / 2)), (int) (x + (w / 2)),
                    (int) (y + (h / 2)));
            drawable.draw(canvas);
        }

        canvas.restore();

    }
}