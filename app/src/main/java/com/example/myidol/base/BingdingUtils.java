package com.example.myidol.base;

import android.util.Log;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myidol.R;
import com.example.myidol.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BingdingUtils {
    @BindingAdapter({ "setAdapter"})
    public static void setAdapter(RecyclerView view, RecyclerView.Adapter adapter) {
        view.setAdapter(adapter);
    }
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_launcher_foreground).into(view);
    }
    @BindingAdapter({"bind:iduser"})
    public static void loadRecentChat(final TextView view, String iduser) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String nodechat = "";
        if(iduser.compareTo(currentUser.getUid())>0){
            nodechat = currentUser.getUid()+"-"+iduser;
        }else if(iduser.compareTo(currentUser.getUid())<0){
            nodechat = iduser +"-" + currentUser.getUid();
        }
        FirebaseDatabase.getInstance().getReference("Chats").child(nodechat).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("sizelist",dataSnapshot.getChildrenCount()+"");
                ArrayList<Message> temp = new ArrayList<>();
                for(DataSnapshot i : dataSnapshot.getChildren()){
                    Message message = i.getValue(Message.class);
                    temp.add(message);
                }
                view.setText(temp.get(temp.size()-1).getContent());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
