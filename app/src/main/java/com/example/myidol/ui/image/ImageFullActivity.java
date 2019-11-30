package com.example.myidol.ui.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myidol.R;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityImageBinding;
import com.example.myidol.model.Photo;
import com.gw.swipeback.SwipeBackLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;


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
        // set swipe destroy activity
        SwipeBackLayout mSwipeBackLayout = new SwipeBackLayout(this);
        mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_TOP);
        mSwipeBackLayout.setMaskAlpha(125);
        mSwipeBackLayout.setSwipeBackFactor(0.5f);
        mSwipeBackLayout.attachToActivity(this);
        // save image
        binding.containerDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  Drawable drawable = binding.ivFullscreen.getDrawable();
                  Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("test","destroy");
    }

}
