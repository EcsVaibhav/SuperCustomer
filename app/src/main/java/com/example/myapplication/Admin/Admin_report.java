package com.example.myapplication.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.ARchildAdapter;
import com.example.myapplication.Adapter.AdminreportAdapter;
import com.example.myapplication.Business.BDash;
import com.example.myapplication.Business.BDashboard;
import com.example.myapplication.Model.AdminRmodel;
import com.example.myapplication.Model.AdminreportDatamodel;
import com.example.myapplication.R;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Admin_report extends AppCompatActivity {


    String selectedYear;
    private static final String JSON_URL = "YOUR_JSON_URL";
    private RequestQueue requestQueue;
    String R1, R2, R3, RC1, RC2, RC3,SCharge;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    Spinner ARyearTV;
    ArrayList<AdminRmodel> arrayList;
    ArrayList<AdminRmodel> arrayList2;
    AdminreportAdapter adapter;
    ARchildAdapter adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report);

        ARyearTV = findViewById(R.id.ARyearTV);

        recyclerView = findViewById(R.id.ARrvl);
        recyclerView2 = findViewById(R.id.ARrvl2);

        R1 = getIntent().getStringExtra("R1");
        R2 = getIntent().getStringExtra("R2");
        R3 = getIntent().getStringExtra("R3");
        RC1 = getIntent().getStringExtra("RC1");
        RC2 = getIntent().getStringExtra("RC2");
        RC3 = getIntent().getStringExtra("RC3");
        SCharge = getIntent().getStringExtra("SCharge");


        showYear();


        arrayList = new ArrayList<>();
        arrayList2 = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(Admin_report.this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(Admin_report.this));






        findViewById(R.id.CreatePdf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Admin_report.this, BDash.class));
                String fileName = "AdminReport"+selectedYear+".xls";
                createExcelFile(fileName,arrayList,arrayList2);
            }
        });



    }

    private void createExcelFile(String fileName, ArrayList<AdminRmodel> dataList,ArrayList<AdminRmodel> dataList2) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Active Customers");

        // Create the header row
        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Active Customers");



        // Populate data rows
        for (int rowIndex = 2; rowIndex <= dataList.size() + 1; rowIndex++) {
            HSSFRow dataRow = sheet.createRow(rowIndex - 1); // Offset by 1 to account for the gap
            AdminRmodel adminModel = dataList.get(rowIndex - 2);

            dataRow.createCell(0).setCellValue(adminModel.getBName());
            dataRow.createCell(1).setCellValue(adminModel.getJanData());
            dataRow.createCell(2).setCellValue(adminModel.getFebData());
            dataRow.createCell(3).setCellValue(adminModel.getMarData());
            dataRow.createCell(4).setCellValue(adminModel.getAprData());
            dataRow.createCell(5).setCellValue(adminModel.getMayData());
            dataRow.createCell(6).setCellValue(adminModel.getJunData());
            dataRow.createCell(7).setCellValue(adminModel.getJulData());
            dataRow.createCell(8).setCellValue(adminModel.getAugData());
            dataRow.createCell(9).setCellValue(adminModel.getSepData());
            dataRow.createCell(10).setCellValue(adminModel.getOctData());
            dataRow.createCell(11).setCellValue(adminModel.getNovData());
            dataRow.createCell(12).setCellValue(adminModel.getDecData());
        }

        HSSFSheet sheet2 = workbook.createSheet("InActive Customers");

        // Create the header row
        HSSFRow headerRow2 = sheet2.createRow(0);
        HSSFCell headerCell2 = headerRow2.createCell(0);
        headerCell2.setCellValue("InActive Customers");



        // Populate data rows
        for (int rowIndex = 1; rowIndex <= dataList2.size(); rowIndex++) {
            HSSFRow dataRow = sheet2.createRow(rowIndex);
            AdminRmodel adminModel = dataList2.get(rowIndex - 1);

            dataRow.createCell(0).setCellValue(adminModel.getBName());


            dataRow.createCell(1).setCellValue(adminModel.getJanData());
            dataRow.createCell(2).setCellValue(adminModel.getFebData());
            dataRow.createCell(3).setCellValue(adminModel.getMarData());
            dataRow.createCell(4).setCellValue(adminModel.getAprData());
            dataRow.createCell(5).setCellValue(adminModel.getMayData());
            dataRow.createCell(6).setCellValue(adminModel.getJunData());
            dataRow.createCell(7).setCellValue(adminModel.getJulData());
            dataRow.createCell(8).setCellValue(adminModel.getAugData());
            dataRow.createCell(9).setCellValue(adminModel.getSepData());
            dataRow.createCell(10).setCellValue(adminModel.getOctData());
            dataRow.createCell(11).setCellValue(adminModel.getNovData());
            dataRow.createCell(12).setCellValue(adminModel.getDecData());
        }

        // Save the workbook to a file
        File filePath = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
        try {
            if (!filePath.exists()) {
                filePath.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



        shareExcelFile(fileName);
        /*downloadExcelFile(fileName);*/

    }

    private void shareExcelFile(String fileName) {
        File excelFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);

        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", excelFile);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        // Grant read permissions to the receiving app
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, "Share Excel File"));
    }

    private void downloadExcelFile(String fileName) {
        File excelFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);

        // Create a Uri for the file
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", excelFile);

        // Create an intent to send the file to the download folder
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private AdminreportDatamodel parseBusinessFromJson(JSONObject json) throws JSONException {
        AdminreportDatamodel info = new AdminreportDatamodel();
        info.setBussinessName(json.getString("BussinessName"));
        info.setBussinessMob(json.getString("BussinessMob"));
        info.setStatus(json.getString("status"));

        JSONObject monthTotalCounts = json.getJSONObject("monthTotalCounts");
        Map<String, Integer> monthCounts = new HashMap<>();
        for (Iterator<String> it = monthTotalCounts.keys(); it.hasNext(); ) {
            String key = it.next();
            int value = monthTotalCounts.getInt(key);
            monthCounts.put(key, value);
        }

        info.setMonthTotalCounts(monthCounts);

        return info;
    }

    private void getinfo(String selectedYear){

        ProgressDialog pd = new ProgressDialog(Admin_report.this);
        pd.setCancelable(false);
        pd.setMessage("Please wait..");
        pd.show();

        arrayList.add(new AdminRmodel("Business Name", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
        arrayList2.add(new AdminRmodel("Business Name", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "http://tsm.ecssofttech.com/Library/GYMapi/getAdminyearreport.php?year="+selectedYear+"";

        // Request a JSON response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    // Parse the JSON response.
                    AdminreportDatamodel[] businesses = new AdminreportDatamodel[response.length()];
                    for (int i = 0; i < response.length(); i++) {
                        // Convert JSON objects to Business objects
                        businesses[i] = parseBusinessFromJson(response.getJSONObject(i));
                    }

                    String Jan = "0", Feb= "0", Mar= "0", Apr= "0", May= "0", Jun= "0", Jul= "0", Aug= "0", Sep= "0", Oct= "0", Nov= "0", Dec = "0";
                    // Now, you can access the data and display it as needed.
                    for (AdminreportDatamodel business : businesses) {
                        String Name =  business.getBussinessName() ;
                        String Mobile =  business.getBussinessMob() ;
                        String Status =  business.getStatus() ;

                        Map<String, Integer> monthCounts = business.getMonthTotalCounts();
                        for (String key : monthCounts.keySet()) {
                            switch (key) {
                                case "1":
                                    Jan = "C: " + monthCounts.get(key)+" - "+CalulatePrice(monthCounts.get(key))+" Rs";
                                    break;
                                case "2":
                                    Feb = "C: " + monthCounts.get(key)+" - "+CalulatePrice(monthCounts.get(key))+" Rs";
                                    break;
                                case "3":
                                    Mar = "C: " + monthCounts.get(key)+" - "+CalulatePrice(monthCounts.get(key))+" Rs";
                                    break;
                                case "4":
                                    Apr = "C: " + monthCounts.get(key)+" - "+CalulatePrice(monthCounts.get(key))+" Rs";
                                    break;
                                case "5":
                                    May = "C: " + monthCounts.get(key)+" - "+CalulatePrice(monthCounts.get(key))+" Rs";
                                    break;
                                case "6":
                                    Jun = "C: " + monthCounts.get(key)+" - "+CalulatePrice(monthCounts.get(key))+" Rs";
                                    break;
                                case "7":
                                    Jul = "C: " + monthCounts.get(key)+" - "+CalulatePrice(monthCounts.get(key))+" Rs";
                                    break;
                                case "8":
                                    Aug = "C: " + monthCounts.get(key)+" - "+CalulatePrice(monthCounts.get(key))+" Rs";
                                    break;
                                case "9":
                                    Sep = "C: " + monthCounts.get(key)+" - "+CalulatePrice(monthCounts.get(key))+" Rs";
                                    break;
                                case "10":
                                    Oct = "C: " + monthCounts.get(key)+" - "+CalulatePrice(monthCounts.get(key))+" Rs";
                                    break;
                                case "11":
                                    Nov = "C: " + monthCounts.get(key)+" - "+CalulatePrice(monthCounts.get(key))+" Rs";
                                    break;
                                case "12":
                                    Dec = "C: " + monthCounts.get(key)+" - "+CalulatePrice(monthCounts.get(key))+" Rs";
                                    break;
                            }
                        }

                        if (Status.equals("active")) {

                            arrayList.add(new AdminRmodel(Name, Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec));
                        }else {
                            arrayList2.add(new AdminRmodel(Name, Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec));
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new AdminreportAdapter(Admin_report.this, arrayList);
                recyclerView.setAdapter(adapter);

                adapter2 = new ARchildAdapter(Admin_report.this, arrayList2);
                recyclerView2.setAdapter(adapter2);

                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle errors here.

                pd.dismiss();
                Toast.makeText(Admin_report.this, "Something went wrong..", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        requestQueue.add(jsonArrayRequest);

    }

    private String CalulatePrice(int Cc) {


        String firstRange[] = R1.split("-");
        int r11 = Integer.parseInt(firstRange[0]);
        int r12 = Integer.parseInt(firstRange[1]);

        String secondRange[] = R2.split("-");
        int r21 = Integer.parseInt(secondRange[0]);
        int r22 = Integer.parseInt(secondRange[1]);

        String thirdRange[] = R3.split("-");
        int r31 = Integer.parseInt(thirdRange[0]);
        int r32 = Integer.parseInt(thirdRange[1]);


        if (Cc>=r11 && Cc<=r12){
            double TB = (Double.parseDouble(String.valueOf(Cc)))*(Double.parseDouble(RC1)) ;
            double fTB = TB + Double.parseDouble(SCharge);
            return String.valueOf(fTB);
        }

        if (Cc>=r21 && Cc<=r22){
            double TB = (Double.parseDouble(String.valueOf(Cc)))*(Double.parseDouble(RC2)) ;
            double fTB = TB + Double.parseDouble(SCharge);
            return String.valueOf(fTB);
        }

        if (Cc>=r31 && Cc<=r32){
            double TB = (Double.parseDouble(String.valueOf(Cc)))*(Double.parseDouble(RC3)) ;
            double fTB = TB + Double.parseDouble(SCharge);
            return String.valueOf(fTB);
        }


        return"0 Rs";
    }


    private void showYear() {

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = thisYear; i >= 2000; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, years);

        Spinner spinYear = (Spinner) findViewById(R.id.ARyearTV);
        spinYear.setAdapter(adapter);

        spinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                arrayList.clear();
                arrayList2.clear();
                selectedYear = years.get(position);
                getinfo(selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}