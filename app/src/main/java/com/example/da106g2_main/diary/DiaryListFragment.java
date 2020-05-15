package com.example.da106g2_main.diary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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

import java.lang.reflect.Type;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryListFragment extends Fragment {
    private final static String TAG = "DiaryListFragment";
    private NavController navController;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private CommunicationTask getDiaryTask;
    private ImageTask[] imageTasks;

    public DiaryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.diaryRecyclerView);

//        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!Util.networkConnected(getActivity())) {
            Util.showToast(getActivity(), R.string.not_connected);
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getAll");

        getDiaryTask = new CommunicationTask(Util.URL + "Android/DiaryServlet", jsonObject.toString());

        List<Diary> diaryList = null; //存資料用

        try {
            String jsonIn = getDiaryTask.execute().get();
            Log.d(TAG, "jsonIN: " + jsonIn);
            Type listType = new TypeToken<List<Diary>>() {
            }.getType();
            diaryList = new Gson().fromJson(jsonIn, listType);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        if (diaryList == null || diaryList.isEmpty()) {
            Util.showToast(getActivity(), R.string.no_data);
            return;
        }

        imageTasks = new ImageTask[diaryList.size()];

        adapter = new RecyclerViewAdapter(diaryList, RecyclerViewAdapter.LAYOUT_DIARY_LIST, navController);
        adapter.setImageTasks(imageTasks, getView());
        recyclerView.setAdapter(adapter);
    }

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

        if (getDiaryTask != null) {
            getDiaryTask.cancel(true);
            getDiaryTask = null;
        }
    }

}
