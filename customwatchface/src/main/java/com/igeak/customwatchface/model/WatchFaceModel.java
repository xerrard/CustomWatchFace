package com.igeak.customwatchface.model;

import android.content.Context;
import android.os.SystemClock;

import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.util.JsonUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by xuqiang on 16-5-19.
 */
public class WatchFaceModel {
    private Context context = null;

    public WatchFaceModel(Context context) {
        this.context = context;
    }

    public Observable<WatchFace> loadWatchimg(final WatchFaceBean watchFaceBean,
                                              final WatchFacesModel.FacePath facePath) {
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
                    if (facePath.equals(WatchFacesModel.FacePath.FACE_CUSTOM)) {
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
                    } else {
                        watchFace.setBackground(AssetsOperation.getWatchfacesElementImg(context,
                                watchFaceBean.getName(),
                                watchFaceBean.getBackground()));
                        watchFace.setDialScale(AssetsOperation.getWatchfacesElementImg(context,
                                watchFaceBean.getName(),
                                watchFaceBean.getDialScale()));
                        watchFace.setHour(AssetsOperation.getWatchfacesElementImg(context,
                                watchFaceBean.getName(),
                                watchFaceBean.getHour()));
                        watchFace.setMinute(AssetsOperation.getWatchfacesElementImg(context,
                                watchFaceBean.getName(),
                                watchFaceBean.getMinute()));
                        watchFace.setSecond(AssetsOperation.getWatchfacesElementImg(context,
                                watchFaceBean.getName(),
                                watchFaceBean.getSecond()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

}
