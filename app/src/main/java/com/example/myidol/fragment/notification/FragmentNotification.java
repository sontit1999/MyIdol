package com.example.myidol.fragment.notification;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myidol.R;
import com.example.myidol.adapter.NotificationAdapter;
import com.example.myidol.adapter.PostsAdapter;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.databinding.FragNotificationBinding;
import com.example.myidol.model.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentNotification extends BaseFragment<FragNotificationBinding,NotificationViewModel> {
    NotificationAdapter adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public Class<NotificationViewModel> getViewmodel() {
        return NotificationViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_notification;
    }

    @Override
    public void setBindingViewmodel() {
         binding.setViewmodel(viewmodel);
         setupRecyclewview();
    }

    private void setupRecyclewview() {
        ArrayList<Notification> arrayList = new ArrayList<>();
        adapter = new NotificationAdapter(getContext(),arrayList);
        binding.rvNotifi.setHasFixedSize(true);
        binding.rvNotifi.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvNotifi.setAdapter(adapter);
    }

    @Override
    public void ViewCreated() {
          viewmodel.getArrNotification().observe(this, new Observer<ArrayList<Notification>>() {
              @Override
              public void onChanged(ArrayList<Notification> notifications) {
                  adapter.setList(notifications);
              }
          });
          getNotification();
    }

    private void getNotification() {
        FirebaseDatabase.getInstance().getReference("notification").child(user.getUid()).addValueEventListener(new ValueEventListener() {
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
                Log.d("sizenotifi",temp.size()+"");
                Collections.reverse(temp);
                viewmodel.setArrNotification(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
