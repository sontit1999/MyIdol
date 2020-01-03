package com.example.myidol.ui.comment;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myidol.R;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityCommentBinding;
import com.example.myidol.model.Comment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.gw.swipeback.SwipeBackLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CommentActivity extends BaseActivity<ActivityCommentBinding,CommentViewmodel> {
    String idpost ;
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
        idpost = getIntent().getStringExtra("idpost");
        Toast.makeText(this, idpost, Toast.LENGTH_SHORT).show();
        binding.setViewmodel(viewmodel);
        // set swipe destroy activity
        SwipeBackLayout mSwipeBackLayout = new SwipeBackLayout(this);
        mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_TOP);
        mSwipeBackLayout.setMaskAlpha(125);
        mSwipeBackLayout.setSwipeBackFactor(0.5f);
        mSwipeBackLayout.attachToActivity(this);
        // set recyclerview
          binding.rvComment.setHasFixedSize(true);
          binding.rvComment.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
          binding.rvComment.setAdapter(viewmodel.adapter);
          viewmodel.getarrComment().observe(this, new Observer<ArrayList<Comment>>() {
              @Override
              public void onChanged(ArrayList<Comment> comments) {
                  viewmodel.adapter.setList(comments);
              }
          });

          binding.tvSend.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  addMessage();
              }
          });
    }

    private void addMessage() {


    }
}
