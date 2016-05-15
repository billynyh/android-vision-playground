package io.github.billynyh.videofacetrack.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by billy on 15/05/16.
 */
public class MediaStoreUtil {
    private static final String TAG = "MediaStoreUtil";

    private static final Uri VIDEO_URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    private static final String[] VIDEO_PROJECTION = {
        MediaStore.Video.VideoColumns._ID,
        MediaStore.Video.VideoColumns.DISPLAY_NAME,
        MediaStore.Video.VideoColumns.WIDTH,
        MediaStore.Video.VideoColumns.HEIGHT,
        MediaStore.Video.VideoColumns.DATE_TAKEN,
        MediaStore.Video.VideoColumns.DATA,
        MediaStore.Video.VideoColumns.DATE_ADDED
    };
    private static final String VIDEO_ORDER = MediaStore.Video.VideoColumns.DATE_ADDED + " desc";

    public static List<SimpleVideoItem> getVideoItemList(Context context, int limit) {
        ArrayList<SimpleVideoItem> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                VIDEO_URI, VIDEO_PROJECTION, null, null, VIDEO_ORDER);
        if (cursor != null && cursor.moveToFirst()) {
            while (true) {
                list.add(cursorToSimpleVideoItem(cursor));
                if (list.size() >= limit || !cursor.moveToNext()) {
                    break;
                }
            }
        }
        return list;
    }

    private static SimpleVideoItem cursorToSimpleVideoItem(Cursor cursor) {
        SimpleVideoItem item = new SimpleVideoItem();
        item.displayName = cursor.getString(1);
        item.width = cursor.getInt(2);
        item.height = cursor.getInt(3);
        item.dateTaken = cursor.getLong(4);
        item.data = cursor.getString(5);

        Log.d(TAG, "displayName: " + item.displayName + " data: " + item.data);
        return item;
    }
}
