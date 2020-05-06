package com.example.da106g2_main.camp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.da106g2_main.R;
import com.example.da106g2_main.tools.CommunicationTask;
import com.example.da106g2_main.tools.RecyclerViewAdapter;
import com.example.da106g2_main.tools.Util;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CampListFragment extends Fragment {
    private final static String TAG = "CampListFragment",
            SHARED_PREFERENCES_GUIDE = "member";

    private NavController navController;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private CommunicationTask getBookingTask;

    public CampListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camp_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.campRecyclerView);

        //設定Layout格式
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);//固定大小及模式
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!Util.networkConnected(getActivity())) {
            Util.showToast(getActivity(), R.string.not_connected);
            return;
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_GUIDE, Context.MODE_PRIVATE);
        String member_id = sharedPreferences.getString("memberID", "");
        if ("".equals(member_id)) {
            navController.navigate(R.id.action_campListFragment_to_memberLoginFragment);
            return;
        }

        //包裝任務
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getAllByBK_Number");
        jsonObject.addProperty("member_id", member_id);

        getBookingTask = new CommunicationTask(Util.URL + "Android/BookingServlet", jsonObject.toString());

        List<Booking> bookingList = null; //存資料用

        try {
            String jsonIn = getBookingTask.execute().get();
            Log.d(TAG, "jsonIN: " + jsonIn);
            Type listType = new TypeToken<List<Booking>>() {
            }.getType();
            bookingList = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(jsonIn, listType);

        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        if (bookingList == null || bookingList.isEmpty()) {
            Util.showToast(getActivity(), R.string.no_data);
            return;
        }

        adapter = new RecyclerViewAdapter(bookingList, RecyclerViewAdapter.LAYOUT_BOOKING_LIST, navController);
        adapter.setView_(getView());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getBookingTask != null) {
            getBookingTask.cancel(true);
            getBookingTask = null;
        }
    }
}
