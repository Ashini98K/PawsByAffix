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

class myAdapter extends FirebaseRecyclerAdapter<model, myAdapter.myviewholder> {

    DatabaseReference databaseReference;
    String fuid;
    int notificationNumber,count;
    FirebaseAuth mAuth;
    private String likecount;
    private boolean isfollowing =false;
    private boolean isLiked = false;
    private boolean notliked = true;
    private String likeCount,firstname,secondname;


    public myAdapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull final myAdapter.myviewholder holder, int position, @NonNull final model model) {
        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();

        fuid =  model.getUserid();





        DatabaseReference getuserref = FirebaseDatabase.getInstance().getReference().child("Following").child(uid);

        getuserref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(fuid).exists() || fuid.equals(uid))
                {

                    isfollowing = true;

                }
                else
                {
                    isfollowing = false;
                   holder.cardRel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if(1==1)
        {



            Glide.with(holder.image.getContext()).load(model.getImage()).into(holder.image);
            holder.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            final String postuserid = model.getUserid();
            final String postid = model.getPostiddate();
            final String username =  Paper.book().read("UserName");
            final String postImage = model.getImage();
            String fullname =  Paper.book().read("FullName");
            final String profimage = Paper.book().read("ProfileImage");
            final String postiddate =  model.getPostiddate();

            holder.username.setText(model.getUsername());
            holder.fullname.setText(model.getFullname());

            holder.captionname.setText(model.getUsername());
            holder.datetime.setText(model.getDatetime());
            holder.caption.setText(model.getCaption());

            isfollowing = true;
            if(model.getUserid().equals(uid))
            {
                holder.deletebtn.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.deletebtn.setVisibility(View.INVISIBLE);
            }



            Glide.with(holder.profileimage.getContext()).load(model.getProfileimage()).into(holder.profileimage);

            //Get likes count
            databaseReference = FirebaseDatabase.getInstance().getReference().child("User_Posts").child(postuserid);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    likeCount = String.valueOf(snapshot.child(postid).child("liked_users").getChildrenCount());
                    holder.likes.setText(likeCount);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            holder.deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Posts").child(model.getPostiddate());

                    dbref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("User_Posts").child(model.getUserid()).child(model.getPostiddate());

                            dbref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    });
                }
            });

            //databaseReference = FirebaseDatabase.getInstance().getReference().child("Following").child("ids");



            databaseReference = FirebaseDatabase.getInstance().getReference().child("User_Posts").child(postuserid);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {



                    if(snapshot.child(postid).child("liked_users").hasChild(uid))
                    {
                        holder.liked_image_red.setVisibility(View.VISIBLE);
                        holder.like_image_outline.setVisibility(View.INVISIBLE);
                        notliked = false;
                    }
                    else
                    {
                        holder.liked_image_red.setVisibility(View.INVISIBLE);
                        holder.like_image_outline.setVisibility(View.VISIBLE);
                        notliked = true;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            holder.image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {



                    if(holder.like_image_outline.getVisibility() == view.VISIBLE)
                    {

                        holder.like_image_outline.setVisibility(View.INVISIBLE);
                        holder.liked_image_red.setVisibility(View.VISIBLE);
                        notliked = false;
                        like(fuid,postiddate,uid,username,postImage,profimage);

                    }
                    else if (holder.liked_image_red.getVisibility() == view.VISIBLE){

                        holder.liked_image_red.setVisibility(View.INVISIBLE);
                        holder.like_image_outline.setVisibility(View.VISIBLE);
                        notliked = true;
                        unlike(fuid,postiddate,uid);

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

                        notliked = false;
                        like(fuid, postiddate, uid, username, postImage, profimage);
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

                        notliked = true;
                        unlike(fuid,postiddate,uid);

                    }

                }
            });
        }


    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card_home, parent, false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{

        ImageView image,profileimage,liked_image_red,like_image_outline;
        TextView username,caption,likes,datetime,fullname,captionname,deletebtn;
        RelativeLayout cardRel;

        public myviewholder(@NonNull View itemView) {
            super(itemView);


            image = (ImageView) itemView.findViewById(R.id.post_image);
            liked_image_red = (ImageView) itemView.findViewById(R.id.like_image_red);
            like_image_outline = (ImageView) itemView.findViewById(R.id.like_image);
            profileimage = (ImageView) itemView.findViewById(R.id.circle_profile_image) ;
            deletebtn = (TextView) itemView.findViewById(R.id.deletebtn) ;
            cardRel = (RelativeLayout) itemView.findViewById(R.id.relcard);
            username = (TextView) itemView.findViewById(R.id.usernamesingle);
            fullname = (TextView) itemView.findViewById(R.id.fullnamesingle);
            captionname = (TextView) itemView.findViewById(R.id.captionnames);
            caption = (TextView) itemView.findViewById(R.id.caption);
            likes = (TextView) itemView.findViewById(R.id.like_count);
            datetime = (TextView) itemView.findViewById(R.id.datettime);

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


                                DatabaseReference notifyref = FirebaseDatabase.getInstance().getReference().child("Notifications");
                                notifyref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if(snapshot.child("NewNotificationCount").exists())
                                        {
                                            notificationNumber = (Integer.parseInt(snapshot.child("NewNotificationCount").getValue().toString()) +(-1));
                                        }
                                        else
                                        {
                                            notificationNumber = -1;
                                        }

                                        if(notificationNumber != 0 && !(uidforpost.equals(Uid)))
                                        {
                                            DatabaseReference rootref = FirebaseDatabase.getInstance().getReference().child("Notifications");

                                            HashMap<String, Object> countMap = new HashMap<>();
                                            countMap.put("NewNotificationCount",notificationNumber);

                                            rootref.updateChildren(countMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(notificationNumber != 0)
                                                    {
                                                        String NewNotificationMessage = username + " has liked your post";

                                                        DatabaseReference notifyref = FirebaseDatabase.getInstance().getReference().child("Notifications").child(fuid).child(String.valueOf(notificationNumber*-1));

                                                        HashMap<String, Object> notifMap = new HashMap<>();
                                                        notifMap.put("NotificationWitnessId",Uid);
                                                        notifMap.put("notificationTime",datetime);
                                                        notifMap.put("NotificationWitnessName",username);
                                                        notifMap.put("NotificationTargetImage",postImage);
                                                        notifMap.put("NotificationWitnessProfileImage",profileImage);
                                                        notifMap.put("NewNotificationMessage",NewNotificationMessage);
                                                        notifMap.put("NotificationId",notificationNumber);

                                                        notifyref.updateChildren(notifMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {



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
                        }
                    });




                }
            }
        });
    }

    //remove unlike

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