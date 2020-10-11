package com.affix.pawsbyaffix;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerticelUprofileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerticelUprofileFragment extends Fragment {
    RecyclerView recview;
    VerticelUprofileAdapter adapter;
    String firebaseChild ="Users";
    String header = "";
    private String uid;
    FirebaseRecyclerOptions<VerticelUprofileModel> options;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VerticelUprofileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SinglePostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VerticelUprofileFragment newInstance(String param1, String param2) {
        VerticelUprofileFragment fragment = new VerticelUprofileFragment();
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
        return inflater.inflate(R.layout.fragment_single_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String friendId = Paper.book().read("FriendUid");
        if(friendId != null)
        {
            uid = Paper.book().read("FriendUid");
            Log.i("g","gg");
            Paper.book().delete("FriendUid");
        }
        else{
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }


        recview = (RecyclerView)getView().findViewById(R.id.recview_user_profile);
        final int pos = Paper.book().read("position");
        Paper.book().delete("position");
        final LinearLayoutManager linearLayoutManager = (new LinearLayoutManager(getActivity().getApplicationContext()));
        recview.setLayoutManager(linearLayoutManager);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                SnapHelper snapHelper = new PagerSnapHelper();
//                snapHelper.attachToRecyclerView(recview);
//                recview.setHasFixedSize(true);
                recview.smoothScrollToPosition(pos);

            }
        }, 200);
        options =
                new FirebaseRecyclerOptions.Builder<VerticelUprofileModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("User_Posts").child(uid).orderByChild("PostNumber"), VerticelUprofileModel.class)
                        .build();


        adapter = new VerticelUprofileAdapter(options,uid);
        recview.setAdapter(adapter);

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

    @Override
    public void onResume() {
        super.onResume();


    }
}