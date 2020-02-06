package com.example.myidol.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myidol.R;
import com.example.myidol.model.Message;
import com.example.myidol.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int KEY_SEND = 1;
    public static final int KEY_RECIVE = 2;
    Context context;
    ArrayList<Message> arrayList;

    public MessageAdapter(Context context, ArrayList<Message> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    public void setList(ArrayList<Message> arrayList){
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(context);
        switch (viewType){
            case KEY_SEND:
                return new SendHoder(li.inflate(R.layout.item_sendmesage,parent,false));
            case KEY_RECIVE:
                return new ReciveHoder(li.inflate(R.layout.item_recivemessage,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = arrayList.get(position);
          switch (getItemViewType(position)){
              case KEY_SEND:
                  final SendHoder sendHoder = (SendHoder) holder;
                  sendHoder.binData(message);
                  sendHoder.tv_time.setTag("none");
                  sendHoder.tv_content.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          if(sendHoder.tv_time.getTag().equals("none")){
                              sendHoder.tv_time.setTag("display");
                              sendHoder.tv_time.setVisibility(View.VISIBLE);
                          }else{
                              sendHoder.tv_time.setTag("none");
                              sendHoder.tv_time.setVisibility(View.GONE);
                          }
                      }
                  });
                  break;
              case KEY_RECIVE:
                  final ReciveHoder reciveHoder = (ReciveHoder) holder;
                  reciveHoder.binData(message);
                  reciveHoder.tv_time.setTag("none");
                  getImage(reciveHoder.iv_avatar,message.getIdsendUser());
                  reciveHoder.tv_content.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          if(reciveHoder.tv_time.getTag().equals("none")){
                              reciveHoder.tv_time.setTag("display");
                              reciveHoder.tv_time.setVisibility(View.VISIBLE);
                              Toast.makeText(context, "display", Toast.LENGTH_SHORT).show();
                          }else {
                              reciveHoder.tv_time.setTag("none");
                              reciveHoder.tv_time.setVisibility(View.GONE);
                              Toast.makeText(context, "none", Toast.LENGTH_SHORT).show();
                          }
                      }
                  });
          }

    }

    private void getImage(final ImageView iv_avatar, String iduser) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(iduser);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    Picasso.get().load(user.getImageUrl()).placeholder(R.drawable.ic_launcher_foreground).into(iv_avatar);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class SendHoder extends RecyclerView.ViewHolder{
        TextView tv_content,tv_time;
        public SendHoder(@NonNull View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.tv_sent);
            tv_time = (TextView) itemView.findViewById(R.id.tv_timesent);
        }
        public void binData(Message message){
            tv_content.setText(message.getContent());
            tv_time.setText(message.getTimeSend());
        }
    }
    class ReciveHoder extends RecyclerView.ViewHolder{
        TextView tv_content,tv_time;
        ImageView iv_avatar;
        public ReciveHoder(@NonNull View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.tv_recive);
            tv_time = (TextView) itemView.findViewById(R.id.tv_timerecieve);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatarChatRecive);
        }
        public void binData(Message message){
            tv_content.setText(message.getContent());
            tv_time.setText(message.getTimeSend());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(arrayList.get(position).getIdsendUser().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            return KEY_SEND;
        }else {
            return KEY_RECIVE;
        }
    }
}
