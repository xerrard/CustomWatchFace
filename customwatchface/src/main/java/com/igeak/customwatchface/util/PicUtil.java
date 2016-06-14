package com.igeak.customwatchface.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.igeak.customwatchface.presenter.loader.ImageResizer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tangyi on 15-8-20.
 */
public class PicUtil {
    private static final String TAG = "PicUtil";


    public static Bitmap file2Bitmap(File file) throws Exception {
        //BufferedInputStream bf = new BufferedInputStream(new FileInputStream(file));
        FileInputStream fileInputStream = new FileInputStream(file);
        return InputStream2Bitmap(fileInputStream);
    }


    public static BitmapDrawable bitmap2Drawable(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        return bd;
    }


    public static Bitmap InputStream2Bitmap(InputStream is) throws Exception {
        InputStream copyiInputStream1;
        InputStream copyiInputStream2;
        byte[] data = FileUtil.InputStreamTOByte(is);
        //copyiInputStream1 = FileUtil.byteTOInputStream(data);
        copyiInputStream2 = FileUtil.byteTOInputStream(data);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = ImageResizer.calculateInSampleSize(options, 320, 320);
        Bitmap bitmap = BitmapFactory.decodeStream(copyiInputStream2, null, options);
        return bitmap;

    }


    private static Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }


    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public static boolean saveBitmapToFile(Bitmap bm, File file) throws IOException {
        if (bm != null && file != null) {
            FileOutputStream output = null;
            Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
            if (file.getPath().endsWith(".png")) {
                format = Bitmap.CompressFormat.PNG;
            }

            if (file.exists()) {
                file.delete(); //如果已经有文件，先删除
            }
            output = new FileOutputStream(file);
            bm.compress(format, 100, output);
            output.close();
            return true;

        }
        return false;
    }


}
