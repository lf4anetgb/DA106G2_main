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
import android.widget.SearchView;

import com.example.da106g2_main.R;
import com.example.da106g2_main.tools.CommunicationTask;
import com.example.da106g2_main.tools.ImageTask;
import com.example.da106g2_main.tools.RecyclerViewAdapter;
import com.example.da106g2_main.tools.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class VideoListFragment extends Fragment implements SearchView.OnQueryTextListener {
    private final static String TAG = "VideoListFragment";
    private NavController navController;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageTask[] imageTasks;
    private CommunicationTask getLiveTask;
    private SearchView svVideo;

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
        svVideo = view.findViewById(R.id.svVideo);
        svVideo.setOnQueryTextListener(this);

        //設定Layout格式
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);//固定大小及模式
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!Util.networkConnected(getActivity())) {
            Util.showToast(getActivity(), R.string.not_connected);
            return;
        }

        //先判斷是否之前有搜尋結果，如有結果就讀取出後請空儲存
        //注意第一次進入惠讀取Exception
        String inStr = "";
        try {
            inStr = getArguments().getString("searchVideo");
            getArguments().remove("searchVideo");
        } catch (Exception e) {
            inStr = "";
        }


        List<Live> liveList = null; //存資料用
        Type listType = new TypeToken<List<Live>>() {
        }.getType();
        JsonObject jsonObject = new JsonObject();

        if (inStr == null || inStr.length() <= 0) {
            //包裝任務
            jsonObject.addProperty("action", "getAll");
        } else {
            jsonObject.addProperty("action", "searchByString");
            jsonObject.addProperty("string", inStr);
        }


        //下命令
        getLiveTask = new CommunicationTask(Util.URL + "Android/LiveServlet", jsonObject.toString());

        try {
            String jsonIn = getLiveTask.execute().get();
            Log.d(TAG, "jsonIN: " + jsonIn);
            liveList = new Gson().fromJson(jsonIn, listType);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        if (liveList == null || liveList.isEmpty()) {
            Util.showToast(getActivity(), R.string.no_data);
            return;
        }

        imageTasks = new ImageTask[liveList.size()];

        adapter = new RecyclerViewAdapter(liveList, RecyclerViewAdapter.LAYOUT_VIDEO_LIST, navController);
        adapter.setImageTasks(imageTasks, getView());
        recyclerView.setAdapter(adapter);
    }

    //離開畫面時執行
    @Override
    public void onStop() {
        super.onStop();

        ImageTask[] imageTasks_ = adapter.getImageTasks();

        for (int i = 0; i < imageTasks_.length; i++) {
            if (imageTasks_[i] != null) {
                imageTasks_[i].cancel(true);
                imageTasks_[i] = null;
            }
        }

        if (getLiveTask != null) {
            getLiveTask.cancel(true);
            getLiveTask = null;
        }
    }

    //搜尋送出
    @Override
    public boolean onQueryTextSubmit(String query) {
        Bundle bundle = new Bundle();
        bundle.putString("searchVideo", query);
        navController.navigate(R.id.action_videoListFragment_self, bundle);
        return true;
    }

    //搜尋改變
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
