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

import java.util.List;

//此為通用RecyclerViewAdapter，只要傳入想Layout的樣式及NavController即可
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List data;
    private int layoutType;
    private NavController navController;

    //用於RecyclerView選擇
    public final static int LAYOUT_VIDEO_LIST = 0;

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
                ViewHolder_VideoList holderVideoList = (ViewHolder_VideoList) holder;
                holderVideoList.textView.setText(live.getLive_id());
                holderVideoList.videoItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("liveID", live.getLive_id());
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
        private ImageView imageView;
        private TextView textView;

        ViewHolder_VideoList(@NonNull View itemView) {
            super(itemView);
            videoItem = itemView.findViewById(R.id.videoItem);
            imageView = itemView.findViewById(R.id.ivLiveItem);
            textView = itemView.findViewById(R.id.tvLiveItem);
        }
    }
}
