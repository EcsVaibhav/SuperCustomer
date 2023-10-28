package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.AdminRmodel;
import com.example.myapplication.R;

import java.util.ArrayList;

public class AdminreportAdapter extends RecyclerView.Adapter<AdminreportAdapter.Viewholder> {

    Context context;

    ArrayList<AdminRmodel> arrayList;



    public AdminreportAdapter(Context context, ArrayList<AdminRmodel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AdminreportAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.adminreportlayout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminreportAdapter.Viewholder holder, int position) {

        if (position==0){
            int color = ContextCompat.getColor(context, R.color.fixcolor);
            holder.childARrvl.setBackgroundColor(color);
            holder.ABnameTV.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.JanData.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.FebData.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.MarData.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.AprData.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.MayData.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.JunData.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.JulData.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.AugData.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.SepData.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.OctData.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.NovData.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.DecData.setTextColor(ContextCompat.getColor(context,R.color.white));

        }
        holder.ABnameTV.setText(arrayList.get(position).getBName());
        holder.JanData.setText(arrayList.get(position).getJanData());
        holder.FebData.setText(arrayList.get(position).getFebData());
        holder.MarData.setText(arrayList.get(position).getAprData());
        holder.AprData.setText(arrayList.get(position).getMarData());
        holder.MayData.setText(arrayList.get(position).getMayData());
        holder.JunData.setText(arrayList.get(position).getJunData());
        holder.JulData.setText(arrayList.get(position).getJulData());
        holder.AugData.setText(arrayList.get(position).getAugData());
        holder.SepData.setText(arrayList.get(position).getSepData());
        holder.OctData.setText(arrayList.get(position).getOctData());
        holder.NovData.setText(arrayList.get(position).getNovData());
        holder.DecData.setText(arrayList.get(position).getDecData());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        TextView ABnameTV;
        RelativeLayout childARrvl;
        TextView JanData,FebData,MarData,AprData,MayData,JunData,JulData,AugData,SepData,OctData,NovData,DecData;
        public Viewholder(@NonNull View itemView) {
            super(itemView);


            childARrvl = itemView.findViewById(R.id.childARrvl);
            ABnameTV = itemView.findViewById(R.id.ABnameTV);
            JanData = itemView.findViewById(R.id.JanData);
            FebData = itemView.findViewById(R.id.FebData);
            MarData = itemView.findViewById(R.id.MarData);
            AprData = itemView.findViewById(R.id.AprData);
            MayData = itemView.findViewById(R.id.MayData);
            JunData = itemView.findViewById(R.id.JunData);
            JulData = itemView.findViewById(R.id.JulData);
            AugData = itemView.findViewById(R.id.AugData);
            SepData = itemView.findViewById(R.id.SepData);
            OctData = itemView.findViewById(R.id.OctData);
            NovData = itemView.findViewById(R.id.NovData);
            DecData = itemView.findViewById(R.id.DecData);
        }
    }
}
