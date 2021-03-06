package com.example.myidol.callback;

import android.view.View;

import com.example.myidol.base.CBAdapter;
import com.example.myidol.model.Photo;
import com.example.myidol.model.Post;

public interface Postcallback extends CBAdapter {
    void onPhotoClick(Post post);
    void onLikeClick(Post post, View view);
    void onCommentClick(Post post);
    void onShareClick(Post post);
    void onAuthorclickClick(Post post);
    void onLoadmore();
}
