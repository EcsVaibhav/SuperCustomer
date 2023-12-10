package com.example.myapplication.User;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.Adapter.UserOffersAdapter;
import com.example.myapplication.Business.AllCustomers;
import com.example.myapplication.Login;
import com.example.myapplication.Model.AllCustomerModel;
import com.example.myapplication.Model.UserOffersModel;
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

public class UserOffers extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView nav;
    public Toolbar toolbar;
    String Mobile;

    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    UserOffersAdapter adapter3;

    SwipeRefreshLayout refresh;
    List<UserOffersModel> findModels;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_offers);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav = findViewById(R.id.nav);

        refresh = findViewById(R.id.refresh);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        progressDialog = new ProgressDialog(UserOffers.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Loading...");

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_user_offers) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_logout) {
                    SharedPreferences.Editor editor = getSharedPreferences(Login.PREFS_NAME, 0).edit();
                    editor.remove("user");
                    editor.apply();
                    overridePendingTransition(0, 0);
                    finish();
                    Intent intent1 = new Intent(UserOffers.this, Login.class);
                    startActivity(intent1);
                    Toast.makeText(UserOffers.this, "LogOut Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_user_Survey) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(UserOffers.this, UserNotification.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_user_scan) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(UserOffers.this, UserQrScan.class);
                    startActivity(intent1);
                    finish();
                }else if (item.getItemId() == R.id.menu_Rewards) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(UserOffers.this, UserRewards.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });

        Intent intent = getIntent();
        Mobile = intent.getStringExtra("Mobile");
        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
        Mobile = sharedPreferences.getString("user", "");

        recyclerView = findViewById(R.id.recylcerView);

        findModels = new ArrayList<>();

        View headview = nav.getHeaderView(0);
        TextView navName = headview.findViewById(R.id.navName);
        navName.setText("Profile ID :"+getIntent().getStringExtra("NavName"));


        /*getData();*/
        new Load().execute();

        SearchView search = findViewById(R.id.searchOffer);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterList(newText);
                return false;
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                findModels.clear();
                new Load().execute();
                refresh.setRefreshing(false);
            }
        });



    }
    private void filterList(String Text) {
        List<UserOffersModel> filteredlist = new ArrayList<>();

        for (UserOffersModel sbModel : findModels){
            if (sbModel.getFullOffer().toLowerCase().contains(Text.toLowerCase()) ||sbModel.getName().toLowerCase().contains(Text.toLowerCase())){
                filteredlist.add(sbModel);
            }
        }

        if (filteredlist.isEmpty()){
            Toast.makeText(UserOffers.this,"Offer not found",Toast.LENGTH_SHORT).show();
        }else {
            adapter3.SetFilteredList(filteredlist);
        }

    }

    private class Load extends AsyncTask<Void, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    getData();
                }
            });
            return null;
        }


    }

    private void getData() {


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new Request.Builder()
                .url("http://tsm.ecssofttech.com/Library/api/Gym_Get_User_Offers.php?Username=" + Mobile + "")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = Objects.requireNonNull(response.body()).string();
            System.out.println(responseString);

            if (!(responseString.isEmpty())) {
            JSONArray contacts = new JSONArray(responseString);


                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);

                    String id = String.valueOf(c.getInt("Id"));
                    String AOP = c.getString("AOP");
                    String SM = c.getString("SM");
                    String O = c.getString("O");
                    String OA = c.getString("OA");
                    String MPO = c.getString("MPO");
                    String APO = c.getString("APO");
                    String VT = c.getString("VT");
                    String D = c.getString("D");

                    String COUNT = c.getString("COUNT");
                    String Name = c.getString("Name");
                    String Username = c.getString("Username");

                    String fLine,sLine,tLine,fullOffer = " ";

                    if (O.equals("Off Upto")) {

                        fLine = AOP + SM + " " + O + " " + OA + "Rs " ;
                        sLine = "On " + MPO + " " + APO + "Rs " ;
                        tLine = VT + " " + D;
                        fullOffer = fLine+sLine+tLine;
                    } else {

                        fLine = AOP + SM + " " + O ;
                        sLine = "On " + OA + MPO + " " + APO + "Rs ";
                        tLine = VT + " " + D;
                        fullOffer = fLine+sLine+tLine;
                    }

                        findModels.add(new UserOffersModel(id, COUNT, Name, Username, fLine,sLine,tLine,fullOffer));


                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter3 = new UserOffersAdapter(findModels, getApplicationContext());
                        recyclerView.setAdapter(adapter3);

                    }
                });

            }
            else {
                findViewById(R.id.animationView).setVisibility(View.VISIBLE);

            }

            progressDialog.dismiss();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }

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
}