package com.example.myidol.fragment.add;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

public class FragmentAdd extends BaseFragment<FragAddBinding,AddViewmodel> {
    public static final int GALLERY = 123;
    public static String TAG = "sondz";
    Uri imageURL = null;
    StorageReference storageReference;
    DatabaseReference postRef;
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
         storageReference = FirebaseStorage.getInstance().getReference("uploads");
         postRef = FirebaseDatabase.getInstance().getReference("post");
         binding.setViewmodel(viewmodel);
         binding.ivIdol.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent();
                 intent.setType("image/*");
                 intent.setAction(Intent.ACTION_GET_CONTENT);
                 startActivityForResult(intent, GALLERY);
             }
         });
         binding.tvPost.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                if(TextUtils.isEmpty(binding.edtMota.getText())){
                    Toast.makeText(getActivity(), "Input description!", Toast.LENGTH_SHORT).show();
                }else if(imageURL == null){
                    Toast.makeText(getActivity(), "Input Image!", Toast.LENGTH_SHORT).show();

                }else{
                      final StorageReference ref = FirebaseStorage.getInstance().getReference().child(System.currentTimeMillis()+"");
                      ref.putFile(imageURL).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                              Toast.makeText(getActivity(), "upload succes!", Toast.LENGTH_SHORT).show();
//
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.d("test", String.valueOf(uri));
                                        DateFormat df = new SimpleDateFormat("h:mm a");
                                        String date = df.format(Calendar.getInstance().getTime());
                                        Post post = new Post(System.currentTimeMillis()+"","Sơn tít",String.valueOf(uri),binding.edtMota.getText().toString(),"0","0","0");
                                        postRef.child(System.currentTimeMillis() + "").setValue(post);
                                        Toast.makeText(getActivity(), "Đã đăng" , Toast.LENGTH_SHORT).show();


                                    }
                                });
                          }
                      });



                }

             }
         });
    }

    @Override
    public void ViewCreated() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY && data.getData()!= null){
            imageURL =  data.getData();
            Picasso.get().load(imageURL).into(binding.ivIdol);
        }
    }

}
