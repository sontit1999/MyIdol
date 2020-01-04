package com.example.myidol.adapter;

import com.example.myidol.BR;
import com.example.myidol.R;
import com.example.myidol.base.BaseAdapter;
import com.example.myidol.base.CBAdapter;
import com.example.myidol.callback.UserCallback;
import com.example.myidol.databinding.ItemUserBinding;
import com.example.myidol.model.User;

public class UserAdapter extends BaseAdapter<User, ItemUserBinding> {
    UserCallback callback;
    @Override
    public int getLayoutId() {
        return R.layout.item_user;
    }

    @Override
    public int getIdVariable() {
        return BR.user;
    }

    @Override
    public int getIdVariableOnclick() {
        return BR.callback;
    }

    @Override
    public CBAdapter getOnclick() {
        return callback;
    }
    public void setCallback(UserCallback callback){
        this.callback = callback;
    }

}
