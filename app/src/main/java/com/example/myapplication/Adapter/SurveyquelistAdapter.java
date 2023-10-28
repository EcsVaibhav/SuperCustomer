package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.QuelistModel;
import com.example.myapplication.R;

import java.util.ArrayList;

public class SurveyquelistAdapter extends RecyclerView.Adapter<SurveyquelistAdapter.ViewHolder> {

    ArrayList<QuelistModel> arrayList;
    Context context;

    public SurveyquelistAdapter(ArrayList<QuelistModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SurveyquelistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.quelist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SurveyquelistAdapter.ViewHolder holder, int position) {
        holder.et_que.setText("Que "+String.valueOf(position+1)+" : "+arrayList.get(position).getQue());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView et_que;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            et_que = itemView.findViewById(R.id.et_que);
        }
    }
}
