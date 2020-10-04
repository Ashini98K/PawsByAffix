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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessageAdapter extends FirebaseRecyclerAdapter <MessageModel, MessageAdapter.myviewholder> {

    private RecyclerViewClickListener listener;
    private String newmessagecount;

    public MessageAdapter(@NonNull FirebaseRecyclerOptions<MessageModel> options, RecyclerViewClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull MessageModel MessageModel) {
        final String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Chats").child(Uid).child(MessageModel.getParticipant2());

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("unread").exists())
                {
                    newmessagecount = snapshot.child("unread").getValue().toString();
                    holder.unreadrel.setVisibility(View.VISIBLE);
                    holder.unread.setText(newmessagecount);
                }
                else{
                    holder.unreadrel.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.username.setText(MessageModel.getUsername());
        holder.time.setText(MessageModel.getTime());
        holder.lastmsg.setText(MessageModel.getLastMessage());
        Glide.with(holder.image.getContext()).load(MessageModel.getProfileImage()).into(holder.image);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card_chat_select_fragments, parent, false);
        return new myviewholder(view);
    }


    public class myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView username,time,lastmsg,unread;
        RelativeLayout unreadrel;


        public myviewholder(@NonNull View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.usernamechattxt);
            time = (TextView) itemView.findViewById(R.id.datettimechattxt);
            lastmsg = (TextView) itemView.findViewById(R.id.lastmsgchattxt);
            unread = (TextView) itemView.findViewById(R.id.unreadtxt);
            image = (ImageView) itemView.findViewById(R.id.ProfileImagechat);
            unreadrel = (RelativeLayout) itemView.findViewById(R.id.unreadrel);

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