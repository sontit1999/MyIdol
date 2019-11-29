package com.example.myidol.adapter;

import com.example.myidol.BR;
import com.example.myidol.R;
import com.example.myidol.base.BaseAdapter;
import com.example.myidol.base.CBAdapter;
import com.example.myidol.callback.PhotoCallback;
import com.example.myidol.databinding.ItemPhotoBinding;
import com.example.myidol.model.Photo;

public class PhotoAdapter extends BaseAdapter<Photo, ItemPhotoBinding> {
    private PhotoCallback callback;
    @Override
    public int getLayoutId() {
        return R.layout.item_photo;
    }

    @Override
    public int getIdVariable() {
        return BR.photo;
    }

    @Override
    public int getIdVariableOnclick() {
        return BR.callback;
    }

    @Override
    public CBAdapter getOnclick() {
        return callback;
    }
    public void setCallback(PhotoCallback callback){
        this.callback =  callback;
    }
}
