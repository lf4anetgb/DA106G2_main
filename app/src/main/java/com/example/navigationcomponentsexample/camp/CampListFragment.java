package com.example.navigationcomponentsexample.camp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.navigationcomponentsexample.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CampListFragment extends Fragment {

    public CampListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camp_list, container, false);
    }
}
