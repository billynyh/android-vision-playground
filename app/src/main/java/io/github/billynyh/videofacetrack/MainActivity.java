package io.github.billynyh.videofacetrack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.billynyh.videofacetrack.data.MediaStoreUtil;
import io.github.billynyh.videofacetrack.data.SimpleVideoItem;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private VideoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new VideoAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncTask<Void, Void, List<VideoListItem>>() {

            @Override
            protected List<VideoListItem> doInBackground(Void... voids) {
                Context context = getApplicationContext();
                List<VideoListItem> list = new ArrayList<>();
                for (SimpleVideoItem item : MediaStoreUtil.getVideoItemList(context, 20)) {
                    list.add(new VideoListItem(
                            item,
                            getUri(item.data),
                            newClickListener(MainActivity.this, item.data)));
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<VideoListItem> list) {
                mAdapter.setData(list);
            }
        }.execute();
    }

    private View.OnClickListener newClickListener(final Activity context, final String path) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                launchVideoTrackerActivity(context, path);
            }
        };
    }

    private static class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {

        private List<VideoListItem> mList;

        public void setData(List<VideoListItem> list) {
            mList = list;
            notifyDataSetChanged();
        }

        @Override
        public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_video, parent, false);
            return new VideoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(VideoViewHolder holder, int position) {
            VideoListItem item = mList.get(position);
            holder.textView.setText(item.videoData.displayName);
            holder.draweeView.setImageURI(item.imageUri);
            holder.root.setOnClickListener(item.clickListener);
        }

        @Override
        public int getItemCount() {
            if (mList == null) {
                return 0;
            }
            return mList.size();
        }
    }

    private static class VideoViewHolder extends RecyclerView.ViewHolder {
        View root;
        TextView textView;
        SimpleDraweeView draweeView;

        public VideoViewHolder(View root) {
            super(root);
            this.root = root;
            this.textView = (TextView) root.findViewById(R.id.text);
            this.draweeView = (SimpleDraweeView) root.findViewById(R.id.thumb);
        }
    }

    private static Uri getUri(String path) {
        return Uri.fromFile(new File(path));
    }

    private static class VideoListItem {
        SimpleVideoItem videoData;
        Uri imageUri;
        View.OnClickListener clickListener;

        public VideoListItem(
                SimpleVideoItem videoData,
                Uri imageUri,
                View.OnClickListener clickListener) {
            this.videoData = videoData;
            this.imageUri = imageUri;
            this.clickListener = clickListener;
        }
    }

    private static void launchVideoTrackerActivity(Activity context, String videoPath) {
        Intent intent = new Intent(context, VideoTrackerActivity.class);
        intent.setData(getUri(videoPath));
        context.startActivity(intent);
    }

}
