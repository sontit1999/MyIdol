package com.example.myidol.ui.editprofile;

import android.view.View;
import android.widget.Toast;

import com.example.myidol.R;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends BaseActivity<ActivityEditProfileBinding,EditProfileViewModel> {
    @Override
    public Class<EditProfileViewModel> getViewmodel() {
        return EditProfileViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_edit_profile;
    }

    @Override
    public void setBindingViewmodel() {
        binding.setViewmodel(viewmodel);
        action();
    }

    private void action() {
        binding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditProfileActivity.this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
