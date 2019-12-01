package com.example.myidol.callback;

import com.example.myidol.base.CBAdapter;
import com.example.myidol.model.IdolHot;
import com.example.myidol.model.Photo;

public interface IdolCallback extends CBAdapter {
    void onPhotoClick(IdolHot idolHot);
}
