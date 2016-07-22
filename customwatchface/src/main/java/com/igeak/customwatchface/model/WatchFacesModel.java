package com.igeak.customwatchface.model;

import android.content.Context;

import com.igeak.android.common.api.GeakApiClient;
import com.igeak.android.wearable.Node;
import com.igeak.android.wearable.NodeApi;
import com.igeak.android.wearable.Wearable;
import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.R;
import com.igeak.customwatchface.util.FileUtil;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by xuqiang on 16-5-19.
 */
public class WatchFacesModel {


    private List<WatchFaceBean> beanList;

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
                    beanList = watchfaces;
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

                WatchFace watchFace = FaceOperation.bean2Face(watchFaceBean,facePath,context);
                if (watchFace == null) {
                    subscriber.onError(new Exception("User = null"));
                } else {
                    subscriber.onNext(watchFace);
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable changeName(final String name, final List<WatchFaceBean> watchbeanlist,
                                 final int position) {
        return Observable.create(new Observable.OnSubscribe<List<WatchFaceBean>>() {
            @Override
            public void call(Subscriber<? super List<WatchFaceBean>> subscriber) {
                try {
                    WatchFaceBean currentbean = watchbeanlist.get(position);
                    FileOperation.changeWatchName(currentbean, name);

                } catch (Exception e) {
                    //throw new RuntimeException(e);
                    subscriber.onError(new Exception(e));
                }
                subscriber.onNext(watchbeanlist);
                subscriber.onCompleted();

            }
        });

    }

    public Observable deleteWatchFace(final List<WatchFaceBean> watchbeanlist,
                                      final int position) {
        return Observable.create(new Observable.OnSubscribe<List<WatchFaceBean>>() {
            @Override
            public void call(Subscriber<? super List<WatchFaceBean>> subscriber) {
                try {
                    WatchFaceBean currentbean = watchbeanlist.get(position);
                    FileOperation.deleteFolder(currentbean.getName());
                    watchbeanlist.remove(position);
                } catch (Exception e) {
                    //throw new RuntimeException(e);
                    subscriber.onError(new Exception(e));
                }
                subscriber.onNext(watchbeanlist);
                subscriber.onCompleted();
            }
        });

    }

    public Observable<WatchFaceBean> zipFileAndSentToWatch(final GeakApiClient googleApiClient,
                                                           final WatchFaceBean watchbeanface,
                                                           final WatchFacesModel.FacePath
                                                                   facePath) {
        return FaceOperation.zipFileAndSent2Watch(googleApiClient, watchbeanface, facePath,
                context);

    }

}
