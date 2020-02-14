package com.example.myidol.fragment.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationViewModel extends BaseViewmodel {
    MutableLiveData<ArrayList<Notification>> arrNotifi = new MutableLiveData<>();
    public NotificationViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<ArrayList<Notification>> getArrNotification(){
        FirebaseDatabase.getInstance().getReference("notification").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Notification> temp = new ArrayList<>();
                for(DataSnapshot i : dataSnapshot.getChildren()){
                    Notification notification = i.getValue(Notification.class);
                   if(temp.size()<15){
                        temp.add(notification);
                    }else {
                        break;
                    }

                }
                Collections.reverse(temp);
                arrNotifi.postValue(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return arrNotifi;
    }
}
