package com.igeak.customwatchface.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Toast;

import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.model.FileOperation;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFaceEditModel;
import com.igeak.customwatchface.util.PicUtil;
import com.igeak.customwatchface.view.view.watchfaceview.PointView;
import com.igeak.customwatchface.view.view.watchfaceview.WatchPreviewView;
import com.soundcloud.android.crop.Crop;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuqiang on 16-5-27.
 */
public class WatchFaceEditPresent implements IWatchFaceEditContract {
    private Context context;
    private WatchFaceEditModel model;
    private IWatchFaceEditView watchfaceview;
    private IBackgroundView backgroundView;
    private IScaleView scaleView;
    private IPointView pointView;

    public WatchFaceEditPresent(Context context
            , IWatchFaceEditView watchfaceview
            , IBackgroundView backgroundView
            , IScaleView scaleView
            , IPointView pointView) {
        this.context = context;
        model = new WatchFaceEditModel(context);
        this.watchfaceview = watchfaceview;
        this.backgroundView = backgroundView;
        this.scaleView = scaleView;
        this.pointView = pointView;
    }

    public List<Bitmap> loadbackImg() {
        try {
            return model.loadbackImg();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void changeBackImg(Bitmap bitmap) {
        watchfaceview.updatebackground(bitmap);
        model.saveBackImg(bitmap);
    }

    public List<Bitmap> loadScaleImg() {
        try {
            return model.loadScaleImg();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void changeScaleImg(Bitmap bitmap) {
        watchfaceview.updateScale(bitmap);
        model.saveScaleImg(bitmap);

    }

    public List<Map<PointView.Type, Bitmap>> loadPointImg() {
        try {
            return model.loadPointImg();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void changePointImg(Map<PointView.Type, Bitmap> point) {
        watchfaceview.updatePoint(point);
        model.savePointImg(point);
    }


    public void loadWatchimg(final WatchFaceBean watchFaceBean) {
        model.loadWatchimg(watchFaceBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WatchFace>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WatchFace watchFace) {
                        watchfaceview.updateWatchFace(watchFace);
                    }

                });
    }

    public void handleCrop(int resultCode, Intent result) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = Crop.getOutput(result);
                InputStream stream;
                stream = context.getContentResolver().openInputStream(uri);
                Drawable drawable = Drawable.createFromStream(stream, null);
                Bitmap bitmap = PicUtil.drawable2Bitmap(drawable);
                changeBackImg(bitmap);
            } else if (resultCode == Crop.RESULT_ERROR) {
                Toast.makeText(context, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void savewatch() {
        try {
            model.savewatch();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
