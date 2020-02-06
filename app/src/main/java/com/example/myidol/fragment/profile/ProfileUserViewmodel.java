package com.example.myidol.fragment.profile;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.adapter.IdolAdapter;
import com.example.myidol.adapter.PhotoAdapter;
import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.callback.RequestAPI;
import com.example.myidol.model.APIClient;
import com.example.myidol.model.IdolHot;
import com.example.myidol.model.Photo;
import com.example.myidol.model.Post;
import com.example.myidol.ui.postnew.PostNewActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ProfileUserViewmodel extends BaseViewmodel {
    public ProfileUserViewmodel(@NonNull Application application) {
        super(application);
    }
    MutableLiveData<ArrayList<Post>> arrayPost = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Post>> getArrayPost(){
        return arrayPost;
    }
    public void setPost(ArrayList<Post> arrayList){
        arrayPost.postValue(arrayList);
    }
    public void uploadAvatar(Uri imageURL, final Context context){
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child(System.currentTimeMillis()+"");
        ref.putFile(imageURL).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference user = FirebaseDatabase.getInstance().getReference();
                        user.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageUrl").setValue(String.valueOf(uri));
                        Toast.makeText(context, "Đã thay đổi ảnh đại diện", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
