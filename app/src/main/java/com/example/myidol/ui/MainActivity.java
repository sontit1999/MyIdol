package com.example.myidol.ui;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myidol.R;
import com.example.myidol.adapter.CommentAdapter;
import com.example.myidol.adapter.ViewPagerChatAdapter;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.callback.Postcallback;
import com.example.myidol.databinding.ActivityMainBinding;
import com.example.myidol.fragment.add.FragmentAdd;
import com.example.myidol.fragment.chat.FragmentChatBasic;
import com.example.myidol.fragment.chat.FragmentChatGroup;
import com.example.myidol.fragment.favorite.FragmentFavorite;
import com.example.myidol.fragment.home.FragmentHome;
import com.example.myidol.fragment.notification.FragmentNotification;
import com.example.myidol.fragment.profile.FragmentProfile;
import com.example.myidol.fragment.profile.FragmentProfileUser;
import com.example.myidol.fragment.search.FragmentSearch;
import com.example.myidol.fragment.video.FragmentVideo;
import com.example.myidol.model.Comment;
import com.example.myidol.model.Notification;
import com.example.myidol.model.Photo;
import com.example.myidol.model.Post;
import com.example.myidol.ui.chat.ChatActivity;
import com.example.myidol.ui.image.ImageFullActivity;
import com.example.myidol.ui.postnew.PostNewActivity;
import com.example.myidol.ui.profile.ProfileUserClientActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewmodel> implements Postcallback {
    ArrayList<Comment> temp = new ArrayList<>();
    final Fragment fragment1 = new FragmentHome();
    final Fragment fragment2 = new FragmentSearch();
    final Fragment fragment4 = new FragmentNotification();
    final Fragment fragment5 = new FragmentProfileUser();
    final FragmentManager fm = getSupportFragmentManager();
    RecyclerView rvcomment;
    Fragment active = fragment1;
    @Override
    public Class<MainViewmodel> getViewmodel() {
        return MainViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void setBindingViewmodel() {
        binding.setViewmodel(viewmodel);
        // load defaut fragment
        fm.beginTransaction().add(R.id.frame, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.frame, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.frame, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.frame, fragment1, "1").commit();
        // set listener bottom nav
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        fm.beginTransaction().hide(active).show(fragment1).commit();
                        active = fragment1;
                        break;
                    case R.id.nav_hot:
                        fm.beginTransaction().hide(active).show(fragment2).commit();
                        active = fragment2;
                        break;
                    case R.id.nav_add:
                        startActivity(new Intent(MainActivity.this, PostNewActivity.class));
                        break;
                    case R.id.nav_notification:
                        fm.beginTransaction().hide(active).show(fragment4).commit();
                        active = fragment4;
                        break;
                    case R.id.nav_user:
                        fm.beginTransaction().hide(active).show(fragment5).commit();
                        active = fragment5;
                        break;
                }
                return true;
            }
        });
        action();
    }

    private void action() {
        binding.actionBar.ivChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
            }
        });
    }

    @Override
    public void onPhotoClick(Post post) {
        Intent intent = new Intent(MainActivity.this, ImageFullActivity.class);
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
        rvcomment = (RecyclerView) view.findViewById(R.id.rv_comment);
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
                Collections.reverse(temp);
                adapter.setList(temp);
                rvcomment.smoothScrollToPosition(0);
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
                    Toast.makeText(MainActivity.this, "Comment not empty!", Toast.LENGTH_SHORT).show();
                }else{
                    String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    Comment comment = new Comment(FirebaseAuth.getInstance().getCurrentUser().getUid(),content,date);
                    DatabaseReference cmt = FirebaseDatabase.getInstance().getReference("comments");
                    cmt.child(post.getIdpost()).child(System.currentTimeMillis()+"").setValue(comment)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Đã comment", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(MainActivity.this, ProfileUserClientActivity.class);
        intent.putExtra("iduser",post.getPublisher());
        startActivity(intent);
    }
    public void addNotification(Post post){
        FirebaseUser currentUer = FirebaseAuth.getInstance().getCurrentUser();
        Notification notification = new Notification(post.getIdpost(),currentUer.getUid(),"comment your post","post",System.currentTimeMillis()+"");
        FirebaseDatabase.getInstance().getReference("notification").child(post.getPublisher()).child(System.currentTimeMillis()+"").setValue(notification);
    }
}
