package com.example.myidol.ui.detailchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myidol.R;
import com.example.myidol.adapter.MessageAdapter;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityDetailChatBinding;
import com.example.myidol.model.GroupChat;
import com.example.myidol.model.Message;
import com.example.myidol.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class DetailChatActivity extends BaseActivity<ActivityDetailChatBinding,DetailChatViewmodel> {
    ArrayList<Message> arrayList = new ArrayList<>();
    MessageAdapter adapter ;
    String iduser;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String nodechat;
    Boolean isGroup = false;
    @Override
    public Class<DetailChatViewmodel> getViewmodel() {
        return DetailChatViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_detail_chat;
    }

    @Override
    public void setBindingViewmodel() {
       binding.setViewmodel(viewmodel);
       Intent intent = getIntent();

       if(intent!=null){
           if(intent.getStringExtra("idgroup")==null){
               iduser = intent.getStringExtra("iduser");
               if(iduser.compareTo(currentUser.getUid())>0){
                   nodechat = currentUser.getUid()+"-"+iduser;
               }else if(iduser.compareTo(currentUser.getUid())<0){
                   nodechat = iduser +"-" + currentUser.getUid();
               }
           }else{
               isGroup = true;
               nodechat = intent.getStringExtra("idgroup");
           }

       }
       getinforReciever();
       setuprecycleview();
       getlistChat();
       action();

    }

    private void getinforReciever() {
       if(isGroup){
              FirebaseDatabase.getInstance().getReference("Groups").child(nodechat).addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      GroupChat groupChat = dataSnapshot.getValue(GroupChat.class);
                      Picasso.get().load(groupChat.getImageGroup()).into(binding.ivAvatarRecive);
                      binding.tvPartner.setText(groupChat.getNamegroup());
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {

                  }
              });
       }else {
           FirebaseDatabase.getInstance().getReference("Users").child(iduser).addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   User userRecive = dataSnapshot.getValue(User.class);
                   binding.tvPartner.setText(userRecive.getUsername());
                   Picasso.get().load(userRecive.getImageUrl()).into(binding.ivAvatarRecive);
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
       }
    }

    private void getlistChat() {
        if(isGroup){
            FirebaseDatabase.getInstance().getReference("Groups").child(nodechat).child("mesage").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("sizelist",dataSnapshot.getChildrenCount()+"");
                    ArrayList<Message> temp = new ArrayList<>();
                    for(DataSnapshot i : dataSnapshot.getChildren()){
                        Message message = i.getValue(Message.class);
                        temp.add(message);
                    }
                    viewmodel.setListChat(temp);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            FirebaseDatabase.getInstance().getReference("Chats").child(nodechat).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("sizelist",dataSnapshot.getChildrenCount()+"");
                    ArrayList<Message> temp = new ArrayList<>();
                    for(DataSnapshot i : dataSnapshot.getChildren()){
                        Message message = i.getValue(Message.class);
                        temp.add(message);
                    }
                    viewmodel.setListChat(temp);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void setuprecycleview() {
        adapter = new MessageAdapter(this,arrayList);
        binding.rvListchat.setHasFixedSize(true);
        binding.rvListchat.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.rvListchat.setAdapter(adapter);
    }
    private void sendmessage(Message message){
         if(isGroup){
             FirebaseDatabase.getInstance().getReference("Groups").child(nodechat).child("mesage").child(System.currentTimeMillis()+"").setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void aVoid) {
                     Toast.makeText(DetailChatActivity.this, "Đã gửi!", Toast.LENGTH_SHORT).show();
                     binding.etChat.setText("");
                 }
             });
         }else {
             FirebaseDatabase.getInstance().getReference("Chats").child(nodechat).child(System.currentTimeMillis()+"").setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void aVoid) {
                     Toast.makeText(DetailChatActivity.this, "Đã gửi!", Toast.LENGTH_SHORT).show();
                     binding.etChat.setText("");
                 }
             });
         }
    }
    private void action() {
        binding.ivSendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.etChat.getText().toString();
                if(TextUtils.isEmpty(message)){
                    Toast.makeText(DetailChatActivity.this, "Message must not empty!", Toast.LENGTH_SHORT).show();
                }else{
                    String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    sendmessage(new Message(message, FirebaseAuth.getInstance().getCurrentUser().getUid(),iduser,date));
                }
            }
        });
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewmodel.getArrlistChat().observe(this, new Observer<ArrayList<Message>>() {
            @Override
            public void onChanged(ArrayList<Message> messages) {
                adapter.setList(messages);
                binding.rvListchat.scrollToPosition(adapter.getItemCount()-1);
            }
        });
    }
}
