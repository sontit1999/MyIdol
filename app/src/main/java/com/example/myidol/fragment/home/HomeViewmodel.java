package com.example.myidol.fragment.home;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.adapter.PostAdapter;
import com.example.myidol.adapter.PostsAdapter;
import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.Photo;
import com.example.myidol.model.Post;
import com.example.myidol.ui.image.ImageFullActivity;

import java.util.ArrayList;

public class HomeViewmodel extends BaseViewmodel {
    PostAdapter adapter = new PostAdapter();
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
    public void gotoImagefull(String url, Context context){
        Intent intent = new Intent(context, ImageFullActivity.class);
        intent.putExtra("photo",new Photo(url));
        context.startActivity(intent);
    }
}
