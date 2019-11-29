package com.example.myidol.callback;

import com.example.myidol.base.CBAdapter;
import com.example.myidol.model.Photo;

public interface PhotoCallback extends CBAdapter {
    void onPhotoClick(Photo photo);
}
