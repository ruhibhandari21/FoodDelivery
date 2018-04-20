package com.cloverinfosoft.fooddelivery.initials;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.Formatter;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloverinfosoft.fooddelivery.R;
import com.cloverinfosoft.fooddelivery.Services.AppConstants;
import com.cloverinfosoft.fooddelivery.Services.OnTaskCompleted;
import com.cloverinfosoft.fooddelivery.Services.WebService;
import com.cloverinfosoft.fooddelivery.dashboard.DashboardActivity;
import com.cloverinfosoft.fooddelivery.utils.PreferencesManager;
import com.cloverinfosoft.fooddelivery.utils.UnCaughtException;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,OnTaskCompleted {
    private EditText edt_emailid, edt_password;
    private Button btn_login;
    private TextView tv_forgotpass;
    private Intent intent;
    private Context mContext;
    private TextView tv_password_error,tv_emailid_error;
    private ImageView img_hide;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesManager=PreferencesManager.getInstance(this);
        if(!preferencesManager.getFirstname().equals(""))
        {
            Intent intent=new Intent(this,DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(
                this));
        setContentView(R.layout.activity_login);

//        preferencesManager.settoken("83597d10be49de34b28c637ffcbcea48");
        mContext=LoginActivity.this;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initUI();
        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        preferencesManager=PreferencesManager.getInstance(this);
        preferencesManager.setdeviceIP(ip);

        callCreateCookiesWS();
    }

    public void initUI() {
        edt_emailid = (EditText) findViewById(R.id.edt_emailid);
        edt_password = (EditText) findViewById(R.id.edt_password);
        tv_password_error=(TextView)findViewById(R.id.tv_password_error);
        tv_emailid_error=(TextView)findViewById(R.id.tv_emailid_error);
        tv_emailid_error.setVisibility(View.GONE);
        tv_password_error.setVisibility(View.GONE);
//        edt_password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_forgotpass = (TextView) findViewById(R.id.tv_forgotpass);
        img_hide=(ImageView)findViewById(R.id.img_hide);
        img_hide.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch ( event.getAction() ) {

                    case MotionEvent.ACTION_UP:
                        edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                        edt_password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                        img_hide.setImageResource(R.mipmap.password_hide);

                        break;

                    case MotionEvent.ACTION_DOWN:
                        edt_password.setInputType(InputType.TYPE_CLASS_TEXT);
                        img_hide.setImageResource(R.mipmap.password_visible);

                        break;

                }
                return true;
            }
        });
    }

    public void initListener() {
        btn_login.setOnClickListener(this);
        tv_forgotpass.setOnClickListener(this);
    }

    private boolean isValidEmaillId(String email){

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
            case R.id.btn_login:
                if(edt_emailid.getText().toString().equals("")&&edt_password.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
                    tv_emailid_error.setVisibility(View.GONE);
                    tv_emailid_error.setText("");
                    tv_password_error.setVisibility(View.VISIBLE);
                    tv_password_error.setText("Invalid EmailId and Password");
                }
                else if(!edt_emailid.getText().toString().equals("")&&!isValidEmaillId(edt_emailid.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
                    tv_emailid_error.setVisibility(View.VISIBLE);
                    tv_emailid_error.setText("Invalid Email Address");
                    tv_password_error.setVisibility(View.GONE);
                }
                else if(!edt_emailid.getText().toString().equals("") && edt_password.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please enter the password", Toast.LENGTH_SHORT).show();
                    tv_emailid_error.setVisibility(View.GONE);
                    tv_emailid_error.setText("");
                    tv_password_error.setVisibility(View.VISIBLE);
                    tv_password_error.setText("Please enter the password");
                }
                else {
                    tv_emailid_error.setVisibility(View.GONE);
                    tv_password_error.setVisibility(View.GONE);
                    callLoginWS();

                }


                break;
            case R.id.tv_forgotpass:
                intent=new Intent(mContext,ForgotPasswordActivity.class);
                startActivity(intent);
                break;
        }
    }



    public void callLoginWS()
    {
        HashMap hashMap=new HashMap();
        hashMap.put("store-code",AppConstants.STORE_CODE);
        hashMap.put("authKey",AppConstants.AUTH_KEY);
        hashMap.put("deviceIp",preferencesManager.getdeviceIP());
        hashMap.put("email",edt_emailid.getText().toString());
        hashMap.put("password",edt_password.getText().toString());
        hashMap.put("token",preferencesManager.gettoken());//AppConstants.TOKEN);
        new WebService(this,this,hashMap,"LoginWS").execute(AppConstants.BASE_URL+AppConstants.LOGIN);
    }

    public void callCreateCookiesWS()
    {
        HashMap hashMap=new HashMap();
        hashMap.put("store-code",AppConstants.STORE_CODE);
        hashMap.put("authKey",AppConstants.AUTH_KEY);
        hashMap.put("deviceIp",preferencesManager.getdeviceIP());
        hashMap.put("device","android");
        new WebService(this,this,hashMap,"CreateCookieWS").execute(AppConstants.BASE_URL+AppConstants.CREATE_COOKIE);
    }


    @Override
    public void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception {

        if(result.equals(""))
            return;


        switch (TAG)
        {
            case "LoginWS":
                if(jsonObject.optString("status").equals("success"))
                {
                    Toast.makeText(mContext, jsonObject.optString("msg").toString(), Toast.LENGTH_SHORT).show();
                    preferencesManager.setUserId(jsonObject.optString("id"));
                    preferencesManager.setFirstname(jsonObject.optString("firstName"));
                    preferencesManager.setLastname(jsonObject.optString("lastName"));
                    preferencesManager.setemail(jsonObject.optString("email"));
                    preferencesManager.setphone(jsonObject.optString("phone"));
                    intent=new Intent(mContext,DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    tv_password_error.setVisibility(View.VISIBLE);
                    tv_password_error.setText(jsonObject.optString("msg").toString());
                    Toast.makeText(mContext, jsonObject.optString("msg").toString(), Toast.LENGTH_SHORT).show();
                }

                break;

            case "CreateCookieWS":
                if(jsonObject.optString("status").equals("success"))
                {
                    preferencesManager.settoken(jsonObject.optString("token"));
                }
                else
                {
                    Toast.makeText(mContext, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }


    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }
}
