package com.igeak.customwatchface.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.util.PicUtil;
import com.igeak.customwatchface.view.view.watchfaceview.PointView;
import com.igeak.customwatchface.view.view.watchfaceview.WatchPreviewView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by xuqiang on 16-5-27.
 */
public class WatchFaceEditModel {
    private Context context;
    private Map<WatchPreviewView.Type, Bitmap> modifyMaps;
    private WatchFaceBean watchfacebean;

    public WatchFaceEditModel(Context context) {
        this.context = context;
        modifyMaps = new HashMap<>();
    }

    public Observable<WatchFace> loadWatchimg(final WatchFaceBean watchFaceBean) {
        this.watchfacebean = watchFaceBean;
        return Observable.create(new Observable.OnSubscribe<WatchFace>() {
            @Override
            public void call(Subscriber<? super WatchFace> subscriber) {

                WatchFace watchFace = new WatchFace();
                watchFace.setName(watchFaceBean.getName());
                watchFace.setAmPm(watchFaceBean.isAmPm());
                watchFace.setHaveTemperature(watchFaceBean.isHaveTemperature());
                watchFace.setShowDate(watchFaceBean.isShowDate());
                watchFace.setShowWeek(watchFaceBean.isShowWeek());
                try {

                    watchFace.setBackground(FileOperation.getWatchfacesElementImg(
                            watchFaceBean.getName(),
                            watchFaceBean.getBackground()));
                    watchFace.setDialScale(FileOperation.getWatchfacesElementImg(
                            watchFaceBean.getName(),
                            watchFaceBean.getDialScale()));
                    watchFace.setHour(FileOperation.getWatchfacesElementImg(
                            watchFaceBean.getName(),
                            watchFaceBean.getHour()));
                    watchFace.setMinute(FileOperation.getWatchfacesElementImg(
                            watchFaceBean.getName(),
                            watchFaceBean.getMinute()));
                    watchFace.setSecond(FileOperation.getWatchfacesElementImg(
                            watchFaceBean.getName(),
                            watchFaceBean.getSecond()));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (watchFace == null) {
                    subscriber.onError(new Exception("User = null"));
                } else {
                    subscriber.onNext(watchFace);
                    subscriber.onCompleted();
                }
            }
        });
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

    public List<Bitmap> loadScaleImg() throws IOException{
        List<Bitmap> backImgs = new ArrayList<Bitmap>();
        String[] backgrounds;
        backgrounds = context.getAssets().list(Const.SCALE_FOLDER_NAME);

        for (String str : backgrounds) {
            InputStream is = context.getAssets().open(Const.SCALE_FOLDER_NAME + "/" + str);
            backImgs.add(PicUtil.InputStream2Bitmap(is));
        }

        return backImgs;
    }

    public List<Map<PointView.Type, Bitmap>> loadPointImg() throws IOException{
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

    public void saveBackImg(Bitmap bitmap) {
        modifyMaps.put(WatchPreviewView.Type.BACKGROUND, bitmap);
    }

    public void saveScaleImg(Bitmap bitmap) {
        modifyMaps.put(WatchPreviewView.Type.DIALSCALE, bitmap);
    }

    public void savePointImg(Map<PointView.Type, Bitmap> point) {
        modifyMaps.put(WatchPreviewView.Type.HOUR, point.get(PointView.Type.HOUR));
        modifyMaps.put(WatchPreviewView.Type.MINUTE, point.get(PointView.Type.MINUTE));
        modifyMaps.put(WatchPreviewView.Type.SECOND, point.get(PointView.Type.SECOND));
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
}
