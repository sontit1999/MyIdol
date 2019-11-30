package com.example.myidol.callback;

import com.example.myidol.base.CBAdapter;
import com.example.myidol.model.Photo;
import com.example.myidol.model.Post;

public interface Postcallback extends CBAdapter {
    void onPhotoClick(Post post);
    void onLikeClick(Post post);
    void onCommentClick(Post post);
    void onDownloadClick(Post post);
    void onDownAuthorclickClick(Post post);
}
