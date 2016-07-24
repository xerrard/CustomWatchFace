package com.igeak.customwatchface.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.igeak.customwatchface.presenter.loader.ImageResizer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xuqiang on 16-6-14.
 */
public class PicOperation {

    /**
     * 优化图片加载，根据当前界面的需要，调整inSampleSize
     * @param is
     * @param reqWidth    需要显示的宽度
     * @param reqHeight   需要显示的高度
     * @return Bitmap     得到我们需要的Bitmap
     * @throws Exception
     */
    public static Bitmap InputStream2Bitmap(InputStream is, int reqWidth, int reqHeight) throws Exception {
        InputStream copyInputStream = copy2Stream(is);
        copyInputStream.mark(copyInputStream.available());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(copyInputStream, null, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = ImageResizer.calculateInSampleSize(options, reqWidth, reqHeight);
        copyInputStream.reset();
        Bitmap bitmap = BitmapFactory.decodeStream(copyInputStream, null, options);
        copyInputStream.close();
        return bitmap;

    }


    /**
     * 复制一个InputStream出来
     * @param in
     * @return
     * @throws IOException
     */
    public static InputStream copy2Stream(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024 * 16];
        int count = -1;
        while ((count = in.read(data, 0, 1024 * 16)) != -1)
            outStream.write(data, 0, count);
        byte[] out = outStream.toByteArray();
        outStream.close();
        in.close();
        return new ByteArrayInputStream(out);
    }


}
