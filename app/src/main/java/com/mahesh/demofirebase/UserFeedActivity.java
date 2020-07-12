package com.mahesh.demofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserFeedActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Feed> stories;
    private DatabaseReference feedRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        recyclerView=findViewById(R.id.recyclerView_user_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        stories = new ArrayList<Feed>();
        feedRef = FirebaseDatabase.getInstance().getReference("feeds");

        feedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()) {
                    Feed feed = data.getValue(Feed.class);
                    if (feed.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        stories.add(feed);
                    }
                }
                System.out.println("sdadas "+stories.size());
                ImageAdapter adapter = new ImageAdapter(getApplicationContext(),stories);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
