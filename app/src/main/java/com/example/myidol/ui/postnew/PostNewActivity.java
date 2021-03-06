package com.example.myidol.ui.postnew;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myidol.R;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityPostNewBinding;
import com.example.myidol.model.Post;
import com.example.myidol.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.myidol.fragment.add.FragmentAdd.GALLERY;

public class PostNewActivity extends BaseActivity<ActivityPostNewBinding,PostNewViewModel>{
    public static int PICK_IMAGE = 54151;
    Uri imageURL = null;
    DatabaseReference postRef;
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
        postRef = FirebaseDatabase.getInstance().getReference("post");
        checkpermisionns();
        setupToolbar();
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
               final String caption = binding.etCaption.getText().toString().trim();
               if(TextUtils.isEmpty(caption)){
                   Toast.makeText(PostNewActivity.this, "Caption not empty", Toast.LENGTH_SHORT).show();
               }else if(imageURL == null){
                   binding.pbLoading.setVisibility(View.VISIBLE);
                   DateFormat df = new SimpleDateFormat("h:mm a");
                   String date = df.format(Calendar.getInstance().getTime());
                   Post post = new Post(System.currentTimeMillis()+"","no",caption, FirebaseAuth.getInstance().getCurrentUser().getUid()+"",date);
                   postRef.child(System.currentTimeMillis() + "").setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           binding.pbLoading.setVisibility(View.GONE);
                           finish();
                       }
                   });
               }else{
                   binding.pbLoading.setVisibility(View.VISIBLE);
                   final StorageReference ref = FirebaseStorage.getInstance().getReference().child(System.currentTimeMillis()+"");
                   ref.putFile(imageURL).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   Log.d("test", String.valueOf(uri));
                                   DateFormat df = new SimpleDateFormat("h:mm a");
                                   String date = df.format(Calendar.getInstance().getTime());
                                   Post post = new Post(System.currentTimeMillis()+"",String.valueOf(uri),caption, FirebaseAuth.getInstance().getCurrentUser().getUid()+"",date);
                                   postRef.child(System.currentTimeMillis() + "").setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                           binding.pbLoading.setVisibility(View.GONE);
                                           finish();
                                       }
                                   });
                                   Toast.makeText(PostNewActivity.this, "Đã đăng" , Toast.LENGTH_SHORT).show();


                               }
                           });
                       }
                   });
               }
           }
       });
    }
    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    private void choosePhotofromgallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("xxx","on result");
        if(resultCode == RESULT_OK){
            if(requestCode == PICK_IMAGE && data != null){
                imageURL =  data.getData();
                Picasso.get().load(imageURL).into(binding.ivPost);
            }
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
