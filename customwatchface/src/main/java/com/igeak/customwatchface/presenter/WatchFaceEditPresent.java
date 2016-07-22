package com.igeak.customwatchface.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.igeak.android.common.api.GeakApiClient;
import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.model.PicOperation;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFaceEditModel;
import com.igeak.customwatchface.model.WatchFacesModel;
import com.igeak.customwatchface.util.PicUtil;
import com.igeak.customwatchface.view.view.watchfaceview.PointView;
import com.orhanobut.logger.Logger;
import com.soundcloud.android.crop.Crop;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuqiang on 16-5-27.
 */
public class WatchFaceEditPresent implements IWatchFaceEditContract.IWatchFaceEditPresent {
    private Context context;
    private WatchFaceEditModel model;
    private IWatchFaceEditContract.IWatchFaceEditView watchfaceview;
    private IWatchFaceEditContract.IBackgroundView backgroundView;
    private IWatchFaceEditContract.IScaleView scaleView;
    private IWatchFaceEditContract.IPointView pointView;

    public WatchFaceEditPresent(Context context
            , IWatchFaceEditContract.IWatchFaceEditView watchfaceview
            , IWatchFaceEditContract.IBackgroundView backgroundView
            , IWatchFaceEditContract.IScaleView scaleView
            , IWatchFaceEditContract.IPointView pointView) {
        this.context = context;
        model = new WatchFaceEditModel(context);
        this.watchfaceview = watchfaceview;
        this.backgroundView = backgroundView;
        this.scaleView = scaleView;
        this.pointView = pointView;
    }

    @Override
    public List<InputStream> loadbackImg() {
        try {
            return model.loadbackImg();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeBackImg(Bitmap bitmap) {
        watchfaceview.updatebackground(bitmap);
        model.saveBackImg(bitmap);
    }



    @Override
    public List<InputStream> loadScaleImg() {
        try {
            return model.loadScaleImg();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeScaleImg(Bitmap bitmap) {
        watchfaceview.updateScale(bitmap);
        model.saveScaleImg(bitmap);

    }

    @Override
    public List<Map<PointView.Type, InputStream>> loadPointImg() {
        try {
            return model.loadPointImg();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changePointImg(Map<PointView.Type, Bitmap> point) {
        watchfaceview.updatePoint(point);
        model.savePointImg(point);
    }


    @Override
    public void loadWatchimg(final WatchFaceBean watchFaceBean, final WatchFacesModel.FacePath facePath) {
        model.loadWatchimg(watchFaceBean, facePath)
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
                        watchfaceview.updateWatchFace(watchFace);
                    }

                });
    }

    public void handleCrop(int resultCode, Intent result) {
        final int width = watchfaceview.getWatchWidth();
        final int height = watchfaceview.getWatchHeight();
        try {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = Crop.getOutput(result);

                //Bitmap photo = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                final InputStream stream = context.getContentResolver().openInputStream(uri);
                //Drawable drawable = Drawable.createFromStream(stream, null);
                //Bitmap bitmap = PicUtil.drawable2Bitmap(drawable);

                //Logger.i("   photo.getWidth() = " + photo.getWidth()
                //        + "   photo.getHeight() = " + photo.getHeight());
                //Bitmap bitmap = PicUtil.InputStream2Bitmap(stream);
                Observable.create(new Observable.OnSubscribe<Bitmap>() {
                    @Override
                    public void call(Subscriber<? super Bitmap> subscriber) {
                        try {
                            Bitmap bitmap = PicOperation.InputStream2Bitmap(stream,width,height);
                            subscriber.onNext(bitmap);
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Bitmap>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Bitmap bitmap) {
                                changeBackImg(bitmap);
                            }

                        });
            } else if (resultCode == Crop.RESULT_ERROR) {
                Toast.makeText(context, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void savewatch(String name) {
        model.savewatch(name)
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
                    public void onNext(WatchFaceBean watchFace) {
                        //watchfaceview.updateWatchFace(watchFace);
                        watchfaceview.updateSaved();
                    }

                });


    }

    @Override
    public void zipFileAndSentToWatch(GeakApiClient googleApiClient, final WatchFaceBean watchface,
                                      final WatchFacesModel.FacePath facePath) {
        model.zipFileAndSentToWatch(googleApiClient, watchface, facePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WatchFaceBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        watchfaceview.showThrowable(e);
                    }

                    @Override
                    public void onNext(WatchFaceBean watchFaceBean) {
                        watchfaceview.updateWatchSent(watchFaceBean);
                    }
                });
    }

    @Override
    public void saveAndSend(String name, GeakApiClient googleApiClient, WatchFaceBean watchface, WatchFacesModel.FacePath facePath) {
        model.saveAndSend(name,googleApiClient, watchface, facePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WatchFaceBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        watchfaceview.showThrowable(e);
                    }

                    @Override
                    public void onNext(WatchFaceBean watchFaceBean) {
                        watchfaceview.updateSaveandSent(watchFaceBean);
                    }
                });
    }


    @Override
    public void creatNewFace(String name) {
        model.createNewFace(name)
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
                    public void onNext(WatchFaceBean watchFace) {
                        //watchfaceview.updateWatchFace(watchFace);
                        watchfaceview.updateSaved();
                    }

                });

    }
}
