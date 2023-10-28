package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.AllCustomerModel;
import com.example.myapplication.R;

import java.util.List;

public class AllCustomerAdapter extends RecyclerView.Adapter<AllCustomerAdapter.ViewHolder>{
    LayoutInflater inflater;
    List<AllCustomerModel> assignModels;
    Context context;

    public AllCustomerAdapter(List<AllCustomerModel> assignModels, Context context) {
        this.assignModels = assignModels;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void SetFilteredList(List<AllCustomerModel> filteredList){
        this.assignModels = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recview4,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AllCustomerModel temp = assignModels.get(position);

        holder.t1.setText(assignModels.get(position).getCount());
        holder.t2.setText(assignModels.get(position).getName());
        holder.t3.setText(assignModels.get(position).getLvisit());
        holder.Cid_tv.setText(assignModels.get(position).getCId());


    }

    @Override
    public int getItemCount() {
        return assignModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView t1,t2,t3,Cid_tv;
        CardView card;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);;

            t1 = itemView.findViewById(R.id.t1);
            t2 = itemView.findViewById(R.id.t2);
            t3 = itemView.findViewById(R.id.t3);
            Cid_tv = itemView.findViewById(R.id.Cid_tv);
            card = itemView.findViewById(R.id.card);


        }
    }
}
