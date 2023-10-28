package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.UrewardModel;
import com.example.myapplication.R;

import java.util.ArrayList;

public class UrewardAdapter extends RecyclerView.Adapter<UrewardAdapter.Viewholder> {

    Context context;
    ArrayList<UrewardModel> arrayList;

    public UrewardAdapter(Context context, ArrayList<UrewardModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public UrewardAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.layout_for_reward,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UrewardAdapter.Viewholder holder, int position) {

        String Tpoints = arrayList.get(position).getTsp();
        String minRedeem = arrayList.get(position).getSetP();
        String B_n = arrayList.get(position).getBName();

        int currentPoints = (int) ((Double.parseDouble(Tpoints)/Double.parseDouble(minRedeem))*100);

        int maxvalue = Integer.parseInt(minRedeem);


        holder.nameB.setText(B_n);
        holder.progressBar.setProgress(currentPoints);
        holder.progressBar.setMax(100);
        holder.progress_tv.setText(Integer.parseInt(Tpoints)+"/"+maxvalue);
        if (Integer.parseInt(Tpoints)>maxvalue){
            holder.btnRL.setVisibility(View.VISIBLE);
            holder.barRL.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView nameB,progress_tv;

        RelativeLayout barRL,btnRL;
        ProgressBar progressBar;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            nameB = itemView.findViewById(R.id.nameB);
            progress_tv = itemView.findViewById(R.id.ProgessTV);
            barRL = itemView.findViewById(R.id.barRL);
            btnRL = itemView.findViewById(R.id.btnRL);

        }
    }
}
