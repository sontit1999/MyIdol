package com.example.myidol.fragment.profile;

import android.app.Application;
import android.util.Log;

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

import java.util.ArrayList;
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
}
