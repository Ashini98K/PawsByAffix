package com.affix.pawsbyaffix;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class VerticelUprofileAdapter extends FirebaseRecyclerAdapter <VerticelUprofileModel, VerticelUprofileAdapter.myviewholder> {

    DatabaseReference databaseReference;
    String fuid;
    FirebaseAuth mAuth;
    private String likecount;
    private String profimage;
    private String username;
    private String captionName;
    private String Fullname;
    private boolean isLiked = false;
    public VerticelUprofileAdapter(@NonNull FirebaseRecyclerOptions<VerticelUprofileModel> options, String fuid) {
        super(options);
        this.fuid = fuid;
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull final VerticelUprofileModel VerticelUprofileModel) {

        holder.cardrel.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();

        holder.captionnames.setText(VerticelUprofileModel.getUsername());


        final String postid = VerticelUprofileModel.getPostiddate();
        final String uid = mAuth.getCurrentUser().getUid();

        //Get likes count
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User_Posts").child(VerticelUprofileModel.getUserid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                likecount = String.valueOf(snapshot.child(postid).child("liked_users").getChildrenCount());
                holder.likes.setText(likecount);

                if(snapshot.child(postid).child("liked_users").hasChild(uid))
                {
                    holder.liked_image_red.setVisibility(View.VISIBLE);
                    holder.like_image_outline.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.liked_image_red.setVisibility(View.INVISIBLE);
                    holder.like_image_outline.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    if(uid.equals(fuid))
    {
        holder.deletebtn.setVisibility(View.VISIBLE);
        username =  Paper.book().read("UserName");
        profimage = Paper.book().read("ProfileImage");
        Fullname =  Paper.book().read("FullName");
        captionName = Paper.book().read("UserName");
    }
    else
        {

            holder.deletebtn.setVisibility(View.GONE);
            username =  Paper.book().read("FriendUserName");
            profimage = Paper.book().read("FriendProfileImage");
            Fullname = Paper.book().read("FriendFullName");
            captionName = Paper.book().read("FriendUserName");
        }

        Glide.with(holder.image.getContext()).load(VerticelUprofileModel.getImage()).into(holder.image);
        holder.likes.setText(String.valueOf(VerticelUprofileModel.getLikes()));
        Glide.with(holder.profileimage.getContext()).load(profimage).into(holder.profileimage);
        holder.username.setText(username);
        holder.fullnamesingle.setText(Fullname);
        holder.datetime.setText(VerticelUprofileModel.getDatetime());
        holder.caption.setText(VerticelUprofileModel.getCaption());

        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Posts").child(VerticelUprofileModel.getPostiddate());
                dbref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("User_Posts").child(VerticelUprofileModel.getUserid()).child(VerticelUprofileModel.getPostiddate());

                        dbref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });

        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if(holder.like_image_outline.getVisibility() == view.VISIBLE)
                {
                    holder.like_image_outline.setVisibility(View.INVISIBLE);
                    holder.liked_image_red.setVisibility(View.VISIBLE);
                    like(fuid,VerticelUprofileModel.getPostiddate(),uid,username,VerticelUprofileModel.getImage(),profimage);
                }
                else if (holder.liked_image_red.getVisibility() == view.VISIBLE){
                    holder.liked_image_red.setVisibility(View.INVISIBLE);
                    holder.like_image_outline.setVisibility(View.VISIBLE);
                    unlike(fuid,VerticelUprofileModel.getPostiddate(),uid);
                }

                return false;
            }
        });

        holder.like_image_outline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(holder.like_image_outline.getVisibility() == view.VISIBLE) {

                    holder.liked_image_red.setVisibility(View.VISIBLE);
                    holder.like_image_outline.setVisibility(View.INVISIBLE);

                    like(fuid, VerticelUprofileModel.getPostiddate(), uid, username, VerticelUprofileModel.getImage(), profimage);
                }


            }
        });

        holder.liked_image_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.liked_image_red.getVisibility() == view.VISIBLE)
                {

                    holder.like_image_outline.setVisibility(View.VISIBLE);
                    holder.liked_image_red.setVisibility(View.INVISIBLE);


                    unlike(fuid,VerticelUprofileModel.getPostiddate(),uid);

                }

            }
        });


    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card_home, parent, false);
        return new myviewholder(view);
    }


    public class myviewholder extends RecyclerView.ViewHolder{

        ImageView image,profileimage,liked_image_red,like_image_outline;
        TextView username,caption,likes,datetime,deletebtn,fullnamesingle,captionnames;
        RelativeLayout cardrel;


        public myviewholder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.post_image);
            liked_image_red = (ImageView) itemView.findViewById(R.id.like_image_red);
            like_image_outline = (ImageView) itemView.findViewById(R.id.like_image);
            profileimage = (ImageView) itemView.findViewById(R.id.circle_profile_image);
            username = (TextView) itemView.findViewById(R.id.usernamesingle);
            caption = (TextView) itemView.findViewById(R.id.caption);
            fullnamesingle = (TextView) itemView.findViewById(R.id.fullnamesingle);
            captionnames = (TextView) itemView.findViewById(R.id.captionnames);
            likes = (TextView) itemView.findViewById(R.id.like_count);
            datetime = (TextView) itemView.findViewById(R.id.datettime);
            deletebtn = (TextView) itemView.findViewById(R.id.deletebtn) ;
            cardrel = (RelativeLayout) itemView.findViewById(R.id.relcard);

        }


    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
    public void like(final String uidforpost, final String datetime, final String Uid, final String username, final String postImage, final String profileImage)
    {

        final DatabaseReference likeref = FirebaseDatabase.getInstance().getReference().child("Posts").child(datetime).child("liked_users").child(Uid);
        HashMap<String, Object> likeMap = new HashMap<>();
        likeMap.put("Uid",Uid);
        likeMap.put("dateTime",datetime);
        likeMap.put("Username",username);

        likeref.updateChildren(likeMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    //posts code gose here
                    final DatabaseReference likeref = FirebaseDatabase.getInstance().getReference().child("User_Posts").child(uidforpost).child(datetime).child("liked_users").child(Uid);
                    HashMap<String, Object> likepostMap = new HashMap<>();
                    likepostMap.put("Uid",Uid);
                    likepostMap.put("dateTime",datetime);
                    likepostMap.put("Username",username);

                    likeref.updateChildren(likepostMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public void unlike(final String uidforpost, final String datetime, final String Uid )
    {
        DatabaseReference likeref = FirebaseDatabase.getInstance().getReference().child("Posts").child(datetime).child("liked_users").child(Uid);

        likeref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    DatabaseReference likeref = FirebaseDatabase.getInstance().getReference().child("User_Posts").child(uidforpost).child(datetime).child("liked_users").child(Uid);

                    likeref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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