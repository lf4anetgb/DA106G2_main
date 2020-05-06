package com.example.da106g2_main.mall;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.da106g2_main.R;
import com.example.da106g2_main.tools.ImageTask;
import com.example.da106g2_main.tools.Util;

import static com.example.da106g2_main.tools.Util.CART;

import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoopingFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = "ShoopingFragment";

    private TextView tvCommodityTitle,//商品名稱
            tvCommodityCounter,//購物數量
            tvShoopingCommodityPrice,//價格
            tvCommodityMessage;//內容描素
    private ImageView imgViewShooping;//商品圖

    private NavController navController;
    private RecyclerView recyclerView;

    private ImageTask imageTask;//圖片任務

    private Item item;//單一商品物件

    public ShoopingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shooping, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgViewShooping = view.findViewById(R.id.imgViewShooping);
        tvCommodityTitle = view.findViewById(R.id.tvCommodityTitle);
        tvCommodityCounter = view.findViewById(R.id.tvCommodityCounter);
        tvShoopingCommodityPrice = view.findViewById(R.id.tvShoopingCommodityPrice);
        tvCommodityMessage = view.findViewById(R.id.tvCommodityMessage);
        view.findViewById(R.id.btnCommodityAdd).setOnClickListener(this);
        view.findViewById(R.id.btnCommodityLess).setOnClickListener(this);
        view.findViewById(R.id.btnShoopingToShoppingCart).setOnClickListener(this);
        view.findViewById(R.id.btnAddShoppingCart).setOnClickListener(this);

        navController = Navigation.findNavController(view);

        String jsonIn = getArguments().getString("item");
        Gson gson = new Gson();
        item = gson.fromJson(jsonIn, Item.class);

        //圖片先處理
        String url = Util.URL + "Android/ItemServlet";
        imageTask = new ImageTask(url, ImageTask.FROM_ITEM, item.getItem_id(), view.getContext().getResources().getDisplayMetrics().widthPixels, imgViewShooping);
        imageTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();

        //文字處理
        tvCommodityTitle.setText(item.getItem_name());
        tvCommodityCounter.setText(String.valueOf(1));
        tvShoopingCommodityPrice.setText(String.valueOf(item.getItem_price()));
        tvCommodityMessage.setText(item.getItem_content());

    }

    @Override
    public void onStop() {
        super.onStop();
        if (imageTask != null) {
            imageTask.cancel(true);
            imageTask = null;
        }

        if (item != null) {
            item = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //+
            case R.id.btnCommodityAdd: {
                int counter = Integer.parseInt(tvCommodityCounter.getText().toString()) + 1;
                if (counter >= item.getItem_stock()) {
                    tvCommodityCounter.setText(String.valueOf(item.getItem_stock()));
                    return;
                }
                tvCommodityCounter.setText(String.valueOf(counter));
            }
            return;

            //-
            case R.id.btnCommodityLess: {
                int counter = Integer.parseInt(tvCommodityCounter.getText().toString()) - 1;
                if (counter <= 1) {
                    tvCommodityCounter.setText(String.valueOf(1));
                    return;
                }
                tvCommodityCounter.setText(String.valueOf(counter));
            }
            return;

            //加入購物車
            case R.id.btnAddShoppingCart: {
                OrderItem orderItem = new OrderItem(item, Integer.parseInt(tvCommodityCounter.getText().toString()));
                int index = CART.indexOf(orderItem);
                if (index == -1) {
                    CART.add(orderItem);
                } else {
                    orderItem = CART.get(index);
                    orderItem.setItem_quantity(orderItem.getItem_quantity() + Integer.parseInt(tvCommodityCounter.getText().toString()));
                }
                StringBuffer sb = new StringBuffer();
                sb.append("購物車增加： ").append(item.getItem_name()).append(" ，數量： ").append(tvCommodityCounter.getText().toString());
                Util.showToast(this.getContext(), sb.toString());
            }
            return;

            //購物車
            case R.id.btnShoopingToShoppingCart: {
                navController.navigate(R.id.action_shoopingFragment_to_shoppingCartFragment);
            }
            return;
        }
    }
}
