package com.example.da106g2_main.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.da106g2_main.R;
import com.example.da106g2_main.video.Live;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

//此為通用RecyclerViewAdapter，只要傳入想Layout的樣式及NavController即可
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List data;
    private int layoutType;
    private NavController navController;
    private ImageTask imageTask = null;

    //用於RecyclerView選擇
    public final static int LAYOUT_VIDEO_LIST = 0;

    //設定多媒體連線物件
    public void setImageTask(ImageTask imageTask) {
        this.imageTask = imageTask;
    }

    public RecyclerViewAdapter(List data, int layoutType, NavController navController) {
        this.data = data;
        this.layoutType = layoutType;
        this.navController = navController;
    }

    public RecyclerViewAdapter(List data, int layoutType) {
        this.data = data;
        this.layoutType = layoutType;
    }

    @Override
    public int getItemViewType(int position) {
        return layoutType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case LAYOUT_VIDEO_LIST: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
                RecyclerViewAdapter.ViewHolder_VideoList holder = new RecyclerViewAdapter.ViewHolder_VideoList(view);
                return holder;
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case LAYOUT_VIDEO_LIST: {
                final Live live = (Live) data.get(position);

                String url = Util.URL + "Android/LiveServlet";
                ViewHolder_VideoList holderVideoList = (ViewHolder_VideoList) holder;

                imageTask = new ImageTask(url, ImageTask.FROM_LIVE, live.getLive_id(), 0, holderVideoList.ivLiveItem);
                imageTask.execute();

                holderVideoList.tvLiveItemTitle.setText(live.getTitle());
                holderVideoList.tvMessage.setText(live.getMember_id() + "·觀看次數：" + live.getWatched_num() + "·");
                holderVideoList.videoItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("live_id", live.getLive_id());
                        bundle.putString("member_id", live.getMember_id());
                        bundle.putString("teaser_content", live.getTeaser_content());
                        bundle.putString("title", live.getTitle());
                        bundle.putString("live_time", live.getLive_time().toString());
                        bundle.putInt("watcher_num", live.getWatched_num());

                        navController.navigate(R.id.action_videoListFragment_to_videoPlayerFragment, bundle);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //影片清單用
    class ViewHolder_VideoList extends RecyclerView.ViewHolder {
        private CardView videoItem;
        private ImageView ivLiveItem;
        private TextView tvLiveItemTitle, tvMessage;

        ViewHolder_VideoList(@NonNull View itemView) {
            super(itemView);
            videoItem = itemView.findViewById(R.id.videoItem);
            ivLiveItem = itemView.findViewById(R.id.ivLiveItem);
            tvLiveItemTitle = itemView.findViewById(R.id.tvLiveItemTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }

}
