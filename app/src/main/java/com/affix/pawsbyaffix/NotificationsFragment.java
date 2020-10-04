package com.affix.pawsbyaffix;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {

    RecyclerView recview;
    NotificationAdapter adapter;
    NotificationAdapter.RecyclerViewClickListener listener;
    FirebaseRecyclerOptions<NotificationModel> options;
    String Uid;
    String receiverId;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickListner();
        Uid = Paper.book().read("Uid");

        receiverId = Paper.book().read("ParticipantId");

        recview = (RecyclerView) getView().findViewById(R.id.recview_notification);

        LinearLayoutManager linearLayoutManager = (new LinearLayoutManager(getActivity().getApplicationContext()));
        recview.setLayoutManager(linearLayoutManager);

        options =
                new FirebaseRecyclerOptions.Builder<NotificationModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Notifications").child(Uid).orderByChild("NotificationId"), NotificationModel.class)
                        .build();

        adapter = new NotificationAdapter(options, listener);

    new ItemTouchHelper(iteSimpleCallback).attachToRecyclerView(recview);
        recview.setAdapter(adapter);
    }

    private void setOnClickListner() {
        listener = new NotificationAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {

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
    ItemTouchHelper.SimpleCallback iteSimpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            //remove the notification
            int NotificationId = options.getSnapshots().get(viewHolder.getAdapterPosition()).getNotificationId() * -1;

            DatabaseReference rootref = FirebaseDatabase.getInstance().getReference().child("Notifications").child(Uid).child(String.valueOf(NotificationId));

            rootref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });

        }
    };

}