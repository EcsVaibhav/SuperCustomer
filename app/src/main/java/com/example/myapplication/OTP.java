package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.BussinessRegistration;
import com.example.myapplication.Business.BDashboard;
import com.example.myapplication.User.UserQrScan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class OTP extends AppCompatActivity {
    EditText num1, num2, num3, num4,num5,num6;
    Button verify;
    ProgressBar progressBar;
    String otp,Mobile;

    TextView resendBtn;

    private boolean resendEnabled = false;

    private int resendTime = 60;


    private int selectedETposition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


        findId();

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                verify.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                String code = num1.getText().toString();
                String code2 = num2.getText().toString();
                String code3 = num3.getText().toString();
                String code4 = num4.getText().toString();
                String code5 = num5.getText().toString();
                String code6 = num6.getText().toString();

                String fcode = code + code2 + code3 + code4 + code5 + code6;

                if (otp!= null)
                {
                    if(fcode.equals(otp)){
                        LoginValidation(Mobile);
                    }
                    else {
                        Toast.makeText(OTP.this, "Wrong Otp", Toast.LENGTH_SHORT).show();
                        verify.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        num1.setText("");
                        num2.setText("");
                        num3.setText("");
                        num4.setText("");
                        num5.setText("");
                        num6.setText("");
                        showKeyboard(num1);
                        selectedETposition =0;
                    }


                }
            }
        });

        num1.addTextChangedListener(textWatcher);
        num2.addTextChangedListener(textWatcher);
        num3.addTextChangedListener(textWatcher);
        num4.addTextChangedListener(textWatcher);
        num5.addTextChangedListener(textWatcher);
        num6.addTextChangedListener(textWatcher);

        showKeyboard(num1);

        startCounter();
        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resendEnabled){
                    SendOTP(Mobile);
                    startCounter();
                }
            }
        });

    }

    private void closeKeyboard()
    {

        View view = this.getCurrentFocus();

        if (view != null) {

            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }


    private void SendOTP(String mobile) {


        Random r  = new Random();
        String Sotp = String.valueOf(r.nextInt(900000) + 100000);

        String msg = "your otp is "+Sotp+" Info sms";
        StringRequest request = new StringRequest(Request.Method.GET, "http://146.88.24.105:8080/api/mt/SendSMS?user=piyushmore&password=12345&number="+mobile+"&text="+msg+"&senderid=INFMTN&channel=Trans&DCS=0&flashsms=0", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject o = new JSONObject(response);
                    String status = o.getString("ErrorMessage");

                    if (status.equals("Done")) {

                        Toast.makeText(OTP.this, "Otp Resend to your mobile number", Toast.LENGTH_SHORT).show();
                    }else {

                        Toast.makeText(OTP.this, "Something went wrong try later..", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(OTP.this, "Something went wrong try later..", Toast.LENGTH_SHORT).show();

                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OTP.this, "Technical error try after some time", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue queue = Volley.newRequestQueue(OTP.this);
        queue.add(request);







    }

    private void showKeyboard(EditText editText){

        editText.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText,InputMethodManager.SHOW_IMPLICIT);

    }

    private void startCounter(){
        resendEnabled = false;
        resendBtn.setTextColor(Color.parseColor("#99000000"));

        new CountDownTimer(resendTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resendBtn.setText("Resend Code ("+(millisUntilFinished/1000)+")");
            }

            @Override
            public void onFinish() {
                resendEnabled = true;
                resendBtn.setText("Resend Code");
                resendBtn.setTextColor(getResources().getColor(R.color.primaryTextColor));
            }
        }.start();
    }


    public final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.length()>0){

                if(selectedETposition == 0){

                    selectedETposition = 1;
                    showKeyboard(num2);

                }else if (selectedETposition == 1){
                    selectedETposition = 2;
                    showKeyboard(num3);

                }else if (selectedETposition == 2){
                    selectedETposition = 3;
                    showKeyboard(num4);

                }else if (selectedETposition == 3){
                    selectedETposition = 4;
                    showKeyboard(num5);

                }else if (selectedETposition == 4){
                    selectedETposition = 5;
                    showKeyboard(num6);

                }else if (selectedETposition == 5){
                    closeKeyboard();
                }

            }


        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_DEL){
            if (selectedETposition == 5){
                selectedETposition = 4;
                showKeyboard(num5);
            }else if (selectedETposition == 4){
                selectedETposition = 3;
                showKeyboard(num4);
            }else if (selectedETposition == 3){
                selectedETposition = 2;
                showKeyboard(num3);
            }else if (selectedETposition == 2){
                selectedETposition = 1;
                showKeyboard(num2);

            }else if (selectedETposition == 1){
                selectedETposition = 0;
                showKeyboard(num1);
            }
            return true;
        }else {
            return super.onKeyUp(keyCode, event);
        }


    }

    private void findId() {
        num1 = findViewById(R.id.txtNum1);
        num2 = findViewById(R.id.txtnum2);
        num3 = findViewById(R.id.txtnum3);
        num4 = findViewById(R.id.txtnum4);
        num5 = findViewById(R.id.txtnum5);
        num6 = findViewById(R.id.txtnum6);
        verify = findViewById(R.id.btnVerify);
        progressBar = findViewById(R.id.progressBar);
        resendBtn = findViewById(R.id.resendBtn);

        Intent intent = getIntent();
        Mobile = intent.getStringExtra("Mobile");
        otp = getIntent().getStringExtra("Otp");
    }

    private String checkBStatus(String B_Mob){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://tsm.ecssofttech.com/Library/GYMapi/getBdetail.php?mobileno="+B_Mob+"")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = Objects.requireNonNull(response.body()).string();
            System.out.println(responseString);

            JSONArray contacts = new JSONArray(responseString);

            for (int i = 0; i < contacts.length(); i++) {
                JSONObject o = contacts.getJSONObject(i);
                return o.getString("Status");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Deactivate";
    }

    private void ShowOkDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(OTP.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_for_deactivate, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogView.findViewById(R.id.Okaybtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private void LoginValidation(String Mobile) {

        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/checknoLogin.php?mobileno="+Mobile+"", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                String s1 = "Admin";
                String s2 = "Business";
                String s3 = "User";
                String s4 = "New";

                if (response.equalsIgnoreCase(s1)) {

                    SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("admin", Mobile);
                    editor.apply();
                    editor.commit();

                    Intent intent = new Intent(OTP.this, BussinessRegistration.class);
                    intent.putExtra("mobile",Mobile);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(OTP.this, "Admin, Welcome To SuperCustomer", Toast.LENGTH_SHORT).show();
                    verify.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
                else if(response.equalsIgnoreCase(s2)) {

                    if (checkBStatus(Mobile).equals("Deactivate")){

                        //Toast.makeText(OTP.this, "Your Account is Deactivated", Toast.LENGTH_SHORT).show();
                        ShowOkDialog();
                        verify.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);


                    }else if (checkBStatus(Mobile).equals("Activate")){

                        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("bussiness", Mobile);
                        editor.apply();
                        editor.commit();

                        Intent intent = new Intent(OTP.this, BDashboard.class);
                        intent.putExtra("Mobile",Mobile);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(OTP.this, "Business, Welcome To SuperCustomer", Toast.LENGTH_SHORT).show();

                        verify.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }else {
                        Toast.makeText(OTP.this, "Something went wrong try later..", Toast.LENGTH_SHORT).show();
                        verify.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }



                }
                else if(response.equalsIgnoreCase(s3) || response.equalsIgnoreCase(s4)) {
                    SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user", Mobile);
                    editor.apply();
                    editor.commit();


                    Intent intent = new Intent(OTP.this, UserQrScan.class);
                    intent.putExtra("Mobile",Mobile);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(OTP.this, "Welcome to SuperCustomer", Toast.LENGTH_SHORT).show();
                    verify.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
                else {
                    Toast.makeText(OTP.this, "something went wrong try later..", Toast.LENGTH_SHORT).show();
                    verify.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OTP.this, "Something went wrong try later..", Toast.LENGTH_SHORT).show();
                verify.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(OTP.this);
        requestQueue.add(request);

    }

}