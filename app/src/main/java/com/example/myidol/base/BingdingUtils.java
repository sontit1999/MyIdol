package com.example.myidol.base;

import android.util.Log;
import android.view.View;
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
import com.example.myidol.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
        if(imageUrl==null){
            imageUrl = "no";
        }
        if(!imageUrl.equals("no")){
            Glide
                    .with(view.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .override(500,250)
                    .into(view);
        }
    }
    @BindingAdapter({"bind:visible"})
    public static void loadImage(View view, String imageUrl) {
        if(imageUrl==null){
            imageUrl = "no";
        }
        if(imageUrl.equals("no")){
            view.setVisibility(View.GONE);
        }
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
                if(temp.size()>0){
                    view.setText(temp.get(temp.size()-1).getContent());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @BindingAdapter({ "imagefromiduser" })
    public static void setImageview(final ImageView view, String iduser) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(iduser);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Picasso.get().load(user.getImageUrl()).placeholder(R.drawable.ic_launcher_foreground).into(view);
//                Glide
//                        .with(view.getContext())
//                        .load(user.getImageUrl())
//                        .placeholder(R.drawable.ic_launcher_foreground)
//                        .override(250,250)
//                        .into(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @BindingAdapter({ "usernamefromiduser" })
    public static void usernamefromiduser(final TextView view, String iduser) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(iduser);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                view.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @BindingAdapter({ "numbercmtfromidpost" })
    public static void numbercmtfromidpost(final TextView view, String idpost) {
        // get number comment
        FirebaseDatabase.getInstance().getReference("comments").
                child(idpost).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                view.setText(dataSnapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @BindingAdapter({ "numberlikefromidpost" })
    public static void numberlikefromidpost(final TextView view, String idpost) {
        // get number comment
        FirebaseDatabase.getInstance().getReference("likes").child(idpost).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("number like :" ,dataSnapshot.getChildrenCount() + "likes");
                       view.setText(dataSnapshot.getChildrenCount() + "");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
    @BindingAdapter({ "islike" })
    public static void islike(final ImageView view, String idpost) {
        final FirebaseUser curentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("likes").child(idpost);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(curentUser.getUid()).exists()){
                    view.setImageResource(R.drawable.icons8liked);
                    view.setTag("liked");
                }else {
                    view.setImageResource(R.drawable.iconsheart);
                    view.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
