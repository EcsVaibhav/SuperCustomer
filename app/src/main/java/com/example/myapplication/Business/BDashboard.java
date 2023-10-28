package com.example.myapplication.Business;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.graphics.Color;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.Login;
import com.example.myapplication.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BDashboard extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView nav;
    public Toolbar toolbar;
    private Button filterButton, resetButton;

    private Calendar selectedStartDate;
    private Calendar selectedEndDate;
    private int initialTotalUsers = 0;
    private Spinner yearSpinner;
    private ArrayAdapter<String> yearAdapter;
    private PieChart pieChart;
    private BarChart barChart;

    ProgressBar Pbar_barchart, Pbar_pichart, filterPro;


    ProgressDialog progressDialog;
    String Mobile;

    int C1F = 0, C2F = 0, C3F = 0, C4F = 0;
    float d1 = 0, d2 = 0, d3 = 0, d4 = 0;
    int c5 = 0, TotalUser = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdashboard);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav = findViewById(R.id.nav);
        resetButton = findViewById(R.id.resetButton);
        Pbar_barchart = findViewById(R.id.Pbar_barchart);
        Pbar_pichart = findViewById(R.id.Pbar_pichart);

        progressDialog = new ProgressDialog(BDashboard.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait..");

        filterButton = findViewById(R.id.filterButton);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        findViewById(R.id.hiddenbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BDashboard.this, BDash.class));
            }
        });


        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_coupons) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(BDashboard.this, AllCoupons.class);
                    intent1.putExtra("NavName", Nav_name());
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_bussiness_dashboard) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_customers) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(BDashboard.this, AllCustomers.class);
                    intent1.putExtra("NavName", Nav_name());
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_Survey) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(BDashboard.this, ChooseSurvey.class);
                    intent1.putExtra("NavName", Nav_name());
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_Mybill) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(BDashboard.this, BMyBills.class);
                    intent1.putExtra("NavName",Nav_name());
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_bussiness_settings) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(BDashboard.this, BussinessSetting.class);
                    intent1.putExtra("NavName", Nav_name());
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_logout) {
                    SharedPreferences.Editor editor = getSharedPreferences(Login.PREFS_NAME, 0).edit();
                    editor.remove("bussiness");
                    editor.apply();

                    Intent intent1 = new Intent(BDashboard.this, Login.class);
                    startActivity(intent1);
                    Toast.makeText(BDashboard.this, "LogOut Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });

        View headview = nav.getHeaderView(0);
        TextView navName = headview.findViewById(R.id.navName);
        navName.setText(Nav_name());

        Intent intent = getIntent();
        Mobile = intent.getStringExtra("Mobile");
        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
        Mobile = sharedPreferences.getString("bussiness", "");

        pieChart = findViewById(R.id.pieChart);

        barChart = findViewById(R.id.barChart);

        /*loadPieChart();*/
        new LoadData().execute();


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog();
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BDashboard.this, BDashboard.class));
                overridePendingTransition(0, 0);
            }
        });

    }

    private class LoadData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    loadPieChart();
                    pieChart.setCenterText("Total Users\n" + TotalUser);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(BDashboard.this);

        builder.setMessage("Are you sure you want to exit");
        builder.setTitle("Alert !");
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finishAffinity();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    private String Nav_name() {

        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
        String NMob = sharedPreferences.getString("bussiness", "");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new Request.Builder()
                .url("http://tsm.ecssofttech.com/Library/GYMapi/getBdetail.php?mobileno=" + NMob + "")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = Objects.requireNonNull(response.body()).string();
            System.out.println(responseString);

            JSONArray contacts = new JSONArray(responseString);

            for (int i = 0; i < contacts.length(); i++) {
                JSONObject o = contacts.getJSONObject(i);
                return o.getString("BusinessName");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Super Customer";
    }

    private void loadPieChart() {

        /*progressDialog.show();*/

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new Request.Builder()
                .url("http://tsm.ecssofttech.com/Library/GYMapi/Gym_Get_Scan_Count.php?username=" + Mobile + "")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = Objects.requireNonNull(response.body()).string();
            System.out.println(responseString);

            JSONArray contacts = new JSONArray(responseString);

            Log.e("Usercount", String.valueOf(contacts.length()));


            int c1 = 0, c2 = 0, c3 = 0, c4 = 0;
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);

                int a = Integer.parseInt(c.getString("count").toString());

                if (a == 1) {
                    c1 += 1;
                } else if (a >= 2 && a <= 4) {
                    c2 += 1;
                } else if (a >= 5 && a <= 9) {
                    c3 += 1;
                } else if (a >= 10) {
                    c4 += 1;
                }
            }
            TotalUser = contacts.length();
            loadPieChartData(c1, c2, c3, c4, contacts.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePieChartLabels() {
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true); // Enable legend
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setDrawInside(false);
        legend.setXEntrySpace(10f);
        legend.setYEntrySpace(0f);
        legend.setTextColor(Color.BLACK);
        legend.setTextSize(12f);
    }

    private void loadPieChartData(int c1, int c2, int c3, int c4, int length) {

        ArrayList<PieEntry> entries = new ArrayList<>();
        if (c1 == 0 && c2 == 0 && c3 == 0 && c4 == 0) {

            entries.add(new PieEntry(c1, "1 visit"));
            entries.add(new PieEntry(c2, "2 to 4"));
            entries.add(new PieEntry(c3, "5 to 9"));
            entries.add(new PieEntry(c4, "10 or more"));

            AlertDialog.Builder builder = new AlertDialog.Builder(BDashboard.this);
            builder.setTitle("Alert !");
            builder.setMessage("No Data found ");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else {
            entries.add(new PieEntry(c1, "1 visit"));
            entries.add(new PieEntry(c2, "2 to 4"));
            entries.add(new PieEntry(c3, "5 to 9"));
            entries.add(new PieEntry(c4, "10 or more"));
        }


        PieDataSet dataSet = new PieDataSet(entries, "Pie Chart");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS[2],ColorTemplate.MATERIAL_COLORS[1],ColorTemplate.MATERIAL_COLORS[0],ColorTemplate.MATERIAL_COLORS[3]);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                float percentage = (value / length) * 100;
                int roundedPercentage = Math.round(percentage);
                if (value == 0) {
                    return "";
                }
                return String.valueOf(roundedPercentage) + "% [" + (int) value + "]";
            }
        });


        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(12f);

        pieChart.setDrawEntryLabels(false); // Disable entry labels
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(16f);

        pieChart.getDescription().setEnabled(false);
        /*pieChart.setCenterText("Total Users\n" + length);*/
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextSize(18f);
        pieChart.animateY(1000);
        pieChart.setData(data);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(true); // Enable legend
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setDrawInside(false);
        legend.setXEntrySpace(10f);
        legend.setYEntrySpace(0f);

        updatePieChartLabels();
        pieChart.invalidate();

        showYear();

    }

    private String formatDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }


    private void filterDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.layout_for_bdashboardfilter, null);

        Button Bd_apply = dialogView.findViewById(R.id.Bd_apply);
        TextView sDate = dialogView.findViewById(R.id.startDatePickerButton);
        TextView eDate = dialogView.findViewById(R.id.endDatePickerButton);
        RelativeLayout main = dialogView.findViewById(R.id.main);
        filterPro = dialogView.findViewById(R.id.filterPro);
        EditText GreterthanEt = dialogView.findViewById(R.id.GreterthanEt);
        EditText LessthanEt = dialogView.findViewById(R.id.LessthanEt);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(false);

        AlertDialog dialog_for_filter = builder.create();
        dialog_for_filter.show();

        sDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        BDashboard.this,
                        (view, year, month, dayOfMonth) -> {
                            selectedStartDate = Calendar.getInstance();
                            selectedStartDate.set(year, month, dayOfMonth);

                            // Update the button text with the selected start date
                            sDate.setText(formatDate(selectedStartDate));
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();

            }
        });
        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        BDashboard.this,
                        (view, year, month, dayOfMonth) -> {
                            selectedStartDate = Calendar.getInstance();
                            selectedStartDate.set(year, month, dayOfMonth);

                            // Update the button text with the selected start date
                            eDate.setText(formatDate(selectedStartDate));
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();

            }
        });


        dialogView.findViewById(R.id.Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_for_filter.dismiss();
            }
        });


        Bd_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sd = sDate.getText().toString();
                String ed = eDate.getText().toString();
                String minV = GreterthanEt.getText().toString();
                String maxV = LessthanEt.getText().toString();


                if (sd.equals("Select From Date") && ed.equals("Select To Date") && !(minV.isEmpty() && maxV.isEmpty())) {

                    if (minV.isEmpty() || maxV.isEmpty()) {
                        Toast.makeText(BDashboard.this, "Please Enter Range value", Toast.LENGTH_SHORT).show();
                    } else if (Integer.parseInt(minV) > Integer.parseInt(maxV)) {
                        Toast.makeText(BDashboard.this, "Please Enter Correct Range value", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.show();
                        dialog_for_filter.dismiss();
                        rangeFilter(minV, maxV);
                    }
                } else if (minV.isEmpty() && maxV.isEmpty() && !(sd.equals("Select From Date") && ed.equals("Select To Date"))) {

                    if (sd.equals("Select From Date") || ed.equals("Select To Date")) {
                        Toast.makeText(BDashboard.this, "Please select Dates", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.show();
                        dialog_for_filter.dismiss();
                        getDataWithDateRange(sDate.getText().toString(), eDate.getText().toString());
                    }

                } else if (!(sd.equals("Select From Date") && ed.equals("Select To Date") && minV.isEmpty() && maxV.isEmpty())) {

                    dialog_for_filter.dismiss();
                    combineFilter(sd, ed, minV, maxV);
                } else {
                    Toast.makeText(BDashboard.this, "Filter Empty", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

    private void combineFilter(String startDate, String endDate, String min, String max) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        String url = "http://tsm.ecssofttech.com/Library/GYMapi/Gym_Get_Scan_Count2.php?username=" + Mobile + "&start_date=" + startDate + "&end_date=" + endDate;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = Objects.requireNonNull(response.body()).string();
            JSONArray contacts = new JSONArray(responseString);

            int c1 = 0;
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);

                int a = Integer.parseInt(c.getString("count").toString());
                int minC = Integer.parseInt(min);
                int maxC = Integer.parseInt(max);

                if (a >= minC && a <= maxC) {
                    c1 += 1;
                }
            }

            String range = min + " - " + max;
            loadCustomePieChartData(c1, range);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void rangeFilter(String min, String max) {


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new Request.Builder()
                .url("http://tsm.ecssofttech.com/Library/GYMapi/Gym_Get_Scan_Count.php?username=" + Mobile + "")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = Objects.requireNonNull(response.body()).string();
            System.out.println(responseString);

            JSONArray contacts = new JSONArray(responseString);


            int c1 = 0;
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);

                int a = Integer.parseInt(c.getString("count").toString());
                int minC = Integer.parseInt(min);
                int maxC = Integer.parseInt(max);

                if (a >= minC && a <= maxC) {
                    c1 += 1;
                }
            }

            String range = min + " - " + max;
            progressDialog.dismiss();
            loadCustomePieChartData(c1, range);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void loadCustomePieChartData(int c1, String r) {


        pieChart.clear();

        ArrayList<PieEntry> entries = new ArrayList<>();
        if (c1 == 0) {

            entries.add(new PieEntry(c1, r));

            AlertDialog.Builder builder = new AlertDialog.Builder(BDashboard.this);
            builder.setTitle("Alert !");
            builder.setMessage("No Data found ");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else {
            entries.add(new PieEntry(c1, r));
        }


        PieDataSet dataSet = new PieDataSet(entries, "Pie Chart");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                float percentage = (value / TotalUser) * 100;
                int roundedPercentage = Math.round(percentage);
                if (value == 0) {
                    return "";
                }
                return String.valueOf(roundedPercentage) + "% [" + (int) value + "]";
            }
        });


        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(12f);

        pieChart.setDrawEntryLabels(false); // Disable entry labels
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(16f);

        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextSize(18f);
        pieChart.animateY(1000);
        pieChart.setData(data);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(true); // Enable legend
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setDrawInside(false);
        legend.setXEntrySpace(10f);
        legend.setYEntrySpace(0f);

        updatePieChartLabels();
        pieChart.invalidate();


    }

    private void getDataWithDateRange(String startDate, String endDate) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        String url = "http://tsm.ecssofttech.com/Library/GYMapi/Gym_Get_Scan_Count2.php?username=" + Mobile + "&start_date=" + startDate + "&end_date=" + endDate;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = Objects.requireNonNull(response.body()).string();
            JSONArray contacts = new JSONArray(responseString);

            int c1 = 0, c2 = 0, c3 = 0, c4 = 0;

            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);

                int a = Integer.parseInt(c.getString("count").toString());


                if (a == 1) {
                    c1 += 1;
                } else if (a >= 2 && a <= 4) {
                    c2 += 1;
                } else if (a >= 5 && a <= 9) {
                    c3 += 1;
                } else if (a >= 10) {
                    c4 += 1;
                }

            }
            loadPieChartData(c1, c2, c3, c4, contacts.length());
            progressDialog.dismiss();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showYear() {

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = thisYear; i >= 2000; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, years);

        Spinner spinYear = (Spinner) findViewById(R.id.spinner_of_year);
        spinYear.setAdapter(adapter);

        spinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                barChart.invalidate();
                barChart.clear();
                String selected = years.get(position);

                LoadYearData loadYearData = new LoadYearData();
                loadYearData.execute(Mobile, selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public class LoadYearData extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... strings) {

            if (strings.length >= 2) {
                String Mobile = strings[0];
                String selected = strings[1];

                loadYearBarChartData(Mobile, selected);

            }

            return null;
        }

    }

    private void loadYearBarChartData(String mobile, String year) {
        int Jan = 0;
        int Feb = 0;
        int Mar = 0;
        int Apr = 0;
        int May = 0;
        int Jun = 0;
        int Jul = 0;
        int Aug = 0;
        int Sep = 0;
        int Oct = 0;
        int Nov = 0;
        int Dec = 0;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new Request.Builder()
                /*.url("http://tsm.ecssofttech.com/Library/GYMapi/Gym_Get_Bar_Chart_Data.php?Username=" + mobile + "&year=" + year + "")*/
                .url("http://tsm.ecssofttech.com/Library/GYMapi/getBarChartData.php?Username=" + mobile + "&year=" + year + "")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseString = Objects.requireNonNull(response.body()).string();
            Log.e("barresponse", responseString);
            JSONObject jsonObject = new JSONObject(responseString);
            JSONObject monthCounts = jsonObject.getJSONObject("monthCounts");
            JSONObject monthTotalCounts = jsonObject.getJSONObject("monthTotalCounts");

            if (monthTotalCounts.getInt("1") > 0) {

                Jan = (int) (((double) monthCounts.getInt("1") / (double) monthTotalCounts.getInt("1")) * 100);
            }
            if (monthTotalCounts.getInt("2") > 0) {

                Feb = (int) (((double) monthCounts.getInt("2") / (double) monthTotalCounts.getInt("2")) * 100);
            }
            if (monthTotalCounts.getInt("3") > 0) {

                Mar = (int) (((double) monthCounts.getInt("3") / (double) monthTotalCounts.getInt("3")) * 100);
            }
            if (monthTotalCounts.getInt("4") > 0) {

                Apr = (int) (((double) monthCounts.getInt("4") / (double) monthTotalCounts.getInt("4")) * 100);
            }
            if (monthTotalCounts.getInt("5") > 0) {

                May = (int) (((double) monthCounts.getInt("5") / (double) monthTotalCounts.getInt("5")) * 100);
            }
            if (monthTotalCounts.getInt("6") > 0) {

                Jun = (int) (((double) monthCounts.getInt("6") / (double) monthTotalCounts.getInt("6")) * 100);
            }
            if (monthTotalCounts.getInt("7") > 0) {

                Jul = (int) (((double) monthCounts.getInt("7") / (double) monthTotalCounts.getInt("7")) * 100);
            }
            if (monthTotalCounts.getInt("8") > 0) {

                Aug = (int) (((double) monthCounts.getInt("8") / (double) monthTotalCounts.getInt("8")) * 100);
            }
            if (monthTotalCounts.getInt("9") > 0) {

                Sep = (int) (((double) monthCounts.getInt("9") / (double) monthTotalCounts.getInt("9")) * 100);
            }
            if (monthTotalCounts.getInt("10") > 0) {

                Oct = (int) (((double) monthCounts.getInt("10") / (double) monthTotalCounts.getInt("10")) * 100);
            }
            if (monthTotalCounts.getInt("11") > 0) {

                Nov = (int) (((double) monthCounts.getInt("11") / (double) monthTotalCounts.getInt("11")) * 100);
            }
            if (monthTotalCounts.getInt("12") > 0) {

                Dec = (int) (((double) monthCounts.getInt("12") / (double) monthTotalCounts.getInt("12")) * 100);
            }

            loadBarChartData(Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadBarChartData(int Jan, int Feb, int Mar, int Apr, int May, int Jun, int Jul, int Aug, int Sep, int Oct, int Nov, int Dec) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.clear();
        entries.add(new BarEntry(0, Jan));
        entries.add(new BarEntry(1, Feb));
        entries.add(new BarEntry(2, Mar));
        entries.add(new BarEntry(3, Apr));
        entries.add(new BarEntry(4, May));
        entries.add(new BarEntry(5, Jun));
        entries.add(new BarEntry(6, Jul));
        entries.add(new BarEntry(7, Aug));
        entries.add(new BarEntry(8, Sep));
        entries.add(new BarEntry(9, Oct));
        entries.add(new BarEntry(10, Nov));
        entries.add(new BarEntry(11, Dec));

        BarDataSet dataSet = new BarDataSet(entries, "");
        int[] colors = new int[]{
                Color.rgb(63, 81, 181),    // Color for January
                Color.rgb(33, 150, 243),   // Color for February
                Color.rgb(76, 175, 80),    // Color for March
                Color.rgb(255, 152, 0),    // Color for April
                Color.rgb(255, 87, 34),    // Color for May
                Color.rgb(121, 85, 72),    // Color for June
                Color.rgb(233, 30, 99),    // Color for July
                Color.rgb(0, 150, 136),    // Color for August
                Color.rgb(156, 39, 176),   // Color for September
                Color.rgb(255, 193, 7),    // Color for October
                Color.rgb(96, 125, 139),   // Color for November
                Color.rgb(158, 158, 158)

        };
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);
        dataSet.setDrawValues(true); // Enable drawing values on top of bars

        BarData data = new BarData(dataSet);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(12f);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int roundedPercentage = Math.round(value);
                if (value == 0) {
                    return "0%";
                }
                return String.valueOf(roundedPercentage) + "%";
            }
        });

        barChart.setDrawValueAboveBar(true);
        barChart.setData(data);


        ArrayList<String> xLabels = new ArrayList<>();
        xLabels.add("January");
        xLabels.add("February");
        xLabels.add("March");
        xLabels.add("April");
        xLabels.add("May");
        xLabels.add("June");
        xLabels.add("July");
        xLabels.add("August");
        xLabels.add("September");
        xLabels.add("October");
        xLabels.add("November");
        xLabels.add("December");

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        /*xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(90);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);*/


        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setEnabled(false);

        barChart.getDescription().setEnabled(false);
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
        barChart.invalidate();

        /*progressDialog.dismiss();*/

    }

}