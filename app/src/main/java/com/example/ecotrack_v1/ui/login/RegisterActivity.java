package com.example.ecotrack_v1.ui.login;

import static android.content.ContentValues.TAG;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecotrack_v1.LoginActivity;
import com.example.ecotrack_v1.MainActivity2;
import com.example.ecotrack_v1.R;
import com.example.ecotrack_v1.ui.login.LoginViewModel;
import com.example.ecotrack_v1.ui.login.LoginViewModelFactory;
import com.example.ecotrack_v1.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.klinker.android.link_builder.Link;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText fullname;
    String userProfile;
    String[] profiles = {"Regular User","Social Worker"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    private Button register;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    String userID;

    private FirebaseFirestore fStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        fullname = (EditText) findViewById(R.id.fullname);
        register = (Button) findViewById(R.id.btnRegister);
        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(RegisterActivity.this, R.layout.list_profiles, profiles);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        fStore = FirebaseFirestore.getInstance();
        TextView loginprompt = (TextView) findViewById(R.id.txt_loginprompt);
        String text = "Already a User? Login";

        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        };
        ss.setSpan(clickableSpan, 16, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginprompt.setText(ss);
        loginprompt.setMovementMethod(LinkMovementMethod.getInstance());
        auth = FirebaseAuth.getInstance();

        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                userProfile = adapterView.getItemAtPosition(i).toString();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_fullname = fullname.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password))
                {
                    Toast.makeText(RegisterActivity.this, "Enter valid credentials", Toast.LENGTH_SHORT).show();

                }
                else if(txt_password.length() <6)
                {
                    Toast.makeText(RegisterActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(userProfile))
                {
                    Toast.makeText(RegisterActivity.this, "Select a User Profile", Toast.LENGTH_SHORT).show();

                }
                else {
                    registerUser(txt_email, txt_password, txt_fullname);
                }


            }
        });
    }



    private void registerUser(String txtEmail, String txtPassword, String txtFullname) {
        auth.createUserWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        userID = auth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("Users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("FullName", txtFullname);
                        user.put("Email", txtEmail);
                        user.put("ProfileType", userProfile);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess: user profile is created for "+ userID );
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: "+ e.toString());
                            }
                        });

                        startActivity(new Intent(RegisterActivity.this, MainActivity2.class));
                        finish();
                    }
                    else {
                        Toast.makeText(RegisterActivity.this,"Registration Failed: Email may already exist.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
            }
        });
    }


}