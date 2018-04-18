package com.cloverinfosoft.fooddelivery.dashboard;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloverinfosoft.fooddelivery.R;
import com.cloverinfosoft.fooddelivery.Services.AppConstants;
import com.cloverinfosoft.fooddelivery.Services.OnTaskCompleted;
import com.cloverinfosoft.fooddelivery.Services.WebService;
import com.cloverinfosoft.fooddelivery.model.MyOrderModel;
import com.cloverinfosoft.fooddelivery.utils.PreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class MyOrderActiviy extends AppCompatActivity implements View.OnClickListener, OnTaskCompleted {

    private TextView tv_noitem, tv_back;
    private ImageView img_back;
    private PreferencesManager preferencesManager;
    private Context mContext;
    private RecyclerView recycler_view;
    private HashMap<Integer, MyOrderModel> hashMap = new HashMap<>();
    private MyOrdersAdapter myOrdersAdapter;
    int rate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_activiy);
        preferencesManager = PreferencesManager.getInstance(this);
        mContext = this;
        initUI();
        initListener();
        callMyOrdersWS();
    }

    HashMap<Integer, MyOrderModel> hashMap2;

    public void HashmapValue(HashMap<Integer, MyOrderModel> hashMap1) {
        if (hashMap2 != null && hashMap2.size() != 0)
            hashMap2.clear();
        hashMap2 = new HashMap<>();
        hashMap2.putAll(hashMap1);
        hashMap.clear();
        hashMap.putAll(hashMap2);
//        myOrdersAdapter=new MyOrdersAdapter(this,hashMap);
//        recycler_view.setAdapter(myOrdersAdapter);
        myOrdersAdapter.notifyDataSetChanged();
    }


    public void initUI() {
        tv_noitem = (TextView) findViewById(R.id.tv_noitem);
        tv_back = (TextView) findViewById(R.id.tv_back);
        img_back = (ImageView) findViewById(R.id.img_back);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        tv_noitem.setVisibility(View.INVISIBLE);
    }


    public void callMyOrdersWS() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        hashMap.put("token", preferencesManager.gettoken());
        new WebService(mContext, this, hashMap, "getOrders").execute(AppConstants.BASE_URL + AppConstants.GET_ORDERS);

    }

    int position = 0;

    public void callRatingWS(String orderItemCode, String rating, int position) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        hashMap.put("token", preferencesManager.gettoken());
        hashMap.put("orderItemCode", orderItemCode);
        hashMap.put("rating", rating);
        rate = Integer.parseInt(rating);
        this.position = position;
        new WebService(mContext, this, hashMap, "rating").execute(AppConstants.BASE_URL + AppConstants.RATING);

    }


    public void initListener() {
        tv_back.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
            case R.id.img_back:
                finish();
                break;
        }
    }

    @Override
    public void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception {

        if (result.equals("")) {

            if(TAG.equals("rating"))
            {
                switch (rate)
                {
                    case 0:
                        hashMap.get(position).setFlag1(false);
                        hashMap.get(position).setFlag2(false);
                        hashMap.get(position).setFlag3(false);
                        hashMap.get(position).setFlag4(false);
                        hashMap.get(position).setFlag5(false);
                        hashMap.get(position).setRating("0");
                        HashmapValue(hashMap);
                        break;
                    case 1:
                        hashMap.get(position).setRating("0");
                        hashMap.get(position).setFlag1(false);
                        hashMap.get(position).setFlag2(false);
                        hashMap.get(position).setFlag3(false);
                        hashMap.get(position).setFlag4(false);
                        hashMap.get(position).setFlag5(false);
                        HashmapValue(hashMap);
                        break;
                    case 2:
                        hashMap.get(position).setRating("1");
                        hashMap.get(position).setFlag1(true);
                        hashMap.get(position).setFlag2(false);
                        hashMap.get(position).setFlag3(false);
                        hashMap.get(position).setFlag4(false);
                        hashMap.get(position).setFlag5(false);
                        HashmapValue(hashMap);
                        break;
                    case 3:
                        hashMap.get(position).setRating("2");
                        hashMap.get(position).setFlag1(true);
                        hashMap.get(position).setFlag2(true);
                        hashMap.get(position).setFlag3(false);
                        hashMap.get(position).setFlag4(false);
                        hashMap.get(position).setFlag5(false);
                        HashmapValue(hashMap);
                        break;
                    case 4:
                        hashMap.get(position).setRating("3");
                        hashMap.get(position).setFlag1(true);
                        hashMap.get(position).setFlag2(true);
                        hashMap.get(position).setFlag3(true);
                        hashMap.get(position).setFlag4(false);
                        hashMap.get(position).setFlag5(false);
                        HashmapValue(hashMap);
                        break;
                    case 5:
                        hashMap.get(position).setRating("4");
                        hashMap.get(position).setFlag1(true);
                        hashMap.get(position).setFlag2(true);
                        hashMap.get(position).setFlag3(true);
                        hashMap.get(position).setFlag4(true);
                        hashMap.get(position).setFlag5(false);
                        HashmapValue(hashMap);
                        break;
                }
            }

            return;
        }

        switch (TAG) {
            case "getOrders":
                if (jsonObject.optString("status").equals("success")) {
                    JSONArray jsonArrayOrders = jsonObject.optJSONArray("orders");
                    int k = 0;
                    for (int j = 0; j < jsonArrayOrders.length(); j++) {
                        JSONObject jsonObject2 = jsonArrayOrders.optJSONObject(j);
                        JSONArray jsonArray = jsonObject2.optJSONArray("orderItems");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            MyOrderModel myOrderModel = new MyOrderModel();
                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                            myOrderModel.setCode(jsonObject1.optString("code"));
                            myOrderModel.setImage(jsonObject1.optString("image"));
                            myOrderModel.setProductName(jsonObject1.optString("productName"));
                            myOrderModel.setQuantity(jsonObject1.optString("quantity"));
                            myOrderModel.setRated(jsonObject1.optString("rated"));
                            myOrderModel.setSellingPrice(jsonObject1.optString("sellingPrice"));
                            myOrderModel.setOrderedDate(jsonObject2.optString("orderedDate"));
                            myOrderModel.setOrderNo(jsonObject2.optString("orderNo"));
                            myOrderModel.setStatus(jsonObject2.optString("status"));
                            myOrderModel.setSubTotal(jsonObject2.optString("subTotal"));
                            myOrderModel.setTotal(jsonObject2.optString("total"));
                            myOrderModel.setTax(jsonObject2.optString("tax"));
                            hashMap.put(k, myOrderModel);
                            k++;
                        }

                    }


                    if (myOrdersAdapter != null) {
                        myOrdersAdapter.notifyDataSetChanged();
                    } else {
                        myOrdersAdapter = new MyOrdersAdapter(MyOrderActiviy.this, hashMap);
                        recycler_view.setAdapter(myOrdersAdapter);
                    }

                } else {
                    Toast.makeText(mContext, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                }
                break;
            case "rating":
                if (jsonObject.optString("status").equals("success")) {
                    switch (rate) {
                        case 0:
                            hashMap.get(position).setFlag1(false);
                            hashMap.get(position).setFlag2(false);
                            hashMap.get(position).setFlag3(false);
                            hashMap.get(position).setFlag4(false);
                            hashMap.get(position).setFlag5(false);
                            HashmapValue(hashMap);
                            break;
                        case 1:
                            hashMap.get(position).setFlag1(true);
                            hashMap.get(position).setFlag2(false);
                            hashMap.get(position).setFlag3(false);
                            hashMap.get(position).setFlag4(false);
                            hashMap.get(position).setFlag5(false);
                            HashmapValue(hashMap);
                            break;
                        case 2:
                            hashMap.get(position).setFlag1(true);
                            hashMap.get(position).setFlag2(true);
                            hashMap.get(position).setFlag3(false);
                            hashMap.get(position).setFlag4(false);
                            hashMap.get(position).setFlag5(false);
                            HashmapValue(hashMap);
                            break;
                        case 3:
                            hashMap.get(position).setFlag1(true);
                            hashMap.get(position).setFlag2(true);
                            hashMap.get(position).setFlag3(true);
                            hashMap.get(position).setFlag4(false);
                            hashMap.get(position).setFlag5(false);
                            HashmapValue(hashMap);
                            break;
                        case 4:
                            hashMap.get(position).setFlag1(true);
                            hashMap.get(position).setFlag2(true);
                            hashMap.get(position).setFlag3(true);
                            hashMap.get(position).setFlag4(true);
                            hashMap.get(position).setFlag5(false);
                            HashmapValue(hashMap);
                            break;
                        case 5:
                            hashMap.get(position).setFlag1(true);
                            hashMap.get(position).setFlag2(true);
                            hashMap.get(position).setFlag3(true);
                            hashMap.get(position).setFlag4(true);
                            hashMap.get(position).setFlag5(true);
                            HashmapValue(hashMap);
                            break;
                    }
                } else {
                    Toast.makeText(mContext, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
