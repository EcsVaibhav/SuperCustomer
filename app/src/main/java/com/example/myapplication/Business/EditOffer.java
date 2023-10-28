package com.example.myapplication.Business;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Login;
import com.example.myapplication.Model.CouponModel;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditOffer extends AppCompatActivity {
    String YearMaster[] = {"Rs","%",};
    TextView t1,t2,t3,t4,t5;
    TextView e1,e2,e3;
    Dialog dialog;
    DatePickerDialog picker;
    CouponModel couponModel;
    Button create,delete;
    String Mobile,AOP,SM,O,OA,MPO,APO,VT,D,Id;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_offer);

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);
        t5 = findViewById(R.id.t5);
        e1 = findViewById(R.id.e1);
        e2 = findViewById(R.id.e2);
        e3 = findViewById(R.id.e3);
        create = findViewById(R.id.button3);
        delete = findViewById(R.id.button4);


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
        Mobile = couponModel.getMobile();


        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME,0);
        Mobile = sharedPreferences.getString("bussiness","");
        t4.setText(VT);
        t5.setText(D);
        t3.setText(MPO);
        e1.setText(AOP);
        e3.setText(APO);

        t1.setText(SM);

        if (SM.equals("Rs"))
        {
            t2.setVisibility(View.VISIBLE);
            e2.setVisibility(View.GONE);
            t2.setText(O);
        }
        else{
            t2.setVisibility(View.VISIBLE);
            e2.setVisibility(View.VISIBLE);
            t2.setText(O);
            e2.setText(OA);
        }

        if (t1.getText().toString().trim().equals("Rs"))
        {
            t2.setText("Off On");
            e2.setVisibility(View.GONE);

        }else  if (t1.getText().toString().trim().equals("%"))
        {
            t2.setText("Off Upto");
            e2.setVisibility(View.VISIBLE);
        }

        /*t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                e2.setText("");
                dialog = new Dialog(EditOffer.this);
                dialog.setContentView(R.layout.sarch_spinner);
                dialog.getWindow().setLayout(650,800);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


                ListView list_view = dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditOffer.this, android.R.layout.simple_list_item_1,YearMaster);

                list_view.setAdapter(adapter);


                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        t1.setText(adapter.getItem(i));
                        if (t1.getText().toString().trim().equals("Rs"))
                        {
                            t2.setText("Off On");
                            e2.setVisibility(View.GONE);

                        }else  if (t1.getText().toString().trim().equals("%"))
                        {
                            t2.setText("Off Upto");
                            e2.setVisibility(View.VISIBLE);
                        }
                        else {
                            Toast.makeText(EditOffer.this, "Select Type", Toast.LENGTH_SHORT).show();
                        }
                        list_view.setAdapter(adapter);
                        dialog.dismiss();
                    }
                });
            }
        });*/

        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDatePicker();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog progressDialog = new ProgressDialog(EditOffer.this);
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
                    Toast.makeText(EditOffer.this, "Enter Amount Or Percentage", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (SM.isEmpty())
                {
                    Toast.makeText(EditOffer.this, "Select Type", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (SM.equals("%") && OA.isEmpty())
                {
                    Toast.makeText(EditOffer.this, "Enter Off Percentage", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (MPO.isEmpty())
                {
                    Toast.makeText(EditOffer.this, "Enter Minimum Purchase", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if (D.isEmpty())
                {
                    Toast.makeText(EditOffer.this, "Select Date", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else{

                    StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, "http://weatherafdm01.com/GYM/Gym_Update_Offer.php", new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String s1 = "Update successful.";
                            String s2 = "Info Already Exist";

                            if (response.equalsIgnoreCase(s1)) {
                                Toast.makeText(EditOffer.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(EditOffer.this, AllCoupons.class);
                                startActivity(intent1);

                                progressDialog.dismiss();
                            }
                            else if (response.equalsIgnoreCase(s2))
                            {
                                Toast.makeText(EditOffer.this, "Record Already Exist", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(EditOffer.this, "Failed To Add", Toast.LENGTH_SHORT).show();
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
                            params.put("Id", Id);

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(EditOffer.this);
                    requestQueue.add(request);

                }



            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditOffer.this);
                builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                deleteCoupon(Id);

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
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

    private void deleteCoupon (String id) {

        ProgressDialog progressDialog = new ProgressDialog(EditOffer.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/DeleteOffer.php?id="+id+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(EditOffer.this, "Offer Close Successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                startActivity(new Intent(EditOffer.this,AllCoupons.class));
                finishAffinity();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(EditOffer.this, "Something went wrong please try later", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(EditOffer.this);
        requestQueue.add(request);

    }
}