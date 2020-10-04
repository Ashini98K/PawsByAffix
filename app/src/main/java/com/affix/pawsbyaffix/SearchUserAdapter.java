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

public class SearchUserAdapter extends FirebaseRecyclerAdapter <SearchUserModel, SearchUserAdapter.myviewholder> {

    private RecyclerViewClickListener listener;

    public SearchUserAdapter(@NonNull FirebaseRecyclerOptions<SearchUserModel> options, RecyclerViewClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull SearchUserModel SearchUserModel) {

        final String myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        final String myFullName = Paper.book().read("FullName");
        final String myUserName = Paper.book().read("UserName");
        final String myProfileImage = Paper.book().read("ProfileImage");
        final String myEmail = Paper.book().read("Email");
        final String myBgImg = Paper.book().read("BackgroundImage");
        final String myBio = Paper.book().read("Bio");

        final String usernameval = SearchUserModel.getUsername();
        final String useruid = SearchUserModel.getUid();
        final String fullnameval = SearchUserModel.getFullname();
        final String profimageval = SearchUserModel.getProfileImage();

        DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("Following").child(myUserId);

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(useruid.equals(myUserId))
                {
                    holder.followbtn.setVisibility(View.INVISIBLE);
                }
                else{
                    if(snapshot.child(useruid).exists())
                    {
                        holder.followbtn.setText("Following");
                    }
                    else
                    {
                        holder.followbtn.setText("Follow");
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.fullname.setText(SearchUserModel.getFullname());
        holder.username.setText(SearchUserModel.getUsername());
        holder.email.setText(SearchUserModel.getEmail());
        holder.bio.setText(SearchUserModel.getBio());


        Glide.with(holder.image.getContext()).load(SearchUserModel.getProfileImage()).into(holder.image);
        Glide.with(holder.bgimg.getContext()).load(SearchUserModel.getBackgroundImage()).into(holder.bgimg);

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
                    userdataMap.put("Bio", myBio);
                    userdataMap.put("BackgroundImage", myBgImg);
                    userdataMap.put("email", myEmail);

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
                                followingMap.put("Bio", myBio);
                                followingMap.put("BackgroundImage", myBgImg);
                                followingMap.put("email", myEmail);

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