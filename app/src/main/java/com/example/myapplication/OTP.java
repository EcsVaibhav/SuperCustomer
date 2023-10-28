package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.BussinessRegistration;
import com.example.myapplication.Business.BussinessSetting;
import com.example.myapplication.User.UserQrScan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.Map;

public class OTP extends AppCompatActivity {
    EditText num1, num2, num3, num4,num5,num6;
    Button verify;
    ProgressBar progressBar;
    String otp,Mobile;
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
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(otp,fcode);

                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful())
                                    {
                                        loginMethod();
                                    }
                                    else {
                                        verify.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(OTP.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        numberotpmove();
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

        Intent intent = getIntent();
        Mobile = intent.getStringExtra("Mobile");
        otp = getIntent().getStringExtra("Otp");
    }

    private void loginMethod() {

        StringRequest request = new StringRequest(Request.Method.POST, "http://tsm.ecssofttech.com/Library/api/Gym_Login.php", new com.android.volley.Response.Listener<String>() {
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
                    Toast.makeText(OTP.this, "Admin Login", Toast.LENGTH_SHORT).show();
                    verify.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
                else if(response.equalsIgnoreCase(s2)) {
                    SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("bussiness", Mobile);
                    editor.apply();
                    editor.commit();

                    Intent intent = new Intent(OTP.this, BussinessSetting.class);
                    intent.putExtra("Mobile",Mobile);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(OTP.this, "Business Login", Toast.LENGTH_SHORT).show();
                    verify.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
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
                    Toast.makeText(OTP.this, "User Login", Toast.LENGTH_SHORT).show();
                    verify.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(OTP.this, "Failed", Toast.LENGTH_SHORT).show();
                    verify.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(OTP.this, "Failed", Toast.LENGTH_SHORT).show();
                verify.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("Mobile",Mobile);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(OTP.this);
        requestQueue.add(request);

    }

    private void numberotpmove() {

        num1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    num2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        num2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    num3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        num3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    num4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        num4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    num5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        num5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    num6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}