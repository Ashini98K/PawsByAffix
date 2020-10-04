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

import io.paperdb.Paper;

public class ListFollowingAdapter extends FirebaseRecyclerAdapter <ListFollowingModel, ListFollowingAdapter.myviewholder> {

    private RecyclerViewClickListener listener;

    public ListFollowingAdapter(@NonNull FirebaseRecyclerOptions<ListFollowingModel> options, RecyclerViewClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull ListFollowingModel ListFollowingModel) {

        final String myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        final String myFullName = Paper.book().read("FullName");
        final String myUserName = Paper.book().read("UserName");
        final String myProfileImage = Paper.book().read("ProfileImage");

        final String usernameval = ListFollowingModel.getUsername();
        final String useruid = ListFollowingModel.getUid();
        final String fullnameval = ListFollowingModel.getFullname();
        final String profimageval = ListFollowingModel.getProfileImage();

        DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("Following").child(myUserId);

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.child(useruid).exists())
                    {
                        holder.followbtn.setText("Unfollow");
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.fullname.setText(ListFollowingModel.getFullname());
        holder.username.setText(ListFollowingModel.getUsername());
        holder.email.setText(ListFollowingModel.getEmail());
        holder.bio.setText(ListFollowingModel.getBio());


        Glide.with(holder.image.getContext()).load(ListFollowingModel.getProfileImage()).into(holder.image);
        Glide.with(holder.bgimg.getContext()).load(ListFollowingModel.getBackgroundImage()).into(holder.bgimg);

        holder.followbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            bgimg = (ImageView) itemView.findViewById(R.id.bgimgsearch);
            followbtn = (Button) itemView.findViewById(R.id.followbtn);
            email = (TextView) itemView.findViewById(R.id.emailsearch);
            bio = (TextView) itemView.findViewById(R.id.biosearchtxt);

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