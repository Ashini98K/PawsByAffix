package com.affix.pawsbyaffix;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

class NotificationAdapter extends FirebaseRecyclerAdapter<NotificationModel, NotificationAdapter.myviewholder> {

    private RecyclerViewClickListener listener;


    public NotificationAdapter(@NonNull FirebaseRecyclerOptions<NotificationModel> options, RecyclerViewClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull NotificationModel NotificationModel) {
        if(!NotificationModel.getNotificationTargetImage().isEmpty())
        {
            holder.TargetImage.setVisibility(View.VISIBLE);
            Glide.with(holder.TargetImage.getContext()).load(NotificationModel.getNotificationTargetImage()).into(holder.TargetImage);
        }

        holder.NotificationMasseage.setText(NotificationModel.getNewNotificationMessage());
        holder.notification_time.setText(NotificationModel.getNotificationTime());
        Glide.with(holder.ProfileImage.getContext()).load(NotificationModel.getNotificationWitnessProfileImage()).into(holder.ProfileImage);

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card_notification, parent, false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ProfileImage,TargetImage;
        TextView NotificationMasseage,notification_time;


        public myviewholder(@NonNull View itemView) {
            super(itemView);


            ProfileImage = (ImageView) itemView.findViewById(R.id.ProfileImagenotification);
            TargetImage = (ImageView) itemView.findViewById(R.id.targetImg);

            NotificationMasseage = (TextView) itemView.findViewById(R.id.notificationmsg);
            notification_time = (TextView) itemView.findViewById(R.id.datettimenotificatoionttxt);

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
