package com.igeak.customwatchface.presenter;

import android.content.Context;

import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.model.AssetsOperation;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFacesModel;
import com.igeak.customwatchface.view.watchfaceview.WatchPreviewView;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuqiang on 16-5-19.
 */
public class WatchFacesPresent {
    private IWatchFacesView mWatchFaceView;
    private WatchFacesModel mWatchFacesModel;
    private Context context;


    public WatchFacesPresent(IWatchFacesView watchFaceView, Context context) {
        this.mWatchFaceView = watchFaceView;
        mWatchFacesModel = new WatchFacesModel(context);
        this.context = context;

    }

    public void getWatchfaceBeanList(WatchFacesModel.FacePath facePath) {
        mWatchFacesModel.getWatchfaceBeanList(facePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<WatchFaceBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<WatchFaceBean> watchFaceBeanList) {
                        mWatchFaceView.updateWatchFaceBeanList(watchFaceBeanList);
                    }
                });
    }


    public void creatNewFace(final WatchFaceBean watchface) {
        Observable.create(new Observable.OnSubscribe<WatchFaceBean>() {
            @Override
            public void call(Subscriber<? super WatchFaceBean> subscriber) {
                try {
                    AssetsOperation.assert2Folder(context, watchface.getName());
                    if (watchface == null) {
                        subscriber.onError(new Exception("User = null"));
                    } else {
                        subscriber.onNext(watchface);
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
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
                        mWatchFaceView.onWatchCreated(watchFaceBean);
                    }
                });


    }


    public interface IWatchFacesView {
        void updateWatchFaceBeanList(List<WatchFaceBean> watchFaceBeanList);

        void updateWatchFace(WatchPreviewView imageview, WatchFace watchFace);

        void onWatchCreated(WatchFaceBean watchFaceBean);
    }


    public void loadWatchimg(final WatchPreviewView imageView, final WatchFaceBean watchFaceBean,
                             final WatchFacesModel.FacePath facePath) {
        mWatchFacesModel.loadWatchimg(watchFaceBean, facePath)
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
                        mWatchFaceView.updateWatchFace(imageView, watchFace);
                    }

                });
    }


}
