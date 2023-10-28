package com.example.myapplication.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Congratulation_forCoupan extends AppCompatActivity {

    TextView ViewCoupanBtn,Bname,offer;

    String AOP, SM, O, OA,  APO, D, Id,BusinessName;
    CardView main;
    String bMobile,uMobile;
    com.airbnb.lottie.LottieAnimationView LottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation_for_coupan);

        main = findViewById(R.id.Mcard);
        ViewCoupanBtn = findViewById(R.id.ViewCoupanBtn);
        LottieAnimationView = findViewById(R.id.floweranim);
        Bname = findViewById(R.id.Bname);
        offer = findViewById(R.id.offer);

        bMobile = getIntent().getStringExtra("Bmobile");
        uMobile = getIntent().getStringExtra("Umobile");

        showOffer();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LottieAnimationView.playAnimation();
                Animation animation = AnimationUtils.loadAnimation(Congratulation_forCoupan.this, android.R.anim.fade_in);
                main.setVisibility(View.VISIBLE);
                main.setAnimation(animation);
            }
        },2200);

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Congratulation_forCoupan.this,UserNotification.class));
                finishAffinity();
            }
        });

        findViewById(R.id.ViewCoupanBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Congratulation_forCoupan.this,UserOffers.class));
                finishAffinity();
            }
        });
    }

    private void showOffer() {

        String jsonArrayString = getIntent().getStringExtra("offer");

        try {
            JSONArray jsonArray = new JSONArray(jsonArrayString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String D = jsonObject.getString("D");
                String B_name = jsonObject.getString("name");
                String AOP = jsonObject.getString("AOP");
                String SM = jsonObject.getString("SM");
                String O = jsonObject.getString("O");
                String OA = jsonObject.getString("OA");
                String MPO = jsonObject.getString("MPO");
                String APO = jsonObject.getString("APO");
                String VT = jsonObject.getString("VT");


                Bname.setText(B_name);
                if (O.equals("Off Upto")) {
                    offer.setText(AOP + SM + " " + O + " " + OA + "Rs On " + MPO + " " + APO + "Rs " + VT + " " + D);
                } else {

                    offer.setText(AOP + SM + " " + O + " On " + OA + MPO + " " + APO + "Rs " + VT + " " + D);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Congratulation_forCoupan.this,UserNotification.class));
        finishAffinity();
    }
}