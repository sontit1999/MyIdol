package com.example.myidol.fragment.add;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FragmentAdd extends BaseFragment<FragAddBinding,AddViewmodel> {
    public static final int GALLERY = 123;
    public static String TAG = "sondz";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
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

         binding.setViewmodel(viewmodel);
         binding.ivIdol.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivityForResult(new Intent(Intent.ACTION_PICK,
                         MediaStore.Images.Media.EXTERNAL_CONTENT_URI), GALLERY);
             }
         });
         binding.tvPost.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
//                 DateFormat df = new SimpleDateFormat("h:mm a");
//                 String date = df.format(Calendar.getInstance().getTime());
//                 DatabaseReference comment = database.getReference("post");
//                 Post post = new Post("Sơn tít 99","https://congngheads.com/media/images/anh-dep/tai-hinh-nen-may-tinh-dep-1559550627/anh-gai-xinh-goi-cam-tuoi-cuoi-lam-hinh-nen-full-hd-05-07-2019-1.jpg","Em này kute quá <3 ","999","999","999");
//                 comment.push().setValue(post);
//                 comment.addValueEventListener(new ValueEventListener() {
//                     @Override
//                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                         Toast.makeText(getActivity(), "Data change", Toast.LENGTH_SHORT).show();
//                     }
//
//                     @Override
//                     public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                     }
//                 });
             }
         });
    }

    @Override
    public void ViewCreated() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY && data!= null){
            String url = data.getData().toString();
            Log.d("test","Url:" + url );
            Picasso.get().load(url).into(binding.ivIdol);
        }
    }


}
