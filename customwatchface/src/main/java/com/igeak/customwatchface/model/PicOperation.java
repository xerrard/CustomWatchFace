package com.igeak.customwatchface.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.igeak.customwatchface.presenter.loader.ImageResizer;
import com.igeak.customwatchface.util.FileUtil;

import java.io.InputStream;

/**
 * Created by xuqiang on 16-6-14.
 */
public class PicOperation {

    /**
     * 优化图片加载，根据当前界面的需要，调整inSampleSize
     * @param is
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws Exception
     */
    public static Bitmap InputStream2Bitmap(InputStream is, int reqWidth, int reqHeight) throws Exception {
        InputStream copyiInputStream;
        byte[] data = FileUtil.InputStreamTOByte(is);
        copyiInputStream = FileUtil.byteTOInputStream(data);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = ImageResizer.calculateInSampleSize(options, reqWidth, reqHeight);
        Bitmap bitmap = BitmapFactory.decodeStream(copyiInputStream, null, options);
        return bitmap;

    }
}
