package com.example.myapplication.Business;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.AllSurveyAdapter;
import com.example.myapplication.Model.ModelFOallsurvey;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExpSurvey extends AppCompatActivity {

    RecyclerView Exp_survey_rvl;
    ProgressBar PB_ExpSurvey;

    String UattendedC;
    ArrayList<ModelFOallsurvey> allSurveyBmodels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_survey);

        Exp_survey_rvl = findViewById(R.id.Exp_survey_rvl);
        PB_ExpSurvey = findViewById(R.id.PB_ExpSurvey);

        allSurveyBmodels = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ExpSurvey.this);
        Exp_survey_rvl.setLayoutManager(linearLayoutManager);
        Exp_survey_rvl.setHasFixedSize(false);

        findViewById(R.id.ES_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /*new Load().execute();*/
        getData();
    }


    private void getData(){

        ProgressDialog progressDialog = new ProgressDialog(ExpSurvey.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        String bussinessMobNo = sharedPreferences.getString("bussiness", "");

        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getAllExpSurveyForBusiness.php?mobileno="+bussinessMobNo+"", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(!(response.equals("NotFound"))){

                try {

                    JSONArray contacts = new JSONArray(response);

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String S_ID = c.getString("S_ID");
                        String Title = c.getString("Title");
                        String Que1 = c.getString("Que1");
                        String Que2 = c.getString("Que2");
                        String Que3 = c.getString("Que3");
                        String Que4 = c.getString("Que4");
                        String Que5 = c.getString("Que5");
                        String Busername = c.getString("Busername");
                        String[] DateTime = c.getString("CreateDateT").split(" ");
                        String ExpDate = c.getString("ExpireDateT");

                        Uattend(S_ID,bussinessMobNo);

                        allSurveyBmodels.add(new ModelFOallsurvey(S_ID,Title,Que1,Que2,Que3,Que4,Que5,Busername,DateTime[0],ExpDate,UattendedC));
                    }

                    AllSurveyAdapter allSurveyAdapter = new AllSurveyAdapter(ExpSurvey.this,allSurveyBmodels);
                    Exp_survey_rvl.setAdapter(allSurveyAdapter);
                    progressDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(ExpSurvey.this, "Record not available", Toast.LENGTH_SHORT).show();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                
                progressDialog.dismiss();
                Toast.makeText(ExpSurvey.this, "Something went Wrong try later", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ExpSurvey.this);
        requestQueue.add(request);


    }

    private void Uattend(String s_id, String bussinessMobNo) {

        UattendedC = "0";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new Request.Builder()
                .url("http://tsm.ecssofttech.com/Library/GYMapi/getSurveySubmitCount.php?Sid=" + s_id + "&BusinessMob=" + bussinessMobNo + "")
                .build();

        try {
            Response response = client.newCall(request).execute();

            UattendedC = Objects.requireNonNull(response.body()).string();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private class Load extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PB_ExpSurvey.setVisibility(View.VISIBLE);

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

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            PB_ExpSurvey.setVisibility(View.GONE);
        }
    }
}