package com.affix.pawsbyaffix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private LinearLayout top;
    private RelativeLayout notificationRel,bottom;
    private TextView notificationtxt;
    private ImageView homeLink, searchLink,notificationLink,messageLink;
    private CircleImageView profileLink;
    private String Uid;
    TextView usernameTextbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        ImageView notificatiomAlert;

        notificationRel = (RelativeLayout) findViewById(R.id.notificationRel);
        notificationtxt = (TextView) findViewById(R.id.notification);

        Intent intent = getIntent();

        Uid = Paper.book().read("Uid");

        DatabaseReference rootref = FirebaseDatabase.getInstance().getReference().child("Notifications");

        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(Uid).getChildrenCount() > 0)
                {
                    String count = String.valueOf(Integer.parseInt(snapshot.child("NewNotificationCount").getValue().toString())*-1);
                    notificationRel.setBackgroundResource(R.drawable.ic_re_bell);
                    notificationtxt.setVisibility(View.VISIBLE);
                    notificationtxt.setText(count);
                }
                else{
                    notificationRel.setBackgroundResource(R.drawable.ic_outline_notifications);
                    notificationtxt.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(intent.getStringExtra("key").equals("gotosearch"))
        {
           //Open friend profilefragment fragment
                Fragment fragment = new ProfileFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment).addToBackStack(null);
                ft.commit();
        }
        else if(intent.getStringExtra("key").equals("gotoprofile"))
        {
            //open userprofile fragment
            Fragment fragment;
            fragment = new UserProfileFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, fragment).addToBackStack(null);
            ft.commit();
        }
        else
        {

            Fragment fragment;
            fragment = new HomeFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, fragment).addToBackStack(null);
            ft.commit();
        }



        String username = Paper.book().read("UserName");
        String profileimage = Paper.book().read("ProfileImage");
        usernameTextbox = findViewById(R.id.usernametxtbox);
        bottom = (RelativeLayout)findViewById(R.id.bottom);
        top = (LinearLayout)findViewById(R.id.top);

        homeLink = (ImageView)findViewById(R.id.home);
        searchLink = (ImageView)findViewById(R.id.search);

        messageLink = (ImageView)findViewById(R.id.messages);
        profileLink = (CircleImageView)findViewById(R.id.profileImg);


        //setting the username
        usernameTextbox.setText(username);
        Glide.with(profileLink.getContext()).load(profileimage).into(profileLink);


        homeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;
                fragment = new HomeFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment).addToBackStack(null);
                ft.commit();
            }
        });

        searchLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;

                fragment = new ListFollowersFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment).addToBackStack(null);
                ft.commit();

            }
        });

        notificationRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;

                fragment = new NotificationsFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment).addToBackStack(null);
                ft.commit();
            }
        });

        messageLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;

                fragment = new MessageFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment).addToBackStack(null);
                ft.commit();
            }
        });

        profileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;

                fragment = new UserProfileFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment).addToBackStack(null);
                ft.commit();
            }
        });


    }


}