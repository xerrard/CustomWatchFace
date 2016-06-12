package com.igeak.customwatchface.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.gson.Gson;
import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.util.FileUtil;
import com.igeak.customwatchface.util.PicUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuqiang on 16-5-19.
 */
public class FileOperation {


    /**
     * 外置表盘位置
     *
     * @return sdcard/custom_watchface/
     */
    public static File getCustomWatchfacesFolder() throws Exception {
        return new File(getCustomWatchfacesFolderPath());
    }

    /**
     * 外置表盘位置
     *
     * @return sdcard/custom_watchface/
     */
    public static String getCustomWatchfacesFolderPath() throws Exception {
        String filepath = new StringBuffer()
                .append(FileUtil.getExternalStoragePath())
                .append(File.separator)
                .append(Const.FOLDER_NAME)
                .toString();

        return filepath;
    }

    /**
     * 获取存储表盘数据的文件夹列表
     *
     * @return
     * @throws Exception
     */
    public static File[] getWatchFaceFileList() throws Exception {
        File[] watchfaces;
        watchfaces = getCustomWatchfacesFolder().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.contains(Const.ASSETS_WATCH_START_NAME)
                        || filename.contains(Const.WATCH_START_NAME);
            }
        });

        return watchfaces;
    }


    /**
     * 获取存储表盘数据的文件夹
     *
     * @return
     * @throws Exception
     */
    public static File getWatchFaceFile(final String watchname) throws Exception {
        File[] watchfaces;
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.equals(watchname);
            }
        };

        watchfaces = getCustomWatchfacesFolder().listFiles(filenameFilter);
        if (watchfaces.length > 0) {
            return watchfaces[0];
        } else {
            return null;
        }

    }


    /**
     * 获取 javabean列表
     *
     * @return
     * @throws Exception
     */
    public static List<WatchFaceBean> getWatchFaceBeanList()
            throws Exception {
        List<WatchFaceBean> watchFaceBeanList = new ArrayList<WatchFaceBean>();
        File[] watchfaces = getWatchFaceFileList();
        for (File watchface : watchfaces) {
            File[] jsonfiles = watchface.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(Const.JSON_EXNAME);
                }
            });
            File jsonfile = jsonfiles[0];
            if (jsonfile != null) {
                watchFaceBeanList.add(jsonToJavaBean(jsonfile));
            }
        }
        return watchFaceBeanList;
    }


    public static WatchFaceBean jsonToJavaBean(File jsonfile) throws FileNotFoundException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(
                new FileInputStream(jsonfile)));
        Gson gson = new Gson();
        WatchFaceBean watchFaceBean = gson.fromJson(bf, WatchFaceBean.class);
        return watchFaceBean;
    }


    public static void javaBean2Json(WatchFaceBean bean, File jsonfile) throws
            IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream
                (jsonfile)));
        Gson gson = new Gson();
        gson.toJson(bean,bw);

    }

    /**
     * 获取表盘元素的drawable
     *
     * @param faceItem    表盘名
     * @param faceElement 表盘元素
     * @return
     * @throws Exception
     */
    public static Bitmap getWatchfacesElementImg(final String faceItem, String
            faceElement) throws Exception {

        return PicUtil.file2Bitmap(getWatchfacesElementFile(faceItem, faceElement));
    }

    /**
     * 获取表盘元素的drawable
     *
     * @param faceItem    表盘名
     * @param faceElement 表盘元素
     * @return
     * @throws Exception
     */
    public static File getWatchfacesElementFile(final String faceItem, String
            faceElement) throws Exception {

        File drawableFile = null;
        File facedir = null;
        File[] facedirs = null; //
        File rootFolder = null;

        facedirs = getCustomWatchfacesFolder().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.equals(faceItem);
            }
        });

        facedir = facedirs[0];
        if (facedir != null && facedir.exists()) {
            drawableFile = new File(facedir, faceElement + Const.PNG_EXNAME);
        }
        return drawableFile;
    }


    /**
     * 将需要发送的表盘打包
     *
     * @param watchfaceName
     * @return
     * @throws Exception
     */
    public static byte[] zipFolder(final String watchfaceName) throws Exception {
        File watchFolder = getWatchFaceFile(watchfaceName);
        FileUtil.zipFolder(watchFolder, getZipfilePath());


        return FileUtil.file2Byte(getZipfilePath());
    }


    public static String getZipfilePath() {
        return new StringBuilder().append(FileUtil.getExternalStoragePath())
                .append(File.separator)
                .append(Const.FOLDER_NAME)
                .append(File.separator)
                .append(Const.ZIPFILE_NAME)
                .toString();
    }


    public static void deleteFolder(String watchfaceName) throws Exception {
        File watchFolder = getWatchFaceFile(watchfaceName);
        FileUtil.deleteDir(watchFolder);
    }


    public static void changeWatchName(WatchFaceBean watchFaceBean, String tarName) throws Exception {



        String oriName = watchFaceBean.getName();
        File watchFolder = getWatchFaceFile(oriName);
        File[] files = watchFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(Const.JSON_EXNAME);
            }
        });

        FileUtil.changeDirName(watchFolder, tarName);


        File jsonfile = files[0];

        watchFaceBean.setName(tarName);
        javaBean2Json(watchFaceBean,jsonfile);

    }
}
