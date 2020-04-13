package com.example.navigationcomponentsexample.video;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationcomponentsexample.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoListFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private RecyclerView recyclerView;
    private List<Live> liveList;

    public VideoListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.liveRecyclerView);
        view.findViewById(R.id.btnSearch).setOnClickListener(this);
        view.findViewById(R.id.btnUpload).setOnClickListener(this);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.GAP_HANDLING_NONE));

        //假資料區
        liveList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String live_id = "LLN000000" + (i + 1), // 直播ID
                    member_id = "MMN000000" + (i + 1), // 會員ID
                    videoAddress = null, // 影片位子
                    teaser_content = "呵呵", // 直播預告內容
                    title = "第二支"; // 直播標頭
            Integer status = 0, // 直播狀態
                    watcher_num = 0; // 觀看人數
            Date live_time = null; // 開始時間
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd"); // 設定日期輸入格式

            try {
                live_time = df.parse("2020/01/02");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            liveList.add(new Live(live_id, member_id, videoAddress, teaser_content, title,
                    live_time, status, watcher_num));

        }

        recyclerView.setAdapter(new LiveAdapter(liveList));
//        liveList.add()
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                navController.navigate(R.id.action_videoListFragment_to_videoSearchFragment);
                break;
            case R.id.btnUpload:
                navController.navigate(R.id.action_videoListFragment_to_videoUploadFragment);
        }
    }

    // RecyclerView調配器
    private class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.ViewHolder> {

        //設定單一的 View 所綁物件
        private class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView imageView;
            private TextView textView;

            private ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.ivLiveItem);
                textView = itemView.findViewById(R.id.tvLiveItem);
            }
        }

        private List<Live> liveList;

        private LiveAdapter(List<Live> liveList) {
            this.liveList = liveList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Live live = liveList.get(position);
            holder.textView.setText(live.getLive_id());

        }

        @Override
        public int getItemCount() {
            return liveList.size();
        }


    }
}