package io.github.billynyh.videofacetrack.vision;

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.net.Uri;

import com.google.android.gms.vision.face.Face;

import java.io.File;
import java.util.List;

import io.github.billynyh.videofacetrack.util.FaceUtil;

/**
 * Created by billy on 15/05/16.
 */
public class VideoFrameTrackData {

  public final String bitmapPath;
  public final Uri imageUri;
  public final List<Face> faces;
  public final List<ScaledFaceData> scaledFaceDataList;
  public final long timeUs;
  public final float aspectRatio;

  private String mDisplayText;

  public VideoFrameTrackData(
      String bitmapPath, List<Face> faces, long timeUs, int width, int height) {
    this.bitmapPath = bitmapPath;
    this.faces = faces;
    this.scaledFaceDataList = FaceUtil.convertFacesToScaledData(width, height, faces);
    this.timeUs = timeUs;
    this.imageUri = Uri.fromFile(new File(bitmapPath));
    this.aspectRatio = 1f * width / height;
  }

  @SuppressLint("DefaultLocale")
  public String getDisplayText() {
    if (mDisplayText == null) {
      StringBuilder builder = new StringBuilder();
      builder.append("Time: ").append(timeUs / 1000 / 1000).append("\n");
      builder.append("Size: ").append(faces.size());
      for (ScaledFaceData data : scaledFaceDataList) {
        builder.append(String.format(" %.2f %.2f", data.x, data.y));
      }
      mDisplayText = builder.toString();
    }
    return mDisplayText;
  }

}
