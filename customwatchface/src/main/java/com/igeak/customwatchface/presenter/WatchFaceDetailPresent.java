package com.igeak.customwatchface.presenter;

import android.app.Activity;
import android.content.Context;

import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFaceDetailModel;
import com.igeak.customwatchface.model.WatchFacesModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by xuqiang on 16-5-19.
 */
public class WatchFaceDetailPresent implements IWatchFaceDetailContract.IWatchFaceDetailPresent {
    private IWatchFaceDetailContract.IWatchFaceDetailView mWatchFaceView;
    private WatchFaceDetailModel mWatchFaceDetailModel;
    private Context context;


    public WatchFaceDetailPresent(Context context, IWatchFaceDetailContract.IWatchFaceDetailView
            watchFaceView) {
        this.mWatchFaceView = watchFaceView;
        mWatchFaceDetailModel = new WatchFaceDetailModel(context);
        this.context = context;
    }

    @Override
    public void loadWatchimg(final WatchFaceBean watchFaceBean,
                             final WatchFacesModel.FacePath facePath) {
        mWatchFaceDetailModel.loadWatchimg(watchFaceBean, facePath)
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

    @Override
    public void zipFileAndSentToWatch(final Activity activity, final WatchFaceBean watchface,
                                      final WatchFacesModel.FacePath facePath) {
        mWatchFaceDetailModel.zipFileAndSentToWatch(activity, watchface, facePath)
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


}
