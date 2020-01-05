package com.example.myidol.fragment.add;

import android.Manifest;
import android.app.Activity;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myidol.R;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.databinding.FragAddBinding;
import com.example.myidol.model.Comment;
import com.example.myidol.model.Message;
import com.example.myidol.model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class FragmentAdd extends BaseFragment<FragAddBinding,AddViewmodel> {
    public static final int GALLERY = 123;
    StorageReference storageReference;
    DatabaseReference postRef;
    Uri imageURL = null;
    @Override
    public Class<AddViewmodel> getViewmodel() {
        return AddViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_add;
    }

    @Override
    public void setBindingViewmodel() {
             postRef = FirebaseDatabase.getInstance().getReference();
             binding.ivIdol.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                    Intent galleryIntent = new Intent(
                             Intent.ACTION_PICK,
                             MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                     startActivityForResult(galleryIntent, GALLERY);
                 }
             });
             binding.tvPost.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     DateFormat df = new SimpleDateFormat("d MMM yyyy HH:mm:ss");
                     String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                     String idpost = postRef.push().getKey();
                     Log.d("hihi",date);
                     Post post = new Post(idpost,"https://i.a4vn.com/2019/7/27/bop-chut-o-eo-keo-chut-o-chan-gai-xinh-chinh-anh-suong-suong-toi-luc-lo-anh-duoc-tag-lai-khien-dan-tinh-choang-vang-954981.jpg",binding.edtMota.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getUid(),date);
                     postRef.child("post").child(idpost).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void aVoid) {
                             Toast.makeText(getActivity(), "Đã đăng", Toast.LENGTH_SHORT).show();
                         }
                     });
                 }
             });
    }

    @Override
    public void ViewCreated() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data!=null)
            switch (requestCode){
                case GALLERY:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    binding.ivIdol.setImageURI(selectedImage);
                    break;
            }

    }

}
