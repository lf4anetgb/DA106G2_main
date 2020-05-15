package com.example.da106g2_main.diary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.da106g2_main.R;
import com.example.da106g2_main.tools.ImageTask;
import com.example.da106g2_main.tools.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryShowImageFragment extends Fragment {
    private ImageView imgViewDiaryShow;
    private ImageTask imageTask;

    public DiaryShowImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary_show_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgViewDiaryShow = view.findViewById(R.id.imgViewDiaryShow);
        String url = Util.URL + "Android/DiaryServlet";
        imageTask = new ImageTask(url, ImageTask.FROM_DIARY, getArguments().getString("diary_id"), 1, imgViewDiaryShow);
        imageTask.execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (imageTask != null) {
            imageTask.cancel(true);
            imageTask = null;
        }
    }
}
