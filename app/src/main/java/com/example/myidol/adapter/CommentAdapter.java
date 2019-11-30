package com.example.myidol.adapter;



import com.example.myidol.BR;
import com.example.myidol.R;
import com.example.myidol.base.BaseAdapter;
import com.example.myidol.base.CBAdapter;
import com.example.myidol.callback.CommentCallback;
import com.example.myidol.databinding.ItemCommentBinding;
import com.example.myidol.model.Comment;

public class CommentAdapter extends BaseAdapter<Comment, ItemCommentBinding> {
    CommentCallback callback;
    @Override
    public int getLayoutId() {
        return R.layout.item_comment;
    }

    @Override
    public int getIdVariable() {
        return com.example.myidol.BR.comment;
    }

    @Override
    public int getIdVariableOnclick() {
        return BR.callback;
    }

    @Override
    public CBAdapter getOnclick() {
        return callback;
    }
    public void setCallback(CommentCallback callback) {
        this.callback = callback;
    }
}
