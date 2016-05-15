package io.github.billynyh.videofacetrack.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.billynyh.videofacetrack.R;
import io.github.billynyh.videofacetrack.data.VideoFaceDataSource;
import io.github.billynyh.videofacetrack.vision.VideoFrameTrackData;

/**
 * Created by billy on 15/05/16.
 */
public class VideoFaceTrackAdapter extends RecyclerView.Adapter<VideoFaceTrackViewHolder> {

  private final VideoFaceDataSource mDataSource;

  public VideoFaceTrackAdapter(VideoFaceDataSource dataSource) {
    this.mDataSource = dataSource;
  }

  @Override
  public VideoFaceTrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new VideoFaceTrackViewHolder(
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_item_face_track, null));
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(VideoFaceTrackViewHolder holder, int position) {
    VideoFrameTrackData data = mDataSource.getItem(position);
    holder.thumbView.setImageURI(data.imageUri);
    holder.thumbView.setAspectRatio(data.aspectRatio);
    holder.thumbView.setFaces(data.scaledFaceDataList);
    holder.textView.setText(data.getDisplayText());
  }

  @Override
  public int getItemCount() {
    return mDataSource.getCount();
  }
}
