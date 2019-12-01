package com.example.myidol.adapter;


import com.example.myidol.R;
import com.example.myidol.base.BaseAdapter;
import com.example.myidol.base.CBAdapter;
import com.example.myidol.callback.IdolCallback;
import com.example.myidol.databinding.ItemIdolhotBinding;
import com.example.myidol.model.IdolHot;

public class IdolAdapter extends BaseAdapter<IdolHot, ItemIdolhotBinding> {
    private IdolCallback callback;
    @Override
    public int getLayoutId() {
        return R.layout.item_idolhot;
    }

    @Override
    public int getIdVariable() {
        return com.example.myidol.BR.idol;
    }

    @Override
    public int getIdVariableOnclick() {
        return com.example.myidol.BR.callback;
    }

    @Override
    public CBAdapter getOnclick() {
        return callback;
    }
    public void setCallback(IdolCallback callback){
        this.callback = callback;
    }
}
