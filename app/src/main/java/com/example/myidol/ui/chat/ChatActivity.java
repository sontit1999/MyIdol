package com.example.myidol.ui.chat;

import com.example.myidol.R;
import com.example.myidol.adapter.ViewPagerChatAdapter;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityChatBinding;
import com.example.myidol.fragment.chat.FragmentChatBasic;
import com.example.myidol.fragment.chat.FragmentChatGroup;
import com.gw.swipeback.SwipeBackLayout;

public class ChatActivity extends BaseActivity<ActivityChatBinding,ChatViewmodel> {
    FragmentChatBasic fragmentChatBasic = new FragmentChatBasic();
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

       setupToolbar();
       setswipedismissActivity();
       // load fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragmentChatBasic).commit();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setswipedismissActivity() {
        // set swipe destroy activity
        SwipeBackLayout mSwipeBackLayout = new SwipeBackLayout(this);
        mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_TOP);
        mSwipeBackLayout.setMaskAlpha(125);
        mSwipeBackLayout.setSwipeBackFactor(0.5f);
        mSwipeBackLayout.attachToActivity(this);
    }

//    private void setupviewpagerandtab() {
//        ViewPagerChatAdapter viewpager = new ViewPagerChatAdapter(getSupportFragmentManager());
//        viewpager.addFragment(new FragmentChatBasic(),"Chats Basic");
//        viewpager.addFragment(new FragmentChatGroup(),"Groups Chat");
//        binding.viewpager.setAdapter(viewpager);
//        binding.tablayout.setupWithViewPager(binding.viewpager);
//    }
}
