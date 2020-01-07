package com.example.myidol.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.adapter.PhotoAdapter;
import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.Photo;

import java.util.ArrayList;

public class ProfileUserViewModel extends BaseViewmodel {
    PhotoAdapter adapter = new PhotoAdapter();
    MutableLiveData<ArrayList<Photo>> arrPhoto = new MutableLiveData<>();
    public ProfileUserViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<ArrayList<Photo>> getArrphoto(){
        return arrPhoto;
    }
    public void setListPhoto(ArrayList<Photo> arrayList){
        arrPhoto.postValue(arrayList);
    }
}
