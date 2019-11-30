package com.example.myidol.fragment.hot;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myidol.R;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.callback.PhotoCallback;
import com.example.myidol.databinding.FragHotBinding;
import com.example.myidol.model.Photo;
import com.example.myidol.ui.image.ImageFullActivity;

import java.util.ArrayList;

import me.yuqirong.cardswipelayout.CardItemTouchHelperCallback;
import me.yuqirong.cardswipelayout.CardLayoutManager;
import me.yuqirong.cardswipelayout.OnSwipeListener;

public class FragmentHot extends BaseFragment<FragHotBinding,HotViewmodel> {
    @Override
    public Class<HotViewmodel> getViewmodel() {
        return HotViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_hot;
    }

    @Override
    public void setBindingViewmodel() {
         binding.setViewmodel(viewmodel);
         binding.rvHotidol.setHasFixedSize(true);
         binding.rvHotidol.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
         binding.rvHotidol.setAdapter(viewmodel.adapter);


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
