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

class UserProfileAdapter extends FirebaseRecyclerAdapter<UserProfileModel, UserProfileAdapter.myviewholder> {

    private UserProfileAdapter.RecyclerViewClickListener listener;


    public UserProfileAdapter(@NonNull FirebaseRecyclerOptions<UserProfileModel> options, UserProfileAdapter.RecyclerViewClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserProfileAdapter.myviewholder holder, int position, @NonNull UserProfileModel UserProfileModel) {

        Glide.with(holder.image.getContext()).load(UserProfileModel.getImage()).into(holder.image);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userpostsgri_single_card, parent, false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView username;


        public myviewholder(@NonNull View itemView) {
            super(itemView);


            image = (ImageView) itemView.findViewById(R.id.userprofilegridimg);

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
