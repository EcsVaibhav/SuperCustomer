package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.Business.BDash;
import com.example.myapplication.Model.AssignModel;
import com.example.myapplication.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AssignAdapter extends RecyclerView.Adapter<AssignAdapter.ViewHolder>{
    LayoutInflater inflater;
    List<AssignModel> assignModels;
    Context context;

    public AssignAdapter(List<AssignModel> assignModels, Context context) {
        this.assignModels = assignModels;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void SetFilteredList(List<AssignModel> filteredList){
        this.assignModels = filteredList;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recview3,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AssignModel temp = assignModels.get(position);

        holder.t1.setText(assignModels.get(position).getBusinessName());
        holder.t2.setText(assignModels.get(position).getBusinessAddress());
        holder.t3.setText(assignModels.get(position).getBusinessMobile1());
        holder.t4.setText(assignModels.get(position).getBusinessMobile2());
        holder.t5.setText(assignModels.get(position).getStatus());
        holder.t6.setText(assignModels.get(position).getRole());

        String Qr = assignModels.get(position).getBusinessName() +","+assignModels.get(position).getBusinessMobile1();

        MultiFormatWriter mWriter = new MultiFormatWriter();

        try {
            //BitMatrix class to encode entered text and set Width & Height
            BitMatrix mMatrix = mWriter.encode(Qr, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
            holder.m_imagemp.setImageBitmap(mBitmap);//Setting generated QR code to imageView
            // to hide the keyboard
            InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);


        } catch (WriterException e) {
            e.printStackTrace();
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = holder.m_imagemp.getDrawable();
                Bitmap image = ((BitmapDrawable) drawable).getBitmap();

                Bitmap image1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.spdfformat);

                createAndSharePDF(image1,image,assignModels.get(position).getBusinessName()+".pdf");

            }
        });

    }

    private void createAndSharePDF( Bitmap image1,Bitmap image,String pdfName) {
        // Create a new PdfDocument
        try {
            PdfDocument pdfDocument = new PdfDocument();

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080, 1920, 1).create(); // A4 page size

            // Start a new page
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            // Create a canvas for drawing
            Canvas canvas = page.getCanvas();

            // Create a paint object for styling
            Paint paint = new Paint();
            Bitmap scalbmp = Bitmap.createScaledBitmap(image1,900,1700,false);

            canvas.drawBitmap(scalbmp,50,50,paint);

            canvas.drawBitmap(image,250,580,paint);

            // Finish the page
            pdfDocument.finishPage(page);

            // Create the PDF file
            File pdfFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), pdfName);

            try {
                pdfDocument.writeTo(new FileOutputStream(pdfFile));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Close the document
            pdfDocument.close();

            // Share the PDF
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", pdfFile);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

            // Grant read permissions to the receiving app
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            context.startActivity(Intent.createChooser(shareIntent, "Share PDF"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void createAndSharePDF( Bitmap image1,Bitmap image,String pdfName) {
        // Create a new PdfDocument
        PdfDocument pdfDocument = new PdfDocument();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.qrtemplate, null);
        // Create a PageInfo
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080, 1920, 1).create(); // A4 page size

        // Start a new page
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // Create a canvas for drawing
        Canvas canvas = page.getCanvas();


        view.measure(View.MeasureSpec.makeMeasureSpec(900, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(1700, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());


        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvasImg = new Canvas(bitmap);
        view.draw(canvasImg);

        // Create a paint object for styling
        Paint paint = new Paint();

        canvas.drawBitmap(bitmap,50,50,paint);

        //canvas.drawBitmap(image,250,580,paint);

        // Finish the page
        pdfDocument.finishPage(page);

        // Create the PDF file
        File pdfFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), pdfName);

        try {
            pdfDocument.writeTo(new FileOutputStream(pdfFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the document
        pdfDocument.close();

        // Share the PDF
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", pdfFile);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        // Grant read permissions to the receiving app
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(Intent.createChooser(shareIntent, "Share PDF"));
    }*/

    @Override
    public int getItemCount() {
        return assignModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView t1,t2,t3,t4,t5,t6;
        CardView card;
        ImageView m_imagemp;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);;

            t1 = itemView.findViewById(R.id.t1);
            t2 = itemView.findViewById(R.id.t2);
            t3 = itemView.findViewById(R.id.t3);
            t4 = itemView.findViewById(R.id.t4);
            t5 = itemView.findViewById(R.id.t5);
            t6 = itemView.findViewById(R.id.t6);
            button = itemView.findViewById(R.id.button);
            m_imagemp = itemView.findViewById(R.id.m_imagemp);

            card = itemView.findViewById(R.id.card);


        }
    }
}
