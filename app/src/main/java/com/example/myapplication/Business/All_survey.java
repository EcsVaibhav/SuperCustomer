package com.example.myapplication.Business;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

public class All_survey extends AppCompatActivity {
    ProgressBar PB_AllSurvey;
    RecyclerView allSurveyRVL;
    ArrayList<ModelFOallsurvey> allSurveyBmodels;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_survey);

        PB_AllSurvey = findViewById(R.id.PB_AllSurvey);

        allSurveyRVL = findViewById(R.id.All_survey_rvl);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(All_survey.this);
        allSurveyRVL.setLayoutManager(linearLayoutManager);
        allSurveyRVL.setHasFixedSize(false);

        allSurveyBmodels = new ArrayList<>();


        findViewById(R.id.AS_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        new LoadApiData().execute();

    }


    private class LoadApiData extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PB_AllSurvey.setProgress(0);
            PB_AllSurvey.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... params) {

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    getData();
                }
            });

            return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            PB_AllSurvey.setVisibility(View.INVISIBLE);

        }
    }

    private void getData() {

        ProgressDialog progressDialog = new ProgressDialog(All_survey.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        String bussinessMobNo = sharedPreferences.getString("bussiness", "");


        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getAllLiveSurveyForBusiness.php?mobileno="+bussinessMobNo+"", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("NotFound")){
                    findViewById(R.id.emanim).setVisibility(View.VISIBLE);
                }else {
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


                            allSurveyBmodels.add(new ModelFOallsurvey(S_ID, Title, Que1, Que2, Que3, Que4, Que5, Busername, DateTime[0], ExpDate, Uattended(S_ID, bussinessMobNo)));

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                AllSurveyAdapter allSurveyAdapter = new AllSurveyAdapter(All_survey.this,allSurveyBmodels);
                allSurveyRVL.setAdapter(allSurveyAdapter);
                progressDialog.dismiss();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(All_survey.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(All_survey.this);
        requestQueue.add(request);

    }


    private String Uattended(String Sid,String bMob){


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new Request.Builder()
                .url("http://tsm.ecssofttech.com/Library/GYMapi/getSurveySubmitCount.php?Sid="+Sid+"&BusinessMob="+bMob+"")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = Objects.requireNonNull(response.body()).string();
            System.out.println(responseString);
            if(!responseString.equals("NotFound0")){
                return responseString;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "0";
    }

}