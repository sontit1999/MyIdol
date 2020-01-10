package com.example.myidol.fragment.notification;

import com.example.myidol.R;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.databinding.FragNotificationBinding;

public class FragmentNotification extends BaseFragment<FragNotificationBinding,NotificationViewModel> {
    @Override
    public Class<NotificationViewModel> getViewmodel() {
        return NotificationViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_notification;
    }

    @Override
    public void setBindingViewmodel() {
         binding.setViewmodel(viewmodel);
    }

    @Override
    public void ViewCreated() {

    }
}
