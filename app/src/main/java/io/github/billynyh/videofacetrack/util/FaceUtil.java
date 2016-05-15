package io.github.billynyh.videofacetrack.util;

import android.graphics.PointF;
import android.util.SparseArray;

import com.google.android.gms.vision.face.Face;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.github.billynyh.videofacetrack.vision.ScaledFaceData;

/**
 * Created by billy on 15/05/16.
 */
public class FaceUtil {

  public static String dumpFace(Face item) {
    StringBuilder builder = new StringBuilder();
    builder.append("id: " + item.getId());
    builder.append(String.format(
        Locale.US,
        " position: (%.2f %.2f)", item.getPosition().x, item.getPosition().y));
    builder.append(" dimen:" + item.getWidth() + " " + item.getHeight());
    return builder.toString();
  }

  public static List<Face> extractFaces(SparseArray<Face> detectResults) {
    ArrayList<Face> list = new ArrayList<>();
    for (int i = 0, n = detectResults.size(); i < n; i++) {
      Face face = detectResults.get(i);
      if (face != null) {
        list.add(face);
      }
    }
    return list;
  }

  public static List<ScaledFaceData> convertFacesToScaledData(
      int width, int height, List<Face> list) {
    ArrayList<ScaledFaceData> result = new ArrayList<>(list.size());
    for (Face face : list) {
      result.add(new ScaledFaceData(
          face.getPosition().x / width,
          face.getPosition().y / height,
          face.getWidth() / width));
    }
    return result;
  }
}
