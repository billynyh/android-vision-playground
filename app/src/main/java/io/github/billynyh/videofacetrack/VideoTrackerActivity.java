package io.github.billynyh.videofacetrack;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.vision.face.FaceDetector;

import java.util.ArrayList;
import java.util.List;

import io.github.billynyh.videofacetrack.data.VideoFaceDataSource;
import io.github.billynyh.videofacetrack.ui.VideoFaceTrackAdapter;
import io.github.billynyh.videofacetrack.vision.VideoFaceExtractor;
import io.github.billynyh.videofacetrack.vision.VideoFrameTrackData;

/**
 * Created by billy on 15/05/16.
 */
public class VideoTrackerActivity extends AppCompatActivity {

  private static final String TAG = "VideoTrackerActivity";

  private RecyclerView mRecyclerView;
  private LinearLayoutManager mLayoutManager;
  private VideoFaceTrackAdapter mAdapter;

  private VideoFaceDataSource mDataSource;
  private VideoFaceExtractor mVideoFaceExtractor;
  private FaceDetector mFaceDetector;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video_tracker);

    Uri videoUri = getIntent().getData();
    mFaceDetector = newFaceDetector(getApplicationContext());
    mVideoFaceExtractor = new VideoFaceExtractor(
        getApplicationContext(),
        newVideoFaceExtractorListener(),
        videoUri.getPath(),
        mFaceDetector);

    mRecyclerView = (RecyclerView) findViewById(R.id.list);
    mLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLayoutManager);

    mDataSource = new VideoFaceDataSource();
    mAdapter = new VideoFaceTrackAdapter(mDataSource);
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override
  protected void onResume() {
    super.onResume();
    mVideoFaceExtractor.start();
  }

  private FaceDetector newFaceDetector(Context context) {
    FaceDetector detector = new FaceDetector.Builder(context)
        .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
        .build();
    return detector;
  }

  private VideoFaceExtractor.VideoFaceExtractorListener newVideoFaceExtractorListener() {
    return new VideoFaceExtractor.VideoFaceExtractorListener() {
      @Override
      public void onFramesDataChanged(List<VideoFrameTrackData> list) {
        mDataSource.setList(list);
        mAdapter.notifyDataSetChanged();
      }
    };
  }
}
