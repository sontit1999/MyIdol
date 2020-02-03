package com.example.myidol.ui.postnew;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myidol.R;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityPostNewBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import static com.example.myidol.fragment.add.FragmentAdd.GALLERY;

public class PostNewActivity extends BaseActivity<ActivityPostNewBinding,PostNewViewModel>{
    public static int PICK_IMAGE = 54151;
    @Override
    public Class<PostNewViewModel> getViewmodel() {
        return PostNewViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_post_new;
    }

    @Override
    public void setBindingViewmodel() {
        checkpermisionns();
        binding.setViewmodel(viewmodel);
        action();
    }

    private void action() {
       binding.ivPost.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                  choosePhotofromgallery();
           }
       });
       binding.ivBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });
       binding.tvPost.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {


               Toast.makeText(PostNewActivity.this, "Đã đăng", Toast.LENGTH_SHORT).show();

       }
       });
    }
    private void choosePhotofromgallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("xxx","on result");
        if(data!=null && requestCode == PICK_IMAGE){
            Uri uri = data.getData();
            binding.ivPost.setImageURI(uri);
        }
    }
    private void checkpermisionns() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 124);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("xxx","on pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("xxx","on stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("xxx","on destroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("xxx","on restart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("xxx","on start");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("xxx","on reseum");
    }
}
