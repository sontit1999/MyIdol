package com.example.myidol.ui.chat;

import com.example.myidol.R;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityChatBinding;
import com.gw.swipeback.SwipeBackLayout;

public class ChatActivity extends BaseActivity<ActivityChatBinding,ChatViewmodel> {
    @Override
    public Class<ChatViewmodel> getViewmodel() {
        return ChatViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_chat;
    }

    @Override
    public void setBindingViewmodel() {
       binding.setViewmodel(viewmodel);
        // set swipe destroy activity
        SwipeBackLayout mSwipeBackLayout = new SwipeBackLayout(this);
        mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_LEFT);
        mSwipeBackLayout.setMaskAlpha(125);
        mSwipeBackLayout.setSwipeBackFactor(0.5f);
        mSwipeBackLayout.attachToActivity(this);
    }
}
