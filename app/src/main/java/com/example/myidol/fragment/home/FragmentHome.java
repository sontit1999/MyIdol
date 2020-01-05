package com.example.myidol.fragment.home;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myidol.R;
import com.example.myidol.adapter.PostsAdapter;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.callback.Postcallback;
import com.example.myidol.databinding.FragHomeBinding;
import com.example.myidol.model.Photo;
import com.example.myidol.model.Post;
import com.example.myidol.model.User;
import com.example.myidol.ui.comment.CommentActivity;
import com.example.myidol.ui.image.ImageFullActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentHome extends BaseFragment<FragHomeBinding,HomeViewmodel> {
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
       // checkfollowing();
        checkfollowing();

    }

    private void checkfollowing() {
        followinglist = new ArrayList<>();
        Log.d("size",followinglist.size()+"");
        DatabaseReference referencefollow = FirebaseDatabase.getInstance().getReference("follows").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
        referencefollow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot i : dataSnapshot.getChildren()){
                       followinglist.add(i.getKey());
                       Log.d("key",i.getKey());
                }
                Log.d("size",followinglist.size()+"");
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
                viewmodel.setPost(temp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setupRecyclerview() {
        arrayList = new ArrayList<>();
        adapter = new PostsAdapter(getContext(),arrayList);
        binding.rvPost.setHasFixedSize(true);
        binding.rvPost.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvPost.setAdapter(adapter);
    }

    @Override
    public void ViewCreated() {
          viewmodel.getarrPost().observe(this, new Observer<ArrayList<Post>>() {
              @Override
              public void onChanged(final ArrayList<Post> posts) {
                  adapter.setList(posts);
              }
          });
    }

}
