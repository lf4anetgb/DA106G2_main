package com.example.da106g2_main.mall;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.da106g2_main.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchCommodityFragment extends Fragment {

    public SearchCommodityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_commodity, container, false);
    }
}
