package com.example.myidol.fragment.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.adapter.UserAdapter;
import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.User;

import java.util.List;

public class SearchViewmodel extends BaseViewmodel {
    MutableLiveData<List<User>> arrUser = new MutableLiveData<>();
    public SearchViewmodel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<List<User>> getArrUser(){
        return arrUser;
    }
}
