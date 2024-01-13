package com.example.ecotrack_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {

    TextView txt;
    BottomNavigationView bnView;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        txt = (TextView) findViewById(R.id.txt_home);
        txt.setText("This is Home");
        bnView = (BottomNavigationView) findViewById(R.id.bnView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ReportActivity.class));
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
                    //startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                }
                else if(id == R.id.nav_profile)
                {
                    startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                    return true;
                }
                else if(id == R.id.nav_green_points)
                {
                    startActivity(new Intent(HomeActivity.this, GreenPointsActivity.class));
                    return true;
                }
                else if(id == R.id.nav_report_info)
                {
                    startActivity(new Intent(HomeActivity.this, StatusActivity.class));
                    return true;
                }
                return false;
            }
        });

    }
}