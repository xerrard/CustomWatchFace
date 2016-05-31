package com.igeak.customwatchface;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by xuqiang on 16-5-23.
 */
public class FileUtil {
    static String ZIPNAME = "mywatch.zip";

    public static String getFileFromBytes(byte[] bytes){
        String filename = ZIPNAME;
        String folderstring = new StringBuffer()
                .append(getExternalStoragePath())
                .append(File.separator)
                .append("customwatchs")
                .toString();

        try {
            File folder = new File(folderstring);
            if (!folder.exists()){
                folder.mkdirs();
            }
            File file = new File(folderstring,filename);
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getExternalStoragePath() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            return Environment.getExternalStorageDirectory()
                    .getPath();

        }
        return null;
    }

    public static void unZipFile(){


    }

}
