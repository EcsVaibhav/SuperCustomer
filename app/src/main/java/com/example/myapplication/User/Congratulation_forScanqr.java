package com.example.myapplication.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Login;
import com.example.myapplication.Model.UserOffersModel;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Congratulation_forScanqr extends AppCompatActivity {

    TextView pointValue,ProgessTV;
    CardView main,main_coupan;

    TextView ViewCoupanBtn,Bname,offer;
    String AOP, SM, O, OA,  APO, D, Id,BusinessName;
    String MPO = "Minimum Purchase Of";
    String VT = "Valid Till";
    String scanCount;

    String Tpoints ;
    String Bmobile ;
    String Points ;
    String minRedeem ;

    private ProgressBar progressBar;
    LottieAnimationView LottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation_for_scanqr);

        Tpoints = getIntent().getStringExtra("Tpoints");
        Bmobile = getIntent().getStringExtra("Bmobile");
        Points = getIntent().getStringExtra("Points");
        minRedeem = getIntent().getStringExtra("minRedeem");
        scanCount = getIntent().getStringExtra("usCount");

        pointValue = findViewById(R.id.pointValue);
        main = findViewById(R.id.Mcard);
        main_coupan = findViewById(R.id.Mcard_coupan);
        LottieAnimationView = findViewById(R.id.floweranim);
        progressBar = findViewById(R.id.progressBar);
        ProgessTV = findViewById(R.id.ProgessTV);
        Bname = findViewById(R.id.Bname);
        offer = findViewById(R.id.offer);
        ViewCoupanBtn = findViewById(R.id.ViewCoupanBtn);
        changeUI();

        findViewById(R.id.tryagainbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void changeUI() {

        int currentPoints = (int) ((Double.parseDouble(Tpoints)/Double.parseDouble(minRedeem))*100);
        int cValue = Integer.parseInt(Tpoints);
        int maxvalue = Integer.parseInt(minRedeem);


        progressBar.setProgress(currentPoints);
        progressBar.setMax(100);
        ProgessTV.setText(Integer.parseInt(Tpoints)+"/"+maxvalue);
        pointValue.setText(Points);

        if (cValue>=maxvalue){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LottieAnimationView.playAnimation();
                    Animation animation = AnimationUtils.loadAnimation(Congratulation_forScanqr.this, android.R.anim.fade_in);
                    main_coupan.setVisibility(View.VISIBLE);
                    main_coupan.setAnimation(animation);
                }
            },2200);

            findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Congratulation_forScanqr.this,UserQrScan.class));
                    finishAffinity();
                }
            });

            addOffer(Bmobile);

            findViewById(R.id.ViewCoupanBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Congratulation_forScanqr.this,UserOffers.class));
                    finishAffinity();
                }
            });

        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LottieAnimationView.playAnimation();
                    Animation animation = AnimationUtils.loadAnimation(Congratulation_forScanqr.this, android.R.anim.fade_in);
                    main.setVisibility(View.VISIBLE);
                    main.setAnimation(animation);
                }
            },2200);

            findViewById(R.id.close_coin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Congratulation_forScanqr.this,UserQrScan.class));
                    finishAffinity();
                }
            });

        }
    }

    private void UpdatePoints(String BMob) {

        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
        String UMob = sharedPreferences.getString("user", "");

        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/saveandupdateScan.php?Username="+BMob+"&Mobile="+UMob+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals("Updated")){
                    Toast.makeText(Congratulation_forScanqr.this, "Offer Credited successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Congratulation_forScanqr.this, "Please Contact to Business", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Congratulation_forScanqr.this);
        requestQueue.add(request);
    }


    private void addOffer(String BMob) {

        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
        String UMobNo = sharedPreferences.getString("user", "");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date seventhDayDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        D = sdf.format(seventhDayDate);

        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getDefaultCoupan.php?mobileno=" +BMob + "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONArray contacts = new JSONArray(response);


                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        AOP = c.getString("DCoff");
                        APO = c.getString("DCminPur");
                        SM = c.getString("DCtype");

                        if (c.getString("DCupto").equals("0")){
                            OA = "";
                        }else {
                            OA = c.getString("DCupto");
                        }

                        BusinessName = c.getString("BusinessName");
                        Id = "0";

                        if (SM.equals("Rs")) {
                            O = "Off";
                        } else {
                            O = "Off Upto";
                        }

                        Bname.setText(BusinessName);
                        if (O.equals("Off Upto")) {
                            offer.setText(AOP + SM + " " + O + " " + OA + "Rs On " + MPO + " " + APO + "Rs " + VT + " " + D);
                        } else {

                            offer.setText(AOP + SM + " " + O + " On " + OA + MPO + " " + APO + "Rs " + VT + " " + D);
                        }

                        submitOffer(BusinessName,UMobNo,BMob,AOP,SM,O,OA,D,MPO,APO,VT,Id);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Congratulation_forScanqr.this);
        requestQueue.add(request);
    }

    private void submitOffer(String businessName, String uMobNo, String bMobN, String aop, String sm, String o, String oa, String d, String mpo, String apo, String vt, String id) {


        try {
            JSONArray selectedItems = new JSONArray();
            JSONObject item = new JSONObject();
            item.put("count", scanCount);
            item.put("name", businessName);
            item.put("mobile", uMobNo);
            item.put("username",bMobN);
            item.put("AOP", aop);
            item.put("SM", sm);
            item.put("O", o);
            item.put("OA", oa);
            item.put("D", d);
            item.put("MPO", mpo);
            item.put("APO", apo);
            item.put("VT", vt);
            item.put("Id", id);

            selectedItems.put(item);


            JSONObject requestBody = new JSONObject();
            requestBody.put("selectedItems", selectedItems);
            RequestQueue requestQueue = Volley.newRequestQueue(Congratulation_forScanqr.this);
            String url = "http://tsm.ecssofttech.com/Library/api/Gym_Save.php";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Handle the response from the web service/API
                            try {
                                if (response.getString("message").equals("Coupon Send successfully")){

                                    UpdatePoints(bMobN);


                                }else {
                                    Toast.makeText(Congratulation_forScanqr.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();

                                }

                                // Display a success message or perform any additional actions
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle the error occurred during the request
                            error.printStackTrace();

                        }
                    });

            // Add the request to the RequestQueue
            requestQueue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Congratulation_forScanqr.this,UserQrScan.class));
        finishAffinity();
    }
}