package com.example.myidol.ui.profile;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.adapter.PhotoAdapter;
import com.example.myidol.adapter.PostsAdapter;
import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.Photo;
import com.example.myidol.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ProfileUserViewModel extends BaseViewmodel {
    PhotoAdapter adapter = new PhotoAdapter();
    PostsAdapter adapterPost = new PostsAdapter();
    MutableLiveData<ArrayList<Photo>> arrPhoto = new MutableLiveData<>();
    MutableLiveData<ArrayList<Post>> arrPost = new MutableLiveData<>();
    public ProfileUserViewModel(@NonNull Application application) {
        super(application);
    }
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
    public MutableLiveData<ArrayList<Post>> getArrPost(String iduser){
        final ArrayList<Post> temp = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("post");
        reference.orderByChild("publisher").equalTo(iduser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                temp.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    temp.add(post);
                }

                Collections.reverse(temp);
               arrPost.postValue(temp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return arrPost;
    }
    public void setListPhoto(ArrayList<Photo> arrayList){
        arrPhoto.postValue(arrayList);
    }
    public void setListPost(ArrayList<Post> arrayList){
        arrPost.postValue(arrayList);
    }
}
