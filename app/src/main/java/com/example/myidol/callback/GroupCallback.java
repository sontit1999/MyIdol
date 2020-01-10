package com.example.myidol.callback;

import com.example.myidol.base.CBAdapter;
import com.example.myidol.model.GroupChat;

public interface GroupCallback extends CBAdapter {
    void onGroupclick(GroupChat groupChat);
}
