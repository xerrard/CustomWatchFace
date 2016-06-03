package com.igeak.customwatchface.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Toast;

import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.model.FileOperation;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFaceEditModel;
import com.igeak.customwatchface.util.PicUtil;
import com.igeak.customwatchface.view.watchfaceview.BackGround;
import com.igeak.customwatchface.view.watchfaceview.PointView;
import com.igeak.customwatchface.view.watchfaceview.WatchPreviewView;
import com.soundcloud.android.crop.Crop;

import java.io.File;
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
public class WatchFaceEditPresent {
    private Context context;
    private WatchFaceEditModel model;
    private IWatchFaceEditView watchfaceview;
    private IBackgroundView backgroundView;
    private IScaleView scaleView;
    private IPointView pointView;
    private Map<WatchPreviewView.Type, Bitmap> modifyMaps;
    private WatchFace watchface;
    private WatchFaceBean watchfacebean;

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
        modifyMaps = new HashMap<>();
    }

    public List<Bitmap> loadbackImg() throws IOException {
        List<Bitmap> backImgs = new ArrayList<Bitmap>();
        String[] backgrounds;
        backgrounds = context.getAssets().list(Const.BACK_FOLDER_NAME);

        for (String str : backgrounds) {
            InputStream is = context.getAssets().open(Const.BACK_FOLDER_NAME + "/" + str);
            backImgs.add(PicUtil.InputStream2Bitmap(is));
        }

        return backImgs;
    }

    public void changeBackImg(Bitmap bitmap) {
        watchfaceview.updatebackground(bitmap);
        modifyMaps.put(WatchPreviewView.Type.BACKGROUND, bitmap);
    }

    public List<Bitmap> loadScaleImg() throws IOException {
        List<Bitmap> backImgs = new ArrayList<Bitmap>();
        String[] backgrounds;
        backgrounds = context.getAssets().list(Const.SCALE_FOLDER_NAME);

        for (String str : backgrounds) {
            InputStream is = context.getAssets().open(Const.SCALE_FOLDER_NAME + "/" + str);
            backImgs.add(PicUtil.InputStream2Bitmap(is));
        }

        return backImgs;
    }

    public void changeScaleImg(Bitmap bitmap) {
        watchfaceview.updateScale(bitmap);
        modifyMaps.put(WatchPreviewView.Type.DIALSCALE, bitmap);
    }

    public List<Map<PointView.Type, Bitmap>> loadPointImg() throws IOException {
        List<Map<PointView.Type, Bitmap>> pointImgs = new ArrayList<>();
        String[] backgrounds;
        backgrounds = context.getAssets().list(Const.POINT_FOLDER_NAME);

        for (String str : backgrounds) {
            Map<PointView.Type, Bitmap> map = new HashMap<>();

            map.put(PointView.Type.HOUR, PicUtil.InputStream2Bitmap(context.getAssets().open
                    (Const.POINT_FOLDER_NAME + "/" + str +
                            "/" + Const.HOUR_NAME + Const.PNG_EXNAME)));
            map.put(PointView.Type.MINUTE, PicUtil.InputStream2Bitmap(context.getAssets().open
                    (Const.POINT_FOLDER_NAME + "/" + str +
                            "/" + Const.MINUTE_NAME + Const.PNG_EXNAME)));
            map.put(PointView.Type.SECOND, PicUtil.InputStream2Bitmap(context.getAssets().open
                    (Const.POINT_FOLDER_NAME + "/" + str +
                            "/" + Const.SECOND_NAME + Const.PNG_EXNAME)));
            pointImgs.add(map);
        }

        return pointImgs;
    }

    public void changePointImg(Map<PointView.Type, Bitmap> point) {
        watchfaceview.updatePoint(point);
        modifyMaps.put(WatchPreviewView.Type.HOUR, point.get(PointView.Type.HOUR));
        modifyMaps.put(WatchPreviewView.Type.MINUTE, point.get(PointView.Type.MINUTE));
        modifyMaps.put(WatchPreviewView.Type.SECOND, point.get(PointView.Type.SECOND));
    }


    public void loadWatchimg(final WatchFaceBean watchFaceBean) {
        watchfacebean = watchFaceBean;
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
                        watchface = watchFace;
                        watchfaceview.updateWatchFace(watchFace);
                    }

                });
    }

    public void handleCrop(int resultCode, Intent result) throws FileNotFoundException {
        if (resultCode == Activity.RESULT_OK) {
            //resultView.setImageURI(Crop.getOutput(result));

            Uri uri = Crop.getOutput(result);
            InputStream stream;
            stream = context.getContentResolver().openInputStream(uri);
            Drawable drawable = Drawable.createFromStream(stream, null);
            //Bitmap bitmap = PicUtil.InputStream2Bitmap(stream); Crop获得的是drawable，获得不到Bitmap
            Bitmap bitmap = PicUtil.drawable2Bitmap(drawable);
            //watchfaceview.updatebackground(drawable);
            changeBackImg(bitmap);


        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(context, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void savewatch() throws Exception {
        for (WatchPreviewView.Type type : modifyMaps.keySet()) {
            PicUtil.saveBitmapToFile(
                    modifyMaps.get(type),
                    FileOperation.getWatchfacesElementFile(
                            watchfacebean.getName()
                            , watchfacebean.getBackground()
                    )
            );
        }


    }


    public interface IWatchFaceEditView {
        void updateWatchFace(WatchFace watchFace);

        void updatebackground(Bitmap bitmap);

        /**
         * Crop需要使用drawable
         *
         * @param drawable
         */
        void updatebackground(Drawable drawable);

        void updateScale(Bitmap bitmap);

        void updatePoint(Map<PointView.Type, Bitmap> map);
    }

    public interface IBackgroundView {

    }

    public interface IScaleView {

    }

    public interface IPointView {

    }
}
