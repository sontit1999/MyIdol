package com.example.myidol.fragment.profile;


import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.myidol.R;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.callback.PhotoCallback;
import com.example.myidol.databinding.FragProfileBinding;
import com.example.myidol.model.Photo;
import com.example.myidol.ui.image.ImageFullActivity;


import java.util.ArrayList;

public class FragmentProfile extends BaseFragment<FragProfileBinding,ProfileViewmodel> {
    public static final int GETIMAGEAVATAR = 9212;
    public static final int GETIMAGECOVER = 92;
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
        // set recyclerview
        binding.rvPhoto.setHasFixedSize(true);
        binding.rvPhoto.setLayoutManager(new GridLayoutManager(getContext(),3));
       // binding.rvPhoto.setAdapter(viewmodel.adapter);
        // click avatar
        binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI), GETIMAGEAVATAR);
            }
        });
        // click cover image
        binding.ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI), GETIMAGECOVER);
            }
        });
    }

    @Override
    public void ViewCreated() {
        viewmodel.getArrayPhoto().observe(this, new Observer<ArrayList<Photo>>() {
            @Override
            public void onChanged(final ArrayList<Photo> photos) {
                viewmodel.adapter.setList(photos);

                binding.progressLoad.setVisibility(View.INVISIBLE);
                viewmodel.adapter.setCallback(new PhotoCallback() {
                    @Override
                    public void onPhotoClick(Photo photo) {
                        Intent intent = new Intent(getActivity(), ImageFullActivity.class);
                        intent.putExtra("photo",photo);
                        startActivity(intent);
                    }
                });
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GETIMAGEAVATAR && data!= null){
            String url = data.getData().toString();
            Log.d("test","Url:" + url );
            binding.ivAvatar.setImageURI(Uri.parse(url));
        }else  if(requestCode == GETIMAGECOVER  && data!= null){
            String url = data.getData().toString();
            Log.d("test","Url:" + url );
            binding.ivCover.setImageURI(Uri.parse(url));
        }
    }
}
