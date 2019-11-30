package com.example.myidol.ui.image;

import android.content.Intent;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myidol.R;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityImageBinding;
import com.example.myidol.model.Photo;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageFullActivity extends BaseActivity<ActivityImageBinding,ImageFullViewmodel>{
    Photo photo;
    @Override
    public Class<ImageFullViewmodel> getViewmodel() {
        return ImageFullViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_image;
    }

    @Override
    public void setBindingViewmodel() {
        binding.setViewmodel(viewmodel);

        // get intent
        Intent intent = getIntent();
        if(intent!=null){
            photo = (Photo) intent.getSerializableExtra("photo");
            Glide.with(this).load(photo.getLinkImage()).into(binding.ivFullscreen);
        }

    }
}
