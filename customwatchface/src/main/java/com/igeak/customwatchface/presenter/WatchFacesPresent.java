package com.igeak.customwatchface.presenter;

import android.content.Context;
import android.util.Log;


import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFacesModel;
import com.igeak.customwatchface.view.watchfaceview.WatchPreviewView;

import java.util.List;

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

    public interface IWatchFacesView {
        void updateWatchFaceBeanList(List<WatchFaceBean> watchFaceBeanList);

        void updateWatchFace(WatchPreviewView imageview, WatchFace watchFace);
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
                        Log.i("xerrard","ddd onNext");
                    }

                });
    }


}
