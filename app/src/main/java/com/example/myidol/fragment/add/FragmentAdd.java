package com.example.myidol.fragment.add;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myidol.R;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.databinding.FragAddBinding;

public class FragmentAdd extends BaseFragment<FragAddBinding,AddViewmodel> {
    public static final int GALLERY = 123;
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
                 Log.d("test","post idol");
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
            binding.ivIdol.setImageURI(Uri.parse(url));
        }
    }
}
