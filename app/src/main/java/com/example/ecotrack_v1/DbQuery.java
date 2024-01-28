package com.example.ecotrack_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class DbQuery extends AppCompatActivity {

  //  public static List<CategoryModel> g_catList = new ArrayList<>();
    public static  boolean isMeOnTopList=false;
    public static FirebaseFirestore g_fireStore;
    public static List<RankModel> g_userList = new ArrayList<>();
    public static int g_usersCount=0;
    public static RankModel myPerformance = new RankModel("null",0,-1);
    public static ProfileModel myProfile = new ProfileModel("Na",null);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_query);
        g_fireStore = FirebaseFirestore.getInstance();
    }
    public  static void getTopUsers(final MyCompleteListener completeListener)
    {
        String myUID = FirebaseAuth.getInstance().getUid();
        g_userList.clear();

        g_fireStore.collection("Users").whereGreaterThan("GREEN_POINTS", 0)
                .orderBy("GREEN_POINTS", Query.Direction.DESCENDING).limit(20)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int rank = 1;
                        for(QueryDocumentSnapshot doc:queryDocumentSnapshots)
                        {
                            g_userList.add(new RankModel(
                                    //see with the database
                                                doc.getString("fullName"),
                                                doc.getLong("GREEN_POINTS").intValue(),
                                                rank
                            ));
                            if(myUID.compareTo(doc.getId())==0)
                            {
                                    isMeOnTopList = true;
                                    myPerformance.setRank(rank);
                            }
                            rank++;

                        }
                        completeListener.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }
    public  static  void getUsersCount(MyCompleteListener completeListener)
    {

        g_fireStore.collection("Users").document("TOTAL_USERS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                            g_usersCount = documentSnapshot.getLong("COUNT").intValue();
                            completeListener.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }





  public static void  loadData(final MyCompleteListener completeListener)
    {
        getUsersData(new MyCompleteListener()
        {
           @Override
           public  void  onSuccess()
           {
               getUsersCount(completeListener);

           }
           @Override
            public  void onFailure()
           {
               completeListener.onFailure();

           }
        });
    }

    private static void getUsersData(MyCompleteListener myCompleteListener) {
        g_fireStore.collection("Users").document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        myProfile.setName(documentSnapshot.getString("fullName"));
                        myProfile.setEmail(documentSnapshot.getString("email"));
                        myCompleteListener.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                            myCompleteListener.onFailure();
                    }
                });
    }


};
