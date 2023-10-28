package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.ViewSurveyModel;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ViewSurveyAdapter extends RecyclerView.Adapter<ViewSurveyAdapter.Viewholder> {

    ArrayList<ViewSurveyModel> arrayList ;
    Context context;

    public ViewSurveyAdapter(ArrayList<ViewSurveyModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewSurveyAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.layout_for_viewsurveyquelist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewSurveyAdapter.Viewholder holder, int position) {

        holder.Que.setText("Question "+String.valueOf(position + 1)+": "+arrayList.get(position).getQue());
        holder.Y.setText(arrayList.get(position).getYP());
        holder.N.setText(arrayList.get(position).getNP());
        holder.Maybe.setText(arrayList.get(position).getMaybeP());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        TextView Que,Y,N,Maybe;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Que = itemView.findViewById(R.id.Que);
            Y = itemView.findViewById(R.id.Y);
            N = itemView.findViewById(R.id.N);
            Maybe = itemView.findViewById(R.id.Maybe);
        }
    }
}
