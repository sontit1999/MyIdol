package com.example.myidol.fragment.chat;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myidol.R;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.callback.GroupCallback;
import com.example.myidol.databinding.FragChatgroupBinding;
import com.example.myidol.model.GroupChat;
import com.example.myidol.ui.detailchat.DetailChatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentChatGroup extends BaseFragment<FragChatgroupBinding,ChatGroupViewmodel> {
    ArrayList<GroupChat> temp = new ArrayList<>();
    @Override
    public Class<ChatGroupViewmodel> getViewmodel() {
        return ChatGroupViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_chatgroup;
    }

    @Override
    public void setBindingViewmodel() {
       binding.setViewmodel(viewmodel);
       binding.btnAdd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String namegroup = binding.etNameGroup.getText().toString().trim();
               if(TextUtils.isEmpty(namegroup)){
                   Toast.makeText(getActivity(), "Name group must not empty!", Toast.LENGTH_SHORT).show();
               }else{
                   addGroup(namegroup);
               }
           }
       });
       setupRecyclerviewGroup();
    }

    private void setupRecyclerviewGroup() {
        binding.rvGroup.setHasFixedSize(true);
        binding.rvGroup.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.rvGroup.setAdapter(viewmodel.adapter);
    }

    private void addGroup(String namegroup) {
        String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        GroupChat groupChat = new GroupChat(System.currentTimeMillis()+"",namegroup,date, FirebaseAuth.getInstance().getCurrentUser().getUid(),"https://f1.pngfuel.com/png/456/572/891/users-icon-group-icon-humans-3-icon-computer-icons-icon-design-person-internet-monochrome-png-clip-art.png");
        FirebaseDatabase.getInstance().getReference("Groups").child(groupChat.getId()).setValue(groupChat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Đã tạo nhóm !", Toast.LENGTH_SHORT).show();
                binding.etNameGroup.setText("");
            }
        });
    }

    @Override
    public void ViewCreated() {
           viewmodel.getArrGroup().observe(this, new Observer<ArrayList<GroupChat>>() {
               @Override
               public void onChanged(final ArrayList<GroupChat> groupChats) {
                   viewmodel.adapter.setList(groupChats);
                   viewmodel.adapter.setCallback(new GroupCallback() {
                       @Override
                       public void onGroupclick(GroupChat groupChat) {
                           Intent intent = new Intent(getContext(), DetailChatActivity.class);
                           intent.putExtra("iduser","none");
                           intent.putExtra("idgroup",groupChat.getId());
                           startActivity(intent);
                       }
                   });
               }
           });
           getGroup();
    }

    private void getGroup() {
        FirebaseDatabase.getInstance().getReference("Groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                temp.clear();
                for(DataSnapshot i : dataSnapshot.getChildren()){
                    GroupChat groupChat = i.getValue(GroupChat.class);
                    temp.add(groupChat);
                }
                viewmodel.setArrGroup(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
