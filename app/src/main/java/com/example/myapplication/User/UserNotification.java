package com.example.myapplication.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.UserSurveyAdapter;
import com.example.myapplication.Business.EditOffer;
import com.example.myapplication.Login;
import com.example.myapplication.Model.AllSurveyBmodel;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserNotification extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView nav;
    public Toolbar toolbar;
    RecyclerView allSurveyRVL;
    ProgressBar pBar_usernotification;
    SwipeRefreshLayout refresh;
    ArrayList<AllSurveyBmodel> allSurveyBmodels;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav = findViewById(R.id.nav);
        allSurveyRVL = findViewById(R.id.All_survey_User_rvl);
        refresh = findViewById(R.id.refresh);


        allSurveyBmodels = new ArrayList<>();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_user_scan) {

                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(UserNotification.this, UserQrScan.class);
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_logout) {
                    SharedPreferences.Editor editor = getSharedPreferences(Login.PREFS_NAME,0).edit();
                    editor.remove("user");
                    editor.apply();
                    overridePendingTransition(0,0);
                    finish();
                    Intent intent1 = new Intent(UserNotification.this, Login.class);
                    startActivity(intent1);
                    Toast.makeText(UserNotification.this, "LogOut Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (item.getItemId() == R.id.menu_user_Survey)
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (item.getItemId() == R.id.menu_user_offers)
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(UserNotification.this, UserOffers.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                }else if (item.getItemId() == R.id.menu_Rewards) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(UserNotification.this, UserRewards.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                }
                else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserNotification.this);
        allSurveyRVL.setLayoutManager(linearLayoutManager);
        allSurveyRVL.setHasFixedSize(false);

        View headview = nav.getHeaderView(0);
        TextView navName = headview.findViewById(R.id.navName);
        navName.setText("Profile ID :"+getIntent().getStringExtra("NavName"));

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allSurveyBmodels.clear();
                fetchData();
                refresh.setRefreshing(false);
            }
        });
        fetchData();


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Alert !");
        builder.setMessage("Are you sure you want to exit");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void fetchData() {

        ProgressDialog progressDialog = new ProgressDialog(UserNotification.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        String MobNo = sharedPreferences.getString("user", "");

        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getAllSurveyForCustomer.php?Mobile="+MobNo+"", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                        JSONArray jsonArray = new JSONArray(response);

                        if (jsonArray.length() == 0){
                            findViewById(R.id.animationView).setVisibility(View.VISIBLE);
                        }else {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                System.out.println(c.toString());

                                String S_ID = c.getString("S_ID");
                                String Title = c.getString("Title");
                                String Que1 = c.getString("Que1");
                                String Que2 = c.getString("Que2");
                                String Que3 = c.getString("Que3");
                                String Que4 = c.getString("Que4");
                                String Que5 = c.getString("Que5");
                                String B = c.getString("Busername");
                                String DateTime = c.getString("CreateDateT");

                                // Create an AllSurveyBmodel object and add it to the list
                                allSurveyBmodels.add(new AllSurveyBmodel(S_ID, Title, Que1, Que2, Que3, Que4, Que5, B, DateTime));
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UserSurveyAdapter allSurveyAdapter = new UserSurveyAdapter(allSurveyBmodels, UserNotification.this);
                                    allSurveyRVL.setAdapter(allSurveyAdapter);
                                }
                            });

                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(UserNotification.this, "something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(UserNotification.this);
        requestQueue.add(request);
        

    }

    private void updateData(String s_id, String title, String que1, String que2, String que3, String que4, String que5, String b, String dateTime) {

        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getbusinessname.php?mobileno="+b+"", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String B_N = "null";

                try {

                    JSONArray contacts = new JSONArray(response);

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        B_N = c.getString("BusinessName");

                    }
                    allSurveyBmodels.add(new AllSurveyBmodel(s_id,title,que1,que2,que3,que4,que5,B_N,dateTime));
                    Toast.makeText(UserNotification.this, allSurveyBmodels.size(), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(UserNotification.this, "Something went wrong try later", Toast.LENGTH_SHORT).show();

            }
        });


        RequestQueue queue = Volley.newRequestQueue(UserNotification.this);
        queue.add(request);
    }

   /* private void getBN(String b) {

        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getbusinessname.php?mobileno="+b+"", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray contacts = new JSONArray(response);

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        B_N = c.getString("BusinessName");

                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(UserNotification.this, "Something went wrong try later", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue queue = Volley.newRequestQueue(UserNotification.this);
        queue.add(request);

    }

    private String getBname(String b) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new Request.Builder()
                .url("http://tsm.ecssofttech.com/Library/GYMapi/getbusinessname.php?mobileno="+b+"")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = Objects.requireNonNull(response.body()).string();
            System.out.println(responseString);

            JSONArray contacts = new JSONArray(responseString);

            String B_N = null;

            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);

                B_N = c.getString("BusinessName");

            }
            return B_N;


        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
*/

}