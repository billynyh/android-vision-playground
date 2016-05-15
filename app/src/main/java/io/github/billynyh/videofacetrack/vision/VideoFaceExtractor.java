package io.github.billynyh.videofacetrack.vision;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.billynyh.videofacetrack.util.CacheFileHelper;
import io.github.billynyh.videofacetrack.util.FaceUtil;

public class VideoFaceExtractor {
  private static final String TAG = "VideoFaceExtractor";

  public interface VideoFaceExtractorListener {
    void onFramesDataChanged(List<VideoFrameTrackData> list);
  }

  private FaceDetector mDetector;
  private CacheFileHelper mCacheFileHelper;
  private VideoFaceExtractorListener mListener;
  private String mVideoPath;

  public VideoFaceExtractor(
      Context context,
      VideoFaceExtractorListener listener,
      String videoPath,
      FaceDetector detector) {
    mVideoPath = videoPath;
    mListener = listener;

    mDetector = detector;
    mCacheFileHelper = new CacheFileHelper(context);
  }

  public void start() {
    long stepUs = 500 * 1000;
    new VideoFaceTrackTask(stepUs).execute();
  }

  private class VideoFaceTrackTask extends AsyncTask<Void, List<VideoFrameTrackData>, List<VideoFrameTrackData>> {

    private long mStepUs;


    public VideoFaceTrackTask(long stepUs) {
      mStepUs = stepUs;
    }

    @Override
    protected List<VideoFrameTrackData> doInBackground(Void... voids) {
      MediaMetadataRetriever retriever = new MediaMetadataRetriever();
      retriever.setDataSource(mVideoPath);


      ArrayList<VideoFrameTrackData> list = new ArrayList<>();
      long durationUs = getDuration(retriever) * 1000;
      int videoWidth = getIntMetadata(retriever, MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
      int videoHeight = getIntMetadata(retriever, MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);

      long timeUs = 0;
      File cacheDir = mCacheFileHelper.getCacheDir("vid-" + hashCode());
      Log.d(TAG, "durationUs " + durationUs);
      while (timeUs < durationUs) {
        Log.d(TAG, "timeUs " + timeUs);
        Bitmap bitmap = retriever.getFrameAtTime(timeUs);
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        //mDetector.receiveFrame(frame);
        SparseArray<Face> detectResults = mDetector.detect(frame);
        dumpFaces(detectResults);

        File bitmapFile = new File(cacheDir, "t-" + timeUs);
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            60,
            mCacheFileHelper.getOutputStream(bitmapFile));

        List<Face> faceList = FaceUtil.extractFaces(detectResults);
        if (faceList.size() > 0) {
          list.add(new VideoFrameTrackData(
              bitmapFile.getPath(),
              faceList,
              timeUs,
              videoWidth,
              videoHeight));
          publishProgress(list);
        }

        timeUs += mStepUs;
      }

      retriever.release();
      return list;
    }

    @Override
    protected void onProgressUpdate(List<VideoFrameTrackData>... values) {
      if (mListener != null) {
        mListener.onFramesDataChanged(values[0]);
      }
    }

    @Override
    protected void onPostExecute(List<VideoFrameTrackData> list) {
      if (mListener != null) {
        mListener.onFramesDataChanged(list);
      }
    }

    private long getDuration(MediaMetadataRetriever retriever) {
      return Long.parseLong(
          retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
    }

    private int getIntMetadata(MediaMetadataRetriever retriever, int key) {
      return Integer.parseInt(retriever.extractMetadata(key));
    }

    private void dumpFaces(SparseArray<Face> list) {
      Log.d(TAG, "- dumpFaces");
      for (int i = 0, n = list.size(); i < n; i++) {
        if (list.get(i) != null) {
          Log.d(TAG, "- " + FaceUtil.dumpFace(list.get(i)));
        }
      }
    }
  }

}
