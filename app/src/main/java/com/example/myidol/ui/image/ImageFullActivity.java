package com.example.myidol.ui.image;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.myidol.R;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityImageBinding;
import com.example.myidol.model.Photo;
import com.gw.swipeback.SwipeBackLayout;

import java.io.ByteArrayOutputStream;
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
        checkpermisionns();
        binding.setViewmodel(viewmodel);
        // get intent
        Intent intent = getIntent();
        if(intent!=null){
            photo = (Photo) intent.getSerializableExtra("photo");
            Glide.with(this).load(photo.getLinkImage()).into(binding.ivFullscreen);
        }
        // set swipe destroy activity
        swipedissmisActivity();
        // save image
        binding.containerDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImageFromImageview(binding.ivFullscreen);
            }
        });
    }

    private void swipedissmisActivity() {
        SwipeBackLayout mSwipeBackLayout = new SwipeBackLayout(this);
        mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_TOP);
        mSwipeBackLayout.setMaskAlpha(125);
        mSwipeBackLayout.setSwipeBackFactor(0.5f);
        mSwipeBackLayout.attachToActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("test","destroy");
    }
    public static File savebitmap(Bitmap bmp) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "testimage.jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }
    public void saveImageFromImageview(ImageView imageView){
        // Get the image from drawable resource as drawable object
        Drawable drawable = imageView.getDrawable();
        // Get the bitmap from drawable object
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

        // Save image to gallery
        String savedImageURL = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "Bird",
                "Image of bird"
        );

        Toast.makeText(this, "Image saved to gallery :" + savedImageURL, Toast.LENGTH_SHORT).show();
        Log.d("test","Image saved to gallery :" + savedImageURL);
    }
    private void checkpermisionns() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 124);
        }
    }
}
