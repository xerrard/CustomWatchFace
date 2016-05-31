package com.igeak.customwatchface.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.gson.Gson;
import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.util.AssertFileUtil;
import com.igeak.customwatchface.util.FileUtil;
import com.igeak.customwatchface.util.PicUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuqiang on 16-5-19.
 */
public class AssetsOperation {

    /**
     * 获取 javabean列表
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static List<WatchFaceBean> getWatchFaceBeanList(Context context)
            throws Exception {
        List<WatchFaceBean> watchFaceBeanList = new ArrayList<WatchFaceBean>();
        String[] watchfaces = context.getAssets().list(Const.FOLDER_NAME);
        for (String str : watchfaces) {
            String filepath = Const.FOLDER_NAME + "/" + str + "/" + Const
                    .WATCHFACEJSON;
            String json = AssertFileUtil.assetsFile2String(context, filepath);
            WatchFaceBean watchFaceBean = AssertFileUtil.jsonToJavaBean(json);
            watchFaceBeanList.add(watchFaceBean);
        }
        return watchFaceBeanList;
    }


    /**
     * 获取表盘元素的drawable
     *
     * @param context
     * @param faceItem    表盘名
     * @param faceElement 表盘元素
     * @return
     * @throws Exception
     */
    public static Bitmap getWatchfacesElementImg(Context context, final String faceItem, String
            faceElement) throws Exception {

        String filepath = Const.FOLDER_NAME + "/" + faceItem + "/" + faceElement + Const.PNG_EXNAME;
        InputStream is = context.getAssets().open(filepath);
        return PicUtil.InputStream2Bitmap(is);
    }

    /**
     * 将需要发送的表盘打包
     * assert打包的过程比较特别，需要先复制到T卡，然后打包
     *
     * @param context
     * @param watchfaceName
     * @return
     * @throws Exception
     */
    public static byte[] zipFolder(Context context, final String
            watchfaceName) throws Exception {
        String assetsPath = Const.FOLDER_NAME + "/" + watchfaceName;
        String filePath = FileOperation.getCustomWatchfacesFolderPath() + "/" + watchfaceName;
        FileUtil.assets2Files(context,assetsPath,filePath);
        return FileOperation.zipFolder(watchfaceName);
    }

}
