package com.example.myapplication.Business;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
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
import com.example.myapplication.Adapter.AllCustomerAdapter;
import com.example.myapplication.Adapter.CouponAdapter;
import com.example.myapplication.Login;
import com.example.myapplication.Model.AllCustomerModel;
import com.example.myapplication.Model.CouponModel;
import com.example.myapplication.Model.SendOfferModel;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AllCustomers extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView nav;
    public Toolbar toolbar;
    String Mobile;
    RecyclerView recyclerView;
    AllCustomerAdapter assignAdapter;

    List<AllCustomerModel> assignModels;
    ProgressDialog progressDialog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_customers);

        findId();
        Intent intent = getIntent();
        Mobile = intent.getStringExtra("Mobile");
        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME,0);
        Mobile = sharedPreferences.getString("bussiness","");

        View headview = nav.getHeaderView(0);
        TextView navName = headview.findViewById(R.id.navName);
        navName.setText(getIntent().getStringExtra("NavName"));

        assignModels = new ArrayList<>();

        progressDialog = new ProgressDialog(AllCustomers.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait..");


        new LoadData().execute();


    }

    private class LoadData extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getData();
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }

    private void findId() {
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
                    Intent intent1 = new Intent(AllCustomers.this, BussinessSetting.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                }
                if (item.getItemId() == R.id.menu_coupons) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(AllCustomers.this, AllCoupons.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                }
                else if (item.getItemId() == R.id.menu_Survey)
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(AllCustomers.this, ChooseSurvey.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                }
                else if (item.getItemId() == R.id.menu_customers)
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (item.getItemId() == R.id.menu_bussiness_dashboard)
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(AllCustomers.this, BDashboard.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                }else if (item.getItemId() == R.id.menu_Mybill) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(AllCustomers.this, BMyBills.class);
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
                    Intent intent1 = new Intent(AllCustomers.this, Login.class);
                    startActivity(intent1);
                    Toast.makeText(AllCustomers.this, "LogOut Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });

        SearchView search = findViewById(R.id.search1);

        recyclerView = findViewById(R.id.recview);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });

    }

    private void getData() {

        ProgressDialog progressDialog  = new ProgressDialog(AllCustomers.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getAllCustomer.php?Mobile=" + Mobile + "", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray contacts = new JSONArray(response);

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);


                        String cid = String.valueOf(c.getInt("Cid"));
                        String Umobile = c.getString("Umob");
                        String sCount = c.getString("Scount");
                        String LastVisit = c.getString("Lvisit");


                        String maskedMobile = "";
                        if (Umobile.length() >= 2) {
                            maskedMobile = Umobile.substring(0, 2) + "xxxxxx" + Umobile.substring(Umobile.length() - 2);
                        } else {
                            maskedMobile = Umobile;
                        }


                        assignModels.add(new AllCustomerModel(cid,maskedMobile,Umobile,sCount,LastVisit));

                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    assignAdapter = new AllCustomerAdapter(assignModels,getApplicationContext());
                    recyclerView.setAdapter(assignAdapter);


                    Collections.sort(assignModels, new Comparator<AllCustomerModel>() {

                        @Override
                        public int compare(AllCustomerModel o1, AllCustomerModel o2) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                Date date1 = dateFormat.parse(o1.getLvisit());
                                Date date2 = dateFormat.parse(o2.getLvisit());
                                return date2.compareTo(date1);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    });

                    assignAdapter.SetFilteredList(assignModels);

                    progressDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(AllCustomers.this);
        requestQueue.add(request);

    }

    private void filterList(String Text) {
        List<AllCustomerModel> filteredlist = new ArrayList<>();

        for (AllCustomerModel sbModel : assignModels){
            if (sbModel.getMobile().toLowerCase().contains(Text.toLowerCase()) ||sbModel.getCount().toLowerCase().contains(Text.toLowerCase()) ||sbModel.getCId().toLowerCase().contains(Text.toLowerCase())){
                filteredlist.add(sbModel);
            }
        }

        if (filteredlist.isEmpty()){
            Toast.makeText(AllCustomers.this,"Account not found",Toast.LENGTH_SHORT).show();
        }else {
            assignAdapter.SetFilteredList(filteredlist);
        }

    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AllCustomers.this);
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