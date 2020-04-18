package com.example.da106g2_main.video;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.da106g2_main.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPlayerFragment extends Fragment {

    private View view;
    private TextView tvVideoPlayer;
    private VideoView liveVideoView;
    private MediaController mediaController;

    public VideoPlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video_player, container, false);
        tvVideoPlayer = view.findViewById(R.id.tvVideoPlayer);
        tvVideoPlayer.setText(getArguments().getString("liveID"));

        liveVideoView = view.findViewById(R.id.liveVideoView);
        String videoPath = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.video001;
        Uri uri = Uri.parse(videoPath);
        liveVideoView.setVideoURI(uri);

        mediaController = new MediaController(view.getContext());
        mediaController.setAnchorView(liveVideoView);
        liveVideoView.setMediaController(mediaController);

        liveVideoView.start();


        return view;
    }
}
