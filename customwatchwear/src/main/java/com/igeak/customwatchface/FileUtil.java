package com.igeak.customwatchface;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by xuqiang on 16-5-23.
 */
public class FileUtil {

    public static File getFileFromBytes(byte[] bytes) {
        String filename = Const.ZIPFILE_NAME;
        String folderstring = new StringBuffer()
                .append(getExternalStoragePath())
                .append(File.separator)
                .append(Const.FOLDER_NAME)
                .toString();

        File file = null;
        try {
            file = new File(folderstring);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(folderstring, filename);
            if (!file.exists()) {
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
        return file;
    }

    public static String getExternalStoragePath() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            return Environment.getExternalStorageDirectory()
                    .getPath();

        }
        return null;
    }

    public static void unZipFile(File file) throws IOException {
        ZipInputStream inZip = null;
        try {
            String path = file.getParent();
            inZip = new ZipInputStream(new FileInputStream(file));
            ZipEntry zipEntry;
            while ((zipEntry = inZip.getNextEntry()) != null) {
                String filename = zipEntry.getName();
                File newFile = new File(path, filename);
                FileOutputStream output = null;
                try {
                    output = new FileOutputStream(newFile);
                    int len = 0;
                    byte[] buffer = new byte[4096];
                    while ((len = inZip.read(buffer)) > 0) {
                        output.write(buffer, 0, len);
                    }
                } finally {
                    // we must always close the output file
                    if (output != null) {
                        output.close();
                    }
                }
            }
        } finally {
            if (inZip != null) {
                inZip.close();
            }
        }
    }

}
