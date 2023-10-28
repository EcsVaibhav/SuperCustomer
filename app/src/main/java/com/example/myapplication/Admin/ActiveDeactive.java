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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActiveDeactive extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView nav;
    public Toolbar toolbar;
    boolean isChecked;

    EditText e1;

    Button b1, b2, u2;

    RadioGroup radioGroup;

    String Status;

    TextView t2, t3, t4, t5;

    String Id, a;

    ProgressDialog progressDialog;

    RelativeLayout updateSRL;
    ProgressBar progressBar, progressBar2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_deactive);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav = findViewById(R.id.nav);
        progressBar = findViewById(R.id.progressBar);
        progressBar2 = findViewById(R.id.progressBar2);
        updateSRL = findViewById(R.id.updateSRL);

        progressDialog = new ProgressDialog(ActiveDeactive.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait..");


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_bussiness_registration) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(ActiveDeactive.this, BussinessRegistration.class);
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_logout) {
                    SharedPreferences.Editor editor = getSharedPreferences(Login.PREFS_NAME, 0).edit();
                    editor.remove("admin");
                    editor.apply();
                    overridePendingTransition(0, 0);
                    finish();
                    Intent intent1 = new Intent(ActiveDeactive.this, Login.class);
                    startActivity(intent1);
                    Toast.makeText(ActiveDeactive.this, "LogOut Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_all_acounts) {
                    Intent intent1 = new Intent(ActiveDeactive.this, AllAcounts.class);
                    startActivity(intent1);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_bill_report) {
                    Intent intent1 = new Intent(ActiveDeactive.this, Admin_report_bill.class);
                    startActivity(intent1);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_active_deactive) {

                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });


        e1 = findViewById(R.id.e1);
        b1 = findViewById(R.id.b1);
        radioGroup = findViewById(R.id.rg);
        b2 = findViewById(R.id.b1);
        t2 = findViewById(R.id.t2);
        t4 = findViewById(R.id.t4);
        t3 = findViewById(R.id.t3);
        t5 = findViewById(R.id.t5);
        u2 = findViewById(R.id.u2);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (checkedId == R.id.r1) {
                    a = "Activate";

                } else {
                    a = "Deactivate";

                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile = e1.getText().toString().trim();

                if (mobile.isEmpty()) {
                    Toast.makeText(ActiveDeactive.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (!(mobile.length() == 10)) {
                    Toast.makeText(ActiveDeactive.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            getData(mobile);

                        }
                    }, 500);
                }
            }
        });

        u2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isChecked == false) {
                    Toast.makeText(ActiveDeactive.this, "Select Status", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateData();
                        }
                    }, 500);
                }

            }
        });


    }

    private void updateData() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = dateFormat.format(new Date());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://weatherafdm01.com/GYM/Gym_Update.php?Id=" + Id + "&Status=" + a + "&Date=" + currentDate + "")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = Objects.requireNonNull(response.body()).string();
            //System.out.println(responseString);
            String str = "Update Successful";
            if (responseString.equals(str)) {

                Toast.makeText(ActiveDeactive.this, "Update Successful", Toast.LENGTH_SHORT).show();
                getData(t3.getText().toString().trim());
                /*progressDialog.dismiss();*/

            } else {
                progressDialog.dismiss();
                Toast.makeText(ActiveDeactive.this, "Something went wrong try later..", Toast.LENGTH_SHORT).show();
            }


        } catch (IOException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    private void getData(String mobile) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client4 = new OkHttpClient();
        okhttp3.Request request4 = new okhttp3.Request.Builder()
                //.url("http://weatherafdm01.com/GYM/selectMob.php?BusinessMobile1=" + mobile + "").build();
                .url("http://tsm.ecssofttech.com/Library/GYMapi/getBdetail.php?mobileno=" + mobile + "").build();
        try {
            Response response4 = client4.newCall(request4).execute();
            String responseString4 = response4.body().string();
            System.out.println(responseString4);

            if (responseString4.equals("Data not found")) {
                Toast.makeText(this, responseString4, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            } else {
                JSONArray contacts = new JSONArray(responseString4);


                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    String BusinessName = c.getString("BusinessName");
                    String PStatus = c.getString("PStatus");
                    String BusinessMobile1 = c.getString("BusinessMobile1");
                    String PDate = c.getString("PDate");
                    String Status = c.getString("Status");
                    String Date = c.getString("Date");
                    Id = c.getString("Id");
                    t2.setText(BusinessName);
                    t3.setText(BusinessMobile1);
                    t4.setText(PStatus + " : " + PDate);
                    t5.setText(Status + " : " + Date);

                    updateSRL.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
            }


        } catch (JSONException | IOException e) {
            e.printStackTrace();
            t2.setText("");
            t3.setText("");
            t4.setText("");
            t5.setText("");
            updateSRL.setVisibility(View.INVISIBLE);
            Toast.makeText(ActiveDeactive.this, "Something went wrong try later.", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}