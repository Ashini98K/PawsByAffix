package com.affix.pawsbyaffix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class SearchActivity extends AppCompatActivity {

    SearchUserAdapter.RecyclerViewClickListener listener;
    FirebaseRecyclerOptions<SearchUserModel> options;
    RecyclerView recview;
    String userid;
    SearchUserAdapter adapter;
    Button followbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setOnClickListner();


        setContentView(R.layout.activity_search);
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recview = (RecyclerView)findViewById(R.id.searchrec);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recview.setLayoutManager(linearLayoutManager);
        options =
                new FirebaseRecyclerOptions.Builder<SearchUserModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), SearchUserModel.class)
                        .build();


        adapter = new SearchUserAdapter(options,listener);


        recview.setAdapter(adapter);


    }

    private void setOnClickListner() {
        listener = new SearchUserAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(SearchActivity.this,MainActivity.class);

                if(!(options.getSnapshots().get(position).getUid().equals(userid)))
                {
                    final String friendId = options.getSnapshots().get(position).getUid();
                    final String friendUserName = options.getSnapshots().get(position).getUsername();
                    final String friendFullName = options.getSnapshots().get(position).getFullname();
                    final String friendProfileImage = options.getSnapshots().get(position).getProfileImage();
                    final String friendBackgroundImage = options.getSnapshots().get(position).getBackgroundImage();
                    final String friendEmail = options.getSnapshots().get(position).getEmail();
                    final String friendBio = options.getSnapshots().get(position).getBio();


                    //get the value of selected user and open the fragment with starting values

                    //Clear previous values
                    Paper.book().write("FriendUid","");
                    Paper.book().write("FriendUserName","");
                    Paper.book().write("FriendFullName",".");
                    Paper.book().write("FriendBio","");
                    Paper.book().write("FriendProfileImage","");
                    Paper.book().write("FriendBackgroundImage","");
                    Paper.book().write("FriendEmail","");

                    //add new values
                    Paper.book().write("FriendUid",friendId);
                    Paper.book().write("FriendUserName",friendUserName);
                    Paper.book().write("FriendFullName",friendFullName);
                    Paper.book().write("FriendBio",friendBio);
                    Paper.book().write("FriendProfileImage",friendProfileImage);
                    Paper.book().write("FriendBackgroundImage",friendBackgroundImage);
                    Paper.book().write("FriendEmail",friendEmail);



                    intent.putExtra("key","gotosearch");

                }
                else
                {
                    intent.putExtra("key","gotoprofile");
                }

                startActivity(intent);

            }
        };
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i("start","fragment started");
        adapter.startListening();
    }




    @Override
    public void onStop() {
        super.onStop();
        Log.i("stop","fragment stopped");
        adapter.stopListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.searchmenu,menu);

        MenuItem item = menu.findItem(R.id.search);
        MenuItem item_home = menu.findItem(R.id.home);

        SearchView searchView = (SearchView)item.getActionView();


        item_home.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("key","menue");
                startActivity(intent);
                return false;
            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void processsearch(String s)
    {

        options =
                        new FirebaseRecyclerOptions.Builder<SearchUserModel>()
                                .setQuery(FirebaseDatabase.getInstance().getReference()
                                        .child("Users").orderByChild("search")
                                        .startAt(s).endAt(s+"\uf8ff"), SearchUserModel.class)
                                .build();

        adapter = new SearchUserAdapter(options, listener);

        adapter.startListening();
        recview.setAdapter(adapter);

    }



}