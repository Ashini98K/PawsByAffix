package com.affix.pawsbyaffix;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {
    DatabaseReference rootref;
    TextView fullnametxt;
    FirebaseAuth mAuth;
    TextView followerCount;
    String uid;
    TextView followingcounttxt;
    ImageView BackgroundImage,profileImage;
    TextView postcount;
    TextView usernametxt,email,bio;
    RecyclerView recview;
    UserProfileAdapter adapter;
    String firebaseChild ="Places";
    String header = "";
    Button editprofile;

    UserProfileAdapter.RecyclerViewClickListener listener;
    FirebaseRecyclerOptions<UserProfileModel> options;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user_profile, container, false);

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BackgroundImage = (ImageView)getView().findViewById(R.id.user_main_image);

        String bgurl = Paper.book().read("BackgroundImage");

        Glide.with(BackgroundImage.getContext()).load(bgurl).into(BackgroundImage);

        setOnClickListner();

        profileImage = (ImageView)getView().findViewById(R.id.user_profile_img);

        String profimg = Paper.book().read("ProfileImage");

        Glide.with(profileImage.getContext()).load(profimg).into(profileImage);

        fullnametxt = (TextView) view.findViewById(R.id.fullNameprofiletxt);
        usernametxt = getView().findViewById(R.id.usernameprof);
        email = getView().findViewById(R.id.emailprof);
        bio = getView().findViewById(R.id.bioprofiletxt);

        String username = Paper.book().read("UserName");
        String fullname = Paper.book().read("FullName");
        String emailtxt = Paper.book().read("Email");
        String Biotxt = Paper.book().read("Bio");

        fullnametxt.setText(fullname);
        usernametxt.setText(username);
        email.setText(emailtxt);
        bio.setText(Biotxt);

        uid = mAuth.getInstance().getUid();
        //get datasnap shot t0 get the followers following and post counts
        followerCount = getView().findViewById(R.id.followercounttxt);


        followingcounttxt =  getView().findViewById(R.id.followingcounttxt);
        postcount =  getView().findViewById(R.id.postscount);
        editprofile = (Button)getView().findViewById(R.id.editProfilebtn);
        //referring to the uid node in followers
        getFollowerCount();
        getFollowingCount();
        getPostCount();

        recview = (RecyclerView)getView().findViewById(R.id.recview_user_profile);

        GridLayoutManager gridLayoutManager = (new GridLayoutManager(getActivity().getApplicationContext(),3));
        recview.setLayoutManager(gridLayoutManager);
        options =
                new FirebaseRecyclerOptions.Builder<UserProfileModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("User_Posts").child(uid).orderByChild("PostNumber"), UserProfileModel.class)
                        .build();


        adapter = new UserProfileAdapter(options,listener);


        recview.setAdapter(adapter);

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new EditProfileFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment).addToBackStack(null);
                ft.commit();

            }
        });


    }

    public void getFollowerCount()
    {
        rootref = FirebaseDatabase.getInstance().getReference().child("Followers").child(uid);

        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long followerscount = snapshot.getChildrenCount();
                String flw = String.valueOf(followerscount);
                followerCount.setText(flw);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getPostCount()
    {
        rootref = FirebaseDatabase.getInstance().getReference().child("User_Posts").child(uid);

        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long postcountval = snapshot.getChildrenCount();
                String pc = String.valueOf(postcountval);
                postcount.setText(pc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getFollowingCount()
    {
        rootref = FirebaseDatabase.getInstance().getReference().child("Following").child(uid);

        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long followingcount = snapshot.getChildrenCount();
                String flwing = String.valueOf(followingcount);
                followingcounttxt.setText(flwing);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    //onclick listner for the items of recycler view

    private void setOnClickListner() {
        listener = new UserProfileAdapter.RecyclerViewClickListener() {
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