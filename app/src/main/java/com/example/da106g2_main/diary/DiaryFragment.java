package com.example.da106g2_main.diary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.da106g2_main.R;
import com.example.da106g2_main.tools.ImageTask;
import com.example.da106g2_main.tools.Util;
import com.google.android.gms.maps.MapView;
import com.google.gson.Gson;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = "DiaryFragment";

    private TextView tvDiaryTitle, tvDiaryMember, tvDiaryDate, tvDiaryWrite;
    private MapView mapView;
    private ImageView imgViewDiary;
    private ImageTask imageTask;
    private NavController navController;

    private Diary diary;

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
        imgViewDiary = view.findViewById(R.id.imgViewDiary);
        imgViewDiary.setOnClickListener(this);
        String jsonIn = getArguments().getString("diary");
        Gson gson = new Gson();
        diary = gson.fromJson(jsonIn, Diary.class);

        int imageSize = view.getContext().getResources().getDisplayMetrics().widthPixels;
        String url = Util.URL + "Android/DiaryServlet";
        imageTask = new ImageTask(url, ImageTask.FROM_DIARY, diary.getDiary_id(), imageSize, imgViewDiary);
        imageTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        tvDiaryDate.setText(diary.getDiary_time().toString());
        tvDiaryMember.setText(diary.getMember_id());
        tvDiaryTitle.setText(diary.getDiary_title());
        tvDiaryWrite.setText(diary.getDiary_write());

    }

    @Override
    public void onStop() {
        super.onStop();
        if (imageTask != null) {
            imageTask.cancel(true);
            imageTask = null;
        }

        if (diary != null) {
            diary = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.imgViewDiary) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("diary_id", diary.getDiary_id());

        navController.navigate(R.id.action_diaryFragment_to_diaryShowImageFragment, bundle);
    }
}
