package com.example.myidol.fragment.home;

import android.app.Application;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.example.myidol.adapter.PostAdapter;
import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeViewmodel extends BaseViewmodel {

    public PostAdapter adapter = new PostAdapter();
    private MutableLiveData<ArrayList<Post>> arrayPost = new MutableLiveData<>();
    public HomeViewmodel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<ArrayList<Post>> getarrPost(){
        return arrayPost;
    }
    public void setPost(ArrayList<Post> arrayList){
        arrayPost.postValue(arrayList);
    }
}
