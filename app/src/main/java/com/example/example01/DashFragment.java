package com.example.example01;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashFragment extends Fragment {

    //firebase auth object
    private static final String TAG = "VMActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabase;
    private FirebaseDatabase firebaseDatabase;

    private RecyclerView recyclerView;
    private ListView listView;
    private List<VMData> vmlist = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_dash, container, false);

        recyclerView = (RecyclerView)rootview.findViewById(R.id.recyclerView);


        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
//        if (firebaseAuth.getCurrentUser() == null) {
//            finish();
//            startActivity(new Intent(getActivity(), LoginActivity.class));
//        }

        //User profile 가져오기
        firebaseUser = firebaseAuth.getCurrentUser();


        //firebase 정의
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase =  firebaseDatabase.getReference(firebaseUser.getUid()).child("KT").child("Resources").child("VM");


        //DB에서 정보 갖고 오기
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vmlist.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Object value = snapshot.getValue();
                    String str = String.valueOf(value);
                    VMData vm = snapshot.getValue(VMData.class);
                    vmlist.add(vm);
                }
                setadapter(vmlist);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return rootview;
    }
    public void setadapter(List<VMData> vmlist) {
        VMAdapter vmadapter = new VMAdapter(getContext(), vmlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(vmadapter);

    }
}

