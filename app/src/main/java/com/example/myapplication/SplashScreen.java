package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Admin.BussinessRegistration;
import com.example.myapplication.Business.BDashboard;
import com.example.myapplication.User.UserQrScan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (checkInternetConnection()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Load().execute();
                }
            }, 1000);

        }else {

            findViewById(R.id.connectionLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.retryBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SplashScreen.this,SplashScreen.class));
                    finishAffinity();
                }
            });
        }


    }

    @SuppressLint("StaticFieldLeak")
    private class Load extends AsyncTask<Void, Integer, Void> {


        @Override
        protected Void doInBackground(Void... params) {


            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    checkandvalidate();
                }
            });


            return null;
        }

    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

            if (isConnected) {
                // You are connected to the internet
                return true;
            }
        }
        return false;
    }


    private String checkBStatus(String B_Mob) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new Request.Builder()
                .url("http://tsm.ecssofttech.com/Library/GYMapi/getBdetail.php?mobileno=" + B_Mob + "")
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
        return "";
    }

    private void checkandvalidate() {


        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
        String user = sharedPreferences.getString("user", "");
        String admin = sharedPreferences.getString("admin", "");
        String business = sharedPreferences.getString("bussiness", "");

        if (admin != "") {
            Intent intent = new Intent(SplashScreen.this, BussinessRegistration.class);
            startActivity(intent);
            finish();
        } else if (business != "") {
            if (checkBStatus(business).equals("Deactivate")) {

                SharedPreferences.Editor editor = getSharedPreferences(Login.PREFS_NAME, 0).edit();
                editor.remove("bussiness");
                editor.apply();

                Intent intent = new Intent(SplashScreen.this, Login.class);
                startActivity(intent);
                finish();

                Toast.makeText(this, "Your Account is Deactivated", Toast.LENGTH_SHORT).show();

            } else if (checkBStatus(business).equals("Activate")) {
                Intent intent = new Intent(SplashScreen.this, BDashboard.class);
                startActivity(intent);
                finish();
            } else {

                SharedPreferences.Editor editor = getSharedPreferences(Login.PREFS_NAME, 0).edit();
                editor.remove("bussiness");
                editor.apply();

                Intent intent = new Intent(SplashScreen.this, Login.class);
                startActivity(intent);
                finish();

                Toast.makeText(this, "Something went wrong Login again..", Toast.LENGTH_SHORT).show();

            }
        } else if (user != "") {
            Intent intent = new Intent(SplashScreen.this, UserQrScan.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashScreen.this, Login.class);
            startActivity(intent);
            finish();
        }


    }


}