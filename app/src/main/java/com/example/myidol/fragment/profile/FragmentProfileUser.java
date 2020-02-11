package com.example.myidol.fragment.profile;

import android.content.Intent;
import android.util.Log;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myidol.R;
import com.example.myidol.adapter.PostsAdapter;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.callback.PhotoCallback;
import com.example.myidol.databinding.FragProfileUserBinding;
import com.example.myidol.model.Photo;
import com.example.myidol.model.Post;
import com.example.myidol.model.User;
import com.example.myidol.ui.comment.CommentActivity;
import com.example.myidol.ui.editprofile.EditProfileActivity;
import com.example.myidol.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import static android.app.Activity.RESULT_OK;


public class FragmentProfileUser extends BaseFragment<FragProfileUserBinding,ProfileUserViewmodel> {
    PostsAdapter adapter ;
    ArrayList<Post> arrayList;
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
        getPost();
        getInfor();
    }

    private void getPost() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("post");
        reference.orderByChild("publisher").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("size",dataSnapshot.getChildrenCount()+"");
                ArrayList<Post> temp = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    temp.add(ds.getValue(Post.class));
                }
                Collections.reverse(temp);
                viewmodel.setPost(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupRecyclerview() {
        // set up recyclerview post
        arrayList = new ArrayList<>();
        binding.rvHotidol.setHasFixedSize(true);
        binding.rvHotidol.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        adapter = new PostsAdapter(getContext(),arrayList,binding.rvHotidol);
        binding.rvHotidol.setAdapter(adapter);

        binding.rvPhotos.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        binding.rvPhotos.setLayoutManager(mLayoutManager);
        binding.rvPhotos.setAdapter(viewmodel.adapter);
    }

    private void init() {
//        binding.btnSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), EditProfileActivity.class));
//            }
//        });
//        binding.containNumberFollowing.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), CommentActivity.class);
//                intent.putExtra("type","following");
//                intent.putExtra("iduser",FirebaseAuth.getInstance().getCurrentUser().getUid());
//                startActivity(intent);
//            }
//        });
//        binding.containNumberFollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), CommentActivity.class);
//                intent.putExtra("type","follower");
//                intent.putExtra("iduser",FirebaseAuth.getInstance().getCurrentUser().getUid());
//                startActivity(intent);
//            }
//        });
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
       viewmodel.getArrayPost().observe(this, new Observer<ArrayList<Post>>() {
           @Override
           public void onChanged(ArrayList<Post> posts) {
               adapter.setList(posts);

           }
       });
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
}
