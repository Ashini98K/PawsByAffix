package com.affix.pawsbyaffix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private String Uid;
    private String friendId;
    private DatabaseReference rootref;
    private ImageView ProfileImageView,BackgroundImageView;
    private Button MessageButton,FollowButton;
    private TextView UserNamTextView
            ,FullNameTextView,BioTextView
            ,EmailTextView,FollowersCountTextView,FollowingCountTextView
            ,PostCountTextView,FollowersFriendTextView,FollowingFriendTextView,PostFriendTextView;

    Adapterclass adapter;
    Adapterclass.RecyclerViewClickListener listener;
    FirebaseRecyclerOptions<ModelClass> options;
    RecyclerView recview;

    private String friendUserName;
    private String friendFullName;
    private String friendProfileImage;
    private String friendBackgroundImage;
    private String friendEmail;
    private String friendBio;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        startActivity(new Intent(getActivity().getApplicationContext(),SearchActivity.class));
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        Paper.book().write("fromProf","fromprofile");

        setOnClickListner();

        //set values from the Paper db
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friendId = Paper.book().read("FriendUid");
         friendUserName =  Paper.book().read("FriendUserName");
         friendFullName = Paper.book().read("FriendFullName");
         friendProfileImage = Paper.book().read("FriendProfileImage");
         friendBackgroundImage =  Paper.book().read("FriendBackgroundImage");
         friendEmail = Paper.book().read("FriendEmail");
         friendBio = Paper.book().read("FriendBio");

        //set references to views
        UserNamTextView = getView().findViewById(R.id.usernamefriendtxt);
        FullNameTextView = getView().findViewById(R.id.fullNamefriendtxt);
        EmailTextView = getView().findViewById(R.id.emailfriendtxt);
        BioTextView = getView().findViewById(R.id.biofriendtxt);
        FollowersFriendTextView = getView().findViewById(R.id.followersfriendtxt);
        FollowingFriendTextView = getView().findViewById(R.id.followingfriendtxt);
        PostFriendTextView = getView().findViewById(R.id.postsfriendtxt);
        FollowersCountTextView = getView().findViewById(R.id.followerfriendcounttxt);
        FollowingCountTextView = getView().findViewById(R.id.followingfriendcounttxt);
        PostCountTextView  = getView().findViewById(R.id.postsfriendcounttxt);

        //Buttons
        FollowButton = (Button)getView().findViewById(R.id.followuserbtn);
        MessageButton = (Button)getView().findViewById(R.id.messageuserbtn);

        //ImageViews
        ProfileImageView = (ImageView) getView().findViewById(R.id.friend_profile_img);
        BackgroundImageView = (ImageView) getView().findViewById(R.id.friend_main_image);

        UserNamTextView.setText(friendUserName);
        FullNameTextView.setText(friendFullName);
        EmailTextView.setText(friendEmail);
        BioTextView.setText(friendBio);

        Glide.with(ProfileImageView.getContext()).load(friendProfileImage).into(ProfileImageView);
        Glide.with(BackgroundImageView.getContext()).load(friendBackgroundImage).into(BackgroundImageView);

        getFollowerCount();
        getFollowingCount();
        getPostCount();



        DatabaseReference rootreference = FirebaseDatabase.getInstance().getReference().child("Following").child(Uid);

        rootreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(friendId).exists())
                {
                    FollowButton.setText("Unfollow");
                }
                else
                {
                    FollowButton.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recview = (RecyclerView)getView().findViewById(R.id.recview_friend_profile);

        GridLayoutManager gridLayoutManager = (new GridLayoutManager(getActivity().getApplicationContext(),3));
        recview.setLayoutManager(gridLayoutManager);
        options =
                new FirebaseRecyclerOptions.Builder<ModelClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("User_Posts").child(friendId).orderByChild("PostNumber"), ModelClass.class)
                        .build();


        adapter = new Adapterclass(options,listener);


        recview.setAdapter(adapter);

        FollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Follow the user
                followUnfollowUser();
            }
        });

        MessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open chat with user
                openchat() ;
            }
        });




    }

    private void openchat() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd,yyyy");

        final String datetime = currentDate.format(calendar.getTime());
        final String chatNo = "0";
        final String myUserName = Paper.book().read("UserName");
        final String myProfileImage = Paper.book().read("ProfileImage");
        Paper.book().write("ParticipantId", friendId);

        DatabaseReference checkChatref = FirebaseDatabase.getInstance().getReference().child("Chats").child(Uid);

        checkChatref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(friendId).exists())
                {
                    DatabaseReference dbref2 = FirebaseDatabase.getInstance().getReference().child("Chats").child(Uid).child(friendId).child("unread");

                    dbref2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                startActivity(new Intent(getActivity().getApplicationContext(), MessageChatActivity.class));
                            }

                        }
                    });
                }
                else
                {
                    DatabaseReference msgref = FirebaseDatabase.getInstance().getReference().child("Chats").child(Uid).child(friendId);
                    HashMap<String, Object>  chatMap = new HashMap<>();
                    chatMap.put("ChatNo",chatNo);
                    chatMap.put("Participant1",Uid);
                    chatMap.put("Participant2",friendId);
                    chatMap.put("username",friendUserName);
                    chatMap.put("ProfileImage",friendProfileImage);
                    chatMap.put("MessageNo","0");
                    chatMap.put("LastMessage"," ");
                    chatMap.put("Time",datetime);

                    msgref.updateChildren(chatMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                DatabaseReference msgref2 = FirebaseDatabase.getInstance().getReference().child("Chats").child(friendId).child(Uid);
                                HashMap<String, Object>  chat2Map = new HashMap<>();
                                chat2Map.put("ChatNo",chatNo);
                                chat2Map.put("Participant1",friendId);
                                chat2Map.put("Participant2",Uid);
                                chat2Map.put("MessageNo","0");
                                chat2Map.put("username",myUserName);
                                chat2Map.put("ProfileImage",myProfileImage);
                                chat2Map.put("LastMessage"," ");
                                chat2Map.put("Time",datetime);


                                msgref2.updateChildren(chat2Map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        startActivity(new Intent(getActivity().getApplicationContext(), MessageChatActivity.class));
                                    }
                                });
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void followUnfollowUser() {

        if(FollowButton.getText().toString().equals("Follow"))
        {


            final String username = Paper.book().read("UserName");
            final String fullname = Paper.book().read("FullName");
            final String emailtxt = Paper.book().read("Email");
            final String Biotxt = Paper.book().read("Bio");
            final String profimg = Paper.book().read("ProfileImage");
            final String bgurl = Paper.book().read("BackgroundImage");

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Following").child(Uid).child(friendId);

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("Uid", friendId);
            userdataMap.put("username", friendUserName);
            userdataMap.put("fullname", friendFullName);
            userdataMap.put("ProfileImage", friendProfileImage);
            userdataMap.put("Bio", friendBio);
            userdataMap.put("BackgroundImage", friendBackgroundImage);
            userdataMap.put("email", friendEmail);

            ref.updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {

                        DatabaseReference refroot = FirebaseDatabase.getInstance().getReference().child("Followers").child(friendId).child(Uid);

                        HashMap<String, Object> followingMap = new HashMap<>();
                        followingMap.put("Uid", Uid);
                        followingMap.put("username", username);
                        followingMap.put("fullname", fullname);
                        followingMap.put("ProfileImage", profimg);
                        followingMap.put("Bio", Biotxt);
                        followingMap.put("BackgroundImage", bgurl);
                        followingMap.put("email", emailtxt);

                        refroot.updateChildren(followingMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {

                                }
                            }
                        });

                    }
                }
            });



        }
        else{

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Following").child(Uid).child(friendId);

            ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        DatabaseReference refroot = FirebaseDatabase.getInstance().getReference().child("Followers").child(friendId).child(Uid);

                        refroot.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {

                                }
                            }
                        });
                    }
                }
            });


        }
    }

    public void getFollowerCount()
    {
        rootref = FirebaseDatabase.getInstance().getReference().child("Followers").child(friendId);

        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long followerscount = snapshot.getChildrenCount();
                String flw = String.valueOf(followerscount);
                FollowersCountTextView.setText(flw);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getPostCount()
    {
        rootref = FirebaseDatabase.getInstance().getReference().child("User_Posts").child(friendId);

        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long postcountval = snapshot.getChildrenCount();
                String pc = String.valueOf(postcountval);
                PostCountTextView.setText(pc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getFollowingCount()
    {
        rootref = FirebaseDatabase.getInstance().getReference().child("Following").child(friendId);

        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                long followingcount = snapshot.getChildrenCount();
                String flwing = String.valueOf(followingcount);
                FollowingCountTextView.setText(flwing);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //onclick listner for the items of recycler view

    private void setOnClickListner() {
        listener = new Adapterclass.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {

                int x = recview.getLayoutManager().getPosition(v);
                Paper.book().write("position",x);
                Fragment fragment = new VerticelUprofileFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment).addToBackStack(null);
                ft.commit();
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("start","fragment started");
        adapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.i("stop","fragment stopped");
        adapter.stopListening();
    }


}