package com.igeak.customwatchface.model;

import android.content.Context;

import com.igeak.customwatchface.Bean.WatchFaceBean;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by xuqiang on 16-5-19.
 */
public class WatchFacesModel {

    public enum FacePath {
        FACE_INNER,
        FACE_CUSTOM
    }

    private Context context = null;

    public WatchFacesModel(Context context) {
        this.context = context;
    }


    /**
     * 获得表盘
     *
     * @param facePath 表盘类型
     * @return
     * @throws Exception
     */
    private List<WatchFaceBean> loadWatchfaceBeanList(FacePath facePath) throws Exception {
        if (facePath.equals(FacePath.FACE_CUSTOM)) {
            return FileOperation.getWatchFaceBeanList();
        } else {
            return AssetsOperation.getWatchFaceBeanList(context);
        }
    }

    public Observable<List<WatchFaceBean>> getWatchfaceBeanList(final FacePath facePath) {
        return Observable.create(new Observable.OnSubscribe<List<WatchFaceBean>>() {
            @Override
            public void call(Subscriber<? super List<WatchFaceBean>> subscriber) {

                try {
                    final List<WatchFaceBean> watchfaces = loadWatchfaceBeanList(facePath);
                    if (watchfaces == null) {
                        subscriber.onError(new Exception("User = null"));
                    } else {
                        subscriber.onNext(watchfaces);
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
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
                    if(facePath.equals(FacePath.FACE_CUSTOM)) {
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
                    }else {
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
