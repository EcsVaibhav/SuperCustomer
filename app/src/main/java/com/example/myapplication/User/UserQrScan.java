package com.example.myapplication.User;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.UserOffersAdapter;
import com.example.myapplication.CaptureAct;
import com.example.myapplication.Login;
import com.example.myapplication.Model.UserOffersModel;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;

public class UserQrScan extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView nav;
    public Toolbar toolbar;

    private int currentPosition = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    RecyclerView UserscanRVL;
    List<UserOffersModel> findModels;
    UserOffersAdapter adapter3;
    Button u2;

    ProgressDialog progressDialog;
    String formattedDate, Name, Number;
    String Mobile;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_qr_scan);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav = findViewById(R.id.nav);

        UserscanRVL = findViewById(R.id.UserscanRVL);
        findModels = new ArrayList<>();

        progressDialog = new ProgressDialog(UserQrScan.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Loading...");


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_user_scan) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_logout) {
                    SharedPreferences.Editor editor = getSharedPreferences(Login.PREFS_NAME, 0).edit();
                    editor.remove("user");
                    editor.apply();
                    overridePendingTransition(0, 0);
                    finish();
                    Intent intent1 = new Intent(UserQrScan.this, Login.class);
                    startActivity(intent1);
                    Toast.makeText(UserQrScan.this, "LogOut Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_user_Survey) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(UserQrScan.this, UserNotification.class);
                    intent1.putExtra("NavName",Nav_name());
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_user_offers) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(UserQrScan.this, UserOffers.class);
                    intent1.putExtra("NavName",Nav_name());
                    startActivity(intent1);
                    finish();
                }else if (item.getItemId() == R.id.menu_Rewards) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(UserQrScan.this, UserRewards.class);
                    intent1.putExtra("NavName",Nav_name());
                    startActivity(intent1);
                    finish();
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });



        findViewById(R.id.OfferCBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UserQrScan.this, UserOffers.class);
                intent1.putExtra("NavName",Nav_name());
                startActivity(intent1);
                finish();
            }
        });
        findViewById(R.id.UnlockCbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(UserQrScan.this, UserNotification.class);
                intent1.putExtra("NavName",Nav_name());
                startActivity(intent1);
                finish();

            }
        });
        findViewById(R.id.RewardsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(UserQrScan.this, UserRewards.class);
                intent1.putExtra("NavName",Nav_name());
                startActivity(intent1);
                finish();
            }
        });

        Intent intent = getIntent();
        Mobile = intent.getStringExtra("Mobile");
        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
        Mobile = sharedPreferences.getString("user", "");

        u2 = findViewById(R.id.u2);

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = dateFormat.format(currentDate);
        /*getData();*/

        View headview = nav.getHeaderView(0);
        TextView navName = headview.findViewById(R.id.navName);
        navName.setText("Profile ID :"+Nav_name());

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                new Load().execute();
                handler.postDelayed(scrollRunnable, 3000);

            }
        },3000);



        u2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (u2.getText().toString().equals("Cancel")) {
                    u2.setText("Scan QR");
                } else {

                    scanCode();

                }

            }
        });


    }

    private String Nav_name(){

        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
        String NMob = sharedPreferences.getString("user", "");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://tsm.ecssofttech.com/Library/GYMapi/getCustomerDetail.php?mobileno="+NMob+"")
                .build();

        try {
            okhttp3.Response response = client.newCall(request).execute();
            String responseString = Objects.requireNonNull(response.body()).string();
            System.out.println(responseString);

            JSONArray contacts = new JSONArray(responseString);

            for (int i = 0; i < contacts.length(); i++) {
                JSONObject o = contacts.getJSONObject(i);
                int uid = o.getInt("Id");
                return String.valueOf(uid);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Super Customer";
    }

    private class Load extends AsyncTask<Void, String, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            getData();
            return null;
        }

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }

    private Runnable scrollRunnable = new Runnable() {
        @Override
        public void run() {
            // Scroll to the next position

            if (adapter3 != null) {

               /* if (currentPosition < adapter3.getItemCount() - 1) {*/
                if (currentPosition < 5) {
                    currentPosition++;
                    UserscanRVL.smoothScrollToPosition(currentPosition);
                } else {
                    // Reset back to the first position
                    currentPosition = 0;
                    UserscanRVL.scrollToPosition(currentPosition);
                }

                handler.postDelayed(this, 3000);
            }
        }
    };

    private void getData() {

        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/api/Gym_Get_User_Offers.php?Username=" + Mobile + "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray contacts = new JSONArray(response);

                    if (contacts.length() > 0) {

                        findViewById(R.id.eme).setVisibility(View.INVISIBLE);

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

                                UserscanRVL.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                adapter3 = new UserOffersAdapter(findModels, getApplicationContext());
                                UserscanRVL.setAdapter(adapter3);

                            }
                        });


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserQrScan.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(UserQrScan.this);
        requestQueue.add(request);


    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);


        try {
            barLaucher.launch(options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        u2.setText("Cancel");
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {

            String Data = result.getContents();
            String[] result1 = Data.split(",");

            Name = result1[0];
            Number = result1[1];

            insertData();
            u2.setText("Scan QR");
        } else {
            u2.setText("Scan QR");
        }
    });

    private void insertData() {
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/scanSubmit.php?Name="+Name+"&Mobile="+Mobile+"&Date="+formattedDate+"&Username="+Number+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("exists")) {

                    ShowOkDialog();
                    progressDialog.dismiss();

                }else if (response.equals("Deactivate")){

                    checkBusinessActive();
                    progressDialog.dismiss();

                }else if (response.equals("Error")) {

                    Toast.makeText(UserQrScan.this, "Something went wrong try later..", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }else {


                    try {
                        JSONArray contacts = new JSONArray(response);


                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);

                            String P = c.getString("Points");

                                StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getDefaultCoupan.php?mobileno=" +Number + "", new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {


                                        try {

                                            JSONArray contacts = new JSONArray(response);


                                            for (int i = 0; i < contacts.length(); i++) {
                                                JSONObject c = contacts.getJSONObject(i);
                                                String MinRedeem = c.getString("MinRedeem");
                                                getTpoints(Number,P,MinRedeem);

                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(UserQrScan.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                    }
                                });

                                RequestQueue requestQueue = Volley.newRequestQueue(UserQrScan.this);
                                requestQueue.add(request);



                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(UserQrScan.this, "Something went wrong try later..", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserQrScan.this, "Something went wrong try later..", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
/*
        StringRequest request = new StringRequest(Request.Method.POST, "http://tsm.ecssofttech.com/Library/api/Gym_User_Scan.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String s1 = "Record inserted successfully";
                String s2 = "User already exists";


                if (response.equalsIgnoreCase(s2)) {
                    Toast.makeText(UserQrScan.this, "You Already Scan For Today", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                } else if (response.equalsIgnoreCase(s1)) {
                    Toast.makeText(UserQrScan.this, "Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserQrScan.this, Congratulation_forScanqr.class));
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(UserQrScan.this, "Failed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("Name", Name);
                params.put("Mobile", Mobile);
                params.put("Date", formattedDate);
                params.put("Username", Number);
                return params;
            }
        };*/
        RequestQueue requestQueue = Volley.newRequestQueue(UserQrScan.this);
        requestQueue.add(request);
    }

    private void ShowOkDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserQrScan.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_for_scantommaro, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogView.findViewById(R.id.Okaybtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void checkBusinessActive() {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserQrScan.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_businessdeactivate_for_user, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogView.findViewById(R.id.Okaybtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private void getTpoints(String bmobile, String Cpoints, String minRedeem) {

        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
        String UMobile = sharedPreferences.getString("user", "");

        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getBusinessScanTPoints.php?Username="+bmobile+"&Mobile="+UMobile+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray contacts = new JSONArray(response);

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String Tpoints = String.valueOf(c.getInt("TP"));
                        String usCount = String.valueOf(c.getInt("ScanCount"));
                        progressDialog.dismiss();
                        //Toast.makeText(UserQrScan.this, "Scan Successfully", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(UserQrScan.this, Congratulation_forScanqr.class);
                        in.putExtra("Bmobile",bmobile);
                        in.putExtra("Tpoints",Tpoints);
                        in.putExtra("Points",Cpoints);
                        in.putExtra("minRedeem",minRedeem);
                        in.putExtra("usCount",usCount);
                        startActivity(in);



                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Toast.makeText(Congratulation_forScanqr.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue queue = Volley.newRequestQueue(UserQrScan.this);
        queue.add(request);
    }


    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserQrScan.this);
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