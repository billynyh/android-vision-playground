package io.github.billynyh.videofacetrack.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import io.github.billynyh.videofacetrack.R;

/**
 * Created by billy on 15/05/16.
 */
public class VideoFaceTrackViewHolder extends RecyclerView.ViewHolder {

  public final FaceDraweeView thumbView;
  public final TextView textView;

  public VideoFaceTrackViewHolder(View itemView) {
    super(itemView);
    this.thumbView = (FaceDraweeView) itemView.findViewById(R.id.thumb);
    this.textView = (TextView) itemView.findViewById(R.id.text);
  }
}
