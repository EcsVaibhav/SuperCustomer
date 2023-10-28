package com.example.myapplication.Business;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.SurveyquelistAdapter;
import com.example.myapplication.Model.QuelistModel;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;

public class B_send_survey extends AppCompatActivity {

    TextView sTitle,qt1,qt2,qt3,qt4,qt5;
    LinearLayout que1ly,que2ly,que3ly,que4ly,que5ly;

    RecyclerView B_surveySend_rvl;


    private String SurveyTitle, q1, q2, q3, q4, q5, timestamp, bussinessMobNo,expDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bsend_survey);

        HashMap<String,String> data = (HashMap<String, String>) getIntent().getSerializableExtra("surveyData");

        SurveyTitle = data.get("Sname");
        timestamp = data.get("timedate");
        bussinessMobNo = data.get("bname");
        expDate = data.get("expDate");

        B_surveySend_rvl = findViewById(R.id.B_surveySend_rvl);
        sTitle = findViewById(R.id.S_title);
        qt1 = findViewById(R.id.et_que1);
        qt2 = findViewById(R.id.et_que2);
        qt3 = findViewById(R.id.et_que3);
        qt4 = findViewById(R.id.et_que4);
        qt5 = findViewById(R.id.et_que5);

        que1ly = findViewById(R.id.que1ly);
        que2ly = findViewById(R.id.que2ly);
        que3ly = findViewById(R.id.que3ly);
        que4ly = findViewById(R.id.que4ly);
        que5ly = findViewById(R.id.que5ly);

        sTitle.setText(SurveyTitle);

        q1 = data.get("que1");

        q2 = data.get("que2");

        q3 = data.get("que3");

        q4 = data.get("que4");

        q5 = data.get("que5");


        B_surveySend_rvl.setLayoutManager(new LinearLayoutManager(B_send_survey.this));
        ArrayList<QuelistModel> surveyquelist = new ArrayList<>();

        surveyquelist.add(new QuelistModel(q1));
        surveyquelist.add(new QuelistModel(q2));

        if (q3.isEmpty()){
            que3ly.setVisibility(View.INVISIBLE);
            q3 = "NA";
        }else {
            surveyquelist.add(new QuelistModel(q3));
        }
        if (q4.isEmpty()){
            que4ly.setVisibility(View.INVISIBLE);
            q4 = "NA";
        }else {
            surveyquelist.add(new QuelistModel(q4));
        }
        if (q5.isEmpty()){
            que5ly.setVisibility(View.INVISIBLE);
            q5 = "NA";
        }else {
            surveyquelist.add(new QuelistModel(q5));
        }

        B_surveySend_rvl.setHasFixedSize(false);
        B_surveySend_rvl.setAdapter(new SurveyquelistAdapter(surveyquelist,B_send_survey.this));


        findViewById(R.id.btn_survey_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_Send_survey).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadSuervey();

            }
        });

    }


    private void UploadSuervey() {


        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/SurveyUpload.php?Title="+SurveyTitle+"&Que1="+q1+"&Que2="+q2+"&Que3="+q3+"&Que4="+q4+"&Que5="+q5+"&Busername="+bussinessMobNo+"&CreateDateT="+timestamp+"&ExpireDateT="+expDate+"", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("Data inserted successfully")){

                    Toast.makeText(B_send_survey.this, "Survey Send successfully", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(B_send_survey.this, ChooseSurvey.class);
                    startActivity(in);
                    finishAffinity();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(B_send_survey.this, "Something went Wrong Please try again", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(B_send_survey.this);
        requestQueue.add(request);

    }
}