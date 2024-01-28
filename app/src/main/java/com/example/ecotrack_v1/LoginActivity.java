package com.example.ecotrack_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecotrack_v1.ui.login.RegisterActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private TextView regprompt;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        username = (EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btnLogin);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        regprompt = (TextView) findViewById(R.id.txt_regPrompt);

        String text = "New to the app? Register";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        };
        ss.setSpan(clickableSpan, 16, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        regprompt.setText(ss);
        regprompt.setMovementMethod(LinkMovementMethod.getInstance());
        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String txtemail = username.getText().toString();
                String txtpassword = password.getText().toString();
                if(TextUtils.isEmpty(txtemail) || TextUtils.isEmpty(txtpassword))
                {
                    Toast.makeText(LoginActivity.this, "Enter valid credentials", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
                else if(txtpassword.length() < 6)
                {
                    Toast.makeText(LoginActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
                else {
                    loginUser(txtemail, txtpassword);
                }
            }
        });
    }

    private void loginUser(String txtemail, String txtpassword) {

        auth.signInWithEmailAndPassword(txtemail, txtpassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity2.class));
                finish();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Login Failure: Please check your email or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity2.class));
        }
    }
}