package com.example.myidol.ui.detailchat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.Message;

import java.util.ArrayList;

public class DetailChatViewmodel extends BaseViewmodel {
    MutableLiveData<ArrayList<Message>> arrListChat = new MutableLiveData<>();
    public DetailChatViewmodel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<ArrayList<Message>> getArrlistChat(){
        return arrListChat;
    }
    public void setListChat(ArrayList<Message> arrayList){
        arrListChat.postValue(arrayList);
    }
}
