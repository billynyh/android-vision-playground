package io.github.billynyh.videofacetrack.data;

import java.util.List;

import io.github.billynyh.videofacetrack.vision.VideoFrameTrackData;

/**
 * Created by billy on 15/05/16.
 */
public class VideoFaceDataSource {

  private List<VideoFrameTrackData> mList;

  public VideoFaceDataSource() {

  }

  public List<VideoFrameTrackData> getList() {
    return mList;
  }

  public VideoFrameTrackData getItem(int position) {
    if (mList == null) {
      return null;
    }
    return mList.get(position);
  }

  public void setList(List<VideoFrameTrackData> list) {
    mList = list;
  }

  public int getCount() {
    if (mList == null) {
      return 0;
    }
    return mList.size();
  }

}
