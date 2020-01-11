package com.example.myidol.fragment.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.Notification;

import java.util.ArrayList;

public class NotificationViewModel extends BaseViewmodel {
    MutableLiveData<ArrayList<Notification>> arrNotifi = new MutableLiveData<>();
    public NotificationViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<ArrayList<Notification>> getArrNotification(){
        return arrNotifi;
    }
    public void setArrNotification(ArrayList<Notification> arrNotification){
       arrNotifi.postValue(arrNotification);
    }
}
