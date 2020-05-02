package com.example.da106g2_main.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.da106g2_main.R;
import com.example.da106g2_main.diary.Diary;
import com.example.da106g2_main.mall.Item;
import com.example.da106g2_main.mall.OrderDetail;
import com.example.da106g2_main.mall.Orders;
import com.example.da106g2_main.video.Live;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

//此為通用RecyclerViewAdapter，只要傳入想Layout的樣式及NavController即可
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List data;
    private int layoutType;
    private NavController navController;
    private ImageTask imageTask = null;
    private CommunicationTask communicationTask = null;

    //用於RecyclerView選擇
    public final static int LAYOUT_VIDEO_LIST = 0;
    public final static int LAYOUT_DIARY_LIST = 1;
    public final static int LAYOUT_MALL_LIST = 2;
    public final static int LAYOUT_ORDER_LIST = 3;
    public final static int LAYOUT_ORDER_DETAIL_LIST = 4;

    //設定圖片連線物件
    public void setImageTask(ImageTask imageTask) {
        this.imageTask = imageTask;
    }

    //設定連線物件
    public void setCommunicationTask(CommunicationTask communicationTask) {
        this.communicationTask = communicationTask;
    }

    public RecyclerViewAdapter(List data, int layoutType, NavController navController) {
        this.data = data;
        this.layoutType = layoutType;
        this.navController = navController;
    }

    public RecyclerViewAdapter(List data, int layoutType) {
        this.data = data;
        this.layoutType = layoutType;
    }

    @Override
    public int getItemViewType(int position) {
        return layoutType;
    }

    //選擇Holder_layout用
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (layoutType) {
            case LAYOUT_VIDEO_LIST: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
                RecyclerViewAdapter.ViewHolder_VideoList videoHolder = new RecyclerViewAdapter.ViewHolder_VideoList(view);
                return videoHolder;
            }

            case LAYOUT_DIARY_LIST: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diary, parent, false);
                RecyclerViewAdapter.Viewolder_DiaryList diaryHolder = new RecyclerViewAdapter.Viewolder_DiaryList(view);
                return diaryHolder;
            }

            case LAYOUT_MALL_LIST: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mall, parent, false);
                RecyclerViewAdapter.ViewHolder_MallList mallHolder = new RecyclerViewAdapter.ViewHolder_MallList(view);
                return mallHolder;
            }

            //訂單類共用Layout
            case LAYOUT_ORDER_LIST:
            case LAYOUT_ORDER_DETAIL_LIST: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
                RecyclerViewAdapter.ViewHolder_OrderList orderHolder = new RecyclerViewAdapter.ViewHolder_OrderList(view);
                return orderHolder;
            }
        }
        return null;
    }

    //各類LAYOUT內部任務
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {

            //直播清單任務
            case LAYOUT_VIDEO_LIST: {
                final Live live = (Live) data.get(position);

                String url = Util.URL + "Android/LiveServlet";
                ViewHolder_VideoList holderVideoList = (ViewHolder_VideoList) holder;

                imageTask = new ImageTask(url, ImageTask.FROM_LIVE, live.getLive_id(), 0, holderVideoList.ivLiveItem);
                imageTask.execute();

                holderVideoList.tvLiveItemTitle.setText(live.getTitle());
                holderVideoList.tvMessage.setText(live.getMember_id() + "·觀看次數：" + live.getWatched_num() + "·");
                holderVideoList.videoItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Bundle bundle = new Bundle();
                        Gson gson = new Gson();
                        bundle.putString("live", gson.toJson(live));

                        navController.navigate(R.id.action_videoListFragment_to_videoPlayerFragment, bundle);
                    }
                });
                return;
            }

            //日誌清單任務
            case LAYOUT_DIARY_LIST: {
                final Diary diary = (Diary) data.get(position);
//                String url = Util.URL + "Android/LiveServlet";
                Viewolder_DiaryList holderVideoList = (Viewolder_DiaryList) holder;

//                imageTask = new ImageTask(url, ImageTask.FROM_LIVE, live.getLive_id(), 0, holderVideoList.ivLiveItem);
//                imageTask.execute();

//                private CardView diaryItem;
//                private TextView tvDiaryItemTitle, tvDiaryDate, tvDiaryWrite;
//                private ImageView ivDiaryItem;

                holderVideoList.tvDiaryItemTitle.setText(diary.getDiary_title());
                holderVideoList.tvDiaryDate.setText(diary.getDiary_time().toString());
                holderVideoList.tvDiaryWrite.setText(diary.getDiary_write());
                holderVideoList.diaryItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        Gson gson = new Gson();
                        bundle.putString("diary", gson.toJson(diary));

                        navController.navigate(R.id.action_diaryListFragment_to_diaryFragment, bundle);
                    }
                });
                return;
            }

            //商城清單任務
            case LAYOUT_MALL_LIST: {
                final Item item = (Item) data.get(position);

                String url = Util.URL + "Android/ItemServlet";
                ViewHolder_MallList holderVideoList = (ViewHolder_MallList) holder;

                imageTask = new ImageTask(url, ImageTask.FROM_ITEM, item.getItem_id(), 0, holderVideoList.imgvMall);
                imageTask.execute();

                holderVideoList.tvMallName.setText(item.getItem_name());
                holderVideoList.tvCommodityPrice.setText("$" + String.valueOf(item.getItem_price()));
                holderVideoList.mallItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Bundle bundle = new Bundle();
                        Gson gson = new Gson();
                        bundle.putString("item", gson.toJson(item));

                        navController.navigate(R.id.action_mallListFragment_to_shoopingFragment, bundle);
                    }
                });
                return;
            }

            //訂單清單任務
            case LAYOUT_ORDER_LIST: {
                final Orders order = (Orders) data.get(position);

                ViewHolder_OrderList holderVideoList = (ViewHolder_OrderList) holder;

                holderVideoList.tvOrderCommodityTitle.setText(order.getOrder_id());
                holderVideoList.tvOrderCommodityPrice.setText("總價格為：$" + String.valueOf(order.getOrder_price()));
                holderVideoList.tvOrderCommodityQuantity.setText("");
                holderVideoList.tvOrderCommodityQuantity.setHeight(0);
                holderVideoList.tvOrderCommodityTime.setText("訂購日期為" + order.getOrder_time().toString());
                holderVideoList.oderItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Bundle bundle = new Bundle();
                        Gson gson = new Gson();
                        bundle.putString("order", gson.toJson(order));

                        navController.navigate(R.id.action_memberOrdersListFragment_to_memberOrderDetailFragment, bundle);
                    }
                });
                return;
            }

            //訂單明細清單任務
            case LAYOUT_ORDER_DETAIL_LIST: {
                final OrderDetail orderDetail = (OrderDetail) data.get(position);

                String url = Util.URL + "Android/ItemServlet";
                ViewHolder_OrderList holderVideoList = (ViewHolder_OrderList) holder;

                //抓取商品圖片
                imageTask = new ImageTask(url, ImageTask.FROM_ITEM, orderDetail.getItem_id(), 0, holderVideoList.imgViewOder);
                imageTask.execute();

                //抓取商品名稱
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getOneItemNoBLOB");
                jsonObject.addProperty("item_id", orderDetail.getItem_id());
                communicationTask = new CommunicationTask(url, jsonObject.toString());
                Item item = null;
                try {
                    String jsonIn = communicationTask.execute().get();
                    Type listType = new TypeToken<Item>() {
                    }.getType();
                    item = new Gson().fromJson(jsonIn, listType);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                holderVideoList.tvOrderCommodityTitle.setText(item.getItem_name());
                holderVideoList.tvOrderCommodityPrice.setText("$" + String.valueOf(orderDetail.getItem_unit_price()));
                holderVideoList.tvOrderCommodityQuantity.setText("購買數量： " + String.valueOf(orderDetail.getItem_quantity()));
                holderVideoList.tvOrderCommodityTime.setText("");
                holderVideoList.tvOrderCommodityTime.setHeight(0);
                return;
            }
        }
    }

    //取得位子
    @Override
    public int getItemCount() {
        return data.size();
    }

    //下列都是個別綁物件用
    //影片清單用
    class ViewHolder_VideoList extends RecyclerView.ViewHolder {
        private CardView videoItem;
        private ImageView ivLiveItem;
        private TextView tvLiveItemTitle, tvMessage;

        ViewHolder_VideoList(@NonNull View itemView) {
            super(itemView);
            videoItem = itemView.findViewById(R.id.videoItem);
            ivLiveItem = itemView.findViewById(R.id.ivLiveItem);
            tvLiveItemTitle = itemView.findViewById(R.id.tvLiveItemTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }

    //日誌清單用
    class Viewolder_DiaryList extends RecyclerView.ViewHolder {
        private CardView diaryItem;
        private TextView tvDiaryItemTitle, tvDiaryDate, tvDiaryWrite;
        private ImageView ivDiaryItem;

        public Viewolder_DiaryList(@NonNull View itemView) {
            super(itemView);
            diaryItem = itemView.findViewById(R.id.diaryItem);
            tvDiaryItemTitle = itemView.findViewById(R.id.tvDiaryItemTitle);
            tvDiaryDate = itemView.findViewById(R.id.tvDiaryItemDate);
            tvDiaryWrite = itemView.findViewById(R.id.tvDiaryItemWrite);
            ivDiaryItem = itemView.findViewById(R.id.ivDiaryItem);
        }
    }

    //商城清單用
    class ViewHolder_MallList extends RecyclerView.ViewHolder {
        private CardView mallItem;
        private TextView tvMallName, tvCommodityPrice;
        private ImageView imgvMall;

        public ViewHolder_MallList(@NonNull View itemView) {
            super(itemView);
            mallItem = itemView.findViewById(R.id.mallItem);
            tvMallName = itemView.findViewById(R.id.tvMallName);
            tvCommodityPrice = itemView.findViewById(R.id.tvCommodityPrice);
            imgvMall = itemView.findViewById(R.id.imgvMall);
        }
    }

    //訂單清單用
    class ViewHolder_OrderList extends RecyclerView.ViewHolder {
        private CardView oderItem;
        private TextView tvOrderCommodityTitle, tvOrderCommodityPrice, tvOrderCommodityQuantity, tvOrderCommodityTime;
        private ImageView imgViewOder;

        public ViewHolder_OrderList(@NonNull View itemView) {
            super(itemView);
            oderItem = itemView.findViewById(R.id.oderItem);
            tvOrderCommodityTitle = itemView.findViewById(R.id.tvOrderCommodityTitle);
            tvOrderCommodityPrice = itemView.findViewById(R.id.tvOrderCommodityPrice);
            tvOrderCommodityQuantity = itemView.findViewById(R.id.tvOrderCommodityQuantity);
            tvOrderCommodityTime = itemView.findViewById(R.id.tvOrderCommodityTime);
            imgViewOder = itemView.findViewById(R.id.imgViewOder);
        }
    }

}
