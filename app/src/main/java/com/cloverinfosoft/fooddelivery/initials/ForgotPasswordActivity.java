package com.cloverinfosoft.fooddelivery.initials;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloverinfosoft.fooddelivery.R;
import com.cloverinfosoft.fooddelivery.Services.AppConstants;
import com.cloverinfosoft.fooddelivery.Services.OnTaskCompleted;
import com.cloverinfosoft.fooddelivery.Services.WebService;
import com.cloverinfosoft.fooddelivery.utils.PreferencesManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, OnTaskCompleted {
    private EditText edt_emailid;
    private Button btn_send;
    private Intent intent;
    private Context mContext;
    private TextView tv_validation;
    private PreferencesManager preferencesManager;
    private TextView tv_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        preferencesManager=PreferencesManager.getInstance(this);
        mContext = ForgotPasswordActivity.this;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initUI();
        initListener();
    }

    public void initUI() {
        edt_emailid = (EditText) findViewById(R.id.edt_emailid);
        tv_link = (TextView) findViewById(R.id.tv_link);
        tv_link.setVisibility(View.INVISIBLE);
        btn_send = (Button) findViewById(R.id.btn_send);
        tv_validation = (TextView) findViewById(R.id.tv_validation);
        tv_validation.setVisibility(View.INVISIBLE);
        tv_validation.setText("");
    }

    public void initListener() {
        btn_send.setOnClickListener(this);
    }

    private boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                if (edt_emailid.getText().toString().equals("")) {
                    tv_validation.setVisibility(View.VISIBLE);
                    tv_validation.setText("Please enter email id");
                } else if (!edt_emailid.getText().toString().equals("") && !isValidEmaillId(edt_emailid.getText().toString().trim())) {
                    tv_validation.setVisibility(View.VISIBLE);
                    tv_validation.setText("Please enter valid email id");
                } else {
                    tv_validation.setVisibility(View.INVISIBLE);
                    tv_validation.setText("");
                    callResetPasswordWS();

                }
                break;
        }
    }

    public void callResetPasswordWS() {
        HashMap hashMap = new HashMap();
        hashMap.put("store-code", AppConstants.STORE_CODE);
        hashMap.put("authKey", AppConstants.AUTH_KEY);
        hashMap.put("deviceIp", preferencesManager.getdeviceIP());
        hashMap.put("email", edt_emailid.getText().toString());
        new WebService(this, this, hashMap, "ResetWS").execute(AppConstants.BASE_URL + AppConstants.RESET);
    }


    @Override
    public void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception {
        if (result.equals(""))
            return;


        switch (TAG) {
            case "ResetWS":
                if (jsonObject.optString("status").equals("success")) {
                    Toast.makeText(mContext, jsonObject.optString("msg").toString(), Toast.LENGTH_SHORT).show();
                    tv_link.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(mContext, jsonObject.optString("msg").toString(), Toast.LENGTH_SHORT).show();
                    tv_link.setVisibility(View.INVISIBLE);
                }

                break;
        }
    }
}
