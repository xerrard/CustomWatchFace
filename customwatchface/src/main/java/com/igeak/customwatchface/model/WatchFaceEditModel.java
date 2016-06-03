package com.igeak.customwatchface.model;

import android.content.Context;

import com.igeak.customwatchface.Bean.WatchFaceBean;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by xuqiang on 16-5-27.
 */
public class WatchFaceEditModel {
    private Context context;

    public WatchFaceEditModel(Context context) {
        this.context = context;
    }

    public Observable<WatchFace> loadWatchimg(final WatchFaceBean watchFaceBean) {
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

}
