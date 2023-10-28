package com.example.myapplication.Business;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.CouponAdapter;
import com.example.myapplication.Login;
import com.example.myapplication.Model.CouponModel;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AllCoupons extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView nav;
    public Toolbar toolbar;
    String Mobile;
    RecyclerView recyclerView;
    CouponAdapter adapter3;
    List<CouponModel> findModels;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_coupens);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav = findViewById(R.id.nav);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_bussiness_settings) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(AllCoupons.this, BussinessSetting.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                }
                if (item.getItemId() == R.id.menu_coupons) {
                    drawerLayout.closeDrawer(GravityCompat.START);

                }
                else if (item.getItemId() == R.id.menu_customers)
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(AllCoupons.this, AllCustomers.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                }
                else if (item.getItemId() == R.id.menu_Survey)
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(AllCoupons.this, ChooseSurvey.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                }
                else if (item.getItemId() == R.id.menu_bussiness_dashboard)
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(AllCoupons.this, BDashboard.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_Mybill) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(AllCoupons.this, BMyBills.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                }
                else if (item.getItemId() == R.id.menu_logout) {
                    SharedPreferences.Editor editor = getSharedPreferences(Login.PREFS_NAME,0).edit();
                    editor.remove("bussiness");
                    editor.apply();
                    overridePendingTransition(0,0);
                    finish();
                    Intent intent1 = new Intent(AllCoupons.this, Login.class);
                    startActivity(intent1);
                    Toast.makeText(AllCoupons.this, "LogOut Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });

        Intent intent = getIntent();
        Mobile = intent.getStringExtra("Mobile");
        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME,0);
        Mobile = sharedPreferences.getString("bussiness","");

        View headview = nav.getHeaderView(0);
        TextView navName = headview.findViewById(R.id.navName);
        navName.setText(getIntent().getStringExtra("NavName"));

        recyclerView = findViewById(R.id.recylcerView);

        findModels = new ArrayList<>();

        getData();

        findViewById(R.id.CreateOfferBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllCoupons.this, CreateOffer.class);
                startActivity(intent);
            }
        });

    }

    private void getData() {

        ProgressDialog progressDialog  = new ProgressDialog(AllCoupons.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, "http://tsm.ecssofttech.com/Library/api/Gym_Get_Offers.php?Username="+Mobile+"", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray contacts = new JSONArray(response);


                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);


                        CouponModel findModel = new CouponModel();

                        findModel.setAOP(c.getString("AOP").toString());
                        findModel.setSM(c.getString("SM").toString());

                        findModel.setO(c.getString("O").toString());

                        findModel.setOA(c.getString("OA").toString());
                        findModel.setMPO(c.getString("MPO").toString());
                        findModel.setAPO(c.getString("APO").toString());
                        findModel.setD(c.getString("D").toString());
                        findModel.setVT(c.getString("VT").toString());
                        findModel.setMobile(c.getString("Mobile").toString());
                        findModel.setId(c.getInt("Id"));

                        try {
                            findModels.add(findModel);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        System.out.println(findModel);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter3 = new CouponAdapter(findModels,getApplicationContext());
                recyclerView.setAdapter(adapter3);
                progressDialog.dismiss();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(AllCoupons.this);
        requestQueue.add(request);

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AllCoupons.this);
        builder.setMessage("Are you sure you want to exit ?");
        builder.setTitle("Alert !");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }



}