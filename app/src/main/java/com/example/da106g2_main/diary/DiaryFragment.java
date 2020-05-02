package com.example.da106g2_main.diary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.da106g2_main.R;
import com.google.android.gms.maps.MapView;
import com.google.gson.Gson;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends Fragment {
    private final static String TAG = "DiaryFragment";

    private TextView tvDiaryTitle, tvDiaryMember, tvDiaryDate, tvDiaryWrite;
    private MapView mapView;
    private RecyclerView imgDiaryRecyclerView;
    private NavController navController;

    public DiaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        tvDiaryDate = view.findViewById(R.id.tvDiaryDate);
        tvDiaryMember = view.findViewById(R.id.tvDiaryMember);
        tvDiaryTitle = view.findViewById(R.id.tvDiaryTitle);
        tvDiaryWrite = view.findViewById(R.id.tvDiaryWrite);
        mapView = view.findViewById(R.id.mapView);
        imgDiaryRecyclerView = view.findViewById(R.id.imgDiaryRecyclerView);
    }

    @Override
    public void onStart() {
        super.onStart();
        String jsonIn = getArguments().getString("diary");
        Gson gson = new Gson();
        Diary diary = gson.fromJson(jsonIn, Diary.class);
        tvDiaryDate.setText(diary.getDiary_time().toString());
        tvDiaryMember.setText(diary.getMember_id());
        tvDiaryTitle.setText(diary.getDiary_title());
        tvDiaryWrite.setText(diary.getDiary_write());

    }

    //RecyclerView圖片專用
    private class ImgRecyclerAdapter {

    }
}
