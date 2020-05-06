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
import com.example.da106g2_main.tools.CommunicationTask;
import com.example.da106g2_main.tools.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPlayerFragment extends Fragment {
    private static final String TAG = "VideoPlayerFragment";

    private TextView tvVideoPlayerTitle, tvVideoPlayerWatcher, tvVideoPlayerTime, tvVideoPlayerTeaserContent;
    private VideoView liveVideoView;
    private MediaController mediaController;
    private CommunicationTask getLiveTask;

    private Live live;

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

        //緩存監聽器
        liveVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    //緩存開始
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        if (liveVideoView.isPlaying()) {
                            liveVideoView.pause();
                        }
                        break;

                    //緩存結束
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        liveVideoView.start();
                        break;

                }
                return true;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        live.setWatched_num(live.getWatched_num() + 1);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "updateLiveNoBLOB");
        jsonObject.addProperty("Live", new Gson().toJson(live));

        getLiveTask = new CommunicationTask(Util.URL + "Android/LiveServlet", jsonObject.toString());
        try {
            getLiveTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getLiveTask.cancel(true);
        getLiveTask = null;
    }
}
