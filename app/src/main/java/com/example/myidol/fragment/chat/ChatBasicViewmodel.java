package com.example.myidol.fragment.chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.adapter.RecentChatAdapter;
import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.User;

import java.util.ArrayList;

public class ChatBasicViewmodel extends BaseViewmodel {
    RecentChatAdapter adapter = new RecentChatAdapter();
    MutableLiveData<ArrayList<User>> arrUser = new MutableLiveData<>();
    public ChatBasicViewmodel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<ArrayList<User>> getArrUser(){
        return arrUser;
    }
    public void setListUser(ArrayList<User> arrayList){
        arrUser.postValue(arrayList);
    }
}
