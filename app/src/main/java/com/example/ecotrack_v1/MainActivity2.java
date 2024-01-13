package com.example.ecotrack_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity2 extends AppCompatActivity {
    BottomNavigationView bnView;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bnView = (BottomNavigationView) findViewById(R.id.bnView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2.this, ReportActivity.class));
            }
        });
        Menu menu = bnView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bnView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_home)
                {
                   startActivity(new Intent(MainActivity2.this, HomeActivity.class));
                }
                else if(id == R.id.nav_profile)
                {
                    startActivity(new Intent(MainActivity2.this, ProfileActivity.class));
                }
                else if(id == R.id.nav_green_points)
                {
                    startActivity(new Intent(MainActivity2.this, GreenPointsActivity.class));
                }
                else if(id == R.id.nav_report_info)
                {
                    startActivity(new Intent(MainActivity2.this, StatusActivity.class));
                }
                return true;
            }
        });

        bnView.setSelectedItemId(R.id.nav_home);

    }
    /*public void loadFragment(Fragment fragment, boolean flag)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(flag) {
            ft.add(R.id.container, fragment);
        }
        else {
            ft.replace(R.id.container, fragment);
        }
        ft.commit();
    }*/
}