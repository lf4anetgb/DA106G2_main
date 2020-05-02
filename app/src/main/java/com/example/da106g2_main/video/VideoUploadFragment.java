package com.example.da106g2_main.video;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.da106g2_main.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoUploadFragment extends Fragment {
    private static final String SHARED_PREFERENCES_GUIDE = "member";

    private NavController navController;

    public VideoUploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_upload, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_GUIDE, Context.MODE_PRIVATE);

        if (!sharedPreferences.getBoolean("login", false)) {
            navController.navigate(R.id.action_videoUploadFragment_to_memberLoginFragment); //導入登入頁面
        }
    }
}
