package com.example.myidol.adapter;

import com.example.myidol.BR;
import com.example.myidol.R;
import com.example.myidol.base.BaseAdapter;
import com.example.myidol.base.CBAdapter;
import com.example.myidol.callback.Postcallback;
import com.example.myidol.databinding.ItemPostBinding;
import com.example.myidol.model.Post;

public class PostAdapter extends BaseAdapter<Post, ItemPostBinding> {
    Postcallback callback;
    @Override
    public int getLayoutId() {
        return R.layout.item_post;
    }

    @Override
    public int getIdVariable() {
        return com.example.myidol.BR.post;
    }

    @Override
    public int getIdVariableOnclick() {
        return BR.callback;
    }

    @Override
    public CBAdapter getOnclick() {
        return callback;
    }
    public void setCallback(Postcallback callback){
        this.callback = callback;
    }
}
