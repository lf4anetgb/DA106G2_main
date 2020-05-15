package com.example.da106g2_main.mall;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.da106g2_main.R;
import com.example.da106g2_main.member.Member;
import com.example.da106g2_main.tools.CommunicationTask;
import com.example.da106g2_main.tools.ImageTask;
import com.example.da106g2_main.tools.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.da106g2_main.tools.Util.CART;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCartFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private final static String TAG = "ShoppingCartFragment",
            SHARED_PREFERENCES_GUIDE = "member";

    private NavController navController;
    private RecyclerView recyclerView;
    private ShoppingRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tvShowTotalAmount, tvCreditBalance;
    private CommunicationTask getMemberTask, putOneOrder;
    private ImageTask imageTask;
    private EditText etCartAddress;
    private RadioGroup rgPaymentMethod;

    private int paywith = 0;

    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.cartRecyclerView);
        view.findViewById(R.id.btnCartCheckout).setOnClickListener(this);
        view.findViewById(R.id.btnCartToShopping).setOnClickListener(this);
        view.findViewById(R.id.imgvAddrMagic).setOnClickListener(this);
        tvShowTotalAmount = view.findViewById(R.id.tvShowTotalAmount);
        tvCreditBalance = view.findViewById(R.id.tvCreditBalance);
        etCartAddress = view.findViewById(R.id.etCartAddress);
        rgPaymentMethod = view.findViewById(R.id.rgPaymentMethod);
        rgPaymentMethod.setOnCheckedChangeListener(this);

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

        //先導過去檢查是否登入
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_GUIDE, Context.MODE_PRIVATE);
        String member_id = sharedPreferences.getString("memberID", "");
        if (member_id == null || member_id.length() <= 0) {
            navController.navigate(R.id.action_shoppingCartFragment_to_memberLoginFragment);
            return;
        }

        //取得目前該會員點數
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getMember");
        jsonObject.addProperty("member_id", member_id);

        getMemberTask = new CommunicationTask(Util.URL + "Android/MemberServlet", jsonObject.toString());
        getMemberTask.execute();
        tvShowTotalAmount.setText(getTotalPrice());
        try {
            String strIn = getMemberTask.get();
            tvCreditBalance.setText(String.valueOf(
                    new GsonBuilder().setDateFormat("yyyy-MM-dd").create()
                            .fromJson(strIn, Member.class).getPoint()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        adapter = new ShoppingRecyclerAdapter(CART);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (imageTask != null) {
            imageTask.cancel(true);
            imageTask = null;
        }
        if (getMemberTask != null) {
            getMemberTask.cancel(true);
            getMemberTask = null;
        }
        if (putOneOrder != null) {
            putOneOrder.cancel(true);
            putOneOrder = null;
        }
    }

    //試算總價格用
    private String getTotalPrice() {
        //包裝任務
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "trialCalculation");
        jsonObject.addProperty("cart", gson.toJson(CART));

        putOneOrder = new CommunicationTask(Util.URL + "Android/OrdersServlet", jsonObject.toString());

        String jsonIn;
        try {
            jsonIn = putOneOrder.execute().get();
            if ("false".equals(jsonIn)) {
                return "0";
            }
            return jsonIn;
        } catch (Exception e) {
            return "0";
        }

    }

    //單選單事件
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbCreditPayment:
                paywith = 0;
                break;

            case R.id.rbCreditCardPayment:
                paywith = 1;
                break;
        }
    }

    //按鈕事件群
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCartCheckout: {

                String address = etCartAddress.getText().toString();
                if (address == null || address.length() <= 0) {
                    Util.showToast(v.getContext(), "請輸入地址");
                    return;
                }

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_GUIDE, Context.MODE_PRIVATE);
                String member_id = sharedPreferences.getString("memberID", "");

                //包裝任務
                Gson gson = new Gson();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("cart", gson.toJson(CART));
                jsonObject.addProperty("member_id", member_id);
                jsonObject.addProperty("order_address", address);
                jsonObject.addProperty("paywith", paywith);

                //使用信用卡付款
                if (paywith == 1) {
                    jsonObject.addProperty("action", "insertWithOrder_details");
                    Bundle bundle = new Bundle();
                    bundle.putString("jsonObject", jsonObject.toString());
                    bundle.putString("TAG", TAG);
                    navController.navigate(R.id.action_shoppingCartFragment_to_toolsCreditCardFragment, bundle);
                    return;
                }
                jsonObject.addProperty("action", "insertWithOrder_details_payPoints");
                putOneOrder = new CommunicationTask(Util.URL + "Android/OrdersServlet", jsonObject.toString());

                String jsonIn;
                try {
                    jsonIn = putOneOrder.execute().get();
                } catch (Exception e) {
                    Util.showToast(v.getContext(), "上傳失敗");
                    Log.d(TAG, e.toString());
                    return;
                }

                if ("false".equals(jsonIn)) {
                    Util.showToast(v.getContext(), "上傳失敗");
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("order_id", jsonIn);
                Util.showToast(v.getContext(), "上傳成功");
                CART = new ArrayList<>();
                navController.navigate(R.id.action_shoppingCartFragment_to_memberOrderDetailFragment, bundle);
                return;
            }

            case R.id.btnCartToShopping: {
                navController.navigate(R.id.action_shoppingCartFragment_to_mallListFragment);
                return;
            }

            case R.id.imgvAddrMagic:{
                etCartAddress.setText("407台中市西屯區市政北七路177號");
            }
        }
    }

    //RecyclerView用
    private class ShoppingRecyclerAdapter extends RecyclerView.Adapter<ShoppingRecyclerAdapter.ViewHolder_CartList> {
        private List data;

        public ShoppingRecyclerAdapter(List data) {
            this.data = data;
        }

        @NonNull
        @Override
        public ViewHolder_CartList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
            ShoppingRecyclerAdapter.ViewHolder_CartList mallHolder = new ShoppingRecyclerAdapter.ViewHolder_CartList(view);
            return mallHolder;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder_CartList holder, int position) {
            final OrderItem orderItem = CART.get(position);

            String url = Util.URL + "Android/ItemServlet";

            imageTask = new ImageTask(url, ImageTask.FROM_ITEM, orderItem.getItem_id(), getContext().getResources().getDisplayMetrics().widthPixels, holder.imgViewCart);
            imageTask.execute();

            holder.tvCartCommodityTitle.setText(orderItem.getItem_name());
            holder.tvCartCommodityPrice.setText("$" + String.valueOf(orderItem.getItem_unit_price()));
            holder.tvCartCounter.setText(String.valueOf(orderItem.getItem_quantity()));

            //刪除
            holder.btnCartDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CART.remove(position);
                    navController.navigate(R.id.action_shoppingCartFragment_self);
                }
            });

            //+
            holder.btnCartCommodityAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int counter = Integer.parseInt(holder.tvCartCounter.getText().toString()) + 1;
                    if (counter > orderItem.getItem_stock()) {
                        counter = orderItem.getItem_stock();
                    }
                    holder.tvCartCounter.setText(String.valueOf(counter));
                    orderItem.setItem_quantity(counter);
                    tvShowTotalAmount.setText(getTotalPrice());
                }
            });

            //-
            holder.btnCartCommodityLess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int counter = Integer.parseInt(holder.tvCartCounter.getText().toString()) - 1;
                    if (counter < 1) {
                        counter = 1;
                    }
                    holder.tvCartCounter.setText(String.valueOf(counter));
                    orderItem.setItem_quantity(counter);
                    tvShowTotalAmount.setText(getTotalPrice());
                }
            });
        }

        public class ViewHolder_CartList extends RecyclerView.ViewHolder {
            private TextView tvCartCommodityTitle, tvCartCommodityPrice, tvCartCounter;
            private ImageView imgViewCart;
            private Button btnCartDelete, btnCartCommodityAdd, btnCartCommodityLess;

            public ViewHolder_CartList(@NonNull View itemView) {
                super(itemView);
                tvCartCommodityTitle = itemView.findViewById(R.id.tvCartCommodityTitle);
                tvCartCommodityPrice = itemView.findViewById(R.id.tvCartCommodityPrice);
                tvCartCounter = itemView.findViewById(R.id.tvCartCounter);
                imgViewCart = itemView.findViewById(R.id.imgViewCart);
                btnCartDelete = itemView.findViewById(R.id.btnCartDelete);
                btnCartCommodityAdd = itemView.findViewById(R.id.btnCartCommodityAdd);
                btnCartCommodityLess = itemView.findViewById(R.id.btnCartCommodityLess);
            }
        }
    }
}
