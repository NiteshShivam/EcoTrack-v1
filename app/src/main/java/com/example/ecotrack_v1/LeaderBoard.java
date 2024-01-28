package com.example.ecotrack_v1;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
//extends Fragment
public class LeaderBoard extends Fragment {
    private TextView totalUsersTV,nyImgTextTV,myScoreTV,myRankTV;
    private RecyclerView usersView;
    private RankAdapter adapter;
    private Dialog progressDialog;
    private TextView dialogText;
    public LeaderBoard()
    {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        View view =  inflater.inflate(R.layout.activity_leader_board,container,false);
        //was using Main Activity
        //  ((LeaderBoard) getActivity()).getSuppportActionBar().setTitle("LeaderBoard");
        // Objects.requireNonNull(getActivity()).setTitle("LeaderBoard");

        initViews(view);

    /*    progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText.setText("Loading....");
        progressDialog.show();
*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);
        DbQuery.getTopUsers(new MyCompleteListener()
        {
            @Override
            public  void onSuccess()
            {
                adapter.notifyDataSetChanged();
                if(DbQuery.myPerformance.getScore()!=0)
                {
                    if(!DbQuery.isMeOnTopList)
                    {
                        calculateRank();
                    }
                    myScoreTV.setText("Green Points : "+ DbQuery.myPerformance.getScore());

                    myRankTV.setText("Rank  "+ DbQuery.myPerformance.getRank());
                }
              //  progressDialog.dismiss();

            }

            private void calculateRank() {
                int lowTopScore =DbQuery.g_userList.get(DbQuery.g_userList.size()-1).getScore();
                int remaining_slots = DbQuery.g_usersCount-20;
                int myslot = (DbQuery.myPerformance.getScore()*remaining_slots)/lowTopScore;
                int rank;
                if(lowTopScore!=DbQuery.myPerformance.getScore())
                {
                    rank = DbQuery.g_usersCount-myslot;
                }
                else
                {
                    rank = 21;
                }
                DbQuery.myPerformance.setRank(rank);
            }

            @Override
            public  void onFailure()
            {
                Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        totalUsersTV.setText("Total Users: "+DbQuery.g_usersCount);
        nyImgTextTV.setText(DbQuery.myPerformance.getName().toUpperCase().substring(0,1));
        return view;

        //adapter = new RankAdapter(DbQuery.g_userList);

    }

    private void initViews(View view) {
        totalUsersTV = view.findViewById(R.id.total_users);
        nyImgTextTV = view.findViewById(R.id.img_txt);
        myScoreTV = view.findViewById(R.id.your_score);
        myRankTV = view.findViewById(R.id.your_rank);
        usersView = view.findViewById(R.id.users_view);
    }
}
