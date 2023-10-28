package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.ModelFOallsurvey;
import com.example.myapplication.R;
import com.example.myapplication.Business.ViewSurveyResult;

import java.util.ArrayList;

public class AllSurveyAdapter extends RecyclerView.Adapter<AllSurveyAdapter.Viewholder> {

    Context context;
    ArrayList<ModelFOallsurvey> bmodelArrayList;


    public AllSurveyAdapter(Context context, ArrayList<ModelFOallsurvey> bmodelArrayList) {
        this.context = context;
        this.bmodelArrayList = bmodelArrayList;
    }

    @NonNull
    @Override
    public AllSurveyAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_survey_blist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllSurveyAdapter.Viewholder holder, int position) {

        String stDate = bmodelArrayList.get(position).getDateTime();
        String expDate = bmodelArrayList.get(position).getExpDate();
        holder.surveyName.setText(bmodelArrayList.get(position).getTitle());
        holder.surveyDT.setText("Survey Period :\n"+stDate+" - "+expDate);
        holder.attendedCountTV.setText("Visited : "+bmodelArrayList.get(position).getAttendCount());
        holder.viewSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(context, ViewSurveyResult.class);
                in.putExtra("SData",bmodelArrayList.get(position));
                context.startActivity(in);

            }
        });

    }

    @Override
    public int getItemCount() {
        return bmodelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        Button viewSurveyBtn;
        TextView surveyName,surveyDT,attendedCountTV;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            viewSurveyBtn = itemView.findViewById(R.id.viewSurveyBtn);
            surveyName = itemView.findViewById(R.id.surveyName);
            surveyDT = itemView.findViewById(R.id.surveyDT);
            attendedCountTV = itemView.findViewById(R.id.attendedCountTV);
        }
    }
}
