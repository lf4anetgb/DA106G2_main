package com.example.da106g2_main.member;

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
import com.example.da106g2_main.mall.Orders;
import com.example.da106g2_main.tools.CommunicationTask;
import com.example.da106g2_main.tools.ImageTask;
import com.example.da106g2_main.tools.RecyclerViewAdapter;
import com.example.da106g2_main.tools.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemberOrdersListFragment extends Fragment {
    private final static String TAG = "MemberOrdersListFragment";
    private NavController navController;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageTask imageTask;
    private CommunicationTask getOrdersTask;

    public MemberOrdersListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member_orders_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.orderRecyclerView);

        //設定Layout格式
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);//固定大小及模式

        if (!Util.networkConnected(getActivity())) {
            Util.showToast(getActivity(), R.string.not_connected);
            return;
        }

        String member_id = getArguments().getString("memberID");
        //包裝任務
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getAll");
        jsonObject.addProperty("member_id", member_id);

        getOrdersTask = new CommunicationTask(Util.URL + "Android/OrdersServlet", jsonObject.toString());
        getOrdersTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();

        List<Orders> orders = null;

        try {
            String jsonIn = getOrdersTask.get();
            Type listType = new TypeToken<List<Orders>>() {
            }.getType();
            orders = new Gson().fromJson(jsonIn, listType);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        if (orders == null || orders.isEmpty()) {
            Util.showToast(getActivity(), R.string.no_data);
            return;
        }

        adapter = new RecyclerViewAdapter(orders, RecyclerViewAdapter.LAYOUT_ORDER_LIST, navController);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (imageTask != null) {
            imageTask.cancel(true);
            imageTask = null;
        }
        if (getOrdersTask != null) {
            getOrdersTask.cancel(true);
            getOrdersTask = null;
        }
    }
}
