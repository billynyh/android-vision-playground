package io.github.billynyh.videofacetrack.util;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by billy on 15/05/16.
 */
public class CacheFileHelper {

    private Context mContext;

    public CacheFileHelper(Context context) {
        mContext = context;
    }

    public File getCacheDir() {
        return mContext.getCacheDir();
    }

    public File getCacheDir(String dirName) {
        File dir = new File(getCacheDir(), dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public OutputStream getOutputStream(File file) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
        }
        return os;
    }
}
