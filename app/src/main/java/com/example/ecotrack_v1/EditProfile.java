package com.example.ecotrack_v1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecotrack_v1.ui.login.RegisterActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {
    EditText new_f_name,new_mail,new_password;
    String[] profiles = {"Regular User","Social Worker"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser fuser;

    Button saveBtn,cancelBtn;
    String userProfile;
    StorageReference storageReference;
    ImageView imageView;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent i = getIntent();
        String fullName = i.getStringExtra("fullName");
        String email = i.getStringExtra("email");

        new_f_name = (EditText) findViewById(R.id.new_full_name);
        new_mail = (EditText) findViewById(R.id.new_email);
        new_password = (EditText)findViewById(R.id.edit_password);
        autoCompleteTextView = findViewById(R.id.auto_complete_txt2);
        saveBtn = (Button)findViewById(R.id.btn_save_update_profile);
        cancelBtn = (Button)findViewById(R.id.btn_cancel);
        imageView = (ImageView)findViewById(R.id.edit_image);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fuser = fAuth.getCurrentUser();
        new_f_name.setText(fullName);
        new_mail.setText(email);
        storageReference = FirebaseStorage.getInstance().getReference();


        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),ProfileActivity.class);
                startActivity(i);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,1000);
            }
        });
        adapterItems = new ArrayAdapter<String>(EditProfile.this, R.layout.list_profiles, profiles);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                userProfile = adapterView.getItemAtPosition(i).toString();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_password = new_password.getText().toString();
             if(new_f_name.getText().toString().isEmpty() || new_mail.getText().toString().isEmpty()||TextUtils.isEmpty(userProfile))
             {
                 Toast.makeText(EditProfile.this,"Some Fields are empty",Toast.LENGTH_SHORT).show();
             } else if (txt_password.length() <6) {
                 Toast.makeText(EditProfile.this,"Password length required more than 6",Toast.LENGTH_SHORT).show();

             } else {
                 String email = new_mail.getText().toString();

                  /*   fuser.updatePassword(txt_password).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void unused) {
                             Toast.makeText(EditProfile.this, "Password Updated", Toast.LENGTH_SHORT).show();
                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(EditProfile.this, "Password Update Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                         }
                     });*/

                 fuser.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void unused) {

                         DocumentReference docRef = fStore.collection("users").document(fuser.getUid());
                         Map<String, Object> edited = new HashMap<>();

                         //Note: match with firebase
                         edited.put("email", email);
                         edited.put("fullname", new_f_name.getText().toString());
                         edited.put("profileType", userProfile);
                         //edited.put("Password",txt_password);
                         //edited.put("userType",);

                         docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void unused) {
                                 Toast.makeText(EditProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                 startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                                 finish();
                             }
                         });
                         Toast.makeText(EditProfile.this, "Email is changed", Toast.LENGTH_SHORT).show();
                         fuser.updatePassword(txt_password).addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void unused) {
                                 Toast.makeText(EditProfile.this, "Password Updated", Toast.LENGTH_SHORT).show();
                             }
                         }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Toast.makeText(EditProfile.this, "Password Update Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                             }
                         });
                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 });

             }
            }
        });
       // Log.d(TAG,"onCreate"+ new_f_name+" "+new_mail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {

                Uri contentUri = data.getData();
                uploadImageToFirebase(contentUri);
            }
        }
    }
    private void uploadImageToFirebase(Uri contentUri) {
        StorageReference image = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into((Target) image);
                        //Log.d("tag","OnSucess: Uploaded Image URl is "+ uri.toString());
                    }

                });
                Toast.makeText(EditProfile.this,"Image is Uploaded",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this,"Upload Failed",Toast.LENGTH_SHORT).show();
            }
        });

    }
}