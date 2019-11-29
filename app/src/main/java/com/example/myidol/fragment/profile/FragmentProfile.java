package com.example.myidol.fragment.profile;


import android.util.Log;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.myidol.R;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.callback.PhotoCallback;
import com.example.myidol.databinding.FragProfileBinding;
import com.example.myidol.model.Photo;


import java.util.ArrayList;

public class FragmentProfile extends BaseFragment<FragProfileBinding,ProfileViewmodel> {
    @Override
    public Class<ProfileViewmodel> getViewmodel() {
        return ProfileViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_profile;
    }

    @Override
    public void setBindingViewmodel() {
        binding.setViewmodel(viewmodel);
        binding.rvPhoto.setHasFixedSize(true);
        binding.rvPhoto.setLayoutManager(new GridLayoutManager(getContext(),3));
        binding.rvPhoto.setAdapter(viewmodel.adapter);

    }

    @Override
    public void ViewCreated() {
        viewmodel.getArrayPhoto().observe(this, new Observer<ArrayList<Photo>>() {
            @Override
            public void onChanged(ArrayList<Photo> photos) {
                viewmodel.adapter.setList(photos);
                binding.progressLoad.setVisibility(View.INVISIBLE);
                viewmodel.adapter.setCallback(new PhotoCallback() {
                    @Override
                    public void onPhotoClick(Photo photo) {
                        Log.d("test","on photo click:"+ photo.getLinkImage());
                    }
                });
            }

        });
    }
}
