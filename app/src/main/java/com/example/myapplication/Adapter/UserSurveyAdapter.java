package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Model.AllSurveyBmodel;
import com.example.myapplication.R;
import com.example.myapplication.User.USubmitSurvey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserSurveyAdapter extends RecyclerView.Adapter<UserSurveyAdapter.Viewholder> {
    
    ArrayList<AllSurveyBmodel> surveylist;
    Context context;

    String DCoff ;
    String DCminPur ;
    String DCtype ;
    String DCupto ;
    String BusinessName ;

    public UserSurveyAdapter(ArrayList<AllSurveyBmodel> surveylist, Context context) {
        this.surveylist = surveylist;
        this.context = context;
    }

    @NonNull
    @Override
    public UserSurveyAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_survey_blist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserSurveyAdapter.Viewholder holder, int position) {

        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getDefaultCoupan.php?mobileno=" + surveylist.get(position).getBusername() + "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONArray contacts = new JSONArray(response);


                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        DCoff = c.getString("DCoff");
                        DCminPur = c.getString("DCminPur");
                        DCtype = c.getString("DCtype");
                        DCupto = c.getString("DCupto");
                        BusinessName = c.getString("BusinessName");

                        holder.surveyName.setText(BusinessName);
                        if (DCtype.equals("Rs")) {
                            holder.surveyDT.setText(DCoff + DCtype + " off on Minimum Purchase of " + DCminPur + "Rs\n");
                        } else {
                            holder.surveyDT.setText(DCoff + DCtype + " off Upto " + DCupto + "Rs On Minimum Purchase of " + DCminPur + "Rs\n");
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

        holder.viewSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in  = new Intent(context, USubmitSurvey.class);
                in.putExtra("SData",surveylist.get(position));
                context.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return surveylist.size();
    }



    public class Viewholder extends RecyclerView.ViewHolder{

        CardView viewSurveyBtn;
        TextView surveyName,surveyDT;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            viewSurveyBtn = itemView.findViewById(R.id.viewSurveyBtn);
            surveyName = itemView.findViewById(R.id.surveyName);
            surveyDT = itemView.findViewById(R.id.surveyDT);
        }
    }
}
