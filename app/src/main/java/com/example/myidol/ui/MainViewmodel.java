package com.example.myidol.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.adapter.CommentAdapter;
import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.Comment;

import java.util.ArrayList;

public class MainViewmodel extends BaseViewmodel {

    public MainViewmodel(@NonNull Application application) {
        super(application);
    }
}
