package com.example.myidol.fragment.chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.adapter.ChatGroupAdapter;
import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.GroupChat;

import java.util.ArrayList;

public class ChatGroupViewmodel extends BaseViewmodel {
    ChatGroupAdapter adapter = new ChatGroupAdapter();
    MutableLiveData<ArrayList<GroupChat>> arrGroup = new MutableLiveData<>();
    public ChatGroupViewmodel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<ArrayList<GroupChat>> getArrGroup(){
        return arrGroup;
    }
    public void setArrGroup(ArrayList<GroupChat> arrGroup){
        this.arrGroup.postValue(arrGroup);
    }
}
