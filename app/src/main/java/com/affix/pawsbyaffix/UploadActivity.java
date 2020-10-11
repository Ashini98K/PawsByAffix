package com.affix.pawsbyaffix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class UploadActivity extends AppCompatActivity {
    private String  saveCurrentDate, saveCurrentTime ;
    private EditText postCaptiontxt;
    private ImageView selectedImage,uploadpostprofileimage,backarrow;
    private TextView usernametextview;
    private static final int GalleryPick = 1;

    private Uri imageuri;
    private RelativeLayout posttxt,canceltxt;
    private DatabaseReference DatabaseRefForUserNode;
    private RelativeLayout selectuploadImage;

    private String postId,profileimage,fullname;
    private long postnumber;
    private String downloadImageUrl;
    private String uid,username;
    private DatabaseReference PostRef;
    private StorageReference PostImageRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        backarrow = (ImageView) findViewById(R.id.backarrow);


        loadingBar = new ProgressDialog(this);

        uploadpostprofileimage = (ImageView) findViewById(R.id.uploadpostprofileimage);

        usernametextview = findViewById(R.id.usernametextview);

        profileimage = Paper.book().read("ProfileImage");

        uid = Paper.book().read("Uid");
        username = Paper.book().read("UserName");
         selectuploadImage = (RelativeLayout) findViewById(R.id.galleryiconrel);
        postCaptiontxt = findViewById(R.id.postcaptionTxt);
        // selectuploadImage = (ImageView) view.findViewById(R.id.select_image_upload);
        posttxt =(RelativeLayout) findViewById(R.id.postrel);
        canceltxt =(RelativeLayout) findViewById(R.id.posticonrel);
        selectedImage = (ImageView) findViewById(R.id.selected_image_to_upload);

        Glide.with(uploadpostprofileimage.getContext()).load(profileimage).into(uploadpostprofileimage);
        usernametextview.setText(username);


        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        selectuploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        posttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingBar.setTitle("Uploading Post");
                loadingBar.setMessage("Please wait we are uploading your post");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                validatePost();

            }
        });

    }


    private void openGallery() {

        //Use the cropimage method
        CropImage.activity(imageuri)
                .setAspectRatio(16,9)
                .start(UploadActivity.this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageuri = result.getUri();

            selectedImage.setVisibility(View.VISIBLE);

            selectedImage.setImageURI(imageuri);
        }
        else{
            Toast.makeText(getApplicationContext(), "Error.. Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UploadActivity.this, UploadActivity.class));
        }
    }

    private void validatePost() {

        String caption = postCaptiontxt.getText().toString();

        if(imageuri == null)
        {
            loadingBar.dismiss();
            Toast.makeText(getApplicationContext(),"Please select a image to post",Toast.LENGTH_LONG);
        }
        else{
            savePhoto();
        }
    }

    private void savePhoto() {


        DatabaseReference refdb = FirebaseDatabase.getInstance().getReference().child("User_Posts");

        refdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.child("postNumber").exists())
               {
                   postnumber = (Integer.parseInt(snapshot.child("postNumber").getValue().toString())) + (-1);
               }
               else{
                   postnumber = -1;
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        PostImageRef = FirebaseStorage.getInstance().getReference().child("PostImages");
        final StorageReference filepath = PostImageRef.child(imageuri.getLastPathSegment() + uid+".jpg");

        final UploadTask uploadTask = filepath.putFile(imageuri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String Message = e.toString();
                Toast.makeText(getApplicationContext(), "Error " + Message, Toast.LENGTH_SHORT).show();


                imageuri = null;
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }

                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();



                            savePostinfo();

                            imageuri = null;
                            selectedImage.setImageURI(imageuri);
                        }
                    }
                });
            }
        });

    }

    private void savePostinfo() {

        Calendar calendar = Calendar.getInstance();
        final String caption = postCaptiontxt.getText().toString();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd,YYYY");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        String datetime = saveCurrentDate + " " + saveCurrentTime;
        String postiddate = saveCurrentDate + saveCurrentTime;

        postId = saveCurrentDate + saveCurrentTime;

        HashMap<String, Object> profileImgMap = new HashMap<>();
        profileImgMap.put("image", downloadImageUrl);
        profileImgMap.put("caption", caption);
        profileImgMap.put("Username", username);
        profileImgMap.put("ProfileImage", profileimage);
        profileImgMap.put("FullName", fullname);
        profileImgMap.put("Likes", 0);
        profileImgMap.put("PostNumber", postnumber);
        profileImgMap.put("userid", uid);
        profileImgMap.put("postiddate", postiddate);
        profileImgMap.put("Datetime", datetime);
        DatabaseRefForUserNode = FirebaseDatabase.getInstance().getReference();
        DatabaseRefForUserNode.child("Posts").child(postId).updateChildren(profileImgMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                                        DatabaseRefForUserNode = FirebaseDatabase.getInstance().getReference().child("User_Posts");

                                        HashMap<String, Object> postMap = new HashMap<>();
                                        postMap.put("postNumber", postnumber);

                                        DatabaseRefForUserNode.updateChildren(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful())
                                                {
                                                    selectedImage.setVisibility(View.GONE);
                                                    postCaptiontxt.setText("");
                                                    loadingBar.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Post uploaded", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        });
                        } else {

                            String message = task.getException().toString();
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(), "Error.." + message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        DatabaseRefForUserNode.child("User_Posts").child(uid).child(postId).updateChildren(profileImgMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        } else {

                            String message = task.getException().toString();

                            Toast.makeText(getApplicationContext(), "Error.." + message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }


}