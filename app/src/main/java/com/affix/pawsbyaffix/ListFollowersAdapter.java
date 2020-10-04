package com.affix.pawsbyaffix;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class ListFollowersAdapter extends FirebaseRecyclerAdapter <ListFollowersModel, ListFollowersAdapter.myviewholder> {

    private RecyclerViewClickListener listener;

    public ListFollowersAdapter(@NonNull FirebaseRecyclerOptions<ListFollowersModel> options, RecyclerViewClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull ListFollowersModel ListFollowersModel) {

        final String myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        final String myFullName = Paper.book().read("FullName");
        final String myUserName = Paper.book().read("UserName");
        final String myProfileImage = Paper.book().read("ProfileImage");

        final String usernameval = ListFollowersModel.getUsername();
        final String useruid = ListFollowersModel.getUid();
        final String fullnameval = ListFollowersModel.getFullname();
        final String profimageval = ListFollowersModel.getProfileImage();

        DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("Following").child(myUserId);

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(useruid).exists())
                {
                    holder.followbtn.setText("Unfollow");
                }
                else
                {
                    holder.followbtn.setText("Follow");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.fullname.setText(ListFollowersModel.getFullname());
        holder.username.setText(ListFollowersModel.getUsername());
        holder.email.setText(ListFollowersModel.getEmail());
        holder.bio.setText(ListFollowersModel.getBio());


        Glide.with(holder.image.getContext()).load(ListFollowersModel.getProfileImage()).into(holder.image);
        Glide.with(holder.bgimg.getContext()).load(ListFollowersModel.getBackgroundImage()).into(holder.bgimg);

        holder.followbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.followbtn.getText().toString().equals("Follow"))
                {


                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Following").child(myUserId).child(useruid);

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Uid", useruid);
                    userdataMap.put("username", usernameval);
                    userdataMap.put("fullname", fullnameval);
                    userdataMap.put("ProfileImage", profimageval);

                    ref.updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {

                                DatabaseReference refroot = FirebaseDatabase.getInstance().getReference().child("Followers").child(useruid).child(myUserId);

                                HashMap<String, Object> followingMap = new HashMap<>();
                                followingMap.put("Uid", myUserId);
                                followingMap.put("username", myUserName);
                                followingMap.put("fullname", myFullName);
                                followingMap.put("ProfileImage", myProfileImage);

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

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Following").child(myUserId).child(useruid);

                    ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                DatabaseReference refroot = FirebaseDatabase.getInstance().getReference().child("Followers").child(useruid).child(myUserId);

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

        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_search_user_card, parent, false);
        return new myviewholder(view);
    }


    public class myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image,bgimg;
        TextView fullname,username,uid,email,bio;
        Button followbtn;


        public myviewholder(@NonNull View itemView) {
            super(itemView);

            fullname = (TextView) itemView.findViewById(R.id.searchfullname);
            username = (TextView) itemView.findViewById(R.id.searchusername);
            uid = (TextView) itemView.findViewById(R.id.uidtxt);
            image = (ImageView) itemView.findViewById(R.id.search_profileimage);
            followbtn = (Button) itemView.findViewById(R.id.followbtn);
            email = (TextView) itemView.findViewById(R.id.emailsearch);
            bio = (TextView) itemView.findViewById(R.id.biosearchtxt);
            bgimg = (ImageView) itemView.findViewById(R.id.bgimgsearch);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());

        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }


}