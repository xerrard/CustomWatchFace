package com.igeak.customwatchface.model;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;

import com.igeak.android.common.api.GeakApiClient;
import com.igeak.android.wearable.Node;
import com.igeak.android.wearable.NodeApi;
import com.igeak.android.wearable.Wearable;
import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.MyApplication;
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
public class WatchFaceDetailModel {
    private Context context = null;

    public WatchFaceDetailModel(Context context) {
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


    public Observable<WatchFaceBean> zipFileAndSentToWatch(final Activity activity, final
    WatchFaceBean watchbeanface,
                                                           final WatchFacesModel.FacePath
                                                                   facePath) {
        return Observable.create(new Observable.OnSubscribe<WatchFaceBean>() {
            @Override
            public void call(Subscriber<? super WatchFaceBean> subscriber) {
                try {

                    /**
                     * 1.打包文件，得到byte[]
                     */
                    byte[] bytes;
                    if (facePath.equals(WatchFacesModel.FacePath.FACE_CUSTOM)) {
                        bytes = FileOperation.zipFolder(watchbeanface.getName());
                    } else {
                        bytes = AssetsOperation.zipFolder(context, watchbeanface.getName());
                        FileOperation.deleteFolder(watchbeanface.getName());
                    }


                    /**
                     * 2.通过通道，将数据发送出去
                     */
                    MyApplication app = (MyApplication) activity.getApplication();
                    GeakApiClient mGoogleApiClient = app.mGoogleApiclent;


                    if (mGoogleApiClient.isConnected()) {
                        NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes
                                (mGoogleApiClient).await();
                        List<Node> nodes = result.getNodes();

                        if(nodes.isEmpty()){
                            subscriber.onError(new Exception("please connect the watch"));
                            return;
                        }
                        Iterator it = nodes.iterator();
                        while (it.hasNext()) {
                            Node node = (Node) it.next();
                            Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(),
                                    Const.MESSAGE_DATA_PATH,
                                    bytes);
                        }
                        if (watchbeanface == null) {
                            subscriber.onError(new Exception("User = null"));
                        }
                        subscriber.onNext(watchbeanface);
                        subscriber.onCompleted();
                    }else {
                        subscriber.onError(new Exception("please open the phone sync"));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}
