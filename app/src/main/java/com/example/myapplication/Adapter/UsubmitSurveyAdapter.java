package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Business.AllCoupons;
import com.example.myapplication.Model.USubmitSurveyModel;
import com.example.myapplication.R;
import com.example.myapplication.User.Congratulation_forCoupan;
import com.example.myapplication.User.UserNotification;
import com.example.myapplication.User.UserOffers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UsubmitSurveyAdapter extends RecyclerView.Adapter<UsubmitSurveyAdapter.Viewholder> {

    ArrayList<USubmitSurveyModel> arrayList;

    String Q1ans = "NA", Q2ans = "NA", Q3ans = "NA", Q4ans = "NA", Q5ans = "NA";

    String AOP, SM, O, OA,  APO, D, Id,BusinessName;
    String MPO = "Minimum Purchase Of";
    String VT = "Valid Till";
    String surveyID,UMobNo,BMobNo,scanCount;
    Context context;

    ProgressDialog progressDialog;

    public UsubmitSurveyAdapter(ArrayList<USubmitSurveyModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public UsubmitSurveyAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.layout_for_usersubmitsurvey, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UsubmitSurveyAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {


        holder.SS_que.setText("Que " + String.valueOf(position + 1) + ": " + arrayList.get(position).getQue());

        holder.radioGroupQueNo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = holder.itemView.findViewById(checkedId);
                String queno = arrayList.get(position).getQueID();
                if (queno.equals("1")) {
                    Q1ans = radioButton.getText().toString().trim();

                }
                if (queno.equals("2")) {
                    Q2ans = radioButton.getText().toString().trim();

                }
                if (queno.equals("3")) {
                    Q3ans = radioButton.getText().toString().trim();

                }
                if (queno.equals("4")) {
                    Q4ans = radioButton.getText().toString().trim();

                }
                if (queno.equals("5")) {
                    Q5ans = radioButton.getText().toString().trim();

                }


            }
        });

        surveyID = arrayList.get(position).getSurveyID();
        UMobNo = arrayList.get(position).getUMobNo();
        BMobNo = arrayList.get(position).getBussinessNO();

    }



    public void submitSurvey() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        for (int i = 0; i < arrayList.size(); i++) {

            String a = arrayList.get(i).getQueID();

            if (a.equals("1") && Q1ans.equals("NA")) {
                Q1ans = "e";
            }
            if (a.equals("2") && Q2ans.equals("NA")) {
                Q2ans = "e";
            }
            if (a.equals("3") && Q3ans.equals("NA")) {
                Q3ans = "e";
            }
            if (a.equals("4") && Q4ans.equals("NA")) {
                Q4ans = "e";
            }
            if (a.equals("5") && Q5ans.equals("NA")) {
                Q5ans = "e";
            }

        }

        if (Q1ans.equals("e") || Q2ans.equals("e") || Q3ans.equals("e") || Q4ans.equals("e") || Q5ans.equals("e")) {
            Toast.makeText(context, "Please Answer all Questions", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {

            /*
            Toast.makeText(context, Q1ans + "\n" + Q2ans + "\n" + Q3ans + "\n" + Q4ans + "\n" + Q5ans+"\n"+surveyID+"\n"+UMobNo+"\n"+BMobNo, Toast.LENGTH_SHORT).show();
*/

            StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/SurveySubmit.php?Sid="+surveyID+"&UserMob="+UMobNo+"&BusinessMob="+BMobNo+"&Que1Ans="+Q1ans+"&Que2Ans="+Q2ans+"&Que3Ans="+Q3ans+"&Que4Ans="+Q4ans+"&Que5Ans="+Q5ans+"", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.equals("inserted")){

                        //Toast.makeText(context, "Thank you for submitting the survey!", Toast.LENGTH_SHORT).show();
                        getScanCount();
                    }else {
                        Toast.makeText(context, "Something went wrong try later....", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Something went wrong try again..", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);

        }
    }

    private void addOffer() {


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date seventhDayDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        D = sdf.format(seventhDayDate);

        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getDefaultCoupan.php?mobileno=" +BMobNo + "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONArray contacts = new JSONArray(response);


                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        AOP = c.getString("DCoff");
                        APO = c.getString("DCminPur");
                        SM = c.getString("DCtype");

                        if (c.getString("DCupto").equals("0")){
                            OA = "";
                        }else {
                            OA = c.getString("DCupto");
                        }

                        BusinessName = c.getString("BusinessName");
                        Id = surveyID;

                        if (SM.equals("Rs")) {
                            O = "Off";
                        } else {
                            O = "Off Upto";
                        }


                        submitOffer(BusinessName,UMobNo,BMobNo,AOP,SM,O,OA,D,MPO,APO,VT,Id);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void getScanCount() {

        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getUserscanCount.php?Username="+UMobNo+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray contacts = new JSONArray(response);


                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        scanCount = c.getString("c");

                    }
                    addOffer();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void submitOffer(String businessName, String uMobNo, String bMobNo, String aop, String sm, String o, String oa, String d, String mpo, String apo, String vt, String id) {


        try {
            JSONArray selectedItems = new JSONArray();
            JSONObject item = new JSONObject();
            item.put("count", scanCount);
            item.put("name", businessName);
            item.put("mobile", uMobNo);
            item.put("username",bMobNo);
            item.put("AOP", aop);
            item.put("SM", sm);
            item.put("O", o);
            item.put("OA", oa);
            item.put("D", d);
            item.put("MPO", mpo);
            item.put("APO", apo);
            item.put("VT", vt);
            item.put("Id", id);

            selectedItems.put(item);


            JSONObject requestBody = new JSONObject();
            requestBody.put("selectedItems", selectedItems);
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "http://tsm.ecssofttech.com/Library/api/Gym_Save.php";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Handle the response from the web service/API
                            try {
                                if (response.getString("message").equals("Coupon Send successfully")){
                                    Toast.makeText(context, "Coupon Unlock successfully", Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(context, Congratulation_forCoupan.class);
                                    in.putExtra("Bmobile",BMobNo);
                                    in.putExtra("Umobile",UMobNo);
                                    in.putExtra("sid",surveyID);
                                    in.putExtra("offer", selectedItems.toString());
                                    context.startActivity(in);
                                    progressDialog.dismiss();

                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                                }

                                // Display a success message or perform any additional actions
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle the error occurred during the request
                            error.printStackTrace();
                            progressDialog.dismiss();
                        }
                    });

            // Add the request to the RequestQueue
            requestQueue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {

        TextView SS_que;
        RadioGroup radioGroupQueNo;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            SS_que = itemView.findViewById(R.id.SS_que);
            radioGroupQueNo = itemView.findViewById(R.id.radioGroupQueNo);
        }
    }
}
