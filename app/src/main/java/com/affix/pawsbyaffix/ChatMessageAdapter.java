package com.affix.pawsbyaffix;

import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import io.paperdb.Paper;

class ChatMessageAdapter extends FirebaseRecyclerAdapter<ChatMessageModel, ChatMessageAdapter.myviewholder> {
    private FirebaseAuth mauth;
    private String Uid;
    private String receiverId;
    private ChatMessageAdapter.RecyclerViewClickListener listener;



    public ChatMessageAdapter(@NonNull FirebaseRecyclerOptions<ChatMessageModel> options, ChatMessageAdapter.RecyclerViewClickListener listener) {
        super(options);
        this.listener = listener;

    }

    @Override
    protected void onBindViewHolder(@NonNull final ChatMessageAdapter.myviewholder holder, int position, @NonNull final ChatMessageModel ChatMessageModel) {
        String userid = Paper.book().read("Uid");
        String friendimage = Paper.book().read("ParticipantImg");

        mauth = FirebaseAuth.getInstance();
        Uid = mauth.getCurrentUser().getUid();
        receiverId = Paper.book().read("ParticipantId");

        final int msgNo = ChatMessageModel.getMsgNo();

        final DatabaseReference refroot = FirebaseDatabase.getInstance().getReference().child("Chats").child(Uid).child(receiverId);


        if(ChatMessageModel.getSenderId().equals(userid))
        {
            holder.relfriend.setVisibility(View.INVISIBLE);

            holder.messagevalself.setText(ChatMessageModel.getMessageValue());

            holder.selftime.setText(ChatMessageModel.SentTime);


        }
        else
            {
                holder.relself.setVisibility(View.INVISIBLE);

                holder.messagevalfriend.setText(ChatMessageModel.getMessageValue());

                holder.friendtime.setText(ChatMessageModel.SentTime);

                Glide.with(holder.image.getContext()).load(friendimage).into(holder.image);

            }



        holder.friendmsgrel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Log.i("Click","Long clicked");

                HashMap<String, Object> delmap = new HashMap<>();
                delmap.put("msgNo",msgNo);

                refroot.child("deletemessage").updateChildren(delmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

                return true;
            }
        });

        holder.mymsgrel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                Log.i("Click","Long clicked");

                HashMap<String, Object> delmap = new HashMap<>();
                delmap.put("msgNo",msgNo);

                refroot.child("deletemessage").updateChildren(delmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

                return true;
            }
        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card_self_message, parent, false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView messagevalself,messagevalfriend,selftime,friendtime;
        RelativeLayout relfriend,relself;
        RelativeLayout friendmsgrel,mymsgrel,self_relative,friend_relative;


        public myviewholder(@NonNull View itemView) {
            super(itemView);

            selftime = itemView.findViewById(R.id.selftime);
            friendtime = itemView.findViewById(R.id.friendtime);
            messagevalself = itemView.findViewById(R.id.rightmsgtxt);
            messagevalfriend = itemView.findViewById(R.id.leftmsgtxt);
            image = (ImageView) itemView.findViewById(R.id.msgProfileImage);
            relfriend = (RelativeLayout)itemView.findViewById(R.id.friend_relative);
            relself = (RelativeLayout)itemView.findViewById(R.id.self_relative);
            friendmsgrel = (RelativeLayout)itemView.findViewById(R.id.msgrightrel);
            mymsgrel = (RelativeLayout)itemView.findViewById(R.id.msgrightrel1);


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
