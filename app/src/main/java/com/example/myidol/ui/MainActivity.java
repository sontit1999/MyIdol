package com.example.myidol.ui;

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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myidol.R;
import com.example.myidol.adapter.CommentAdapter;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.callback.Postcallback;
import com.example.myidol.databinding.ActivityMainBinding;
import com.example.myidol.fragment.add.FragmentAdd;
import com.example.myidol.fragment.favorite.FragmentFavorite;
import com.example.myidol.fragment.home.FragmentHome;
import com.example.myidol.fragment.notification.FragmentNotification;
import com.example.myidol.fragment.profile.FragmentProfileUser;
import com.example.myidol.fragment.search.FragmentSearch;
import com.example.myidol.model.Comment;
import com.example.myidol.model.Photo;
import com.example.myidol.model.Post;
import com.example.myidol.ui.image.ImageFullActivity;
import com.example.myidol.ui.profile.ProfileUserClientActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewmodel> implements Postcallback {
    ArrayList<Comment> temp = new ArrayList<>();
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
        loadFragment(new FragmentHome());
        // set listener bottom nav
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        loadFragment(new FragmentHome());
                        break;
                    case R.id.nav_hot:
                        loadFragment(new FragmentSearch());
                        break;
                    case R.id.nav_add:
                        loadFragment(new FragmentAdd());
                        break;
                    case R.id.nav_notification:
                        loadFragment(new FragmentNotification());
                        break;
                    case R.id.nav_user:
                        loadFragment(new FragmentProfileUser());
                        break;
                }
                return true;
            }

        });
    }
    public void loadFragment(Fragment fragment){
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
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
        Toast.makeText(this, "share click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthorclickClick(Post post) {
        Intent intent = new Intent(MainActivity.this, ProfileUserClientActivity.class);
        intent.putExtra("iduser",post.getPublisher());
        startActivity(intent);
    }

}
