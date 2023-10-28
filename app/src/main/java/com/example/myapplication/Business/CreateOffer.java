package com.example.myapplication.Business;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Login;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateOffer extends AppCompatActivity {
    String YearMaster[] = {"Rs","%",};
    TextView t1,t2,t3,t4,t5;
    EditText e1,e2,e3;
    Dialog dialog;
    DatePickerDialog picker;
    Button create;
    String Mobile;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offer);

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);
        t5 = findViewById(R.id.t5);
        e1 = findViewById(R.id.e1);
        e2 = findViewById(R.id.e2);
        e3 = findViewById(R.id.e3);
        create = findViewById(R.id.button3);

        Intent intent = getIntent();
        Mobile = intent.getStringExtra("Mobile");
        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME,0);
        Mobile = sharedPreferences.getString("bussiness","");

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                e2.setText("");
                dialog = new Dialog(CreateOffer.this);
                dialog.setContentView(R.layout.sarch_spinner);
                dialog.getWindow().setLayout(650,800);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


                ListView list_view = dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateOffer.this, android.R.layout.simple_list_item_1,YearMaster);

                list_view.setAdapter(adapter);


                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        t1.setText(adapter.getItem(i));
                        if (t1.getText().toString().trim().equals("Rs"))
                        {
                            t2.setText("Off");
                            e2.setVisibility(View.GONE);

                        }else  if (t1.getText().toString().trim().equals("%"))
                        {
                            t2.setText("Off Upto");
                            e2.setVisibility(View.VISIBLE);
                        }
                        else {
                            Toast.makeText(CreateOffer.this, "Select Type", Toast.LENGTH_SHORT).show();
                        }
                        list_view.setAdapter(adapter);
                        dialog.dismiss();
                    }
                });
            }
        });

        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(CreateOffer.this,
                        new DatePickerDialog.OnDateSetListener() {
                            String fmonth, fDate;
                            int month;
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                try {
                                    if (monthOfYear < 10 && dayOfMonth < 10) {

                                        fmonth = "0" + monthOfYear;
                                        month = Integer.parseInt(fmonth) + 1;
                                        fDate = "0" + dayOfMonth;
                                        String paddedMonth = String.format("%02d", month);
                                        t5.setText(fDate + "/" + paddedMonth + "/" + year);
                                        System.out.println(t5.getText().toString().trim());

                                    }
                                    else if (monthOfYear < 10) {

                                        fmonth = "0" + monthOfYear;
                                        month = Integer.parseInt(fmonth) + 1;
                                        fDate = String.valueOf(dayOfMonth);
                                        String paddedMonth = String.format("%02d", month);
                                        t5.setText(fDate + "/" + paddedMonth + "/" + year);
                                        System.out.println(t5.getText().toString().trim());

                                    }
                                    else if (dayOfMonth < 10) {

                                        fmonth = "0" + monthOfYear;
                                        month = Integer.parseInt(fmonth) + 1;
                                        fDate = "0" + dayOfMonth;
                                        String paddedMonth = String.format("%02d", month);
                                        t5.setText(fDate + "/" + paddedMonth + "/" + year);
                                        System.out.println(t5.getText().toString().trim());

                                    }
                                    else {

                                        fmonth = "0" + monthOfYear;
                                        month = Integer.parseInt(fmonth) + 1;
                                        String paddedMonth = String.format("%02d", month);
                                        t5.setText(dayOfMonth + "/" + paddedMonth + "/" + year);
                                        System.out.println(t5.getText().toString().trim());

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                        }, year, month, day);
                picker.show();
                t5.setText(t5.getText());*/
                showDatePicker();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog progressDialog = new ProgressDialog(CreateOffer.this);
                progressDialog.setMessage("Please Wait");
                progressDialog.show();

                String AOP = e1.getText().toString().trim();
                String SM = t1.getText().toString().trim();
                String O = t2.getText().toString().trim();
                String OA = e2.getText().toString().trim();
                String MPO = t3.getText().toString().trim();
                String APO = e3.getText().toString().trim();
                String VT = t4.getText().toString().trim();
                String D = t5.getText().toString().trim();

                if (AOP.isEmpty())
                {
                    Toast.makeText(CreateOffer.this, "Enter Amount Or Percentage", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (SM.isEmpty())
                {
                    Toast.makeText(CreateOffer.this, "Select Type", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (SM.equals("%") && OA.isEmpty())
                {
                    Toast.makeText(CreateOffer.this, "Enter Off Percentage", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (MPO.isEmpty())
                {
                    Toast.makeText(CreateOffer.this, "Enter Minimum Purchase", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (D.isEmpty())
                {
                    Toast.makeText(CreateOffer.this, "Select Date", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else{

                    StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, "http://tsm.ecssofttech.com/Library/api/Gym_Create_Offers.php", new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String s1 = "Record Inserted Successfully";
                            String s2 = "Info Already Exist";

                            if (response.equalsIgnoreCase(s1)) {
                                Toast.makeText(CreateOffer.this, "Offer Created Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(CreateOffer.this, AllCoupons.class);
                                startActivity(intent1);
                                finishAffinity();

                                progressDialog.dismiss();
                            }
                            else if (response.equalsIgnoreCase(s2))
                            {
                                Toast.makeText(CreateOffer.this, "Record Already Exist", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(CreateOffer.this, "Failed To Add", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.put("AOP", AOP);
                            params.put("SM", SM);
                            params.put("O", O);
                            params.put("OA", OA);
                            params.put("MPO", MPO);
                            params.put("APO", APO);
                            params.put("VT", VT);
                            params.put("D", D);
                            params.put("Mobile", Mobile);

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(CreateOffer.this);
                    requestQueue.add(request);

                }

            }
        });
    }

    private void showDatePicker() {


        Calendar TodayCalender = Calendar.getInstance();
        int tomorrowYear = TodayCalender.get(Calendar.YEAR);
        int tomorrowMonth = TodayCalender.get(Calendar.MONTH);
        int tomorrowDay = TodayCalender.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedExpDate = Calendar.getInstance();
                    selectedExpDate.set(year, month, dayOfMonth);

                    t5.setText(formatDate(selectedExpDate));
                },
                tomorrowYear,
                tomorrowMonth,
                tomorrowDay
        );
        datePickerDialog.getDatePicker().setMinDate(TodayCalender.getTimeInMillis());
        datePickerDialog.show();
    }

    private String formatDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }


}