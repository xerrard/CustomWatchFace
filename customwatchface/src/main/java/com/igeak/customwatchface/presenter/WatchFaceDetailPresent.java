package com.igeak.customwatchface.presenter;

import android.app.Activity;
import android.content.Context;

import com.igeak.android.common.api.GeakApiClient;
import com.igeak.android.wearable.Node;
import com.igeak.android.wearable.NodeApi;
import com.igeak.android.wearable.Wearable;
import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.MyApplication;
import com.igeak.customwatchface.model.AssetsOperation;
import com.igeak.customwatchface.model.FileOperation;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFaceModel;
import com.igeak.customwatchface.model.WatchFacesModel;

import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by xuqiang on 16-5-19.
 */
public class WatchFaceDetailPresent {
    private IWatchFaceDetailView mWatchFaceView;
    private WatchFaceModel mWatchFaceModel;
    private Context context;


    public WatchFaceDetailPresent(Context context, IWatchFaceDetailView watchFaceView) {
        this.mWatchFaceView = watchFaceView;
        mWatchFaceModel = new WatchFaceModel(context);
        this.context = context;
    }

    public void loadWatchimg(final WatchFaceBean watchFaceBean,
                             final WatchFacesModel.FacePath facePath) {
        mWatchFaceModel.loadWatchimg(watchFaceBean, facePath)
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
                        mWatchFaceView.updateWatchFaceDetailView(watchFace);
                    }

                });
    }


    public void zipFileAndSentToWatch(final Activity activity, final WatchFaceBean watchface,
                                      final WatchFacesModel.FacePath facePath) {
        Observable.create(new Observable.OnSubscribe<WatchFaceBean>() {
            @Override
            public void call(Subscriber<? super WatchFaceBean> subscriber) {
                try {
                    byte[] bytes;
                    if (facePath.equals(WatchFacesModel.FacePath.FACE_CUSTOM)) {
                        bytes = FileOperation.zipFolder(watchface.getName());
                    }else {
                        bytes = AssetsOperation.zipFolder(context, watchface.getName());
                        FileOperation.deleteFolder(watchface.getName());
                    }
                    sentToWatch(activity, bytes);
                    if (watchface == null) {
                        subscriber.onError(new Exception("User = null"));
                    } else {
                        subscriber.onNext(watchface);
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WatchFaceBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WatchFaceBean watchFaceBean) {
                        mWatchFaceView.updateWatchSent(watchFaceBean);
                    }
                });


    }


    public void sentToWatch(Activity activity, final byte[] bytes) {
        MyApplication app = (MyApplication) activity.getApplication();
        final GeakApiClient mGoogleApiClient = app.mGoogleApiclent;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (mGoogleApiClient.isConnected()) {
                    NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes
                            (mGoogleApiClient).await();
                    List<Node> nodes = result.getNodes();
                    Iterator it = nodes.iterator();
                    while (it.hasNext()) {
                        Node node = (Node) it.next();
                        Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(),
                                Const.MESSAGE_DATA_PATH,
                                bytes);
                    }
                }
            }
        });
        thread.start();

    }


    public interface IWatchFaceDetailView {
        void updateWatchFaceDetailView(WatchFace watchFace);

        void updateWatchSent(WatchFaceBean watchFace);
    }

}
