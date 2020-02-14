package com.example.myidol.ui.detailpost;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.base.BaseViewmodel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

public class DetailpostViewmodel  extends BaseViewmodel {
    MutableLiveData<Boolean> isdelete = new MutableLiveData<>();
    public DetailpostViewmodel(@NonNull Application application) {
        super(application);
    }
    public void deletePost(final String idpost){
        FirebaseDatabase.getInstance().getReference("post").child(idpost).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isdelete.setValue(true);
            }
        });
    }
    public MutableLiveData<Boolean> getStatusDelete(){
        return isdelete;
    }
}
