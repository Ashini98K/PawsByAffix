package com.affix.pawsbyaffix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference refUsers;
    private String firebaseUserId;
    private String userVal,usernameval;
    private String emailVal;
    private String passwordVal, phoneval;
    private FirebaseUser userID;
    private EditText fullname, email, password, username;
    private Button registerBtn;
    private TextView singInLink,pplink,tclink;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        registerBtn = (Button) findViewById(R.id.signup);
        username = (EditText) findViewById(R.id.userName);
        fullname = (EditText) findViewById(R.id.fullname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        singInLink = (TextView) findViewById(R.id.signinlink);
        loadingBar = new ProgressDialog(this);
        pplink= findViewById(R.id.pp);
        tclink = findViewById(R.id.tc);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        singInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        pplink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/e/2PACX-1vQAjcedLbE2AJwDsuthSDiyBGztdWG85DW3oaV-gFstp-xrydbTMUvxpoiwjVEqt9gHpZtc-u3dOKtH/pub")));
            }
        });

        tclink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/e/2PACX-1vRFNHkWzszqGlAiGlNlMTPe8oFOTly_xLNY2Hcu3PQ5BH9QmTXOM_zOom74Y8jr_JVgnyysugVGSi8J/pub")));
            }
        });


    }

    private void registerUser() {
        userVal = username.getText().toString().trim();
        usernameval = fullname.getText().toString().trim();
        emailVal = email.getText().toString().trim();
        passwordVal = password.getText().toString().trim();


        if (TextUtils.isEmpty(userVal)) {
            Toast.makeText(this, "username missing", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(emailVal)) {
            Toast.makeText(this, "email missing", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(passwordVal)) {
            Toast.makeText(this, "Password missing", Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(usernameval)) {
            Toast.makeText(this, "Password missing", Toast.LENGTH_LONG).show();
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait we are creating your account..");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            mAuth.createUserWithEmailAndPassword(emailVal, passwordVal)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(RegisterActivity.this, "User Registration Successful", Toast.LENGTH_LONG).show();

                                userID = mAuth.getCurrentUser();
                                addUserData();

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegisterActivity.this, "User Registration failed...Try again ", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();

                            }

                        }
                    });


        }

    }

    private void addUserData() {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (!(datasnapshot.child("Users").child(userID.getUid()).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Uid", userID.getUid());
                    userdataMap.put("username", userVal);
                    userdataMap.put("fullname", usernameval);
                    userdataMap.put("email", emailVal);
                    userdataMap.put("Bio", "Hey I am a Pawser");
                    userdataMap.put("phone", phoneval);
                    userdataMap.put("ProfileImage", "https://firebasestorage.googleapis.com/v0/b/affixpawsapplication-11331.appspot.com/o/ProfileImages%2Fillustration-cute-dog-avatar_79416-105.jpg?alt=media&token=fb952629-a767-4100-a819-459b6cf3784f");
                    userdataMap.put("LoginStatus", "OffLine");
                    userdataMap.put("BackgroundImage", "https://firebasestorage.googleapis.com/v0/b/affixpawsapplication-11331.appspot.com/o/backgroundImages%2F36a-background-gradients-1600.png?alt=media&token=5111d76c-b65e-435c-a94a-9b56fcee8daa");
                    userdataMap.put("search", userVal.toLowerCase());


                    RootRef.child("Users").child(userID.getUid()).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Congratulations!!, your  account has been created ", Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Network error.. Please try again", Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();

                                    }
                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseerror) {

            }
        });


    }

}