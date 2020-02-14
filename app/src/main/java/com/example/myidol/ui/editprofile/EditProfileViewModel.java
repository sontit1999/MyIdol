package com.example.myidol.ui.editprofile;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class EditProfileViewModel extends BaseViewmodel {
    private MutableLiveData<Boolean> isUpdate = new MutableLiveData<>();
    public MutableLiveData<Boolean> getStatusUpdate(){
        return isUpdate ;
    }
    public EditProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public void uploadImage(Uri imageURL){
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child(System.currentTimeMillis()+"");
        ref.putFile(imageURL).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference user = FirebaseDatabase.getInstance().getReference();
                        user.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageUrl").setValue(String.valueOf(uri));

                    }
                });
            }
        });

    }
    public void updateProfile(User user, final Context context){
        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("id",user.getId());
        hashMap.put("username",user.getUsername());
        hashMap.put("sentenceslike",user.getSentenceslike());
        hashMap.put("address",user.getAddress());
        hashMap.put("imageUrl",user.getImageUrl());
        hashMap.put("phonenumber",user.getPhonenumber());
        mdatabase.child("Users").child(user.getId()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Đã cập nhật thông tin", Toast.LENGTH_SHORT).show();
                isUpdate.setValue(true);
            }
        });

    }
}
