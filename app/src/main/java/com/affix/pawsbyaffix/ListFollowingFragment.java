package com.affix.pawsbyaffix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFollowingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFollowingFragment extends Fragment {

    ListFollowingAdapter.RecyclerViewClickListener listener;
    FirebaseRecyclerOptions<ListFollowingModel> options;
    RecyclerView recview;
    String userid;
    ListFollowingAdapter adapter;
    Button followbtn;

    private RelativeLayout followerLink, searchLink;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFollowingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFollwingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFollowingFragment newInstance(String param1, String param2) {
        ListFollowingFragment fragment = new ListFollowingFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_follwing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        followerLink = (RelativeLayout)getView().findViewById(R.id.rr2_1);
        searchLink = (RelativeLayout) getView().findViewById(R.id.rr1_1);

        setOnClickListner();

        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recview = (RecyclerView)getView().findViewById(R.id.recview_list_follwing);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recview.setLayoutManager(linearLayoutManager);

        options =
                new FirebaseRecyclerOptions.Builder<ListFollowingModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Following").child(userid), ListFollowingModel.class)
                        .build();


        adapter = new ListFollowingAdapter(options,listener);


        recview.setAdapter(adapter);


        followerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;

                fragment = new ListFollowersFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment).addToBackStack(null);
                ft.commit();

            }
        });

        searchLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(),SearchActivity.class));
            }
        });
    }

    private void setOnClickListner() {
        listener = new ListFollowingAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {

                final String friendId = options.getSnapshots().get(position).getUid();
                final String friendUserName = options.getSnapshots().get(position).getUsername();
                final String friendFullName = options.getSnapshots().get(position).getFullname();
                final String friendProfileImage = options.getSnapshots().get(position).getProfileImage();
                final String friendBackgroundImage = options.getSnapshots().get(position).getBackgroundImage();
                final String friendEmail = options.getSnapshots().get(position).getEmail();
                final String friendBio = options.getSnapshots().get(position).getBio();
                //get the value of selected user and open the fragment with starting values

                //Clear previous values
                Paper.book().write("FriendUid","");
                Paper.book().write("FriendUserName","");
                Paper.book().write("FriendFullName","");
                Paper.book().write("FriendBio",".");
                Paper.book().write("FriendProfileImage","");
                Paper.book().write("FriendBackgroundImage","");
                Paper.book().write("FriendEmail",".");

                //add new values
                Paper.book().write("FriendUid",friendId);
                Paper.book().write("FriendUserName",friendUserName);
                Paper.book().write("FriendFullName",friendFullName);
                Paper.book().write("FriendBio",friendBio);
                Paper.book().write("FriendProfileImage",friendProfileImage);
                Paper.book().write("FriendBackgroundImage",friendBackgroundImage);
                Paper.book().write("FriendEmail",friendEmail);

                //Open new fragment
                Fragment fragment = new ProfileFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container,fragment).addToBackStack(null);
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