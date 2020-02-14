package com.example.myidol.fragment.profile;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myidol.R;
import com.example.myidol.adapter.CommentAdapter;
import com.example.myidol.adapter.PostsAdapter;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.callback.PhotoCallback;
import com.example.myidol.callback.Postcallback;
import com.example.myidol.databinding.FragProfileUserBinding;
import com.example.myidol.model.Comment;
import com.example.myidol.model.Notification;
import com.example.myidol.model.Photo;
import com.example.myidol.model.Post;
import com.example.myidol.model.User;
import com.example.myidol.ui.comment.CommentActivity;
import com.example.myidol.ui.editprofile.EditProfileActivity;
import com.example.myidol.ui.login.LoginActivity;
import com.example.myidol.ui.profile.ProfileUserClientActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static android.app.Activity.RESULT_OK;


public class FragmentProfileUser extends BaseFragment<FragProfileUserBinding,ProfileUserViewmodel> {
    RecyclerView rvcomment;
    ArrayList<Comment> temp = new ArrayList<>();
    public static int PICK_IMAGEGallery = 125;

    public String getUrlProfile() {
        return urlProfile;
    }

    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;
    }

    String urlProfile = "https://i.pinimg.com/originals/82/49/22/824922ef9208b68312a930256116dd5c.jpg";
    @Override
    public Class<ProfileUserViewmodel> getViewmodel() {
        return ProfileUserViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_profile_user;
    }

    @Override
    public void setBindingViewmodel() {
        init();
        binding.setViewmodel(viewmodel);
        setupRecyclerview();
        getInfor();
    }

    private void setupRecyclerview() {
        // set up recyclerview post
        binding.rvHotidol.setHasFixedSize(true);
        binding.rvHotidol.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        binding.rvHotidol.setAdapter(viewmodel.postAdapter);

        binding.rvPhotos.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        binding.rvPhotos.setLayoutManager(mLayoutManager);
        binding.rvPhotos.setAdapter(viewmodel.adapter);
    }

    private void init() {

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        binding.templateInfor.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 viewmodel.gotoImagefull(urlProfile,getContext());
            }
        });
    }

    @Override
    public void ViewCreated() {
      viewmodel.getArrphoto().observe(this, new Observer<ArrayList<Photo>>() {
          @Override
          public void onChanged(ArrayList<Photo> photos) {
              viewmodel.adapter.setList(photos);
              viewmodel.adapter.setCallback(new PhotoCallback() {
                  @Override
                  public void onPhotoClick(Photo photo) {
                      viewmodel.gotoImagefull(photo.getLinkImage(),getContext());
                  }
              });
          }
      });
      viewmodel.getArrayPost().observe(this, new Observer<ArrayList<Post>>() {
          @Override
          public void onChanged(ArrayList<Post> posts) {
              viewmodel.postAdapter.setList(posts);
              viewmodel.postAdapter.setCallback(new Postcallback() {
                  @Override
                  public void onPhotoClick(Post post) {
                      viewmodel.gotoImagefull(post.getLinkImage(),getContext());
                  }

                  @Override
                  public void onLikeClick(Post post,View view) {
                      ImageView iv = (ImageView) view;
                      if(iv.getTag().equals("liked")){
                          // romove like
                          iv.setImageResource(R.drawable.icons8like);
                          FirebaseDatabase.getInstance().getReference().child("likes").child(post.getIdpost()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

                      }else{
                          // add like
                          addNotification(post);
                          Animation rotate = AnimationUtils.loadAnimation(getContext(),R.anim.like);
                          iv.startAnimation(rotate);
                          iv.setImageResource(R.drawable.icons8liked);
                          FirebaseDatabase.getInstance().getReference().child("likes").child(post.getIdpost()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);

                      }
                  }

                  @Override
                  public void onCommentClick(final Post post) {
                      final View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_comment_fragment,null);
                      // ánh xạ
                      rvcomment = (RecyclerView) view.findViewById(R.id.rv_comment);
                      final EditText etContent = (EditText) view.findViewById(R.id.et_comment);
                      ImageView iv_send = (ImageView) view.findViewById(R.id.iv_send);
                      ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
                      final TextView tv_numbercmt = (TextView) view.findViewById(R.id.numbercomment);
                      // set uprecyclerview
                      final CommentAdapter adapter = new CommentAdapter(getContext(),temp);
                      rvcomment.setHasFixedSize(true);
                      rvcomment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                      rvcomment.setAdapter(adapter);
                      // lắng nghe comment của post
                      final DatabaseReference cmtofPost = FirebaseDatabase.getInstance().getReference("comments");
                      cmtofPost.child(post.getIdpost()).addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              tv_numbercmt.setText(dataSnapshot.getChildrenCount() + " comments");
                              temp.clear();
                              for(DataSnapshot i : dataSnapshot.getChildren()){
                                  Comment comment = i.getValue(Comment.class);
                                  temp.add(comment);
                              }
                              Collections.reverse(temp);
                              adapter.setList(temp);
                              rvcomment.smoothScrollToPosition(0);
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError databaseError) {

                          }
                      });
                      final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(),R.style.SheetDialog);
                      bottomSheetDialog.setContentView(view);
                      bottomSheetDialog.show();
                      // set actiom
                      iv_send.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              String content = etContent.getText().toString();
                              if(TextUtils.isEmpty(content)){
                                  Toast.makeText(getActivity(), "Comment not empty!", Toast.LENGTH_SHORT).show();
                              }else{
                                  String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                  Comment comment = new Comment(FirebaseAuth.getInstance().getCurrentUser().getUid(),content,date);
                                  DatabaseReference cmt = FirebaseDatabase.getInstance().getReference("comments");
                                  cmt.child(post.getIdpost()).child(System.currentTimeMillis()+"").setValue(comment)
                                          .addOnSuccessListener(new OnSuccessListener<Void>() {
                                              @Override
                                              public void onSuccess(Void aVoid) {
                                                  Toast.makeText(getContext(), "Đã comment", Toast.LENGTH_SHORT).show();
                                                  etContent.setText("");
                                                  if(!post.getPublisher().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                                      addNotification(post);
                                                  }

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
                      Intent intent = new Intent(getActivity(), ProfileUserClientActivity.class);
                      intent.putExtra("iduser",post.getPublisher());
                      startActivity(intent);
                  }

                  @Override
                  public void onLoadmore() {

                  }
              });
          }
      });
      viewmodel.getMoreFeature(binding.templateInfor.tvNumberpost,binding.templateInfor.tvNumberFollower,binding.templateInfor.tvNumberfollowing);
    }
    public void getInfor(){
        final DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);
                setUrlProfile(user1.getImageUrl());
                Log.d("user",user1.getUsername());
                binding.templateInfor.setUser(user1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode== PICK_IMAGEGallery && data!=null){
           // binding.ivAvatar.setImageURI(data.getData());
            viewmodel.uploadAvatar(data.getData(),getContext());
        }
    }
    public void addNotification(Post post){
        FirebaseUser currentUer = FirebaseAuth.getInstance().getCurrentUser();
        Notification notification = new Notification(post.getIdpost(),currentUer.getUid(),"comment your post","post",System.currentTimeMillis()+"");
        FirebaseDatabase.getInstance().getReference("notification").child(post.getPublisher()).child(System.currentTimeMillis()+"").setValue(notification);
    }
}
