package com.cloverinfosoft.fooddelivery.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener ,OnTaskCompleted{
    private Intent intent;
    private Context mContext;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String CURRENTFRAGMENT = "";
    private Fragment fragment;
    private PreferencesManager preferencesManager;
    private RelativeLayout rel_menu, rel_profile;
    public ImageView img_menu,img_profile,img_cart;
    public TextView tv_menu,tv_profile,tv_count;
    public RelativeLayout fragmentContainer;
    private RelativeLayout rel_cart;
    public static int quantity = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        preferencesManager = PreferencesManager.getInstance(this);
        mContext = DashboardActivity.this;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initUI();
        initListener();
        callSetupFragment(SCREENS.HOME, null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        callCartListingWS();
    }

    public void callCartListingWS() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        hashMap.put("token", preferencesManager.gettoken());
        new WebService(mContext, this, hashMap, "cartListing").execute(AppConstants.BASE_URL + AppConstants.CART_LISTING);
    }
    public void initUI() {
        rel_menu = (RelativeLayout) findViewById(R.id.rel_menu);
        rel_cart=(RelativeLayout)findViewById(R.id.rel_cart);
        rel_profile = (RelativeLayout) findViewById(R.id.rel_profile);
        img_menu=(ImageView)findViewById(R.id.img_menu);
        img_profile=(ImageView)findViewById(R.id.img_profile);
        tv_menu=(TextView)findViewById(R.id.tv_menu);
        tv_profile=(TextView)findViewById(R.id.tv_profile);
        tv_count=(TextView)findViewById(R.id.tv_count);
        tv_count.setVisibility(View.GONE);
        img_cart=(ImageView)findViewById(R.id.img_cart);
        img_cart.setVisibility(View.VISIBLE);
        fragmentContainer=(RelativeLayout)findViewById(R.id.fragmentContainer);

    }

    public void initListener() {
        rel_menu.setOnClickListener(this);
        rel_profile.setOnClickListener(this);
        rel_cart.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_cart:
                Intent intent=new Intent(mContext,AddToCartActivity.class);
                startActivity(intent);
                break;
            case R.id.rel_menu:
                tv_menu.setTextColor(getResources().getColor(R.color.colorGreen));
                img_menu.setImageResource(R.mipmap.icn_menu);
                tv_profile.setTextColor(getResources().getColor(R.color.colorLabelColor));
                img_profile.setImageResource(R.mipmap.icn_profile);
                callSetupFragment(SCREENS.HOME, null);
                break;
            case R.id.rel_profile:
                tv_menu.setTextColor(getResources().getColor(R.color.colorLabelColor));
                img_menu.setImageResource(R.mipmap.icn_inactive);
                tv_profile.setTextColor(getResources().getColor(R.color.colorGreen));
                img_profile.setImageResource(R.mipmap.profile_green);
                callSetupFragment(SCREENS.PROFILE, null);
                break;

        }
    }

    @Override
    public void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception {
        if (result.equals(""))
            return;
        switch (TAG) {
            case "cartListing":
                if (jsonObject.optString("status").equals("success")) {

                    JSONArray jsonArray = jsonObject.optJSONArray("items");
                    quantity=quantity+jsonArray.length();
                tv_count.setText(jsonArray.length()+"");
                    tv_count.setVisibility(View.VISIBLE);
                    img_cart.setVisibility(View.INVISIBLE);
                }
                else
                {
                    quantity=0;
                    tv_count.setVisibility(View.INVISIBLE);
                    img_cart.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    public enum SCREENS {
        HOME, PROFILE
    }

    public void callSetupFragment(SCREENS screens, Object data) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (screens) {
            case HOME:
                fragment = HomeFragment.newInstance("", "");
                CURRENTFRAGMENT = SCREENS.HOME.toString();
                break;
            case PROFILE:
                fragment = ProfileFragment.newInstance("", "");
                CURRENTFRAGMENT = SCREENS.PROFILE.toString();
                break;

        }
        fragmentTransaction.replace(R.id.fragmentContainer, fragment, CURRENTFRAGMENT);
        fragmentTransaction.commit();
    }

}
