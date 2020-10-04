package com.affix.pawsbyaffix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {
    Timer timer;
    private FirebaseAuth mAuth=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Paper.init(this);
        mAuth = FirebaseAuth.getInstance();
    }



    @Override
    public void onStart() {
        super.onStart();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null)
                {

                    getUserData();


                } else{

                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        } ,3000);

    }

    public void getUserData(){

        //make a refernce to the user node of the database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        //make a reference to the uid node of the database
        DatabaseReference rootref = databaseReference.child(FirebaseAuth.getInstance().getUid());

        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get values from the child names pof the sanpshot
                String Username = snapshot.child("username").getValue().toString();
                String ProfileImage = snapshot.child("ProfileImage").getValue().toString();
                String fullname = snapshot.child("fullname").getValue().toString();
                String Uid = snapshot.child("Uid").getValue().toString();
                String bgimg = snapshot.child("BackgroundImage").getValue().toString();
                String Bio = snapshot.child("Bio").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                //write the data to paper db
                Paper.book().write("UserName",Username);
                Paper.book().write("ProfileImage",ProfileImage);
                Paper.book().write("BackgroundImage",bgimg);
                Paper.book().write("FullName",fullname);
                Paper.book().write("Email",email);
                Paper.book().write("Bio",Bio);
                Paper.book().write("Uid",Uid);

                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                intent.putExtra("key","splash");
                startActivity(intent);
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}