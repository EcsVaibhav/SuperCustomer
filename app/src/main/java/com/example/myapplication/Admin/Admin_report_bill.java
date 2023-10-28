package com.example.myapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Business.BDash;
import com.example.myapplication.Login;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Admin_report_bill extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView nav;
    public Toolbar toolbar;

    ProgressDialog progressDialog;
    String rangeOne = "0-0", rangeTwo = "0-0", rangeThree = "0-0", r1Cost = "0.0", r2Cost = "0.0", r3Cost = "0.0",serviceCharge = "0.0";
    EditText r11, r12, r1, r21, r22, r2, r31, r32, r3,serviceChageEt;
    TextView Range1, Range2, Range3, rc1, rc2, rc3,serviceChargeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report_bill);

        findId();

        findViewById(R.id.RangeUpdateBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRangeDialog();
            }
        });


        findViewById(R.id.CalBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });

        findViewById(R.id.Admin_reportBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(Admin_report_bill.this,Admin_report.class);
                in.putExtra("R1",rangeOne);
                in.putExtra("R2",rangeTwo);
                in.putExtra("R3",rangeThree);
                in.putExtra("RC1",r1Cost);
                in.putExtra("RC2",r2Cost);
                in.putExtra("RC3",r3Cost);
                in.putExtra("SCharge",serviceCharge);
                startActivity(in);

                //Toast.makeText(Admin_report_bill.this, "In process", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findId() {
        drawerLayout = findViewById(R.id.my_drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav = findViewById(R.id.nav);

        Range1 = findViewById(R.id.range_1);
        Range2 = findViewById(R.id.range_2);
        Range3 = findViewById(R.id.range_3);
        rc1 = findViewById(R.id.Rcost1);
        rc2 = findViewById(R.id.Rcost2);
        rc3 = findViewById(R.id.Rcost3);
        serviceChargeTV = findViewById(R.id.serviceChargeTV);

        progressDialog = new ProgressDialog(Admin_report_bill.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_bussiness_registration) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(Admin_report_bill.this, BussinessRegistration.class);
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_logout) {
                    SharedPreferences.Editor editor = getSharedPreferences(Login.PREFS_NAME, 0).edit();
                    editor.remove("admin");
                    editor.apply();
                    overridePendingTransition(0, 0);
                    finish();
                    Intent intent1 = new Intent(Admin_report_bill.this, Login.class);
                    startActivity(intent1);
                    Toast.makeText(Admin_report_bill.this, "LogOut Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_all_acounts) {

                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent1 = new Intent(Admin_report_bill.this, AllAcounts.class);
                    startActivity(intent1);
                    finish();
                } else if (item.getItemId() == R.id.menu_active_deactive) {
                    Intent intent1 = new Intent(Admin_report_bill.this, ActiveDeactive.class);
                    startActivity(intent1);
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId() == R.id.menu_bill_report) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });


        new LoadData().execute();


    }

    private class LoadData extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            getRanges();

            return null;
        }


    }


    private void getDate() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.layoutforchoosedate, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        TextView selectedDate = dialogView.findViewById(R.id.chooseDateTV);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialogView.findViewById(R.id.Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        selectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Admin_report_bill.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedExpDate = Calendar.getInstance();
                        selectedExpDate.set(year, month, dayOfMonth);

                        selectedDate.setText(formatDate(selectedExpDate));
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });

        dialogView.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedDate.getText().toString().equals("Select Date") || selectedDate.getText().toString().isEmpty()) {
                    Toast.makeText(Admin_report_bill.this, "select Date", Toast.LENGTH_SHORT).show();
                } else {

                    Intent in = new Intent(Admin_report_bill.this,All_Business_bills.class);
                    in.putExtra("sDate",selectedDate.getText().toString());
                    in.putExtra("R1",rangeOne);
                    in.putExtra("R2",rangeTwo);
                    in.putExtra("R3",rangeThree);
                    in.putExtra("RC1",r1Cost);
                    in.putExtra("RC2",r2Cost);
                    in.putExtra("RC3",r3Cost);
                    in.putExtra("SCharge",serviceCharge);
                    startActivity(in);

                    dialog.dismiss();
                }

            }
        });


    }
    private String formatDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void getRanges() {
        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/getAdminCategory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject o = jsonArray.getJSONObject(i);


                        rangeOne = o.getString("CRange1");
                        Range1.setText(rangeOne);
                        rangeTwo = o.getString("CRange2");
                        Range2.setText(rangeTwo);
                        rangeThree = o.getString("CRange3");
                        Range3.setText(rangeThree + "+");

                        r1Cost = o.getString("Cost1");
                        rc1.setText(r1Cost + " Rs");
                        r2Cost = o.getString("Cost2");
                        rc2.setText(r2Cost + " Rs");
                        r3Cost = o.getString("Cost3");
                        rc3.setText(r3Cost + " Rs");

                        serviceCharge = o.getString("SCharge");
                        serviceChargeTV.setText(serviceCharge+" Rs");

                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Admin_report_bill.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        RequestQueue q = Volley.newRequestQueue(Admin_report_bill.this);
        q.add(request);
    }

    private void updateData(String c1, String c2, String c3, String R1, String R2, String R3,String SCharge) {


        StringRequest request = new StringRequest(Request.Method.GET, "http://tsm.ecssofttech.com/Library/GYMapi/UpdateAdminRanges.php?c1=" + c1 + "&c2=" + c2 + "&c3=" + c3 + "&CR1=" + R1 + "&CR2=" + R2 + "&CR3=" + R3 + "&SCharge="+SCharge+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("updated")) {
                    getRanges();
                } else {
                    Toast.makeText(Admin_report_bill.this, "Something went wrong try later...", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Admin_report_bill.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        RequestQueue q = Volley.newRequestQueue(Admin_report_bill.this);
        q.add(request);
    }


    private void showRangeDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.layout_for_rangeupdateadmin, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        r11 = dialogView.findViewById(R.id.R_11);
        r12 = dialogView.findViewById(R.id.R_12);
        r1 = dialogView.findViewById(R.id.c1);

        r21 = dialogView.findViewById(R.id.R_21);
        r22 = dialogView.findViewById(R.id.R_22);
        r2 = dialogView.findViewById(R.id.c2);

        r31 = dialogView.findViewById(R.id.R_31);
        r32 = dialogView.findViewById(R.id.R_32);
        r3 = dialogView.findViewById(R.id.c3);

        serviceChageEt = dialogView.findViewById(R.id.serviceChageEt);


        String[] ra1 = rangeOne.split("-");
        String[] ra2 = rangeTwo.split("-");
        String[] ra3 = rangeThree.split("-");

        r11.setText(ra1[0]);
        r12.setText(ra1[1]);
        r1.setText(r1Cost);

        r21.setText(ra2[0]);
        r22.setText(ra2[1]);
        r2.setText(r2Cost);

        r31.setText(ra3[0]);
        r32.setText(ra3[1]);
        r3.setText(r3Cost);

        serviceChageEt.setText(serviceCharge);


        AlertDialog dialog = builder.create();
        dialog.show();

        dialogView.findViewById(R.id.Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.Rupdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                if (r1.getText().toString().isEmpty() || r2.getText().toString().isEmpty() || r3.getText().toString().isEmpty() ||
                        r11.getText().toString().isEmpty() || r12.getText().toString().isEmpty() || r21.getText().toString().isEmpty() || r22.getText().toString().isEmpty() ||
                        r31.getText().toString().isEmpty() || r32.getText().toString().isEmpty()) {
                    Toast.makeText(Admin_report_bill.this, "All filed required..", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else {

                    boolean Irn1 = Integer.parseInt(r11.getText().toString().trim()) >= Integer.parseInt(r12.getText().toString().trim());
                    boolean Irn2 = Integer.parseInt(r21.getText().toString().trim()) >= Integer.parseInt(r22.getText().toString().trim());
                    boolean Irn3 = Integer.parseInt(r31.getText().toString().trim()) >= Integer.parseInt(r32.getText().toString().trim());

                    boolean c1_2 = Integer.parseInt(r12.getText().toString().trim()) == Integer.parseInt(r21.getText().toString().trim());
                    boolean c2_3 = Integer.parseInt(r22.getText().toString().trim()) == Integer.parseInt(r31.getText().toString().trim());

                    if (Irn1 || Irn2 || Irn3) {
                        Toast.makeText(Admin_report_bill.this, "Invalid Range", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else if (c1_2 || c2_3) {
                        Toast.makeText(Admin_report_bill.this, "Invalid Range", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {

                        double c_1 = Double.parseDouble(r1.getText().toString().trim());
                        double c_2 = Double.parseDouble(r2.getText().toString().trim());
                        double c_3 = Double.parseDouble(r3.getText().toString().trim());

                        double S_Charge = Double.parseDouble(serviceChageEt.getText().toString().trim());

                        String rn1 = r11.getText().toString().trim() + "-" + r12.getText().toString().trim();
                        String rn2 = r21.getText().toString().trim() + "-" + r22.getText().toString().trim();
                        String rn3 = r31.getText().toString().trim() + "-" + r32.getText().toString().trim();

                        updateData(String.valueOf(c_1), String.valueOf(c_2), String.valueOf(c_3), rn1, rn2, rn3,String.valueOf(S_Charge));
                        dialog.dismiss();
                    }

                }

            }
        });


    }


}