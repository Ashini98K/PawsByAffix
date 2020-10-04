package com.affix.pawsbyaffix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class MessageChatActivity extends AppCompatActivity {

    RecyclerView recview;
    ChatMessageAdapter adapter;
    ChatMessageAdapter.RecyclerViewClickListener listener;
    FirebaseRecyclerOptions<ChatMessageModel> options;
    String Uid;
    TextView usernametxt;
    ImageView profileimage;
    ImageButton sendBtn;
    int messageNo;
    String receiverId;
    EditText mesgTxt;
    int unredcount;
    AlertDialog.Builder builder;
    private  int msgNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_message_details);

        builder = new AlertDialog.Builder(this);

        Uid = Paper.book().read("Uid");

        usernametxt = findViewById(R.id.usernametxtbox);
        profileimage = (ImageView) findViewById(R.id.profileImg);

        sendBtn = (ImageButton) findViewById(R.id.msgsendbtn);
        mesgTxt = (EditText) findViewById(R.id.messageText);
        receiverId =  Paper.book().read("ParticipantId");

        String friendusername = Paper.book().read("ParticipantName");
        usernametxt.setText(friendusername);

        String profimage = Paper.book().read("ParticipantImg");

        Glide.with(profileimage).load(profimage).into(profileimage);

        recview = (RecyclerView)findViewById(R.id.message_detail_recview);

        LinearLayoutManager linearLayoutManager = (new LinearLayoutManager(getApplicationContext()));
        recview.setLayoutManager(linearLayoutManager);

        options =
                new FirebaseRecyclerOptions.Builder<ChatMessageModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Chats").child(Uid).child(receiverId).child("Messages").orderByChild("MsgNo"), ChatMessageModel.class)
                        .build();

        adapter = new ChatMessageAdapter(options,listener);


        recview.setAdapter(adapter);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mesgTxt.getText().toString().isEmpty())
                {
                    //do nothing
                }
                else{
                    sendMessage();
                }

            }
        });
    }

    private void sendMessage() {

        final String messagevalue = mesgTxt.getText().toString();
        final String senderId = Paper.book().read("Uid");
        final String receiverId =  Paper.book().read("ParticipantId");


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("HH:mm:ss a");

        final String datetime = currentDate.format(calendar.getTime());


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(senderId).child(receiverId);

        //make a reference to the uid node of the database
        DatabaseReference rootref = databaseReference.child("MessageNo");

        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageNo = (Integer.valueOf(snapshot.getValue().toString())+1);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(senderId).child(receiverId);


                databaseReference.child("Messages").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(!(snapshot.child(String.valueOf(messageNo)).exists()))
                        {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(senderId).child(receiverId);

                            HashMap<String, Object> msgMap = new HashMap<>();
                            msgMap.put("MsgNo",messageNo);
                            msgMap.put("MessageValue",messagevalue);
                            msgMap.put("SenderId",senderId);
                            msgMap.put("SentTime",datetime);

                            databaseReference.child("Messages").child(String.valueOf(messageNo)).updateChildren(msgMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {

                                        DatabaseReference msg3Ref = FirebaseDatabase.getInstance().getReference().child("Chats").child(senderId).child(receiverId);

                                        HashMap<String, Object> msg3Map = new HashMap<>();
                                        msg3Map.put("MessageNo",messageNo);

                                        msg3Ref.updateChildren(msg3Map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful())
                                                {
                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(receiverId).child(senderId);

                                                    HashMap<String, Object> msgMap = new HashMap<>();
                                                    msgMap.put("MsgNo",messageNo);
                                                    msgMap.put("MessageValue",messagevalue);
                                                    msgMap.put("SenderId",senderId);
                                                    msgMap.put("SentTime",datetime);

                                                    databaseReference.child("Messages").child(String.valueOf(messageNo)).updateChildren(msgMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                            {
                                                                DatabaseReference msg3Ref = FirebaseDatabase.getInstance().getReference().child("Chats").child(receiverId).child(senderId);

                                                                HashMap<String, Object> msg3Map = new HashMap<>();
                                                                msg3Map.put("MessageNo",messageNo);

                                                                msg3Ref.updateChildren(msg3Map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {


                                                                        DatabaseReference msg4Ref = FirebaseDatabase.getInstance().getReference().child("Chats").child(receiverId).child(senderId);

                                                                        msg4Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                if(snapshot.child("unread").exists())
                                                                                {
                                                                                    unredcount = Integer.parseInt(snapshot.child("unread").getValue().toString())+1;
                                                                                }
                                                                                else{
                                                                                    unredcount = 1;
                                                                                }

                                                                                DatabaseReference msg4Ref = FirebaseDatabase.getInstance().getReference().child("Chats").child(receiverId).child(senderId);
                                                                                HashMap<String, Object> msg4Map = new HashMap<>();
                                                                                msg4Map.put("unread",unredcount);

                                                                                msg4Ref.updateChildren(msg4Map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(receiverId).child(senderId);

                                                                                        HashMap<String, Object> msgMap = new HashMap<>();
                                                                                        msgMap.put("LastMessage",messagevalue);

                                                                                        databaseReference.updateChildren(msgMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if(task.isSuccessful())
                                                                                                {
                                                                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(senderId).child(receiverId);

                                                                                                    HashMap<String, Object> msgMap = new HashMap<>();
                                                                                                    msgMap.put("LastMessage",messagevalue);

                                                                                                    databaseReference.updateChildren(msgMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                                        }
                                                                                                    });
                                                                                                }

                                                                                            }
                                                                                        });
                                                                                    }
                                                                                });

                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                            }
                                                                        });


                                                                    }
                                                                });
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

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mesgTxt.setText("");

    }

    private void setOnClickListner() {
        listener = new ChatMessageAdapter.RecyclerViewClickListener() {

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

        DatabaseReference rootref = FirebaseDatabase.getInstance().getReference().child("Chats").child(Uid).child(receiverId).child("Messages");
        rootref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ChatMessageModel ChatMessageModel = snapshot.getValue(ChatMessageModel.class);

                recview.smoothScrollToPosition(recview.getAdapter().getItemCount());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

        DatabaseReference refroot2 = FirebaseDatabase.getInstance().getReference().child("Chats").child(Uid).child(receiverId).child("deletemessage");

        refroot2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() > 0) {
                    msgNumber = Integer.parseInt(snapshot.child("msgNo").getValue().toString());
                    showDialogBox(msgNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void showDialogBox(final int number) {

        builder.setMessage("Do you want to delete this Message ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatabaseReference refroot = FirebaseDatabase.getInstance().getReference().child("Chats").child(Uid).child(receiverId).child("Messages").child(String.valueOf(number));

                        refroot.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                DatabaseReference refroot = FirebaseDatabase.getInstance().getReference().child("Chats").child(Uid).child(receiverId).child("deletemessage").child("msgNo");

                                refroot.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });

                            }
                        });

                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button

                        DatabaseReference refroot = FirebaseDatabase.getInstance().getReference().child("Chats").child(Uid).child(receiverId).child("deletemessage").child("msgNo");

                        refroot.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

                        dialog.cancel();

                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Caution !");
        alert.show();
    }

}