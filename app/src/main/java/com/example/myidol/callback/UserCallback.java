package com.example.myidol.callback;

import com.example.myidol.base.CBAdapter;
import com.example.myidol.model.User;

public interface UserCallback extends CBAdapter {
    void onAvatarClick(User user);
    void onFollowClick(User user);
}
