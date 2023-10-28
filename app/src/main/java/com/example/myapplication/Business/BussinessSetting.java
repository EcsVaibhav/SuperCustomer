package com.example.myapplication.Business;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Login;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

public class BussinessSetting extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView nav;
    public Toolbar toolbar;
    TextView t1, t2, CdeatilTV,qrBname;
    Button button, button2;
    String Mobile;

    String DCoff, DCminPur, DCtype, DCupto;
    String perScanPoint;

    ImageView myQr;
    String minPoint, selected_offer_type;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bussiness_setting);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav = findViewById(R.id.nav);
        CdeatilTV = findViewById(R.id.CdeatilTV);
        myQr = findViewById(R.id.myQr);
        qrBname = findViewById(R.id.qrBname);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_coupons) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(BussinessSetting.this, AllCoupons.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_bussiness_settings) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_Survey) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(BussinessSetting.this, ChooseSurvey.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_bussiness_dashboard) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(BussinessSetting.this, BDashboard.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_customers) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(BussinessSetting.this, AllCustomers.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_Mybill) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(BussinessSetting.this, BMyBills.class);
                    intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_logout) {
                    SharedPreferences.Editor editor = getSharedPreferences(Login.PREFS_NAME, 0).edit();
                    editor.remove("bussiness");
                    editor.apply();
                    overridePendingTransition(0, 0);
                    finish();
                    Intent intent1 = new Intent(BussinessSetting.this, Login.class);
                    startActivity(intent1);
                    Toast.makeText(BussinessSetting.this, "LogOut Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });

        Intent intent = getIntent();
        Mobile = intent.getStringExtra("Mobile");
        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
        Mobile = sharedPreferences.getString("bussiness", "");

        View headview = nav.getHeaderView(0);
        TextView navName = headview.findViewById(R.id.navName);
        navName.setText(getIntent().getStringExtra("NavName"));

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        button = findViewById(R.id.button);


        getData();
        getDefaultCoupon();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(BussinessSetting.this);
                progressDialog.setMessage("Please Wait");
                progressDialog.show();

                String PerScan = t1.getText().toString().trim();
                String MinRedeem = t2.getText().toString().trim();

                if (PerScan.isEmpty()) {
                    Toast.makeText(BussinessSetting.this, "Enter Per Scan reward", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (MinRedeem.isEmpty()) {
                    Toast.makeText(BussinessSetting.this, "Enter minmum redeem point", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {

                    showRewardPointDialog();
                    progressDialog.dismiss();

                }

            }
        });

        findViewById(R.id.DCbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOfferDialog();
            }
        });

    }


    private void getData() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client4 = new OkHttpClient();
        okhttp3.Request request4 = new okhttp3.Request.Builder()
                .url("http://tsm.ecssofttech.com/Library/api/Gym_Get_Points.php?Mobile=" + Mobile + "").build();
        try {
            okhttp3.Response response4 = client4.newCall(request4).execute();
            String responseString4 = response4.body().string();
            System.out.println(responseString4);

            JSONArray contacts = new JSONArray(responseString4);


            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);
                t1.setText(c.getString("PerScan"));
                t2.setText(c.getString("MinRedeem"));

                String bn = c.getString("BusinessName");
                String bm = c.getString("BusinessMobile1");

                qrBname.setText(bn);
                String Qr = bn +","+bm;

                MultiFormatWriter mWriter = new MultiFormatWriter();

                try {
                    //BitMatrix class to encode entered text and set Width & Height
                    BitMatrix mMatrix = mWriter.encode(Qr, BarcodeFormat.QR_CODE, 500, 500);
                    BarcodeEncoder mEncoder = new BarcodeEncoder();
                    Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
                    myQr.setImageBitmap(mBitmap);//Setting generated QR code to imageView


                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Toast.makeText(BussinessSetting.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }
    private void getDefaultCoupon() {

        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getDefaultCoupan.php?mobileno=" + Mobile + "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONArray contacts = new JSONArray(response);


                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        DCoff = c.getString("DCoff");
                        DCminPur = c.getString("DCminPur");
                        DCtype = c.getString("DCtype");
                        DCupto = c.getString("DCupto");

                        if (DCtype.equals("Rs")) {
                            CdeatilTV.setText(DCoff + DCtype + " off on Minimum Purchase of " + DCminPur + "Rs");
                        } else {
                            CdeatilTV.setText(DCoff + DCtype + " off Upto " + DCupto + "Rs On Minimum Purchase of " + DCminPur + "Rs");
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(BussinessSetting.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }


    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BussinessSetting.this);
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


    private void showRewardPointDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.layout_for_rewardpoint, null);

        EditText PerScanET = dialogView.findViewById(R.id.PerScanET);
        EditText MinimumPointET = dialogView.findViewById(R.id.MinimumPointET);
        Button submit = dialogView.findViewById(R.id.submitButton);

        PerScanET.setText(t1.getText().toString());
        MinimumPointET.setText(t2.getText().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialogView.findViewById(R.id.Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                perScanPoint = PerScanET.getText().toString();
                minPoint = MinimumPointET.getText().toString();
                if (perScanPoint.isEmpty()) {
                    PerScanET.setError("Require to update");
                } else if (minPoint.isEmpty()) {
                    MinimumPointET.setError("Require to update");
                } else {


                    StringRequest request = new StringRequest(Request.Method.POST, "http://tsm.ecssofttech.com/Library/api/Gym_Get_Points_Update.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String s1 = "Update Successful";


                            if (response.equalsIgnoreCase(s1)) {
                                Toast.makeText(BussinessSetting.this, "Update Successful", Toast.LENGTH_SHORT).show();
                                getData();

                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(BussinessSetting.this, "Failed", Toast.LENGTH_SHORT).show();

                        }
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.put("PerScan", perScanPoint);
                            params.put("MinRedeem", minPoint);
                            params.put("Mobile", Mobile);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(BussinessSetting.this);
                    requestQueue.add(request);

                    dialog.dismiss();

                }
            }
        });


    }

    private void showOfferDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.layout_for_defaultoffer, null);

        EditText offET = dialogView.findViewById(R.id.e1);
        EditText MinimumPurchaseET = dialogView.findViewById(R.id.e3);
        EditText uptoET = dialogView.findViewById(R.id.ValidateET);
        LinearLayout ValidateLy = dialogView.findViewById(R.id.ValidateLy);
        Button customButton = dialogView.findViewById(R.id.button4);

        offET.setText(DCoff);
        MinimumPurchaseET.setText(DCminPur);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        ArrayList<String> type = new ArrayList<String>();
        if (DCtype.equals("Rs")) {
            type.add("Rs");
            type.add("%");
        } else {
            type.add("%");
            type.add("Rs");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, type);

        Spinner spinYear = (Spinner) dialogView.findViewById(R.id.t1);
        spinYear.setAdapter(adapter);

        spinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                selected_offer_type = type.get(position);
                if (selected_offer_type.equals("%")) {
                    ValidateLy.setVisibility(View.VISIBLE);
                    if (DCupto.equals("0")){
                        uptoET.setText("");
                    }else {
                        uptoET.setText(DCupto);
                    }

                } else {
                    ValidateLy.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


        dialogView.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (offET.getText().toString().isEmpty()) {
                    offET.setError("require to update");
                } else if (MinimumPurchaseET.getText().toString().isEmpty()) {
                    MinimumPurchaseET.setError("require to update");

                } else if (selected_offer_type.equals("%") && uptoET.getText().toString().isEmpty()) {

                    uptoET.setError("require to update");

                } else {
                    DCoff = offET.getText().toString();
                    DCminPur = MinimumPurchaseET.getText().toString();
                    if (selected_offer_type.equals("Rs")) {
                        DCupto = "0";
                    } else {
                        DCupto = uptoET.getText().toString();
                    }

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.GET,
                            "http://tsm.ecssofttech.com/Library/GYMapi/updateDefaultCoupon.php?DCoff=" + DCoff + "&DCminPur=" + DCminPur + "&DCtype=" + selected_offer_type + "&DCupto=" + DCupto + "&Mobile=" + Mobile + "",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equals("updated")) {

                                        Toast.makeText(BussinessSetting.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                                        getDefaultCoupon();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Toast.makeText(BussinessSetting.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );


                    RequestQueue requestQueue = Volley.newRequestQueue(BussinessSetting.this);
                    requestQueue.add(stringRequest);


                    dialog.dismiss();


                }
            }
        });


    }


}