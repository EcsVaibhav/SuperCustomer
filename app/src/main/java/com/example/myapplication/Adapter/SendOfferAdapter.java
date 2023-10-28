package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Business.AllCoupons;
import com.example.myapplication.R;
import com.example.myapplication.Model.SendOfferModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SendOfferAdapter extends RecyclerView.Adapter<SendOfferAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<SendOfferModel> findModels;
    List<SendOfferModel> filteredList;
    Context context;
    String AOP, SM, O, OA, MPO, APO, VT, D,Id;
    private boolean isAllSelected = false;

    public SendOfferAdapter(List<SendOfferModel> findModels, Context context, String AOP, String SM, String O, String OA, String MPO, String APO, String VT, String D, String Id) {
        this.findModels = findModels;
        this.filteredList = new ArrayList<>(findModels);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.AOP = AOP;
        this.SM = SM;
        this.O = O;
        this.OA = OA;
        this.MPO = MPO;
        this.APO = APO;
        this.VT = VT;
        this.D = D;
        this.Id = Id;
    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.userdesign, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SendOfferModel temp = filteredList.get(position);
        String Umobile = temp.getMobile();

        String maskedMobile = "";
        if (Umobile.length() >= 2) {
            maskedMobile = Umobile.substring(0, 2) + "xxxxxx" + Umobile.substring(Umobile.length() - 2);
        } else {
            maskedMobile = Umobile;
        }

        holder.t1.setText(temp.getCOUNT());
        holder.t2.setText(maskedMobile);
    }


    public void setFilteredList(List<SendOfferModel> filteredList) {
        this.filteredList.clear();
        this.filteredList.addAll(filteredList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView t1, t2, t3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.t1);
            t2 = itemView.findViewById(R.id.t2);
        }


    }

    public void saveSelectedItemsToDatabase() {
        // Create a JSON array to hold the selected items
        JSONArray selectedItems = new JSONArray();

        for (SendOfferModel model : filteredList) {

            JSONObject item = new JSONObject();
            try {
                item.put("count", model.getCOUNT());
                item.put("name", model.getName());
                item.put("mobile", model.getMobile());
                item.put("username", model.getUsername());
                item.put("AOP", AOP);
                item.put("SM", SM);
                item.put("O", O);
                item.put("OA", OA);
                item.put("D", D);
                item.put("MPO", MPO);
                item.put("APO", APO);
                item.put("VT", VT);
                item.put("Id", Id);

                selectedItems.put(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Create a JSON object to hold the request body
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("selectedItems", selectedItems);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Instantiate the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Define the URL of your web service/API
        String url = "http://tsm.ecssofttech.com/Library/api/Gym_Save.php";

        // Create a request object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response from the web service/API
                        try {
                            if (response.getString("message").equals("Coupon Send successfully")){
                                Toast.makeText(context, "Coupon Send successfully", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(context, AllCoupons.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.getApplicationContext().startActivity(in);

                            }else {
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
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }
}
