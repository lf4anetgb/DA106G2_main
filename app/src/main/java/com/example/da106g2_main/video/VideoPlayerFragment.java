package com.example.da106g2_main.video;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.da106g2_main.R;
import com.example.da106g2_main.tools.Util;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPlayerFragment extends Fragment {
    private static final String TAG = "VideoPlayerFragment";

    private TextView tvVideoPlayerTitle, tvVideoPlayerWatcher, tvVideoPlayerTime, tvVideoPlayerTeaserContent;
    private VideoView liveVideoView;
    private MediaController mediaController;

    Live live;

    public VideoPlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvVideoPlayerTitle = view.findViewById(R.id.tvVideoPlayerTitle);
        tvVideoPlayerWatcher = view.findViewById(R.id.tvVideoPlayerWatcher);
        tvVideoPlayerTime = view.findViewById(R.id.tvVideoPlayerTime);
        tvVideoPlayerTeaserContent = view.findViewById(R.id.tvVideoPlayerTeaserContent);
        liveVideoView = view.findViewById(R.id.liveVideoView);

        String jsonIn = getArguments().getString("live");
        Gson gson = new Gson();
        live = gson.fromJson(jsonIn, Live.class);

        //測試影片
//        String videoPath = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.video002;
//        Uri uri = Uri.parse(videoPath);

        Uri uri = Uri.parse(Util.URL + "Android/GetVideoServlet?live_id=" + live.getLive_id());
        liveVideoView.setVideoURI(uri);

        mediaController = new MediaController(view.getContext());
        mediaController.setAnchorView(liveVideoView);
        liveVideoView.setMediaController(mediaController);




    }

    @Override
    public void onStart() {
        super.onStart();

        tvVideoPlayerTitle.setText(live.getTitle());
        tvVideoPlayerWatcher.setText(String.valueOf(live.getWatched_num()));
        tvVideoPlayerTime.setText(live.getLive_time().toString());
        tvVideoPlayerTeaserContent.setText(live.getTeaser_content());

        liveVideoView.requestFocus();
        liveVideoView.getCurrentPosition();

        liveVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                liveVideoView.start();
            }
        });
    }
}
