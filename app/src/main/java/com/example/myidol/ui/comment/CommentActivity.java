package com.example.myidol.ui.comment;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myidol.R;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityCommentBinding;
import com.example.myidol.model.Comment;
import com.gw.swipeback.SwipeBackLayout;

import java.util.ArrayList;

public class CommentActivity extends BaseActivity<ActivityCommentBinding,CommentViewmodel> {
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
    }
}
