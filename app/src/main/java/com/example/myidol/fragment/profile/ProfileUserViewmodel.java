package com.example.myidol.fragment.profile;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.adapter.IdolAdapter;
import com.example.myidol.adapter.PhotoAdapter;
import com.example.myidol.adapter.PostAdapter;
import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.callback.RequestAPI;
import com.example.myidol.model.APIClient;
import com.example.myidol.model.IdolHot;
import com.example.myidol.model.Photo;
import com.example.myidol.model.Post;
import com.example.myidol.ui.MainActivity;
import com.example.myidol.ui.image.ImageFullActivity;
import com.example.myidol.ui.postnew.PostNewActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class ProfileUserViewmodel extends BaseViewmodel {
    PhotoAdapter adapter = new PhotoAdapter();
    PostAdapter postAdapter = new PostAdapter();
    MutableLiveData<ArrayList<Photo>> arrPhoto = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Photo>> getArrphoto(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("post");
        reference.orderByChild("publisher").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Photo> temp = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    if(temp.size()<6){
                        temp.add(new Photo(post.getLinkImage()));
                    }else {
                        break;
                    }

                }
                Collections.shuffle(temp);
                arrPhoto.postValue(temp);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return arrPhoto;
    }
    public ProfileUserViewmodel(@NonNull Application application) {
        super(application);
    }
    MutableLiveData<ArrayList<Post>> arrayPost = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Post>> getArrayPost(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("post");
        reference.orderByChild("publisher").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Post> temp = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    temp.add(ds.getValue(Post.class));
                }
                Collections.reverse(temp);
                arrayPost.postValue(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
    public void gotoImagefull(String url,Context context){
        Intent intent = new Intent(context, ImageFullActivity.class);
        intent.putExtra("photo",new Photo(url));
        context.startActivity(intent);
    }
    public void getMoreFeature(final TextView tvNumberPost, final TextView tvNumberFollower, final TextView tvNumberFollowing){
        DatabaseReference follow = FirebaseDatabase.getInstance().getReference("follows").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("follows");
        follow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvNumberFollower.setText(dataSnapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference following = FirebaseDatabase.getInstance().getReference("follows").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
        following.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvNumberFollowing.setText(dataSnapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference post = FirebaseDatabase.getInstance().getReference("post");
        post.orderByChild("publisher").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvNumberPost.setText(dataSnapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
