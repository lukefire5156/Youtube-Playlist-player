package com.example.collegekhabri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddVideo extends AppCompatActivity {
    Button video;
    EditText link;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth sAuth;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        video = findViewById(R.id.addvideo);
        link =  findViewById(R.id.videolink);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vlink = link.getText().toString();
                if (TextUtils.isEmpty(vlink)){
                    Toast.makeText(AddVideo.this, "fill up Youtube link column", Toast.LENGTH_LONG).show();
                }
                else{
                    if (!URLUtil.isValidUrl(vlink)){
                        Toast.makeText(AddVideo.this, "Enter the correct Youtube Video link with https initials.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        DatabaseReference emid = mDatabase.child("user").child(acct.getId()).child("Video");
                        String Vl= vlink.replace("m.","");
                        String strNew = Vl.replace("watch?v=", "embed/");
                        emid.child(String.valueOf(System.currentTimeMillis())).setValue(strNew);
                        Toast.makeText(AddVideo.this, "Video added successfully!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AddVideo.this, Videos.class));
                    }
                }

            }
        });

    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(AddVideo.this,Videos.class));
    }


}
