package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Business.EditOffer;
import com.example.myapplication.Model.AllCustomerModel;
import com.example.myapplication.R;
import com.example.myapplication.Business.SendOffer;
import com.example.myapplication.Model.UserOffersModel;
import com.example.myapplication.User.UserQrScan;

import java.util.List;

public class UserOffersAdapter extends RecyclerView.Adapter<UserOffersAdapter.ViewHolder>{
    LayoutInflater inflater;
    List<UserOffersModel> findModels;
    Context context;


    public UserOffersAdapter(List<UserOffersModel> findModels, Context context) {
        this.findModels = findModels;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void SetFilteredList(List<UserOffersModel> filteredList){
        this.findModels = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.batmya,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserOffersModel temp = findModels.get(position);

        holder.Bname.setText(temp.getName());

        holder.Offer.setText(temp.getOffer_1Line()+"\n"+temp.getOffer_2Line());

        SpannableStringBuilder combinedText = new SpannableStringBuilder();
        SpannableString ss = new SpannableString(temp.getOffer_1Line());
        SpannableString ss1 = new SpannableString(temp.getOffer_2Line());
        SpannableString ss2 = new SpannableString(temp.getOffer_3Line());
        StyleSpan sp = new StyleSpan(Typeface.BOLD);
        StyleSpan sp1 = new StyleSpan(Typeface.ITALIC);
        RelativeSizeSpan textSizeSpan = new RelativeSizeSpan(0.95f);
        RelativeSizeSpan textSizeSpan1 = new RelativeSizeSpan(0.9f);

        ss.setSpan(sp, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss1.setSpan(textSizeSpan,0,ss1.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(sp1,0,ss2.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(textSizeSpan1,0,ss2.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        combinedText.append(ss);
        combinedText.append("\n");
        combinedText.append(ss1);
        combinedText.append("\n");
        combinedText.append("\n");
        combinedText.append(ss2);

        holder.Offer.setText(combinedText);
        holder.TCtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowOkDialog(v);
            }
        });


    }
    private void ShowOkDialog(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
        builder.setCancelable(false);
        builder.setTitle("Terms and Conditions");
        builder.setMessage("1. The offer or coupon is provided by the mentioned outlet on coupon or offer.\n" +
                "2. The offer or coupon can not be clubbed together with any other offer or coupon.\n" +
                "3. The offer provider outlet  is entitled unconditionally to modify, accept or reject any offer,coupon without any prior notification.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return findModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView Bname,Offer,TCtv;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);;

            Bname = itemView.findViewById(R.id.Bname);
            Offer = itemView.findViewById(R.id.offer);
            TCtv = itemView.findViewById(R.id.TCtv);



        }
    }
}
