package com.cloverinfosoft.fooddelivery.dashboard;

import android.content.Context;
import android.content.Intent;
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
import com.cloverinfosoft.fooddelivery.model.CartItemsModel;
import com.cloverinfosoft.fooddelivery.utils.PreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class AddToCartActivity extends AppCompatActivity implements OnTaskCompleted, View.OnClickListener {
    private RecyclerView recycler_view;
    private AddToCartAdapter addToCartAdapter;
    private PreferencesManager preferencesManager;
    private Context mContext;
    private TextView tv_noitem, tv_back;
    private ImageView img_back, img_pay_online, img_cod;
    private RelativeLayout add_to_cart, rel_pay_online, rel_cod, rel_checkout;
    private TextView tv_total, tv_subtotal, tv_tax, tv_chk_total, tv_checkout_count;
    private HashMap<Integer, CartItemsModel> hashMap = new HashMap<>();
    private String paymentMethod = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);
        preferencesManager = PreferencesManager.getInstance(this);
        mContext = this;
        paymentMethod = "online";
        initUI();
        initListener();
        callCartListingWS();
    }

    public void initUI() {
        tv_noitem = (TextView) findViewById(R.id.tv_noitem);
        img_pay_online = (ImageView) findViewById(R.id.img_pay_online);
        img_cod = (ImageView) findViewById(R.id.img_cod);
        rel_pay_online = (RelativeLayout) findViewById(R.id.rel_pay_online);
        rel_cod = (RelativeLayout) findViewById(R.id.rel_cod);
        rel_checkout = (RelativeLayout) findViewById(R.id.rel_checkout);
        add_to_cart = (RelativeLayout) findViewById(R.id.activity_add_to_cart);
        tv_back = (TextView) findViewById(R.id.tv_back);
        img_back = (ImageView) findViewById(R.id.img_back);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        tv_noitem.setVisibility(View.GONE);
        add_to_cart.setVisibility(View.GONE);

        tv_total = (TextView) findViewById(R.id.tv_total);
        tv_subtotal = (TextView) findViewById(R.id.tv_subtotal);
        tv_tax = (TextView) findViewById(R.id.tv_tax);
        tv_chk_total = (TextView) findViewById(R.id.tv_chk_total);
        tv_checkout_count = (TextView) findViewById(R.id.tv_checkout_count);
    }


    public void callCartListingWS() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        hashMap.put("token", preferencesManager.gettoken());
        new WebService(mContext, this, hashMap, "cartListing").execute(AppConstants.BASE_URL + AppConstants.CART_LISTING);
    }

    public void callCartUpdateQuantityWS(String cartItemCode, String qty_action) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        hashMap.put("token", preferencesManager.gettoken());
        hashMap.put("cartItemCode", cartItemCode);
        hashMap.put("qty_action", qty_action);
        hashMap.put("qty", "1");
        new WebService(mContext, this, hashMap, "updateQuantity").execute(AppConstants.BASE_URL + AppConstants.UPDATE_CART_QUANTITY);
    }


    public void callRemoveItemWS(String cartItemCode) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        hashMap.put("token", preferencesManager.gettoken());
        hashMap.put("cartItemCode", cartItemCode);

        new WebService(mContext, this, hashMap, "removeItem").execute(AppConstants.BASE_URL + AppConstants.REMOVE_CART_ITEM);
    }

    public void callReadyToCheckout() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        hashMap.put("token", preferencesManager.gettoken());
        new WebService(mContext, this, hashMap, "readyToCheckout").execute(AppConstants.BASE_URL + AppConstants.READY_TO_CHECKOUT);
    }


    public void callMakeCOD(String dummy_order_code, String paymentMethod) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        hashMap.put("token", preferencesManager.gettoken());
        hashMap.put("dummy_order_code", dummy_order_code);
        hashMap.put("paymentMethod", paymentMethod);

        new WebService(mContext, this, hashMap, "makecod").execute(AppConstants.BASE_URL + AppConstants.MAKE_COD);
    }

    public void callMakeOrderOnlineBeforePage(String dummy_order_code) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        hashMap.put("token", preferencesManager.gettoken());
        hashMap.put("dummy_order_code", dummy_order_code);
        hashMap.put("paymentMethod", 5 + "");
        new WebService(mContext, this, hashMap, "makeDummyOrderReadyForOnlinePayment").execute(AppConstants.BASE_URL + AppConstants.makeDummyOrderReadyForOnlinePayment);
    }


    public void callupdateDummyOrderBytxnid(String txnid) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        hashMap.put("token", preferencesManager.gettoken());
        hashMap.put("txnid", txnid);
        hashMap.put("paid", 1 + "");
        hashMap.put("paymentResponse", "");
        new WebService(mContext, this, hashMap, "updateDummyOrderBytxnid").execute(AppConstants.BASE_URL + AppConstants.updateDummyOrderBytxnid);
    }

    public void calldummyOrdertoLiveOrder(String dummy_order_code) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        hashMap.put("token", preferencesManager.gettoken());
        hashMap.put("dummy_order_code", dummy_order_code);
        new WebService(mContext, this, hashMap, "dummyOrdertoLiveOrder").execute(AppConstants.BASE_URL + AppConstants.dummyOrdertoLiveOrder);
    }

    public void initListener() {
        tv_back.setOnClickListener(this);
        img_back.setOnClickListener(this);
        rel_pay_online.setOnClickListener(this);
        rel_cod.setOnClickListener(this);
        rel_checkout.setOnClickListener(this);
    }

    String ordercode = "";

    @Override
    public void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception {
        if (result.equals(""))
            return;
        switch (TAG) {
            case "removeItem":
                if (jsonObject.optString("status").equals("success")) {

                    Toast.makeText(mContext, "Item removed successfully", Toast.LENGTH_SHORT).show();
                    callCartListingWS();
                } else {
                    Toast.makeText(mContext, "Error while removing item", Toast.LENGTH_SHORT).show();
                }
                break;
            case "cartListing":
                hashMap.clear();
                if (jsonObject.optString("status").equals("success")) {
                    int quantity = 0;
                    JSONArray jsonArray = jsonObject.optJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                        CartItemsModel cartItemsModel = new CartItemsModel();
                        cartItemsModel.setCode(jsonObject1.optString("code"));
                        cartItemsModel.setName(jsonObject1.optString("name"));
                        cartItemsModel.setOriginalPrice(jsonObject1.optString("originalPrice"));
                        cartItemsModel.setPrice_included_tax(jsonObject1.optString("price_included_tax"));
                        cartItemsModel.setProduct_type(jsonObject1.optString("product_type"));
                        cartItemsModel.setQuantity(jsonObject1.optString("quantity"));
                        quantity = quantity + Integer.parseInt(jsonObject1.optString("quantity"));
                        cartItemsModel.setSlug(jsonObject1.optString("slug"));
                        cartItemsModel.setSellingPrice(jsonObject1.optString("sellingPrice"));
                        cartItemsModel.setTax_rate(jsonObject1.optString("tax_rate"));
                        hashMap.put(i, cartItemsModel);

                    }

                    tv_total.setText("₹" + jsonObject.optJSONObject("cart_info").optString("total"));
                    tv_subtotal.setText("₹" + jsonObject.optJSONObject("cart_info").optString("subTotal"));
                    tv_tax.setText("₹" + jsonObject.optJSONObject("cart_info").optString("tax"));
                    tv_checkout_count.setText("Checkout (" + quantity + ")");

                    tv_chk_total.setText("₹" + String.format("%.2f", Double.valueOf(jsonObject.optJSONObject("cart_info").optString("total"))));
                    tv_noitem.setVisibility(View.GONE);
                    add_to_cart.setVisibility(View.VISIBLE);
                    addToCartAdapter = new AddToCartAdapter(this, hashMap);
                    recycler_view.setAdapter(addToCartAdapter);
                } else {
                    tv_noitem.setVisibility(View.VISIBLE);
                    add_to_cart.setVisibility(View.GONE);
                    Toast.makeText(mContext, "No items added in cart", Toast.LENGTH_SHORT).show();
                }
                break;

            case "updateQuantity":
                if (jsonObject.optString("status").equals("success")) {
                    callCartListingWS();
                } else {
                    Toast.makeText(mContext, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                }
                break;

            case "readyToCheckout":
                if (jsonObject.optString("status").equals("success")) {
                    if (paymentMethod.equals("cod")) {
                        callMakeCOD(jsonObject.optString("dummy_order_code"), paymentMethod);
                    } else {
                        ordercode = jsonObject.optString("dummy_order_code");
                        callMakeOrderOnlineBeforePage(jsonObject.optString("dummy_order_code"));
                    }

                } else {
                    Toast.makeText(mContext, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                }
                break;

            case "makeDummyOrderReadyForOnlinePayment":
                if (jsonObject.optString("status").equals("success")) {
                    callupdateDummyOrderBytxnid(jsonObject.optString("paymentId"));

                } else {
                    Toast.makeText(mContext, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                }
                break;
            case "updateDummyOrderBytxnid":
                if (jsonObject.optString("status").equals("success")) {

                    calldummyOrdertoLiveOrder(ordercode);
                } else {
                    Toast.makeText(mContext, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                }
                break;

            case "dummyOrdertoLiveOrder":
                if (jsonObject.optString("status").equals("success")) {

                    Intent in = new Intent(mContext, MyOrderActiviy.class);
                    startActivity(in);
                    finish();
                } else {
                    Toast.makeText(mContext, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                }
                break;
            case "makecod":
                if (jsonObject.optString("status").equals("success")) {
                    Toast.makeText(mContext, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(mContext, MyOrderActiviy.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(mContext, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
            case R.id.img_back:
                finish();
                break;

            case R.id.rel_pay_online:
                img_pay_online.setImageResource(R.mipmap.radio_selected);
                img_cod.setImageResource(R.mipmap.radio_deselect);
                paymentMethod = "online";
                break;

            case R.id.rel_cod:
                paymentMethod = "cod";
                img_pay_online.setImageResource(R.mipmap.radio_deselect);
                img_cod.setImageResource(R.mipmap.radio_selected);

                break;
            case R.id.rel_checkout:
//                if (paymentMethod.equals("cod"))
                    callReadyToCheckout();
//                else
//                    Toast.makeText(mContext, "Online Payment Under Implementation", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
