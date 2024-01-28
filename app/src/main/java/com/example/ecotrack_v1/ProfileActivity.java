package com.example.ecotrack_v1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    BottomNavigationView bnView;
    FloatingActionButton fab;
    TextView fullName, email, profileType;
    Button logout,profile_edit,change_psswd;

    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String old_name,old_email,old_type;

    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //modified by Nitesh
        profile_edit = (Button)findViewById(R.id.btn_edit_profile);
        //over
        bnView = (BottomNavigationView) findViewById(R.id.bnView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fullName = (TextView) findViewById(R.id.txt_profile_fullname);
        email = (TextView) findViewById(R.id.txt_profile_email);
        profileType = (TextView) findViewById(R.id.txt_profileType);
        logout = (Button) findViewById(R.id.btn_profile_logout);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value!=null && value.exists()) {
                    fullName.setText("Full Name: " + value.getString("fullname"));
                    old_name = value.getString("fullname");
                    email.setText("Email: " + value.getString("email"));
                    old_email = value.getString("email");
                    profileType.setText("You are logged in as a " + value.getString("profileType"));
                    old_type = value.getString(value.getString("profileType"));
                }
            }
        });
        //Nitesh
        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),EditProfile.class);
                i.putExtra("email",old_email);
                i.putExtra("fullName",old_name);
                i.putExtra("ProfileType",old_type);
                startActivity(i);
            }
        });


        //over
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, ReportActivity.class));
                finish();
            }
        });
        Menu menu = bnView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Toast.makeText(ProfileActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileActivity.this, StartActivity.class));
                finish();
            }
        });
        bnView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_home)
                {
                    startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_profile)
                {
                    //startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                }
                else if(id == R.id.nav_green_points)
                {
                    startActivity(new Intent(ProfileActivity.this, GreenPointsActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_report_info)
                {
                    startActivity(new Intent(ProfileActivity.this, StatusActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
}