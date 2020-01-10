package com.example.myidol.ui.comment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.adapter.CommentAdapter;
import com.example.myidol.adapter.UsersAdapter;
import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.Comment;
import com.example.myidol.model.User;

import java.util.ArrayList;

public class CommentViewmodel extends BaseViewmodel {
    public CommentViewmodel(@NonNull Application application) {
        super(application);
    }

}
