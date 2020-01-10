package com.example.myidol.ui.comment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myidol.R;
import com.example.myidol.adapter.UsersAdapter;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityCommentBinding;
import com.example.myidol.model.Comment;
import com.example.myidol.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.gw.swipeback.SwipeBackLayout;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CommentActivity extends BaseActivity<ActivityCommentBinding,CommentViewmodel> {
    ArrayList<User> arrayList = new ArrayList<>();
    UsersAdapter adapter;
    String type = "";
    String iduser = "";
    String idpost = "";
    @Override
    public Class<CommentViewmodel> getViewmodel() {
        return CommentViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_comment;
    }

    @Override
    public void setBindingViewmodel() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if(type.equals("likes")){
             type = "likes";
             idpost = intent.getStringExtra("idpost");
             binding.tvType.setText("User like this post");
        }else if(type.equals("follower")){
            type = "follower";
            iduser = intent.getStringExtra("iduser");
            binding.tvType.setText("Follower");
        }else if(type.equals("following")){
            type = "following";
            iduser = intent.getStringExtra("iduser");
            binding.tvType.setText("Following");
        }
        action();
        setUpRecyclewviewUser();
        getUser();
    }

    private void action() {
        setswipedismissActivity();
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpRecyclewviewUser() {
        adapter = new UsersAdapter(this,arrayList);
        binding.rvUser.setHasFixedSize(true);
        binding.rvUser.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.rvUser.setAdapter(adapter);
    }
    private void getUser(){
        switch (type){
            case "likes":
                getUserLikePost(idpost);
                break;
            case "follower":
                getUserFollower(iduser);
                break;
            case "following":
                getUserFollowing(iduser);
                break;
        }
    }

    private void getUserLikePost(String idpost) {
        final FirebaseUser curentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("likes").child(idpost);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                final ArrayList<String> arrIDuserlike = new ArrayList<>();
                for(DataSnapshot i : dataSnapshot.getChildren()){
                    arrIDuserlike.add(i.getKey());
                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot o : dataSnapshot.getChildren()){
                            User user = o.getValue(User.class);
                            if(arrIDuserlike.contains(user.getId())){
                                arrayList.add(user);
                            }
                        }
                        adapter.setList(arrayList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void getUserFollower(String iduser) {
        DatabaseReference referencefollow = FirebaseDatabase.getInstance().getReference("follows").child(iduser).child("follows");
        referencefollow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> arrIDuserFollow = new ArrayList<>();
                arrayList.clear();
                for(DataSnapshot i : dataSnapshot.getChildren()){
                    arrIDuserFollow.add(i.getKey());
                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot o : dataSnapshot.getChildren()){
                            User user = o.getValue(User.class);
                            if(arrIDuserFollow.contains(user.getId())){
                                arrayList.add(user);
                            }
                        }
                        adapter.setList(arrayList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getUserFollowing(String iduser){
        DatabaseReference referencefollow = FirebaseDatabase.getInstance().getReference("follows").child(iduser).child("following");
        referencefollow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> arrIDuserFollow = new ArrayList<>();
                arrayList.clear();
                for(DataSnapshot i : dataSnapshot.getChildren()){
                    arrIDuserFollow.add(i.getKey());
                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot o : dataSnapshot.getChildren()){
                            User user = o.getValue(User.class);
                            if(arrIDuserFollow.contains(user.getId())){
                                arrayList.add(user);
                            }
                        }
                        adapter.setList(arrayList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setswipedismissActivity() {
        // set swipe destroy activity
        SwipeBackLayout mSwipeBackLayout = new SwipeBackLayout(this);
        mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_LEFT);
        mSwipeBackLayout.setMaskAlpha(125);
        mSwipeBackLayout.setSwipeBackFactor(0.5f);
        mSwipeBackLayout.attachToActivity(this);
    }
}
