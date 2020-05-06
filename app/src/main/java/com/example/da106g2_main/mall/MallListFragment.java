package com.example.da106g2_main.mall;

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
public class MallListFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = "MallListFragment";

    private NavController navController;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageTask imageTask;
    private CommunicationTask getCommodityTask;

    public MallListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mall_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.mallRecyclerView);
        view.findViewById(R.id.btnSearchCommodity).setOnClickListener(this);
        view.findViewById(R.id.btnMallToShoppingCart).setOnClickListener(this);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!Util.networkConnected(getActivity())) {
            Util.showToast(getActivity(), R.string.not_connected);
            return;
        }

        //包裝任務
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getAll");

        //下命令
        getCommodityTask = new CommunicationTask(Util.URL + "Android/ItemServlet", jsonObject.toString());

        List<Item> itemList = null; //存資料用

        try {
            String jsonIn = getCommodityTask.execute().get();
            Log.d(TAG, "jsonIN: " + jsonIn);
            Type listType = new TypeToken<List<Item>>() {
            }.getType();
            itemList = new Gson().fromJson(jsonIn, listType);

        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        if (itemList == null || itemList.isEmpty()) {
            Util.showToast(getActivity(), R.string.no_data);
            return;
        }

        adapter = new RecyclerViewAdapter(itemList, RecyclerViewAdapter.LAYOUT_MALL_LIST, navController);
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

        if (getCommodityTask != null) {
            getCommodityTask.cancel(true);
            getCommodityTask = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearchCommodity:
                navController.navigate(R.id.action_mallListFragment_to_searchCommodityFragment);
                return;
            case R.id.btnMallToShoppingCart:
                navController.navigate(R.id.action_mallListFragment_to_shoppingCartFragment);
                return;
        }
    }
}
