package com.example.myapplication.Business;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.SendOfferAdapter;
import com.example.myapplication.Login;
import com.example.myapplication.Model.CouponModel;
import com.example.myapplication.Model.SendOfferModel;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.OkHttpClient;

public class SendOffer extends AppCompatActivity {
    String Mobile;
    RecyclerView recyclerView;
    SendOfferAdapter adapter;
    List<SendOfferModel> findModels;
    ArrayList<SendOfferModel> filterDataList;
    EditText searchEditText;
    CouponModel couponModel;
    ImageView filterButton;
    String AOP, SM, O, OA, MPO, APO, VT, D, Id;
    private boolean isAllSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_offer);

        couponModel = getIntent().getParcelableExtra("data");
        AOP = couponModel.getAOP();
        APO = couponModel.getAPO();
        MPO = couponModel.getMPO();
        SM = couponModel.getSM();
        D = couponModel.getD();
        O = couponModel.getO();
        OA = couponModel.getOA();
        VT = couponModel.getVT();
        Id = String.valueOf(couponModel.getId());


        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
        Mobile = sharedPreferences.getString("bussiness", "");

        searchEditText = findViewById(R.id.searchEditText);
        recyclerView = findViewById(R.id.recyclerView);
        filterButton = findViewById(R.id.filterButtonOn);

        findModels = new ArrayList<>();


        getData();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SendOffer.this, AllCoupons.class));
                finishAffinity();
            }
        });

        findViewById(R.id.filterButtonOff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.filterButtonOff).setVisibility(View.INVISIBLE);
                filterButton.setVisibility(View.VISIBLE);
                adapter.setFilteredList(findModels);
                if(adapter.getItemCount() == 0){
                    findViewById(R.id.saveButton).setVisibility(View.INVISIBLE);
                }else {
                    findViewById(R.id.saveButton).setVisibility(View.VISIBLE);
                }
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showfilterdialog();
            }
        });

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (adapter != null) {
                        adapter.saveSelectedItemsToDatabase();
                    } else {
                        throw new IllegalStateException("Adapter is null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void showfilterdialog() {


        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.layout_for_sendfilter, null);

        EditText aboveEt = dialogView.findViewById(R.id.GreterthanEt);
        EditText belowEt = dialogView.findViewById(R.id.LessthanEt);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        dialogView.findViewById(R.id.Apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maxCount = 0;
                int minCount = 0;


                String below = belowEt.getText().toString().trim();
                String above = aboveEt.getText().toString().trim();

                if (above.isEmpty() || below.isEmpty()){
                    /*dialog.dismiss();*/
                    Toast.makeText(SendOffer.this, "Enter Range", Toast.LENGTH_SHORT).show();
                }else if (Integer.parseInt(above)>Integer.parseInt(below)){
                    Toast.makeText(SendOffer.this, "Invalid Range", Toast.LENGTH_SHORT).show();
                }else {

                    findViewById(R.id.filterButtonOff).setVisibility(View.VISIBLE);
                    filterButton.setVisibility(View.INVISIBLE);

                    maxCount = Integer.parseInt(below);
                    minCount = Integer.parseInt(above);

                    filterData(maxCount, minCount);
                    dialog.dismiss();
                }


            }
        });

        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    private void filterData(int maxCount, int minCount) {

        filterDataList = new ArrayList<>();

        for (SendOfferModel model : findModels) {
            int count = Integer.parseInt(model.getCOUNT());

            if (minCount>maxCount){
                if (count >=minCount){
                    filterDataList.add(model);
                }else if (count<=maxCount){
                    filterDataList.add(model);
                }
            }else {
                if (count >= minCount && count <= maxCount) {
                    filterDataList.add(model);
                }
            }
        }
        adapter.setFilteredList(filterDataList);
        if (filterDataList.size()==0){
            findViewById(R.id.saveButton).setVisibility(View.INVISIBLE);
        }

    }


    private void getData() {

        ProgressDialog progressDialog = new ProgressDialog(SendOffer.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/Gym_Get_Scan_Count.php?username=" + Mobile + "", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray contacts = new JSONArray(response);

                    if (contacts.length() == 0){
                        findViewById(R.id.saveButton).setVisibility(View.INVISIBLE);
                    }else {

                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);

                            SendOfferModel findModel = new SendOfferModel();

                            findModel.setName(c.getString("name").toString());
                            findModel.setMobile(c.getString("mobile").toString());
                            findModel.setUsername(c.getString("username").toString());
                            findModel.setCOUNT(c.getString("count").toString());
                            findModel.setId(c.getInt("id"));
                            findModel.setD(D);
                            findModel.setVT(VT);
                            findModel.setAPO(APO);
                            findModel.setMPO(MPO);
                            findModel.setOA(OA);
                            findModel.setO(O);
                            findModel.setSM(SM);
                            findModel.setAOP(AOP);
                            findModels.add(findModel);

                            System.out.println(findModel);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new SendOfferAdapter(findModels, getApplicationContext(), AOP, SM, O, OA, MPO, APO, VT, D, Id);
                recyclerView.setAdapter(adapter);
                Collections.sort(findModels, new Comparator<SendOfferModel>() {
                    @Override
                    public int compare(SendOfferModel o1, SendOfferModel o2) {
                        int count1 = Integer.parseInt(o1.getCOUNT());
                        int count2 = Integer.parseInt(o2.getCOUNT());
                        return Integer.compare(count2, count1);
                    }
                });

                adapter.setFilteredList(findModels);

                progressDialog.dismiss();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(SendOffer.this);
        requestQueue.add(request);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, AllCoupons.class));
        finishAffinity();
    }
}
