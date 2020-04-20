package com.example.da106g2_main.video;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.da106g2_main.R;
import com.example.da106g2_main.tools.CommunicationTask;
import com.example.da106g2_main.tools.ImageTask;
import com.example.da106g2_main.tools.RecyclerViewAdapter;
import com.example.da106g2_main.tools.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class VideoListFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = "VideoListFragment";
    private NavController navController;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageTask imageTask;
    private CommunicationTask getLiveTask;

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

        recyclerView.setHasFixedSize(true);//固定大小及模式

        //設定Layout格式
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.networkConnected(getActivity())) {
            //包裝任務
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");

            //下命令
            getLiveTask = new CommunicationTask(Util.URL + "Android/LiveServlet", jsonObject.toString());

            List<Live> liveList = null; //存資料用

            try {
                String jsonIn = getLiveTask.execute().get();
                Log.d(TAG, "jsonIN: " + jsonIn);
                Type listType = new TypeToken<List<Live>>() {
                }.getType();
                liveList = new Gson().fromJson(jsonIn, listType);
                Log.d(TAG, "liveList.get(1).getWatcher_num():" + liveList.get(1).getWatched_num().toString());
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }

            if (!(liveList == null || liveList.isEmpty())) {
                adapter = new RecyclerViewAdapter(liveList, RecyclerViewAdapter.LAYOUT_VIDEO_LIST, navController);
                adapter.setImageTask(imageTask);
                recyclerView.setAdapter(adapter);
            }

        }
    }

    //導向用
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                navController.navigate(R.id.action_videoListFragment_to_videoSearchFragment);
                break;
            case R.id.btnUpload:
                navController.navigate(R.id.action_videoListFragment_to_videoUploadFragment);
                break;
        }
    }

    //離開畫面時執行
    @Override
    public void onStop() {
        super.onStop();
        if (imageTask != null) {
            imageTask.cancel(true);
        }

        if (getLiveTask != null) {
            getLiveTask.cancel(true);
        }
    }
}
