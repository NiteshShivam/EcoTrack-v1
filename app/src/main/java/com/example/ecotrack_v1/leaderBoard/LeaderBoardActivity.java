package com.example.ecotrack_v1.leaderBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ecotrack_v1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<ScoreData> list;
    ScoreAdapter adapter;

    FirebaseFirestore firestore ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_boardact);
        recyclerView = findViewById(R.id.LeaderrecyclerView);
        progressBar = findViewById(R.id.LeaderprogressBar);
        firestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firestore.collection("users");

        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        // Assuming 'firestore' is your instance of FirebaseFirestore

        //Match the word greenPoints
        collectionReference.orderBy("greenPoints", Query.Direction.DESCENDING)
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            //ScoreData data = documentSnapshot.toObject(ScoreData.class);
                            String fullname = documentSnapshot.getString("fullname");
                            long greenPoints = documentSnapshot.getLong("greenPoints");
                            ScoreData data1 = new ScoreData();
                            data1.setFullname(fullname);
                            data1.setGreenPoints(greenPoints);
                            list.add(data1);

                        }

                        adapter = new ScoreAdapter(list, LeaderBoardActivity.this);
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LeaderBoardActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }
}