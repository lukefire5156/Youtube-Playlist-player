package com.example.collegekhabri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class Videos extends AppCompatActivity {

    RecyclerView recyclerView;
    //ImageView addV;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth sAuth;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        //addV = findViewById(R.id.add);
        recyclerView = (RecyclerView) findViewById(R.id.add);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
                startActivity(new Intent(Videos.this,AddVideo.class));
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        final DatabaseReference unref = mDatabase.child("user").child(acct.getId());
        unref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Video")) {
                    Vector<YouTubeVideos> youtubeVideos = new Vector<YouTubeVideos>();
                    for (DataSnapshot snapshot : dataSnapshot.child("Video").getChildren()){
                        String yt= snapshot.getValue().toString();


                        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\""+yt+"\" frameborder=\"0\" allowfullscreen></iframe>") );

                        VideoAdapter videoAdapter = new VideoAdapter(youtubeVideos);

                        recyclerView.setAdapter(videoAdapter);
                    }


                }
                else{
                    Vector<YouTubeVideos> youtubeVideos = new Vector<YouTubeVideos>();
                    VideoAdapter videoAdapter = new VideoAdapter(youtubeVideos);

                    recyclerView.setAdapter(videoAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/bSMZknDI6bg\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/KyJ71G2UxTQ\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/y8Rr39jKFKU\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/8Hg1tqIwIfI\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/uhQ7mh_o_cM\" frameborder=\"0\" allowfullscreen></iframe>") );

        VideoAdapter videoAdapter = new VideoAdapter(youtubeVideos);

        recyclerView.setAdapter(videoAdapter);*/

    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(Videos.this,SecondActivity.class));
    }

}
