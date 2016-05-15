package io.github.billynyh.videofacetrack.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import io.github.billynyh.videofacetrack.vision.ScaledFaceData;

/**
 * Created by billy on 15/05/16.
 */
public class FaceDraweeView extends SimpleDraweeView {

  private List<ScaledFaceData> mList;
  private Paint mFacePaint;

  public FaceDraweeView(Context context) {
    super(context);
    init();
  }

  public FaceDraweeView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public FaceDraweeView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    mFacePaint = new Paint();
    mFacePaint.setAntiAlias(true);
    mFacePaint.setStyle(Paint.Style.STROKE);
    mFacePaint.setColor(0xffffff66);
  }

  public void setFaces(List<ScaledFaceData> list) {
    mList = list;
    postInvalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (mList != null) {
      int w = getWidth();
      int h = getHeight();
      for (ScaledFaceData face : mList) {
        canvas.drawCircle(face.x * w, face.y * h, face.r * w, mFacePaint);
      }
    }
  }
}
