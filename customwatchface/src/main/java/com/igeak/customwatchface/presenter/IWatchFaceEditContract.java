package com.igeak.customwatchface.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFacesModel;
import com.igeak.customwatchface.view.IView;
import com.igeak.customwatchface.view.view.watchfaceview.PointView;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by xuqiang on 16-6-6.
 */
public interface IWatchFaceEditContract {


    interface IWatchFaceEditView extends IView {
        void updateWatchFace(WatchFace watchFace);

        //void updatebackground(InputStream bitmap);

        void updatebackground(Bitmap bitmap);

        void updateScale(Bitmap bitmap);

        void updatePoint(Map<PointView.Type, Bitmap> map);


        void updateSaved();
    }

    interface IBackgroundView {

    }

    interface IScaleView {

    }

    interface IPointView {

    }


    interface IWatchFaceEditPresent extends IPresenter<IWatchFaceEditView>{
        List<InputStream> loadbackImg();
        void changeBackImg(Bitmap bitmap);

        List<InputStream> loadScaleImg();
        void changeScaleImg(Bitmap bitmap);

        List<Map<PointView.Type, InputStream>> loadPointImg();
        void changePointImg(Map<PointView.Type, Bitmap> point);

        void loadWatchimg(final WatchFaceBean watchFaceBean,final WatchFacesModel.FacePath facePath);


        void savewatch(String name);
        void creatNewFace(String name);
    }
}
