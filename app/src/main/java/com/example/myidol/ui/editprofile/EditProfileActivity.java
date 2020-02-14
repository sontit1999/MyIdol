package com.example.myidol.ui.editprofile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.example.myidol.R;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityEditProfileBinding;
import com.example.myidol.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends BaseActivity<ActivityEditProfileBinding,EditProfileViewModel> {
    public static int PICK_IMAGE = 54151;
    User users ;
    ProgressDialog pd;
    Uri imageURL = null;
    @Override
    public Class<EditProfileViewModel> getViewmodel() {
        return EditProfileViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_edit_profile;
    }

    @Override
    public void setBindingViewmodel() {
        binding.setViewmodel(viewmodel);
        action();
        checkpermisionns();
        getUser();

        viewmodel.getStatusUpdate().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    finish();
                }
            }
        });
    }

    private void action() {

        binding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sentencelike = binding.edtSentenceslike.getText().toString().trim();
                String address = binding.edtUsername.getText().toString().trim();
                String username = binding.edtAddress.getText().toString().trim();
                String phone = binding.edtPhone.getText().toString().trim();
                if(sentencelike.equals("") || username.equals("") || address.equals("") || phone.equals("")){
                    Toast.makeText(EditProfileActivity.this, "Not empty any field", Toast.LENGTH_SHORT).show();
                }else {
                    User user = new User(FirebaseAuth.getInstance().getUid(),users.getImageUrl(),username,sentencelike,address,phone);
                    Log.d("user",users.getImageUrl());
                    viewmodel.updateProfile(user,EditProfileActivity.this);
                }


            }
        });
        binding.ivCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   choosePhotofromgallery();
            }
        });
    }
    private void checkpermisionns() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 124);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == PICK_IMAGE && data != null){
                imageURL =  data.getData();
                Picasso.get().load(imageURL).into(binding.ivAvatar);
                viewmodel.uploadImage(imageURL);
            }
        }
    }
    private void choosePhotofromgallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }
    public void getUser(){
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);
                users = user1;
                binding.setUser(user1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
