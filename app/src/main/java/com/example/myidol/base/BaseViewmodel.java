package com.example.myidol.base;

import android.app.Application;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;

public abstract class BaseViewmodel extends AndroidViewModel {

    public BaseViewmodel(@NonNull Application application) {
        super(application);
    }
}
