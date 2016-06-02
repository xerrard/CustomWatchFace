package com.igeak.customwatchface.watchfaceview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.util.Log;
import android.view.animation.Transformation;

import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.MyWatchFace;
import com.igeak.customwatchface.PicUtil;


/**
 * Created by tangyi on 15-8-17.
 */
public abstract class BaseElement {

    public MyWatchFace.Type mType = MyWatchFace.Type.NULL;

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

    private Matrix matrix = new Matrix();

    private Transformation mTransformation = new Transformation();

    private String pngName = "";

    float view_width;
    float view_height;

    public BaseElement(Drawable drawable) {
        isEnable = true;
        mDrawable = (BitmapDrawable) drawable;
    }

    public BaseElement(Bitmap bitmap) {
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


    public void layout(float view_width, float view_height, Time mTime) {
        Log.d(Const.TAG, " view_width = " + view_width + " view_height = " + view_height);
//起始坐标为中心点
        this.view_height = view_height;
        this.view_width = view_width;
        start_x = view_width / 2;
        start_y = view_height / 2;

        if (mType == MyWatchFace.Type.HOUR) {
            mRotate = 360.0f * mTime.hour / 24.0f;
        } else if (mType == MyWatchFace.Type.MINUTE) {
            mRotate = 360.0f * mTime.minute / 60.0f;
        } else if (mType == MyWatchFace.Type.SECOND) {
            mRotate = 360.0f * mTime.second / 60.0f;
        } else {
            mRotate = 0.0f;
        }
    }

    public void setDrawable(BitmapDrawable drawable) {
        mDrawable = drawable;
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
            int density = mDrawable.getBitmap().getDensity();

            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();

            int width = (int) (density / 160.0f * w);
            int height = (int) (density / 160.0f * h);
            Log.d(Const.TAG, " w = " + w + " h = " + h);
            Log.d(Const.TAG, " mDrawable.getBitmap().getDensity(); = " + mDrawable.getBitmap()
                    .getDensity());
            Log.d(Const.TAG, " w = " + mDrawable.getBitmap().getWidth() + " h = " + mDrawable
                    .getBitmap().getHeight());
            Log.d(Const.TAG, " mDrawable.getBitmap().getScaledWidth() = " + mDrawable.getBitmap()
                    .getScaledWidth(canvas));


            if (mType.equals(MyWatchFace.Type.BACKGROUND)||mType.equals(MyWatchFace.Type.DIALSCALE)) {
                drawable.setBounds((int) (x - (view_width / 2)), (int) (y - (view_height / 2)),
                        (int) (x +
                                (view_width / 2)),
                        (int) (y + (view_height / 2)));
            } else {
                drawable.setBounds((int) (x - (width / 2)), (int) (y - (height / 2)), (int) (x +
                                (width / 2)),
                        (int) (y + (height / 2)));
            }
            drawable.draw(canvas);
        }

        canvas.restore();

    }
}
