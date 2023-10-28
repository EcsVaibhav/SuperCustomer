package com.example.myapplication.Admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.AssignAdapter;
import com.example.myapplication.Login;
import com.example.myapplication.Model.AssignModel;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class AllAcounts extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView nav;
    public Toolbar toolbar;

    RecyclerView recyclerView;
    AssignAdapter assignAdapter;

    TextView TotalBTV, TotalCTV;
    List<AssignModel> assignModels;
    ProgressDialog progressDialog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_acounts);


        findId();

        assignModels = new ArrayList<>();

        progressDialog = new ProgressDialog(AllAcounts.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait..");

        /*getData();*/
        new Load().execute();


    }

    private class Load extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
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
        TotalBTV = findViewById(R.id.TotalBTV);
        TotalCTV = findViewById(R.id.TotalCTV);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_bussiness_registration) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(AllAcounts.this, BussinessRegistration.class);
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_logout) {
                    SharedPreferences.Editor editor = getSharedPreferences(Login.PREFS_NAME, 0).edit();
                    editor.remove("admin");
                    editor.apply();
                    overridePendingTransition(0, 0);
                    finish();
                    Intent intent1 = new Intent(AllAcounts.this, Login.class);
                    startActivity(intent1);
                    Toast.makeText(AllAcounts.this, "LogOut Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_all_acounts) {

                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_active_deactive) {
                    Intent intent1 = new Intent(AllAcounts.this, ActiveDeactive.class);
                    startActivity(intent1);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_bill_report) {
                    Intent intent1 = new Intent(AllAcounts.this, Admin_report_bill.class);
                    startActivity(intent1);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
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

                if (s.isEmpty()) {
                    assignAdapter.SetFilteredList(assignModels);
                } else {
                    filterList(s);
                }
                return true;
            }
        });

    }

    private void filterList(String Text) {
        List<AssignModel> filteredlist = new ArrayList<>();

        for (AssignModel sbModel : assignModels) {
            if (sbModel.getBusinessName().toLowerCase().contains(Text.toLowerCase()) || sbModel.getBusinessMobile1().toLowerCase().contains(Text.toLowerCase()) || sbModel.getBusinessMobile2().toLowerCase().contains(Text.toLowerCase()) || sbModel.getStatus().toLowerCase().contains(Text.toLowerCase())) {
                filteredlist.add(sbModel);
            }
        }

        assignAdapter.SetFilteredList(filteredlist);

    }

    private void getData() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client4 = new OkHttpClient();
        okhttp3.Request request4 = new okhttp3.Request.Builder()
                .url("http://tsm.ecssofttech.com/Library/GYMapi/GymDisplay.php").build();
        try {
            Response response4 = client4.newCall(request4).execute();
            String responseString4 = response4.body().string();
            System.out.println(responseString4);

            JSONArray contacts = new JSONArray(responseString4);

            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);


                AssignModel assignModel = new AssignModel();

                assignModel.setBusinessName(c.getString("BusinessName").toString());
                assignModel.setBusinessAddress(c.getString("BusinessAddress").toString());
                assignModel.setBusinessMobile1(c.getString("BusinessMobile1").toString());
                assignModel.setBusinessMobile2(c.getString("Date").toString());
                assignModel.setStatus(c.getString("Status").toString());
                assignModel.setRole(c.getString("Tcustomer").toString());
                //  assignModel.setId(c.getInt("Id"));

                assignModels.add(assignModel);


            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setLayoutManager(new LinearLayoutManager(AllAcounts.this));
                    assignAdapter = new AssignAdapter(assignModels, AllAcounts.this);
                    recyclerView.setAdapter(assignAdapter);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AllAcounts.this, "something went wrong try later..", Toast.LENGTH_LONG).show();
        }


        setTCount();

    }

    private void setTCount() {

        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getTBTCforAdmin.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject o = jsonArray.getJSONObject(i);
                        String TCustomer = o.getString("Tc");
                        String TBusiness = o.getString("Tb");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TotalCTV.setText("Total Customer :\n" + TCustomer);
                                TotalBTV.setText("Total Business :\n" + TBusiness);
                            }
                        });

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AllAcounts.this, "Something went wrong try later...", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(AllAcounts.this);
        queue.add(request);
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent in2 = new Intent(AllAcounts.this, AllAcounts.class);
        startActivity(in2);
        finish();
    }
}