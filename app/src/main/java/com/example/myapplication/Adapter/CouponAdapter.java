package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Business.AllCoupons;
import com.example.myapplication.Model.CouponModel;
import com.example.myapplication.Business.EditOffer;
import com.example.myapplication.R;
import com.example.myapplication.Business.SendOffer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder>{
    LayoutInflater inflater;
    List<CouponModel> findModels;
    Context context;

    public CouponAdapter(List<CouponModel> findModels, Context context) {
        this.findModels = findModels;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.batmya2,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        CouponModel temp = findModels.get(position);

        holder.t1.setText("Offer");
        if (findModels.get(position).getOA().equals("")) {

            String o1 = findModels.get(position).getAOP() + findModels.get(position).getSM() + " " + findModels.get(position).getO();
            String o2 = "On" + findModels.get(position).getOA() + " " + findModels.get(position).getMPO() + " " + findModels.get(position).getAPO() + "Rs";
            String o3 = findModels.get(position).getVT() + " " + findModels.get(position).getD();

            StyleSpan sp = new StyleSpan(Typeface.BOLD);
            StyleSpan sp1 = new StyleSpan(Typeface.ITALIC);
            RelativeSizeSpan textSizeSpan = new RelativeSizeSpan(0.9f);
            RelativeSizeSpan textSizeSpan1 = new RelativeSizeSpan(0.8f);

            SpannableStringBuilder combinedText = new SpannableStringBuilder();
            SpannableString ss1 = new SpannableString(o1);
            SpannableString ss2 = new SpannableString(o2);
            SpannableString ss3 = new SpannableString(o3);

            ss1.setSpan(sp, 0, ss1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss2.setSpan(textSizeSpan, 0, ss2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss3.setSpan(textSizeSpan1, 0, ss3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss3.setSpan(sp1, 0, ss3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            combinedText.append(ss1);
            combinedText.append("\n");
            combinedText.append(ss2);
            combinedText.append("\n");
            combinedText.append(ss3);

            holder.t2.setText(combinedText);

        }else {

            String o1 = findModels.get(position).getAOP() + findModels.get(position).getSM() + " " + findModels.get(position).getO()+" "+findModels.get(position).getOA()+"Rs" ;
            String o2 = "On " + findModels.get(position).getMPO() + " " + findModels.get(position).getAPO() + "Rs";
            String o3 = findModels.get(position).getVT() + " " + findModels.get(position).getD();

            StyleSpan sp = new StyleSpan(Typeface.BOLD);
            StyleSpan sp1 = new StyleSpan(Typeface.ITALIC);
            RelativeSizeSpan textSizeSpan = new RelativeSizeSpan(0.9f);
            RelativeSizeSpan textSizeSpan1 = new RelativeSizeSpan(0.8f);

            SpannableStringBuilder combinedText = new SpannableStringBuilder();
            SpannableString ss1 = new SpannableString(o1);
            SpannableString ss2 = new SpannableString(o2);
            SpannableString ss3 = new SpannableString(o3);

            ss1.setSpan(sp, 0, ss1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss2.setSpan(textSizeSpan, 0, ss2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss3.setSpan(textSizeSpan1, 0, ss3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss3.setSpan(sp1, 0, ss3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            combinedText.append(ss1);
            combinedText.append("\n");
            combinedText.append(ss2);
            combinedText.append("\n");
            combinedText.append(ss3);

            holder.t2.setText(combinedText);
        }


        holder.t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,EditOffer.class);
                intent.putExtra("data",findModels.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.sendPB.setVisibility(View.VISIBLE);
                holder.send.setVisibility(View.INVISIBLE);

                Intent intent = new Intent(context,SendOffer.class);
                intent.putExtra("data",findModels.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(v.getRootView().getContext());
                builder.setCancelable(false);
                builder.setMessage("Are you sure you want to close this offer");
                builder.setTitle("Confirmation !");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCoupon(String.valueOf(findModels.get(position).getId()));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });
    }

    private void deleteCoupon (String id) {


        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/DeleteOffer.php?id="+id+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Offer Close Successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context,AllCoupons.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something went wrong please try later", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

    }

    @Override
    public int getItemCount() {
        return findModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView t1,t2,t3;
        CardView card;
        ProgressBar sendPB;
        Button send,close;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            t1 = itemView.findViewById(R.id.textView);
            t2 = itemView.findViewById(R.id.textView4);
            t3 = itemView.findViewById(R.id.t1);
            send = itemView.findViewById(R.id.send);
            close = itemView.findViewById(R.id.close);
            sendPB = itemView.findViewById(R.id.sendPB);

        }
    }


}
