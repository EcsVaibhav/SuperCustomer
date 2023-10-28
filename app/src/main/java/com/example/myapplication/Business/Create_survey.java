package com.example.myapplication.Business;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Create_survey extends AppCompatActivity {

    private EditText etSurveyTitle, que1, que2, que3, que4, que5;

    TextView ExpireDatel;
    Calendar selectedExpDate;

    private String SurveyTitle, q1, q2, q3, q4, q5, timestamp, bussinessMobNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_survey);

        etSurveyTitle = findViewById(R.id.et_SurveyTitle);
        que1 = findViewById(R.id.et_que1);
        que2 = findViewById(R.id.et_que2);
        que3 = findViewById(R.id.et_que3);
        que4 = findViewById(R.id.et_que4);
        que5 = findViewById(R.id.et_que5);

        ExpireDatel =  findViewById(R.id.ExpireDate);


        AlertDialog.Builder builder = new AlertDialog.Builder(Create_survey.this);
        builder.setTitle("Instruction..");
        builder.setCancelable(false);
        builder.setMessage("1. At least first two questions necessary\n" +
                "2. Answer format define as YES NO Maybe \n" +
                "3. Please Enter Questions Which suitable to Answer format");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        ExpireDatel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });




        findViewById(R.id.btn_Create_survey).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkEt()) {

                    uploadSurvey();

                }
            }
        });

    }

    private void showDatePicker() {


        Calendar tomorrowCalendar = Calendar.getInstance();
        tomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1);
        int tomorrowYear = tomorrowCalendar.get(Calendar.YEAR);
        int tomorrowMonth = tomorrowCalendar.get(Calendar.MONTH);
        int tomorrowDay = tomorrowCalendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedExpDate = Calendar.getInstance();
                    selectedExpDate.set(year, month, dayOfMonth);

                    ExpireDatel.setText(formatDate(selectedExpDate));
                },
                tomorrowYear,
                tomorrowMonth,
                tomorrowDay
        );
        datePickerDialog.getDatePicker().setMinDate(tomorrowCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private String formatDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }


    private boolean checkEt() {

        if (etSurveyTitle.getText().toString().isEmpty()
                || ExpireDatel.getText().toString().equals("Select Expire Date")
                || que1.getText().toString().isEmpty()
                || que2.getText().toString().isEmpty()
                ) {

            Toast.makeText(this, "Enter required field", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void uploadSurvey() {

        SurveyTitle = etSurveyTitle.getText().toString();
        q1 = que1.getText().toString();
        q2 = que2.getText().toString();
        q3 = que3.getText().toString();
        q4 = que4.getText().toString();
        q5 = que5.getText().toString();

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mma");
        timestamp = dateFormat.format(currentDate);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        bussinessMobNo = sharedPreferences.getString("bussiness", "");


        HashMap<String,String> surveyData = new HashMap<>();
        surveyData.put("Sname",SurveyTitle);
        surveyData.put("bname",bussinessMobNo);
        surveyData.put("timedate",timestamp);
        surveyData.put("expDate",ExpireDatel.getText().toString());
        surveyData.put("que1",q1);
        surveyData.put("que2",q2);
        surveyData.put("que3",q3);
        surveyData.put("que4",q4);
        surveyData.put("que5",q5);



        //Toast.makeText(this, "Survey Generate", Toast.LENGTH_SHORT).show();
        Intent in = new Intent(Create_survey.this, B_send_survey.class);
        in.putExtra("surveyData",surveyData);
        startActivity(in);



    }
}