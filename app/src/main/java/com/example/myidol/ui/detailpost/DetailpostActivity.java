package com.example.myidol.ui.detailpost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myidol.R;
import com.example.myidol.adapter.CommentAdapter;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityDetailpostBinding;
import com.example.myidol.model.Comment;
import com.example.myidol.model.Photo;
import com.example.myidol.model.Post;
import com.example.myidol.model.User;
import com.example.myidol.ui.MainActivity;
import com.example.myidol.ui.comment.CommentActivity;
import com.example.myidol.ui.image.ImageFullActivity;
import com.example.myidol.ui.profile.ProfileUserClientActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gw.swipeback.SwipeBackLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class DetailpostActivity extends BaseActivity<ActivityDetailpostBinding,DetailpostViewmodel> {
    CommentAdapter adapter;
    Post post;
    ArrayList<Comment> arrayList;
    String idpost = "";
    @Override
    public Class<DetailpostViewmodel> getViewmodel() {
        return DetailpostViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_detailpost;
    }

    @Override
    public void setBindingViewmodel() {
        post = new Post();




        binding.setViewmodel(viewmodel);
        Intent intent = getIntent();
        if(intent!=null){
            idpost = intent.getStringExtra("idpost");
        }
        action();
        setswipedismissActivity();
        setUpRecyclerviewComment();
        getPost();
        getComment();
        islike(idpost,binding.ivHearts,binding.numberlikepost,binding.numbercommentpost);
    }

    private void action() {
        binding.numberlikepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailpostActivity.this, CommentActivity.class);
                intent.putExtra("type","likes");
                intent.putExtra("idpost",post.getIdpost());
                startActivity(intent);
            }
        });
        binding.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.scroll.scrollTo(0,binding.scroll.getBottom());
        binding.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(DetailpostActivity.this, binding.ivMore);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
                popup.show();//showing popup menu
            }
        });
       binding.ivHearts.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(binding.ivHearts.getTag().equals("liked")){
                   // romove like
                   binding.ivHearts.setImageResource(R.drawable.icons8like);
                   FirebaseDatabase.getInstance().getReference().child("likes").child(idpost).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

               }else{
                   // add like
                   Animation rotate = AnimationUtils.loadAnimation(DetailpostActivity.this,R.anim.like);
                   binding.ivHearts.startAnimation(rotate);
                   binding.ivHearts.setImageResource(R.drawable.icons8liked);
                   FirebaseDatabase.getInstance().getReference().child("likes").child(idpost).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
               }
           }
       });
       binding.ivSend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String content = binding.etComment.getText().toString();
               if(TextUtils.isEmpty(content)){
                   Toast.makeText(DetailpostActivity.this, "Comment not empty!", Toast.LENGTH_SHORT).show();
               }else{
                   String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                   Comment comment = new Comment(FirebaseAuth.getInstance().getCurrentUser().getUid(),content,date);
                   DatabaseReference cmt = FirebaseDatabase.getInstance().getReference("comments");
                   cmt.child(idpost).child(System.currentTimeMillis()+"").setValue(comment)
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Toast.makeText(DetailpostActivity.this, "Đã comment", Toast.LENGTH_SHORT).show();
                                   binding.etComment.setText("");
                                   binding.scroll.scrollTo(0,binding.scroll.getBottom());
                               }
                           })
                   ;
               }
           }
       });
       binding.ivPost.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(DetailpostActivity.this, ImageFullActivity.class);
               intent.putExtra("photo",new Photo(post.getLinkImage()));
               startActivity(intent);
           }
       });
       // get infor user publisher
    }

    private void getComment() {
        final DatabaseReference cmtofPost = FirebaseDatabase.getInstance().getReference("comments");
        cmtofPost.child(idpost).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("sizecmt",dataSnapshot.getChildrenCount() + "");
                arrayList.clear();
                for(DataSnapshot i : dataSnapshot.getChildren()){
                    Comment comment = i.getValue(Comment.class);
                    arrayList.add(comment);
                }
                Collections.reverse(arrayList);
                adapter.setList(arrayList);
                binding.rvComment.scrollToPosition(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpRecyclerviewComment() {
        arrayList = new ArrayList<>();
        adapter = new CommentAdapter(this,arrayList);
        binding.rvComment.setHasFixedSize(true);
        binding.rvComment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvComment.setAdapter(adapter);
    }


    private void getPost() {
        FirebaseDatabase.getInstance().getReference("post").child(idpost).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post mainpost = dataSnapshot.getValue(Post.class);
                post = dataSnapshot.getValue(Post.class);
                binding.setPost(mainpost);
                Log.d("post",post.getPublisher());
                getInforUser(post.getPublisher());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void islike(final String postid, final ImageView ivlike, final TextView tvnumberlike, final TextView tvnumberComment){
        {
            final FirebaseUser curentUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("likes").child(postid);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(curentUser.getUid()).exists()){
                        ivlike.setImageResource(R.drawable.icons8liked);
                        ivlike.setTag("liked");
                    }else {
                        ivlike.setImageResource(R.drawable.iconsheart);
                        ivlike.setTag("like");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            // get number like
            FirebaseDatabase.getInstance().getReference("likes").child(postid).
                    addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d("number like :" ,dataSnapshot.getChildrenCount() + "likes");
                            tvnumberlike.setText(dataSnapshot.getChildrenCount() + " likes");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
            // get number comment
            FirebaseDatabase.getInstance().getReference("comments").
                    child(postid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("sizecmt",dataSnapshot.getChildrenCount() + "");
                    tvnumberComment.setText(dataSnapshot.getChildrenCount() + " comments");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }
    private void setswipedismissActivity() {
        // set swipe destroy activity
        SwipeBackLayout mSwipeBackLayout = new SwipeBackLayout(this);
        mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_LEFT);
        mSwipeBackLayout.setMaskAlpha(125);
        mSwipeBackLayout.setSwipeBackFactor(0.5f);
        mSwipeBackLayout.attachToActivity(this);
    }
    public void getInforUser(String iduser) {
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(iduser);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("userxxxxx",user.getImageUrl());
                binding.tvNameAuthor.setText(user.getUsername());
                Picasso.get().load(user.getImageUrl()).into(binding.ivAvatar);
                binding.tvNameAuthor.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
