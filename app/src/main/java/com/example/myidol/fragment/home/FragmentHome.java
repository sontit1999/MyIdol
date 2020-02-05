package com.example.myidol.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.myidol.R;
import com.example.myidol.adapter.PostsAdapter;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.callback.ILoadMore;
import com.example.myidol.databinding.FragHomeBinding;

import com.example.myidol.model.Post;
import com.example.myidol.model.User;
import com.example.myidol.ui.chat.ChatActivity;
import com.example.myidol.ui.postnew.PostNewActivity;
import com.example.myidol.ui.register.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.myidol.ui.postnew.PostNewActivity.PICK_IMAGE;

public class FragmentHome extends BaseFragment<FragHomeBinding,HomeViewmodel>{
    ArrayList<Post> arrayList;
    PostsAdapter adapter;
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
        arrayList = new ArrayList<>();
        binding.rvPost.setHasFixedSize(true);
        binding.rvPost.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new PostsAdapter(getContext(),arrayList);
        binding.rvPost.setAdapter(adapter);
    }

    @Override
    public void ViewCreated() {
          checkfollowing();
          viewmodel.getarrPost().observe(this, new Observer<ArrayList<Post>>() {
              @Override
              public void onChanged(final ArrayList<Post> posts) {
                  adapter.setList(posts);
                  binding.pbLoading.setVisibility(View.GONE);
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
}
