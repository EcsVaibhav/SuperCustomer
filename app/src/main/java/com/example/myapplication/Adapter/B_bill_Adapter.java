package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.Admin_report_bill;
import com.example.myapplication.Business.AllCoupons;
import com.example.myapplication.Model.AssignModel;
import com.example.myapplication.Model.BBillModel;
import com.example.myapplication.Model.SendOfferModel;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class B_bill_Adapter extends RecyclerView.Adapter<B_bill_Adapter.Viewholder> {

    Context context;
    ArrayList<BBillModel> arrayList;

    public B_bill_Adapter(Context context, ArrayList<BBillModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }



    @NonNull
    @Override
    public B_bill_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.layoutforbill,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull B_bill_Adapter.Viewholder holder, int position) {

        holder.bName.setText(arrayList.get(position).getBName());
        holder.bMob.setText(arrayList.get(position).getBMob());
        holder.bPeriod.setText(arrayList.get(position).getBbPeriod());
        holder.bTC.setText(arrayList.get(position).getBTc());
        holder.bAmount.setText(arrayList.get(position).getBBAmount()+" Rs");


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        TextView bName,bMob,bPeriod,bTC,bAmount;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            bName = itemView.findViewById(R.id.BnameTV);
            bMob = itemView.findViewById(R.id.BmobTV);

            bPeriod = itemView.findViewById(R.id.BperiodTv);
            bTC = itemView.findViewById(R.id.TCTv);
            bAmount = itemView.findViewById(R.id.BamountTv);


        }
    }

    public void SetFilteredList(ArrayList<BBillModel> filteredList){
        this.arrayList = filteredList;
        notifyDataSetChanged();
    }

    public void insertData() {

        for (int r = 0;r<arrayList.size();r++) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            String url = "http://tsm.ecssofttech.com/Library/GYMapi/sendBillbusiness.php?" +
                    "BName="+arrayList.get(r).getBName()+ "&BMobile="+arrayList.get(r).getBMob()+
                    "&BBillPeriod="+arrayList.get(r).getBbPeriod()+ "&TotalCustomer="+arrayList.get(r).getBTc()+ "&BillAmount="+arrayList.get(r).getBBAmount()+"";

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e("response",response);
                            if (!(response.equals("inserted"))){
                                Toast.makeText(context, "Something went wrong try later..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

            requestQueue.add(request);


        }
    }


}
