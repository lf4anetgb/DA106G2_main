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
import com.example.da106g2_main.mall.OrderDetail;
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
public class MemberOrderDetailFragment extends Fragment {
    private final static String TAG = "MemberOrderDetailFragment";

    private NavController navController;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageTask imageTask;
    private CommunicationTask getOrderDetailsTask;

    public MemberOrderDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member_order_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.orderDetailRecyclerView);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        if (!Util.networkConnected(getActivity())) {
            Util.showToast(getActivity(), R.string.not_connected);
            return;
        }

        String jsonIn = getArguments().getString("order");
        String orderID = null;
        if (jsonIn == null || jsonIn.length() <= 0) {
            orderID = getArguments().getString("order_id");
        } else {
            Gson gson = new Gson();
            orderID = gson.fromJson(jsonIn, Orders.class).getOrder_id();
        }

        //包裝任務
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getAll");
        jsonObject.addProperty("order_id", orderID);

        getOrderDetailsTask = new CommunicationTask(Util.URL + "Android/Order_detailServlet", jsonObject.toString());
        getOrderDetailsTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();

        List<OrderDetail> orderDetails = null;
        try {
            String jsonIn = getOrderDetailsTask.get();
            Type listType = new TypeToken<List<OrderDetail>>() {
            }.getType();
            orderDetails = new Gson().fromJson(jsonIn, listType);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        if (orderDetails == null || orderDetails.isEmpty()) {
            Util.showToast(getActivity(), R.string.no_data);
            return;
        }

        adapter = new RecyclerViewAdapter(orderDetails, RecyclerViewAdapter.LAYOUT_ORDER_DETAIL_LIST, navController);
        adapter.setCommunicationTask(getOrderDetailsTask);
        adapter.setImageTask(imageTask, getView());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (imageTask != null) {
            imageTask.cancel(true);
            imageTask = null;
        }

        if (getOrderDetailsTask != null) {
            getOrderDetailsTask.cancel(true);
            getOrderDetailsTask = null;
        }
    }
}
