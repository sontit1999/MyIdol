package com.example.myidol.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.myidol.R;
import com.example.myidol.adapter.CommentAdapter;
import com.example.myidol.adapter.PostsAdapter;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.callback.ILoadMore;
import com.example.myidol.callback.Postcallback;
import com.example.myidol.databinding.FragHomeBinding;

import com.example.myidol.model.Comment;
import com.example.myidol.model.Notification;
import com.example.myidol.model.Post;
import com.example.myidol.model.User;
import com.example.myidol.ui.MainActivity;
import com.example.myidol.ui.chat.ChatActivity;
import com.example.myidol.ui.postnew.PostNewActivity;
import com.example.myidol.ui.profile.ProfileUserClientActivity;
import com.example.myidol.ui.register.RegisterActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static com.example.myidol.ui.postnew.PostNewActivity.PICK_IMAGE;

public class FragmentHome extends BaseFragment<FragHomeBinding,HomeViewmodel>{
    RecyclerView rvcomment;
    ArrayList<Comment> temp = new ArrayList<>();
    DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("post");
    ArrayList<String> followinglist;
    @Override
    public Class<HomeViewmodel> getViewmodel() {
        return HomeViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_home;
    }

    @Override
    public void setBindingViewmodel() {
        binding.setViewmodel(viewmodel);
        setupRecyclerview();


    }

    private void checkfollowing() {
        followinglist = new ArrayList<>();
        DatabaseReference referencefollow = FirebaseDatabase.getInstance().getReference("follows").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
        referencefollow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followinglist.clear();
                for(DataSnapshot i : dataSnapshot.getChildren()){
                       followinglist.add(i.getKey());
                }
                getPost();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPost() {
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Post> temp = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    for(String id : followinglist){
                        if(id.equals(post.getPublisher())){
                            temp.add(post);
                            break;
                        }
                    }
                }
                Collections.shuffle(temp);
                viewmodel.setPost(temp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setupRecyclerview() {

        binding.rvPost.setHasFixedSize(true);
        binding.rvPost.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvPost.setAdapter(viewmodel.adapter);

    }

    @Override
    public void ViewCreated() {
          checkfollowing();
          viewmodel.getarrPost().observe(this, new Observer<ArrayList<Post>>() {
              @Override
              public void onChanged(final ArrayList<Post> posts) {
                  viewmodel.adapter.setList(posts);
                  binding.pbLoading.setVisibility(View.GONE);
                  viewmodel.adapter.setCallback(new Postcallback() {
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
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("xxx"," on actack");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("xxx"," on destroy view");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("xxx"," on destroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("xxx"," on detack");
    }
    public void addNotification(Post post){
        FirebaseUser currentUer = FirebaseAuth.getInstance().getCurrentUser();
        Notification notification = new Notification(post.getIdpost(),currentUer.getUid(),"comment your post","post",System.currentTimeMillis()+"");
        FirebaseDatabase.getInstance().getReference("notification").child(post.getPublisher()).child(System.currentTimeMillis()+"").setValue(notification);
    }
    public void addLikeNotification(Post post){
        FirebaseUser currentUer = FirebaseAuth.getInstance().getCurrentUser();
        Notification notification = new Notification(post.getIdpost(),currentUer.getUid(),"like your post","post",System.currentTimeMillis()+"");
        FirebaseDatabase.getInstance().getReference("notification").child(post.getPublisher()).child(System.currentTimeMillis()+"").setValue(notification);
    }
}
