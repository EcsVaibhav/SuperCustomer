package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.BussinessRegistration;
import com.example.myapplication.Business.BDashboard;
import com.example.myapplication.User.UserQrScan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    EditText mobile;
    Button button;
    ProgressBar progressBar;

    TextView PolicyTV;

    public  static  String PREFS_NAME="MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mobile = findViewById(R.id.mobile);
        button = findViewById(R.id.b);
        progressBar = findViewById(R.id.progressBar);
        PolicyTV = findViewById(R.id.PolicyTV);
        designPolicyText();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                String mobileNumber = mobile.getText().toString().trim();

                if (mobileNumber.isEmpty()) {
                    Toast.makeText(Login.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    button.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
                else if (mobileNumber.length()!=10) {
                    Toast.makeText(Login.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                    button.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                } else {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            closeKeyboard();
                            LoginValidation(mobileNumber);
                            /*button.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);*/
                        }
                    },1000);

                    /*PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + mobileNumber, 60, TimeUnit.SECONDS, Login.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    button.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    button.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    Intent intent = new Intent(Login.this,OTP.class);
                                    intent.putExtra("Mobile",mobileNumber);
                                    intent.putExtra("Otp",s);
                                    startActivity(intent);
                                    finish();
                                    button.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);

                                }
                            });*/

                }
            }

        });

        PolicyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Privacy_policy.class));
            }
        });
    }
    private void designPolicyText(){

        String o1 = PolicyTV.getText().toString();

        StyleSpan sp1 = new StyleSpan(Typeface.ITALIC);
        SpannableString ss1 = new SpannableString(o1);
        UnderlineSpan underlineSpan = new UnderlineSpan();

        ss1.setSpan(sp1, 27, 41, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss1.setSpan(underlineSpan, 27, 41, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        PolicyTV.setText(ss1);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

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
                mobile.setText("");
            }
        });
        alertDialog.show();
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

                    Intent intent = new Intent(Login.this, BussinessRegistration.class);
                    intent.putExtra("mobile",Mobile);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(Login.this, "Admin, Welcome To SuperCustomer", Toast.LENGTH_SHORT).show();
                    button.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
                else if(response.equalsIgnoreCase(s2)) {

                    if (checkBStatus(Mobile).equals("Deactivate")){

                        //Toast.makeText(Login.this, "Your Account is Deactivated", Toast.LENGTH_SHORT).show();
                        ShowOkDialog();
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);


                    }else if (checkBStatus(Mobile).equals("Activate")){

                        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("bussiness", Mobile);
                        editor.apply();
                        editor.commit();

                        Intent intent = new Intent(Login.this, BDashboard.class);
                        intent.putExtra("Mobile",Mobile);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(Login.this, "Business, Welcome To SuperCustomer", Toast.LENGTH_SHORT).show();
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    }else {
                        Toast.makeText(Login.this, "Something went wrong try later..", Toast.LENGTH_SHORT).show();
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }



                }
                else if(response.equalsIgnoreCase(s3) || response.equalsIgnoreCase(s4)) {
                    SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user", Mobile);
                    editor.apply();
                    editor.commit();


                    Intent intent = new Intent(Login.this, UserQrScan.class);
                    intent.putExtra("Mobile",Mobile);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(Login.this, "Welcome to SuperCustomer", Toast.LENGTH_SHORT).show();
                    button.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
                else {
                    Toast.makeText(Login.this, "something went wrong try later..", Toast.LENGTH_SHORT).show();
                    button.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "Something went wrong try later..", Toast.LENGTH_SHORT).show();
                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(request);

    }

}