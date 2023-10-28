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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.ViewSurveyAdapter;
import com.example.myapplication.Model.ModelFOallsurvey;
import com.example.myapplication.Model.QuelistModel;
import com.example.myapplication.Model.ViewSurveyModel;
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

public class ViewSurveyResult extends AppCompatActivity {
    ModelFOallsurvey SDataList;
    TextView VS_titleTv,SAcount;

    ArrayList<ViewSurveyModel> surveyModelArrayList;

    RecyclerView VsurveyRvl;
    TextView VS_q1, VS_q2, VS_q3, VS_q4, VS_q5;
    String VSQ1_Y, VSQ1_N, VSQ1_MB;
    String VSQ2_Y, VSQ2_N, VSQ2_MB;
    String VSQ3_Y, VSQ3_N, VSQ3_MB;
    String VSQ4_Y, VSQ4_N, VSQ4_MB;
    String VSQ5_Y, VSQ5_N, VSQ5_MB;

    String TotalUser;

    int q1perYes = 0; int q1perNo = 0; int q1perMaybe = 0;
    int q2perYes = 0; int q2perNo = 0; int q2perMaybe = 0;
    int q3perYes = 0; int q3perNo = 0; int q3perMaybe = 0;
    int q4perYes = 0; int q4perNo = 0; int q4perMaybe = 0;
    int q5perYes = 0; int q5perNo = 0; int q5perMaybe = 0;

    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_survey_result);

        SDataList = getIntent().getParcelableExtra("SData");
        BindId();
        progressDialog = new ProgressDialog(ViewSurveyResult.this);
        surveyModelArrayList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewSurveyResult.this);
        VsurveyRvl.setLayoutManager(linearLayoutManager);
        VsurveyRvl.setHasFixedSize(false);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new APICallTask().execute();


    }



    private class APICallTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait..");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    setTVData();
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();


        }
    }

    public void BindId(){

        VsurveyRvl = findViewById(R.id.VsurveyRvl);
        VS_titleTv = findViewById(R.id.VS_titleTv);
        SAcount = findViewById(R.id.SAcount);
        VS_q1 = findViewById(R.id.VS_q1);
        VS_q2 = findViewById(R.id.VS_q2);
        VS_q3 = findViewById(R.id.VS_q3);
        VS_q4 = findViewById(R.id.VS_q4);
        VS_q5 = findViewById(R.id.VS_q5);


    }

    public void setTVData(){

        VS_titleTv.setText(SDataList.getTitle());

        TotalUserC();
        validatePercentage(SDataList.getS_ID());



    }

    public void TotalUserC(){

        ProgressDialog progressDialog1 = new ProgressDialog(ViewSurveyResult.this);
        progressDialog1.setMessage("Please Wait..");
        progressDialog1.setCancelable(false);
        progressDialog1.show();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        String bussinessMobNo = sharedPreferences.getString("bussiness", "");


        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getUserCunderB.php?Username="+bussinessMobNo+"", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray contacts = new JSONArray(response);

                    if (!response.isEmpty()) {
                        TotalUser = String.valueOf(contacts.length());
                        String c = "Visited Survey : " + SDataList.getAttendCount() + "/" + TotalUser + "";
                        SAcount.setText(c);
                        progressDialog1.dismiss();
                    }

                } catch (Exception e) {
                    progressDialog1.dismiss();
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(ViewSurveyResult.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ViewSurveyResult.this);
        requestQueue.add(request);
    }

    public  String calculatePercentage(int given,int Total){

        String result = "0";
        float g = (float) given;
        float t = (float) Total;

        int f = (int) ((g/t)*100);

        result = String.valueOf(f);

        return result;
    }

    public void validatePercentage(String sid){

        int ToatalVisited = Integer.parseInt(SDataList.getAttendCount());


        VSQ1_Y = "Yes 0%"; VSQ1_N = "No 0%"; VSQ1_MB = "Maybe 0%";

        VSQ2_Y = "Yes 0%"; VSQ2_N = "No 0%"; VSQ2_MB = "Maybe 0%";

        VSQ3_Y = "Yes 0%"; VSQ3_N = "No 0%"; VSQ3_MB = "Maybe 0%";

        VSQ4_Y = "Yes 0%"; VSQ4_N = "No 0%"; VSQ4_MB = "Maybe 0%";

        VSQ5_Y = "Yes 0%"; VSQ5_N = "No 0%"; VSQ5_MB = "Maybe 0%";


        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getSurveyResultData.php?Sid="+sid+"", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray contacts = new JSONArray(response);


                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);


                        String q1 = c.getString("Que1Ans");
                        String q2 = c.getString("Que2Ans");
                        String q3 = c.getString("Que3Ans");
                        String q4 = c.getString("Que4Ans");
                        String q5 = c.getString("Que5Ans");

                        switch (q1) {
                            case "YES":
                                q1perYes++;
                                break;
                            case "NO":
                                q1perNo++;
                                break;
                            case "MAYBE":
                                q1perMaybe++;
                                break;
                        }

                        switch (q2) {
                            case "YES":
                                q2perYes++;
                                break;
                            case "NO":
                                q2perNo++;
                                break;
                            case "MAYBE":
                                q2perMaybe++;
                                break;
                        }

                        switch (q3) {
                            case "YES ":
                                q3perYes++;
                                break;
                            case "NO ":
                                q3perNo++;
                                break;
                            case "MAYBE ":
                                q3perMaybe++;
                                break;
                        }

                        switch (q4) {
                            case "YES":
                                q4perYes++;
                                break;
                            case "NO":
                                q4perNo++;
                                break;
                            case "MAYBE":
                                q4perMaybe++;
                                break;
                        }

                        switch (q5) {
                            case "YES":
                                q5perYes++;
                                break;
                            case "NO":
                                q5perNo++;
                                break;
                            case "MAYBE":
                                q5perMaybe++;
                                break;
                        }


                    }
                    VSQ1_Y = "Yes "+calculatePercentage(q1perYes,ToatalVisited)+"%"; VSQ1_N = "No "+calculatePercentage(q1perNo,ToatalVisited)+"%"; VSQ1_MB = "Maybe "+calculatePercentage(q1perMaybe,ToatalVisited)+"%";

                    VSQ2_Y = "Yes "+calculatePercentage(q2perYes,ToatalVisited)+"%"; VSQ2_N = "No "+calculatePercentage(q2perNo,ToatalVisited)+"%"; VSQ2_MB = "Maybe "+calculatePercentage(q2perMaybe,ToatalVisited)+"%";

                    VSQ3_Y = "Yes "+calculatePercentage(q3perYes,ToatalVisited)+"%"; VSQ3_N = "No "+calculatePercentage(q3perNo,ToatalVisited)+"%"; VSQ3_MB = "Maybe "+calculatePercentage(q3perMaybe,ToatalVisited)+"%";

                    VSQ4_Y = "Yes "+calculatePercentage(q4perYes,ToatalVisited)+"%"; VSQ4_N = "No "+calculatePercentage(q4perNo,ToatalVisited)+"%"; VSQ4_MB = "Maybe "+calculatePercentage(q4perMaybe,ToatalVisited)+"%";

                    VSQ5_Y = "Yes "+calculatePercentage(q5perYes,ToatalVisited)+"%"; VSQ5_N = "No "+calculatePercentage(q5perNo,ToatalVisited)+"%"; VSQ5_MB = "Maybe "+calculatePercentage(q5perMaybe,ToatalVisited)+"%";



                } catch (Exception e) {
                    e.printStackTrace();
                }

                surveyModelArrayList.add(new ViewSurveyModel(SDataList.getQue1(),VSQ1_Y,VSQ1_N,VSQ1_MB));
                surveyModelArrayList.add(new ViewSurveyModel(SDataList.getQue2(),VSQ2_Y,VSQ2_N,VSQ2_MB));

                if (!(SDataList.getQue3().equals("NA"))){
                    surveyModelArrayList.add(new ViewSurveyModel(SDataList.getQue3(),VSQ3_Y,VSQ3_N,VSQ3_MB));
                }

                if (!(SDataList.getQue4().equals("NA"))){
                    surveyModelArrayList.add(new ViewSurveyModel(SDataList.getQue4(),VSQ4_Y,VSQ4_N,VSQ4_MB));

                }

                if (!(SDataList.getQue5().equals("NA "))){
                    surveyModelArrayList.add(new ViewSurveyModel(SDataList.getQue5(),VSQ5_Y,VSQ5_N,VSQ5_MB));

                }

                ViewSurveyAdapter viewSurveyAdapter = new ViewSurveyAdapter(surveyModelArrayList,ViewSurveyResult.this);
                VsurveyRvl.setAdapter(viewSurveyAdapter);


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ViewSurveyResult.this);
        requestQueue.add(request);




    }


}