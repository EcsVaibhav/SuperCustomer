package com.example.myapplication.Business;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.AllSurveyAdapter;
import com.example.myapplication.Adapter.BMyBillsAdapter;
import com.example.myapplication.Login;
import com.example.myapplication.Model.BBillModel;
import com.example.myapplication.Model.ModelFOallsurvey;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BMyBills extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView nav;
    public Toolbar toolbar;

    BMyBillsAdapter adapter;

    ArrayList<BBillModel> arrayList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmy_bills);


        findId();
        arrayList = new ArrayList<>();



        View headview = nav.getHeaderView(0);
        TextView navName = headview.findViewById(R.id.navName);
        navName.setText(getIntent().getStringExtra("NavName"));

        getData();
    }

    private void getData() {

        ProgressDialog progressDialog = new ProgressDialog(BMyBills.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        String bussinessMobNo = sharedPreferences.getString("bussiness", "");


        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getBMYbill.php?BMobile="+bussinessMobNo+"", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    try {

                        JSONArray contacts = new JSONArray(response);

                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);

                            String BName = c.getString("BName");
                            String BMobile = c.getString("BMobile");

                            String BillPeriod = c.getString("BBillPeriod");
                            String BTc = c.getString("TotalCustomer");
                            String BAmount = c.getString("BillAmount");



                            arrayList.add(new BBillModel(BName,BMobile,BillPeriod,BTc,BAmount) );

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                adapter = new BMyBillsAdapter(BMyBills.this,arrayList);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(BMyBills.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(BMyBills.this);
        requestQueue.add(request);

    }

    private void findId() {
        drawerLayout = findViewById(R.id.my_drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav = findViewById(R.id.nav);

        recyclerView = findViewById(R.id.BbillRecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(BMyBills.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_bussiness_settings) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(BMyBills.this, BussinessSetting.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                }
                if (item.getItemId() == R.id.menu_coupons) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(BMyBills.this, AllCoupons.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                }
                else if (item.getItemId() == R.id.menu_Survey)
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(BMyBills.this, ChooseSurvey.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                }
                else if (item.getItemId() == R.id.menu_customers)
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(BMyBills.this, AllCustomers.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                }
                else if (item.getItemId() == R.id.menu_bussiness_dashboard)
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(BMyBills.this, BDashboard.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                }
                else if (item.getItemId() == R.id.menu_Mybill)
                {
                    drawerLayout.closeDrawer(GravityCompat.START);

                }
                else if (item.getItemId() == R.id.menu_logout) {
                    SharedPreferences.Editor editor = getSharedPreferences(Login.PREFS_NAME,0).edit();
                    editor.remove("bussiness");
                    editor.apply();
                    overridePendingTransition(0,0);
                    finish();
                    Intent intent1 = new Intent(BMyBills.this, Login.class);
                    startActivity(intent1);
                    Toast.makeText(BMyBills.this, "LogOut Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });


    }
}