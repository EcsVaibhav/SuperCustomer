package com.example.myapplication.Admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.Login;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class BussinessRegistration extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView nav;
    public Toolbar toolbar;

    TextView textView3;
    EditText e1, e2, e3, e4, e5;
    String Mobile;
    Button b;
    ProgressDialog progressDialog;
    ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness_registration);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav = findViewById(R.id.nav);

        textView3 = findViewById(R.id.textView2);
        progressBar = findViewById(R.id.progressBar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Intent intent = getIntent();
        Mobile = intent.getStringExtra("Mobile");

        progressDialog = new ProgressDialog(BussinessRegistration.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait..");


        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_bussiness_registration) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_logout) {
                    SharedPreferences.Editor editor = getSharedPreferences(Login.PREFS_NAME, 0).edit();
                    editor.remove("admin");
                    editor.apply();
                    overridePendingTransition(0, 0);
                    finish();
                    Intent intent1 = new Intent(BussinessRegistration.this, Login.class);
                    startActivity(intent1);
                    Toast.makeText(BussinessRegistration.this, "LogOut Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_all_acounts) {
                    Intent intent1 = new Intent(BussinessRegistration.this, AllAcounts.class);
                    startActivity(intent1);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_active_deactive) {
                    Intent intent1 = new Intent(BussinessRegistration.this, ActiveDeactive.class);
                    startActivity(intent1);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_bill_report) {
                    Intent intent1 = new Intent(BussinessRegistration.this, Admin_report_bill.class);
                    startActivity(intent1);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });

        e1 = findViewById(R.id.e1);
        e2 = findViewById(R.id.e2);
        e3 = findViewById(R.id.e3);
        e4 = findViewById(R.id.e4);
        e5 = findViewById(R.id.e5);
        b = findViewById(R.id.b);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        registerB();
                    }
                }, 1000);

            }
        });


    }

    private void registerB() {

        String BusinessName = e1.getText().toString().trim();
        String BusinessAddress = e2.getText().toString().trim();
        String BusinessMobile1 = e3.getText().toString().trim();
        String BusinessMobile2 = e4.getText().toString().trim();
        String BusinessCity = e5.getText().toString().trim();
        String Status = "Deactivate";
        String Role = "Business";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = dateFormat.format(new Date());

        if (BusinessName.isEmpty()) {
            Toast.makeText(BussinessRegistration.this, "Enter Business Name", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (BusinessAddress.isEmpty()) {
            Toast.makeText(BussinessRegistration.this, "Enter Business Address", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (BusinessCity.isEmpty()) {
            Toast.makeText(BussinessRegistration.this, "Enter City Name", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (BusinessMobile1.isEmpty()) {
            Toast.makeText(BussinessRegistration.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (BusinessMobile1.length() != 10) {
            Toast.makeText(BussinessRegistration.this, "Enter a correct Alternate Mobile Number", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } /*else if (BusinessMobile2.isEmpty()) {
                    Toast.makeText(BussinessRegistration.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (BusinessMobile2.length() != 10) {
                    Toast.makeText(BussinessRegistration.this, "Enter a correct Alternate Mobile Number", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }*/ else {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client4 = new OkHttpClient();
            okhttp3.Request request4 = new okhttp3.Request.Builder()
                    .url("http://tsm.ecssofttech.com/Library/GYMapi/registerBusiness.php?BusinessName=" + BusinessName + "&BusinessAddress=" + BusinessAddress + "&BusinessMobile1=" + BusinessMobile1 + "&BusinessMobile2=" + BusinessMobile1 + "&Status=" + Status + "&Role=" + Role + "&BusinessCity=" + BusinessCity + "&Date=" + currentDate + "").build();
            try {
                Response response4 = client4.newCall(request4).execute();
                String responseString4 = response4.body().string();
                // System.out.println(responseString4);
                if (responseString4.equalsIgnoreCase("data inserted successfully")) {
                    Toast.makeText(BussinessRegistration.this, "Business Registration Successful", Toast.LENGTH_SHORT).show();

                    e1.setText("");
                    e2.setText("");
                    e3.setText("");
                    e4.setText("");
                    e5.setText("");
                    progressDialog.dismiss();
                } else if (responseString4.equals("User already exists")){
                    Toast.makeText(this, "User already exists..", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else {
                    Toast.makeText(BussinessRegistration.this, "Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(BussinessRegistration.this, "Error!" + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }


    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent in2 = new Intent(BussinessRegistration.this, BussinessRegistration.class);
        startActivity(in2);
        finish();
    }
}