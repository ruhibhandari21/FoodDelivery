package com.cloverinfosoft.fooddelivery.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloverinfosoft.fooddelivery.R;
import com.cloverinfosoft.fooddelivery.Services.AppConstants;
import com.cloverinfosoft.fooddelivery.Services.OnTaskCompleted;
import com.cloverinfosoft.fooddelivery.Services.WebService;
import com.cloverinfosoft.fooddelivery.initials.LoginActivity;
import com.cloverinfosoft.fooddelivery.utils.PreferencesManager;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by admin on 4/10/2018.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener ,OnTaskCompleted{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private RelativeLayout rel_logout,rel_my_orders;
    private Intent intent;
    private Context mContext;
    private ImageView img_profile;
    private TextView tv_name,tv_employee_id,tv_loyalty_points,tv_my_orders,tv_logout;
    private PreferencesManager preferencesManager;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        preferencesManager=PreferencesManager.getInstance(getActivity());
        mContext = getActivity();
        initUI();
        initListener();
        return view;
    }


    public void initUI() {
        tv_name=(TextView)view.findViewById(R.id.tv_name);
        tv_employee_id=(TextView)view.findViewById(R.id.tv_employee_id);
        tv_loyalty_points=(TextView)view.findViewById(R.id.tv_loyalty_points);
        tv_my_orders=(TextView)view.findViewById(R.id.tv_my_orders);
        tv_logout=(TextView)view.findViewById(R.id.tv_logout);
        rel_logout=(RelativeLayout)view.findViewById(R.id.rel_logout);
        rel_my_orders=(RelativeLayout)view.findViewById(R.id.rel_my_orders);
        tv_name.setText(preferencesManager.getFirstname()+" "+preferencesManager.getLastname());
    }

    public void initListener() {
        tv_my_orders.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        rel_my_orders.setOnClickListener(this);
        rel_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rel_logout:
            case R.id.tv_logout:
                callLogoutWS();

                break;
            case R.id.rel_my_orders:
            case R.id.tv_my_orders:
                Intent i=new Intent(mContext,MyOrderActiviy.class);
                startActivity(i);
                break;


        }
    }

    public void callLogoutWS()
    {
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        hashMap.put("token", preferencesManager.gettoken());
        new WebService(mContext, this, hashMap, "logout").execute(AppConstants.BASE_URL + AppConstants.LOGOUT);

    }

    @Override
    public void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception {
        if(result.equals(""))
        {
            return;
        }
        switch(TAG)
        {
            case "logout":
                if(jsonObject.optString("status").equals("success"))
                {
                    preferencesManager.clearPrefrences();
                    Intent intent=new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    ((DashboardActivity)mContext).finish();
                }
                else
                {
                    Toast.makeText(mContext, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
