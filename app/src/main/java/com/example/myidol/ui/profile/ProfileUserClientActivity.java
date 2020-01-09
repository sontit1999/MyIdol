package com.example.myidol.ui.profile;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myidol.R;
import com.example.myidol.adapter.CommentAdapter;
import com.example.myidol.adapter.PhotoAdapter;
import com.example.myidol.adapter.PostsAdapter;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.callback.PhotoCallback;
import com.example.myidol.callback.Postcallback;
import com.example.myidol.databinding.ActivityProfileClientUserBinding;
import com.example.myidol.model.Comment;
import com.example.myidol.model.Photo;
import com.example.myidol.model.Post;
import com.example.myidol.model.User;
import com.example.myidol.ui.MainActivity;
import com.example.myidol.ui.detailchat.DetailChatActivity;
import com.example.myidol.ui.image.ImageFullActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gw.swipeback.SwipeBackLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ProfileUserClientActivity extends BaseActivity<ActivityProfileClientUserBinding,ProfileUserViewModel> implements Postcallback {
    ArrayList<Post> arrayList;
    PostsAdapter adapter;
    String iduser;
    ArrayList<Comment> temp = new ArrayList<>();
    @Override
    public Class<ProfileUserViewModel> getViewmodel() {
        return ProfileUserViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_profile_client_user;
    }

    @Override
    public void setBindingViewmodel() {
        Intent intent = getIntent();
        if(intent!=null){
            iduser = intent.getStringExtra("iduser");
        }
        binding.setViewmodel(viewmodel);
        setwipedismisActivity();
        setupRecyclerview();
        getPost(iduser);
        getPhotos();
        getInfor();
        isFollowing(iduser,binding.btnFollow);
        action();
        viewmodel.getArrphoto().observe(this, new Observer<ArrayList<Photo>>() {
            @Override
            public void onChanged(ArrayList<Photo> photos) {
                viewmodel.adapter.setList(photos);
                viewmodel.adapter.setCallback(new PhotoCallback() {
                    @Override
                    public void onPhotoClick(Photo photo) {
                        Intent intent = new Intent(ProfileUserClientActivity.this, ImageFullActivity.class);
                        intent.putExtra("photo",photo);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void setwipedismisActivity() {
        // set swipe destroy activity
        SwipeBackLayout mSwipeBackLayout = new SwipeBackLayout(this);
        mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_TOP);
        mSwipeBackLayout.setMaskAlpha(125);
        mSwipeBackLayout.setSwipeBackFactor(0.5f);
        mSwipeBackLayout.attachToActivity(this);
    }

    private void action() {
        binding.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileUserClientActivity.this, DetailChatActivity.class);
                intent.putExtra("iduser",iduser);
                startActivity(intent);
            }
        });
        binding.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser curentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(binding.btnFollow.getText().equals("follow")){
                    FirebaseDatabase.getInstance().getReference("follows").child(curentUser.getUid())
                            .child("following").child(iduser).setValue(true);
                    FirebaseDatabase.getInstance().getReference("follows").child(iduser)
                            .child("follows").child(curentUser.getUid()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference("follows").child(curentUser.getUid())
                            .child("following").child(iduser).removeValue();
                    FirebaseDatabase.getInstance().getReference("follows").child(iduser)
                            .child("follows").child(curentUser.getUid()).removeValue();
                }
            }
        });
    }

    private void getPhotos() {
        ArrayList<Photo> temp = new ArrayList<>();

    }

    private void getInfor() {
        FirebaseDatabase.getInstance().getReference("Users").child(iduser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                binding.setUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference follow = FirebaseDatabase.getInstance().getReference("follows").child(iduser).child("follows");
        follow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                binding.numberFollow.setText(dataSnapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(iduser.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            binding.btnChat.setVisibility(View.GONE);
            binding.btnFollow.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerview() {
        arrayList = new ArrayList<>();
        adapter = new PostsAdapter(this,arrayList);
        binding.rvPost.setHasFixedSize(true);
        binding.rvPost.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvPost.setAdapter(adapter);

        // recyclerview photo

        binding.rvPhotos.setHasFixedSize(true);
        binding.rvPhotos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvPhotos.setAdapter(viewmodel.adapter);
    }
    private void getPost(String iduser) {
        final ArrayList<Photo> temp = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("post");
        reference.orderByChild("publisher").equalTo(iduser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    temp.add(new Photo(post.getLinkImage()));
                    arrayList.add(post);
                }
                Log.d("sizepost",arrayList.size() + "");
                Collections.reverse(arrayList);
                Collections.shuffle(temp);
                viewmodel.setListPhoto(temp);
                adapter.setList(arrayList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onPhotoClick(Post post) {
       Intent intent = new Intent(ProfileUserClientActivity.this,ImageFullActivity.class);
       intent.putExtra("photo",new Photo(post.getLinkImage()));
       startActivity(intent);
    }

    @Override
    public void onLikeClick(Post post) {

    }

    @Override
    public void onCommentClick(final Post post) {
        // nạp layout vào view
        final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.bottom_sheet_comment_fragment,null);
        // ánh xạ
        final RecyclerView rvcomment = (RecyclerView) view.findViewById(R.id.rv_comment);
        final EditText etContent = (EditText) view.findViewById(R.id.et_comment);
        ImageView iv_send = (ImageView) view.findViewById(R.id.iv_send);
        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        final TextView tv_numbercmt = (TextView) view.findViewById(R.id.numbercomment);
        // set uprecyclerview
        final CommentAdapter adapter = new CommentAdapter(this,temp);
        rvcomment.setHasFixedSize(true);
        rvcomment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvcomment.setAdapter(adapter);
        // lắng nghe comment của post
        final DatabaseReference cmtofPost = FirebaseDatabase.getInstance().getReference("comments");
        cmtofPost.child(post.getIdpost()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("sizecmt",dataSnapshot.getChildrenCount() + "");
                tv_numbercmt.setText(dataSnapshot.getChildrenCount() + " comments");
                temp.clear();
                for(DataSnapshot i : dataSnapshot.getChildren()){
                    Comment comment = i.getValue(Comment.class);
                    temp.add(comment);
                }
                adapter.setList(temp);
                rvcomment.scrollToPosition(adapter.getItemCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        // set actiom
        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = etContent.getText().toString();
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(ProfileUserClientActivity.this, "Comment not empty!", Toast.LENGTH_SHORT).show();
                }else{
                    String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    Comment comment = new Comment(FirebaseAuth.getInstance().getCurrentUser().getUid(),content,date);
                    DatabaseReference cmt = FirebaseDatabase.getInstance().getReference("comments");
                    cmt.child(post.getIdpost()).child(System.currentTimeMillis()+"").setValue(comment)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ProfileUserClientActivity.this, "Đã comment", Toast.LENGTH_SHORT).show();
                                    etContent.setText("");
                                }
                            })
                    ;
                }
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
    }

    @Override
    public void onShareClick(Post post) {

    }

    @Override
    public void onAuthorclickClick(Post post) {

    }
    private void isFollowing(final String id, final Button tvfollow){
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
}
