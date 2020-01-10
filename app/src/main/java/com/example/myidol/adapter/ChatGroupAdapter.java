package com.example.myidol.adapter;

import com.example.myidol.BR;
import com.example.myidol.R;
import com.example.myidol.base.BaseAdapter;
import com.example.myidol.base.CBAdapter;
import com.example.myidol.callback.GroupCallback;
import com.example.myidol.databinding.ItemGroupchatBinding;
import com.example.myidol.model.GroupChat;

public class ChatGroupAdapter extends BaseAdapter<GroupChat, ItemGroupchatBinding> {
    GroupCallback callback;
    @Override
    public int getLayoutId() {
        return R.layout.item_groupchat;
    }

    @Override
    public int getIdVariable() {
        return BR.group;
    }

    @Override
    public int getIdVariableOnclick() {
        return BR.callback;
    }

    @Override
    public CBAdapter getOnclick() {
        return (CBAdapter) callback;
    }
    public void setCallback(GroupCallback callback){
        this.callback = callback;
    }
}
