package com.example.myapplication.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.UrewardAdapter;
import com.example.myapplication.Adapter.UserOffersAdapter;
import com.example.myapplication.Login;
import com.example.myapplication.Model.UrewardModel;
import com.example.myapplication.Model.UserOffersModel;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.OkHttpClient;

public class UserRewards extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView nav;
    public Toolbar toolbar;

    RecyclerView rewardRVL;
    UrewardAdapter adapter;

    SwipeRefreshLayout refresh;
    ArrayList<UrewardModel> rewardlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rewards);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav = findViewById(R.id.nav);
        rewardRVL = findViewById(R.id.rewardRVL);
        refresh = findViewById(R.id.refresh);

        rewardlist = new ArrayList<>();
        rewardRVL.setLayoutManager(new LinearLayoutManager(UserRewards.this));

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_user_scan) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(UserRewards.this, UserQrScan.class);
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_logout) {
                    SharedPreferences.Editor editor = getSharedPreferences(Login.PREFS_NAME, 0).edit();
                    editor.remove("user");
                    editor.apply();
                    overridePendingTransition(0, 0);
                    finish();
                    Intent intent1 = new Intent(UserRewards.this, Login.class);
                    startActivity(intent1);
                    Toast.makeText(UserRewards.this, "LogOut Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_user_Survey) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(UserRewards.this, UserNotification.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_user_offers) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(UserRewards.this, UserOffers.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_Rewards) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });

        View headview = nav.getHeaderView(0);
        TextView navName = headview.findViewById(R.id.navName);
        navName.setText("Profile ID :"+getIntent().getStringExtra("NavName"));

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rewardlist.clear();
                getData();
                refresh.setRefreshing(false);
            }
        });

        getData();
    }

    public void getData() {
        ProgressDialog progressDialog = new ProgressDialog(UserRewards.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
        String Mobile = sharedPreferences.getString("user", "");

        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getAllUserScan.php?Mobile="+Mobile+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    if (!(response.isEmpty())) {
                        JSONArray contacts = new JSONArray(response);

                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);

                            String Bmob = c.getString("Bmob");
                            String Bname = c.getString("Bname");
                            String setPoints = c.getString("setPoints");
                            String TSP = c.getString("TSP");
                            /*Integer.parseInt(TSP)<Integer.parseInt(setPoints) &&*/

                            if (Integer.parseInt(TSP)>0) {

                                rewardlist.add(new UrewardModel(Bmob, Bname, TSP, setPoints));
                            }


                        }


                        adapter = new UrewardAdapter( getApplicationContext(),rewardlist);
                        rewardRVL.setAdapter(adapter);


                    }else {
                        Toast.makeText(UserRewards.this, "Currently you don't have rewards", Toast.LENGTH_SHORT).show();
                    }


                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(UserRewards.this, "Something went wrong Try later", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(UserRewards.this);
        requestQueue.add(request);


    }



    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserRewards.this);
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
}