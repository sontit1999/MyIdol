package com.example.myidol.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myidol.R;
import com.example.myidol.model.Notification;
import com.example.myidol.model.User;
import com.example.myidol.ui.profile.ProfileUserClientActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.myViewHoder> {
    Context context;
    ArrayList<User> arrayList;

    public UsersAdapter() {
    }

    public UsersAdapter(Context context, ArrayList<User> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    public void setList(ArrayList<User> arrayList){
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new myViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHoder holder, int position) {
        final FirebaseUser curentUser = FirebaseAuth.getInstance().getCurrentUser();
        final User user = arrayList.get(position);
        getnumberFollower(user.getId(),holder.tvfollower);
        holder.bindview(user);
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileUserClientActivity.class);
                intent.putExtra("iduser",user.getId());
                context.startActivity(intent);
            }
        });
        holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileUserClientActivity.class);
                intent.putExtra("iduser",user.getId());
                context.startActivity(intent);
            }
        });
        holder.tvfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.tvfollow.getText().equals("follow")){
                    FirebaseDatabase.getInstance().getReference("follows").child(curentUser.getUid())
                            .child("following").child(user.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference("follows").child(user.getId())
                            .child("follows").child(curentUser.getUid()).setValue(true);
                    addNotification(user.getId());
                }else{
                    FirebaseDatabase.getInstance().getReference("follows").child(curentUser.getUid())
                            .child("following").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference("follows").child(user.getId())
                            .child("follows").child(curentUser.getUid()).removeValue();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class myViewHoder extends RecyclerView.ViewHolder{
        ImageView iv_avatar;
        TextView tv_name,tvfollow,tvfollower,tvLocation;


        public myViewHoder(@NonNull View itemView) {
            super(itemView);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tvfollow = ( TextView) itemView.findViewById(R.id.tv_folow);
            tvfollower = ( TextView) itemView.findViewById(R.id.tv_followerSearch);
            tvLocation = ( TextView) itemView.findViewById(R.id.tv_location);
        }
        public void bindview(User user){
            Picasso.get().load(user.getImageUrl()).placeholder(R.drawable.ic_launcher_background).resize(65,65).into(iv_avatar);
            tv_name.setText(user.getUsername());
            isFollowing(user.getId(),tvfollow);
            tvLocation.setText(user.getAddress());
        }
    }
    private void isFollowing(final String id, final TextView tvfollow){
        {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("follows").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("following");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(id).exists()){
                        tvfollow.setText("unfollow");
                    }else{
                        tvfollow.setText("follow");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
    public void addNotification(String iduser){
        FirebaseUser currentUer = FirebaseAuth.getInstance().getCurrentUser();
        Notification notification = new Notification("null",currentUer.getUid(),"start following you","follow",System.currentTimeMillis()+"");
        FirebaseDatabase.getInstance().getReference("notification").child(iduser).child(System.currentTimeMillis()+"").setValue(notification);
    }
    public void getnumberFollower(String iduser, final TextView tv_numberfollower){
        DatabaseReference follow = FirebaseDatabase.getInstance().getReference("follows").child(iduser).child("follows");
        follow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 tv_numberfollower.setText(dataSnapshot.getChildrenCount() + " follower" );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
