package com.example.myapplication.Business;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Login;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;

public class ChooseSurvey extends AppCompatActivity {


    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView nav;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_survey);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav = findViewById(R.id.nav);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        nav.setNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.menu_bussiness_settings) {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent1 = new Intent(this, BussinessSetting.class);
                intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                startActivity(intent1);
                finish();
            }
            if (item.getItemId() == R.id.menu_coupons) {

                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent1 = new Intent(this, AllCoupons.class);
                intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                startActivity(intent1);
                finish();

            }else if (item.getItemId() == R.id.menu_Mybill) {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent1 = new Intent(ChooseSurvey.this, BMyBills.class);
                intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                startActivity(intent1);
                finish();
            }
            else if (item.getItemId() == R.id.menu_customers)
            {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent1 = new Intent(this, AllCustomers.class);
                intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                startActivity(intent1);
                finish();
            }
            else if (item.getItemId() == R.id.menu_bussiness_dashboard)
            {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent1 = new Intent(this, BDashboard.class);
                intent1.putExtra("NavName",getIntent().getStringExtra("NavName"));
                startActivity(intent1);
                finish();
            }
            else if (item.getItemId() == R.id.menu_Survey)
            {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            else if (item.getItemId() == R.id.menu_logout) {
                SharedPreferences.Editor editor = getSharedPreferences(Login.PREFS_NAME,0).edit();
                editor.remove("bussiness");
                editor.apply();
                overridePendingTransition(0,0);
                finish();
                Intent intent1 = new Intent(this, Login.class);
                startActivity(intent1);
                Toast.makeText(this, "LogOut Successfully", Toast.LENGTH_SHORT).show();
                finish();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            else {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return true;
        });




        View headview = nav.getHeaderView(0);
        TextView navName = headview.findViewById(R.id.navName);
        navName.setText(getIntent().getStringExtra("NavName"));


        findViewById(R.id.OnGoiningSurveyBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ChooseSurvey.this, All_survey.class));

            }
        });

        findViewById(R.id.CreateNewSurvey).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseSurvey.this, Create_survey.class));
            }
        });


        findViewById(R.id.ExpiredSurveyBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ChooseSurvey.this, ExpSurvey.class));
                
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChooseSurvey.this);
        builder.setMessage("Are you sure you want to exit ?");
        builder.setTitle("Alert !");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}