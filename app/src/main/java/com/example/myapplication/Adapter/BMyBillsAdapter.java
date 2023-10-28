package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.BBillModel;
import com.example.myapplication.R;

import java.util.ArrayList;

public class BMyBillsAdapter extends RecyclerView.Adapter<BMyBillsAdapter.Viewholder> {
    Context context;

    ArrayList<BBillModel> arrayList;

    public BMyBillsAdapter(Context context, ArrayList<BBillModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public BMyBillsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.layoutfor_businessbill,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BMyBillsAdapter.Viewholder holder, int position) {


        holder.bPeriod.setText(arrayList.get(position).getBbPeriod());
        holder.bTC.setText(arrayList.get(position).getBTc());
        holder.bAmount.setText(arrayList.get(position).getBBAmount()+" Rs");

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView bPeriod,bTC,bAmount;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            bPeriod = itemView.findViewById(R.id.BperiodTv);
            bTC = itemView.findViewById(R.id.TCTv);
            bAmount = itemView.findViewById(R.id.BamountTv);
        }
    }
}
