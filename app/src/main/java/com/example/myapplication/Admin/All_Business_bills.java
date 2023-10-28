package com.example.myapplication.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.B_bill_Adapter;
import com.example.myapplication.Model.AssignModel;
import com.example.myapplication.Model.BBillModel;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class All_Business_bills extends AppCompatActivity {

    String SDate , R1 , R2 , R3 , RC1 , RC2 , RC3 ,serviceCharge;
    RecyclerView recyclerView;

    SearchView searchbill;

    ProgressDialog progressDialog;

    Button sendBill;
    ArrayList<BBillModel> arrayList;
    B_bill_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_business_bills);

        recyclerView = findViewById(R.id.billRVL);
        searchbill = findViewById(R.id.searchbill);
        sendBill = findViewById(R.id.send_billBtn);

        SDate = getIntent().getStringExtra("sDate");
        R1 = getIntent().getStringExtra("R1");
        R2 = getIntent().getStringExtra("R2");
        R3 = getIntent().getStringExtra("R3");
        RC1 = getIntent().getStringExtra("RC1");
        RC2 = getIntent().getStringExtra("RC2");
        RC3 = getIntent().getStringExtra("RC3");
        serviceCharge = getIntent().getStringExtra("SCharge");

        arrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(All_Business_bills.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);

        progressDialog = new ProgressDialog(All_Business_bills.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait..");

        new LoadData().execute();


        searchbill.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(arrayList.size()>0){
                    filterList(query);}
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(arrayList.size()>0){
                filterList(newText);}


                return false;
            }
        });

        findViewById(R.id.send_billBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSendingBill();
            }
        });

    }

    private class InsertDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            if (adapter != null){
                adapter.insertData();

            }else {
                Log.e("ErrorToSendBill","Adapter is null");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            Intent in = new Intent(All_Business_bills.this, Admin_report_bill.class);
            startActivity(in);
            finishAffinity();
        }
    }
    private void startSendingBill() {
        progressDialog.show();
        new InsertDataTask().execute();
    }

    public class LoadData extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    CalculateData();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }

    private void filterList(String Text) {
        ArrayList<BBillModel> filteredlist = new ArrayList<>();

        for (BBillModel sbModel : arrayList) {
            if (sbModel.getBName().toLowerCase().contains(Text.toLowerCase()) || sbModel.getBMob().toLowerCase().contains(Text.toLowerCase()) || sbModel.getBbPeriod().toLowerCase().contains(Text.toLowerCase()) || sbModel.getBTc().toLowerCase().contains(Text.toLowerCase())) {
                filteredlist.add(sbModel);
            }
        }

        adapter.SetFilteredList(filteredlist);
        if (!(adapter.getItemCount()>0)){
            findViewById(R.id.send_billBtn).setVisibility(View.INVISIBLE);
        }else {
            findViewById(R.id.send_billBtn).setVisibility(View.VISIBLE);
        }

    }



    private String CDate(){
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            Date date = inputFormat.parse(SDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM-yyyy", Locale.getDefault());

            String formattedDate = outputFormat.format(date);

            return formattedDate;
        } catch (Exception e) {
            e.printStackTrace();
            return SDate;
        }


    }

    public String getFirstDayOfMonth(String inputDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = dateFormat.parse(inputDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);

            SimpleDateFormat resultDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return resultDateFormat.format(calendar.getTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void CalculateData(){
        ProgressDialog pd = new ProgressDialog(All_Business_bills.this);
        pd.setCancelable(false);
        pd.setMessage("Please wait...");
        pd.show();

        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getBdataForAdmin.php?sDate="+getFirstDayOfMonth(SDate)+"&eDate="+SDate+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i<jsonArray.length(); i++){
                        JSONObject o = jsonArray.getJSONObject(i);

                        String Bname = o.getString("Bname");
                        String Bmob = o.getString("Bmob");
                        String Ccount = o.getString("Ccount");

                        arrayList.add(new BBillModel(Bname,Bmob,CDate(),Ccount,calculateTotalBill(Integer.parseInt(Ccount))));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new B_bill_Adapter(All_Business_bills.this,arrayList);
                            recyclerView.setAdapter(adapter);

                            if (!(adapter.getItemCount()>0)){
                                findViewById(R.id.send_billBtn).setVisibility(View.INVISIBLE);
                            }

                            pd.dismiss();
                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pd.dismiss();
                            findViewById(R.id.send_billBtn).setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                findViewById(R.id.send_billBtn).setVisibility(View.INVISIBLE);
                    }
                });
                Toast.makeText(All_Business_bills.this, "something went wrong", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });


        RequestQueue queue = Volley.newRequestQueue(All_Business_bills.this);
        queue.add(request);
    }

    private String calculateTotalBill(int ccount) {

        String firstRange[] = R1.split("-");
        int r11 = Integer.parseInt(firstRange[0]);
        int r12 = Integer.parseInt(firstRange[1]);

        String secondRange[] = R2.split("-");
        int r21 = Integer.parseInt(secondRange[0]);
        int r22 = Integer.parseInt(secondRange[1]);

        String thirdRange[] = R3.split("-");
        int r31 = Integer.parseInt(thirdRange[0]);
        int r32 = Integer.parseInt(thirdRange[1]);

        double S_charge = Double.parseDouble(serviceCharge);

        if (ccount>=r11 && ccount<=r12){
            double TB = (Double.parseDouble(String.valueOf(ccount)))*(Double.parseDouble(RC1)) ;
            double fTB = TB + S_charge;
            return String.valueOf(fTB);
        }

        if (ccount>=r21 && ccount<=r22){
            double TB = (Double.parseDouble(String.valueOf(ccount)))*(Double.parseDouble(RC2)) ;
            double fTB = TB + S_charge;
            return String.valueOf(fTB);
        }

        if (ccount>=r31 && ccount<=r32){
            double TB = (Double.parseDouble(String.valueOf(ccount)))*(Double.parseDouble(RC3)) ;
            double fTB = TB + S_charge;
            return String.valueOf(fTB);
        }

        return "0.0";
    }
}