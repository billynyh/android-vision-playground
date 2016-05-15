package io.github.billynyh.videofacetrack;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by billy on 15/05/16.
 */
public class VideoFaceTrackApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
