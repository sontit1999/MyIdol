package com.example.myidol.fragment.favorite;

import android.content.Intent;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.myidol.R;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.callback.PhotoCallback;
import com.example.myidol.databinding.FragFavoviteBinding;
import com.example.myidol.model.Photo;
import com.example.myidol.ui.image.ImageFullActivity;

import java.util.ArrayList;

public class FragmentFavorite extends BaseFragment<FragFavoviteBinding,FavoriteViewmodel> {
    @Override
    public Class<FavoriteViewmodel> getViewmodel() {
        return FavoriteViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_favovite;
    }

    @Override
    public void setBindingViewmodel() {
        binding.setViewmodel(viewmodel);
        binding.rvFavoriteIdol.setHasFixedSize(true);
        binding.rvFavoriteIdol.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.rvFavoriteIdol.setAdapter(viewmodel.adapter);
    }

    @Override
    public void ViewCreated() {
        viewmodel.getArrayPhoto().observe(this, new Observer<ArrayList<Photo>>() {
            @Override
            public void onChanged(ArrayList<Photo> photos) {
                viewmodel.adapter.setList(photos);
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
}
