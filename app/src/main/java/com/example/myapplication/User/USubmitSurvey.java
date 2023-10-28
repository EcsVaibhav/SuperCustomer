package com.example.myapplication.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.UsubmitSurveyAdapter;
import com.example.myapplication.Model.AllSurveyBmodel;
import com.example.myapplication.Model.USubmitSurveyModel;
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

public class USubmitSurvey extends AppCompatActivity {

    RadioGroup radioGroupQ1, radioGroupQ2, radioGroupQ3, radioGroupQ4, radioGroupQ5;
    TextView sTitile_tv, tv_que1, tv_que2, tv_que3, tv_que4, tv_que5;

    ScrollView USsurveyscroll;

    ProgressDialog progressDialog;
    String BusinessNo;
    RecyclerView USS_rvl;
    ArrayList<USubmitSurveyModel> surveyModelArrayList;
    ProgressBar progressBarsurveySubmit;
    String Q1ans, Q2ans, Q3ans, Q4ans, Q5ans;

    String UMobNo;
    UsubmitSurveyAdapter adapter;
    AllSurveyBmodel surveyBmodel;
    SharedPreferences sharedPreferences ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usubmit_survey);

        surveyBmodel = getIntent().getParcelableExtra("SData");
        sharedPreferences = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        UMobNo = sharedPreferences.getString("user", "");

        USS_rvl = findViewById(R.id.USS_rvl);
        surveyModelArrayList = new ArrayList<>();

        progressDialog = new ProgressDialog(USubmitSurvey.this);

        progressBarsurveySubmit = findViewById(R.id.progressBarsurveySubmit);
        sTitile_tv = findViewById(R.id.sTitile_tv);
        radioGroupQ1 = findViewById(R.id.radioGroupQueNo1);
        radioGroupQ2 = findViewById(R.id.radioGroupQueNo2);
        radioGroupQ3 = findViewById(R.id.radioGroupQueNo3);
        radioGroupQ4 = findViewById(R.id.radioGroupQueNo4);
        radioGroupQ5 = findViewById(R.id.radioGroupQueNo5);
        USsurveyscroll = findViewById(R.id.USsurveyscroll);

        tv_que1 = findViewById(R.id.tv_que1);
        tv_que2 = findViewById(R.id.tv_que2);
        tv_que3 = findViewById(R.id.tv_que3);
        tv_que4 = findViewById(R.id.tv_que4);
        tv_que5 = findViewById(R.id.tv_que5);

        LinearLayoutManager layoutManager = new LinearLayoutManager(USubmitSurvey.this);
        USS_rvl.setLayoutManager(layoutManager);
        USS_rvl.setHasFixedSize(false);



        /*setData();*/
        /*getBusinessNo(surveyBmodel.getS_ID());*/
        new APICallTask().execute();
        fillradio();

        findViewById(R.id.submitSurveyBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (Q1ans == null || Q2ans == null || Q3ans == null || Q4ans == null || Q5ans == null) {
                    Toast.makeText(USubmitSurvey.this, "Please Answer all Questions", Toast.LENGTH_SHORT).show();
                } else {

                    new APICallTask().execute();

                    Intent in = new Intent(USubmitSurvey.this,UserNotification.class);
                    overridePendingTransition(0,0);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();

                }*/
                adapter.submitSurvey();

            }
        });


    }

    private void getBusinessNo(String Sid) {
        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getSurveydetailbyid.php?S_ID="+Sid+"", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject o = jsonArray.getJSONObject(i);

                        BusinessNo =  o.getString("Busername");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                setData(BusinessNo);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(USubmitSurvey.this);
        requestQueue.add(request);
    }


    private class APICallTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    getBusinessNo(surveyBmodel.getS_ID());
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
    private void scrollToBottom() {
        USsurveyscroll.post(new Runnable() {
            @Override
            public void run() {
                USsurveyscroll.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void setData(String Bno){

        sTitile_tv.setText(surveyBmodel.getTitle());

        surveyModelArrayList.add(new USubmitSurveyModel(surveyBmodel.getQue1(),"1",surveyBmodel.getS_ID(),Bno,UMobNo));
        surveyModelArrayList.add(new USubmitSurveyModel(surveyBmodel.getQue2(),"2",surveyBmodel.getS_ID(),Bno,UMobNo));

        if (!(surveyBmodel.getQue3().equals("NA"))){
        surveyModelArrayList.add(new USubmitSurveyModel(surveyBmodel.getQue3(),"3",surveyBmodel.getS_ID(),Bno,UMobNo));

        }

        if (!(surveyBmodel.getQue4().equals("NA"))){
            surveyModelArrayList.add(new USubmitSurveyModel(surveyBmodel.getQue4(),"4",surveyBmodel.getS_ID(),Bno,UMobNo));

        }

        if (!(surveyBmodel.getQue5().equals("NA "))){
            surveyModelArrayList.add(new USubmitSurveyModel(surveyBmodel.getQue5(),"5",surveyBmodel.getS_ID(),BusinessNo,UMobNo));

        }

        adapter = new UsubmitSurveyAdapter(surveyModelArrayList,USubmitSurvey.this);
        USS_rvl.setAdapter(adapter);

    }

    private void fillradio() {

        radioGroupQ1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = findViewById(checkedId);
                Q1ans = radioButton.getText().toString().trim();

            }
        });
        radioGroupQ2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = findViewById(checkedId);
                Q2ans = radioButton.getText().toString().trim();

            }
        });
        radioGroupQ3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = findViewById(checkedId);
                Q3ans = radioButton.getText().toString().trim();
                scrollToBottom();

            }
        });
        radioGroupQ4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = findViewById(checkedId);
                Q4ans = radioButton.getText().toString().trim();

            }
        });
        radioGroupQ5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = findViewById(checkedId);
                Q5ans = radioButton.getText().toString().trim();

            }
        });


    }

}