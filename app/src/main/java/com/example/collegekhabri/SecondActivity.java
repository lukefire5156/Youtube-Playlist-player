package com.example.collegekhabri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondActivity extends AppCompatActivity {

    ImageView imageView;
    TextView name,email;
    Button signOut,save,Videos;
    EditText userName;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth sAuth;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    int check=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        imageView = findViewById(R.id.imageView);

        name = findViewById(R.id.textName);
        email = findViewById(R.id.textEmail);
        save =(Button) findViewById(R.id.saveChanges);
        Videos= (Button) findViewById(R.id.Videos);

        Videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this,Videos.class);
                startActivity(intent);
            }
        });

        userName= findViewById(R.id.userName);
        //id = findViewById(R.id.textID);
        signOut = findViewById(R.id.button);

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
                    if (dataSnapshot.hasChild("Username")) {
                        String un = dataSnapshot.child("Username").getValue().toString();
                        userName.setText(un);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SecondActivity.this, "Working fine", Toast.LENGTH_SHORT).show();

                }
            });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username= userName.getText().toString();

                if(TextUtils.isEmpty(username)){
                    Toast.makeText(SecondActivity.this,"Please enter your username.",Toast.LENGTH_LONG).show();
                }
                else{
                    database = FirebaseDatabase.getInstance();
                    mDatabase = database.getReference();
                    //mDatabase.child(uid);

                    DatabaseReference emid = mDatabase.child("user").child(acct.getId());
                    emid.child("Username").setValue(username);
                    //emid.child("flag").setValue("1");
                    Toast.makeText(SecondActivity.this, "Username saved successfully!", Toast.LENGTH_LONG).show();

                }

            }
        });



        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username= userName.getText().toString();
                database = FirebaseDatabase.getInstance();
                mDatabase = database.getReference();
                final DatabaseReference unref = mDatabase.child("user").child(acct.getId());
                unref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("Username")) {
                            check=1;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SecondActivity.this, "Working fine", Toast.LENGTH_SHORT).show();

                    }
                });
                if(check==0){
                    if(TextUtils.isEmpty(username)){
                        Toast.makeText(SecondActivity.this,"Please enter your username.",Toast.LENGTH_LONG).show();
                    }
                    else{
                        database = FirebaseDatabase.getInstance();
                        mDatabase = database.getReference();
                        //mDatabase.child(uid);

                        DatabaseReference emid = mDatabase.child("user").child(acct.getId());
                        emid.child("Username").setValue(username);
                        //emid.child("flag").setValue("1");
                        Toast.makeText(SecondActivity.this, "Username saved successfully!", Toast.LENGTH_LONG).show();
                        signOut();

                    }
                    //Toast.makeText(SecondActivity.this, "Please Enter your Username before signing out.", Toast.LENGTH_LONG).show();
                }else {
                    switch (v.getId()) {
                        case R.id.button:
                            signOut();
                            break;
                    }
                }
            }
        });


        //GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {

            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            database = FirebaseDatabase.getInstance();
            mDatabase = database.getReference();
            //mDatabase.child(uid);
            DatabaseReference emid = mDatabase.child("user").child(personId);
            name.setText("NAME:"+personName);
            email.setText("Email-ID:"+personEmail);
            emid.child("email").setValue(personEmail);
            emid.child("name").setValue(personName);
            emid.child("profilepicURL").setValue(String.valueOf(personPhoto));

            //id.setText(personId);
            Glide.with(this).load(String.valueOf(personPhoto)).into(imageView);


        }


    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SecondActivity.this, "Signed out successfully!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SecondActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onBackPressed(){
        finishAffinity();
    }

}
