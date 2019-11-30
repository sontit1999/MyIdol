package com.example.myidol.ui.comment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.adapter.CommentAdapter;
import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.model.Comment;

import java.util.ArrayList;

public class CommentViewmodel extends BaseViewmodel {
    CommentAdapter adapter = new CommentAdapter();
    MutableLiveData<ArrayList<Comment>> arrComment = new MutableLiveData<>();
    public CommentViewmodel(@NonNull Application application) {
        super(application);
    }
   public MutableLiveData<ArrayList<Comment>> getarrComment(){
           ArrayList<Comment> arrayList = new ArrayList<>();
           arrayList.add(new Comment("https://live.staticflickr.com/4625/38601020180_6d76e03a8d_b.jpg","Sơn tít","Em này xinh quá","30 phút"));
           arrayList.add(new Comment("https://live.staticflickr.com/4625/38601020180_6d76e03a8d_b.jpg","Sơn tít","Em này xinh quá","30 phút"));
           arrayList.add(new Comment("https://live.staticflickr.com/4625/38601020180_6d76e03a8d_b.jpg","Sơn tít","Em này xinh quá","30 phút"));
           arrayList.add(new Comment("https://live.staticflickr.com/4625/38601020180_6d76e03a8d_b.jpg","Sơn tít","Em này xinh quá","30 phút"));
           arrayList.add(new Comment("https://live.staticflickr.com/4625/38601020180_6d76e03a8d_b.jpg","Sơn tít","Em này xinh quá","30 phút"));
           arrayList.add(new Comment("https://live.staticflickr.com/4625/38601020180_6d76e03a8d_b.jpg","Sơn tít","Em này xinh quá","30 phút"));
           arrayList.add(new Comment("https://live.staticflickr.com/4625/38601020180_6d76e03a8d_b.jpg","Sơn tít","Em này xinh quá","30 phút"));
           arrayList.add(new Comment("https://live.staticflickr.com/4625/38601020180_6d76e03a8d_b.jpg","Sơn tít","Em này xinh quá","30 phút"));
           arrComment.postValue(arrayList);
          return arrComment;
   }
}
