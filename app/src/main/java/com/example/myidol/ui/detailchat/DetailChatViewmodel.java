package com.example.myidol.ui.detailchat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DetailChatViewmodel extends BaseViewmodel {
    MutableLiveData<Boolean> isdelete = new MutableLiveData<>();
    public MutableLiveData<Boolean> getStatusdelete(){
        return isdelete;
    }
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
    public void deleteNoteChat(String nodechat){
        FirebaseDatabase.getInstance().getReference("Chats").child(nodechat).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isdelete.setValue(true);
            }
        });
    }
}
