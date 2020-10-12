package com.affix.pawsbyaffix;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {
    ImageView profileImage,backgroundImage;
    EditText username,fullname,bio;
    Button updatebtn,logoutbtn;
    FirebaseAuth mAuth;
    StorageReference databaseReference;
    StorageReference storageReference;
    private Uri imageuri;
    private DatabaseReference DatabaseRefForUserNode;
    private int ImageCode;
    private String uid;
    private String downloadprofileImageUrl = "https://firebasestorage.googleapis.com/v0/b/affixpawsapplication-11331.appspot.com/o/ProfileImages%2Fillustration-cute-dog-avatar_79416-105.jpg?alt=media&token=fb952629-a767-4100-a819-459b6cf3784f";
    private String downloadBgImageUrl = "https://firebasestorage.googleapis.com/v0/b/affixpawsapplication-11331.appspot.com/o/backgroundImages%2F36a-background-gradients-1600.png?alt=media&token=5111d76c-b65e-435c-a94a-9b56fcee8daa";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String dpimg = Paper.book().read("ProfileImage");
        String bgimg = Paper.book().read("BackgroundImage");
        String fullnamepp = Paper.book().read("FullName");
        String usernamepp = Paper.book().read("UserName");
        String biopp = Paper.book().read("Bio");


        uid = Paper.book().read("Uid");
        backgroundImage = (ImageView)getView().findViewById(R.id.bacgroundImageedit);
        profileImage = (ImageView)getView().findViewById(R.id.user_profile_img);
        username = getView().findViewById(R.id.editusername);
        fullname = getView().findViewById(R.id.editfullname);
        bio = getView().findViewById(R.id.editbio);
        logoutbtn = (Button) getView().findViewById(R.id.logoutbtn);
        updatebtn = (Button) getView().findViewById(R.id.updatebtn);

        //Glide - Connecting a URL with the image
        Glide.with(backgroundImage.getContext()).load(bgimg).into(backgroundImage);
        Glide.with(profileImage.getContext()).load(dpimg).into(profileImage);
        fullname.setText(fullnamepp);
        username.setText(usernamepp);
        bio.setText(biopp);


        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageCode = 1;
                openGallery();

            }
        });

        backgroundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageCode = 2;
                openGallery();

            }
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateUpdate();

            }
        });
    }

    private void openGallery() {

        //Use the crop image method
        CropImage.activity(imageuri)
                .start(getContext(),this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            imageuri = data.getData();

            if(ImageCode == 1)
            {
                profileImage.setImageURI(imageuri);

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageuri = result.getUri();

                profileImage.setImageURI(imageuri);
            }
            else if(ImageCode == 2)
            {
                backgroundImage.setImageURI(imageuri);

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageuri = result.getUri();

                backgroundImage.setImageURI(imageuri);


            }


            savePhoto();

        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), "Error.. Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), getActivity().getClass()));
            getActivity().finish();
        }
    }


    private void validateUpdate() {

        String usernameval = username.getText().toString();
        String FullnameVal = fullname.getText().toString();
        String BioVal = bio.getText().toString();


        if (TextUtils.isEmpty(usernameval))
        {
            Toast.makeText(getActivity().getApplicationContext(),"UserName is empty",Toast.LENGTH_LONG);
        }
        else if (TextUtils.isEmpty(FullnameVal))
        {
            Toast.makeText(getActivity().getApplicationContext(),"Fullname is empty",Toast.LENGTH_LONG);
        }
        else if (TextUtils.isEmpty(BioVal))
        {
            Toast.makeText(getActivity().getApplicationContext(),"Bio is empty",Toast.LENGTH_LONG);
        }
        else{
            savePostinfo();
        }
    }

    private void savePhoto() {

        databaseReference = FirebaseStorage.getInstance().getReference().child(uid);
        final StorageReference filepath = databaseReference.child(imageuri.getLastPathSegment() +".jpg");

        final UploadTask uploadTask = filepath.putFile(imageuri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String Message = e.toString();
                Toast.makeText(getActivity().getApplicationContext(), "Error " + Message, Toast.LENGTH_SHORT).show();


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

                        if(ImageCode == 1)
                        {
                            downloadprofileImageUrl = filepath.getDownloadUrl().toString();


                        }
                        else if(ImageCode == 2)
                        {
                            downloadBgImageUrl = filepath.getDownloadUrl().toString();


                        }


                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {


                            if(ImageCode == 1)
                            {
                                downloadprofileImageUrl = task.getResult().toString();

                                HashMap<String, Object> profileimageMap = new HashMap<>();
                                profileimageMap.put("ProfileImage", downloadprofileImageUrl);

                                DatabaseRefForUserNode = FirebaseDatabase.getInstance().getReference();
                                DatabaseRefForUserNode.child("Users").child(uid).updateChildren(profileimageMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {


                                                    Toast.makeText(getActivity().getApplicationContext(), "Profile Image updated", Toast.LENGTH_SHORT).show();
                                                    Paper.book().write("ProfileImage",downloadprofileImageUrl);
                                                    ImageView proflink = (ImageView)getActivity().findViewById(R.id.profileImg);
                                                    String dpimg = Paper.book().read("ProfileImage");
                                                    Glide.with(proflink.getContext()).load(dpimg).into(proflink);




                                                } else {

                                                    String message = task.getException().toString();

                                                    Toast.makeText(getActivity().getApplicationContext(), "Error.." + message, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                            }
                            else if(ImageCode == 2)
                            {
                                downloadBgImageUrl = task.getResult().toString();


                                HashMap<String, Object> profileimageMap = new HashMap<>();
                                profileimageMap.put("BackgroundImage", downloadBgImageUrl);

                                DatabaseRefForUserNode = FirebaseDatabase.getInstance().getReference();
                                DatabaseRefForUserNode.child("Users").child(uid).updateChildren(profileimageMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {


                                                    Toast.makeText(getActivity().getApplicationContext(), "Backfround Image updated", Toast.LENGTH_SHORT).show();
                                                    Paper.book().write("BackgroundImage",downloadBgImageUrl);




                                                } else {

                                                    String message = task.getException().toString();

                                                    Toast.makeText(getActivity().getApplicationContext(), "Error.." + message, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                             }

                            imageuri = null;
                        }
                    }
                });
            }
        });

    }

    private void savePostinfo() {

        final String usernameVal = username.getText().toString();
        final String fullnameVal = fullname.getText().toString();
        final String bioVal = bio.getText().toString();

        HashMap<String, Object> profileUpdateMap = new HashMap<>();
        profileUpdateMap.put("ProfileImage", downloadprofileImageUrl);
        profileUpdateMap.put("BackgroundImage", downloadBgImageUrl);
        profileUpdateMap.put("Bio", bioVal);
        profileUpdateMap.put("fullname", fullnameVal);
        profileUpdateMap.put("username", usernameVal);
        profileUpdateMap.put("search", usernameVal.toLowerCase());

        DatabaseRefForUserNode = FirebaseDatabase.getInstance().getReference();
        DatabaseRefForUserNode.child("Users").child(uid).updateChildren(profileUpdateMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {


                            Toast.makeText(getActivity().getApplicationContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                            Paper.book().write("UserName",usernameVal);
                            Paper.book().write("FullName",fullnameVal);
                            Paper.book().write("Bio",bioVal);




                        } else {

                            String message = task.getException().toString();

                            Toast.makeText(getActivity().getApplicationContext(), "Error.." + message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    public void logoutUser()
    {
        Paper.book().destroy();
        FirebaseAuth.getInstance().signOut();
        getActivity().finishAndRemoveTask();
        startActivity(new Intent(getActivity().getApplicationContext(),LoginActivity.class));


    }


}