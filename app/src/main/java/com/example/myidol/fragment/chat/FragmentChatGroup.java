package com.example.myidol.fragment.chat;

import com.example.myidol.R;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.databinding.FragChatgroupBinding;

public class FragmentChatGroup extends BaseFragment<FragChatgroupBinding,ChatGroupViewmodel> {
    @Override
    public Class<ChatGroupViewmodel> getViewmodel() {
        return ChatGroupViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_chatgroup;
    }

    @Override
    public void setBindingViewmodel() {
       binding.setViewmodel(viewmodel);
    }

    @Override
    public void ViewCreated() {

    }
}
