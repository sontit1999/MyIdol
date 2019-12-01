package com.example.myidol.fragment.hot;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.myidol.R;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.callback.IdolCallback;
import com.example.myidol.databinding.FragHotBinding;
import com.example.myidol.model.IdolHot;
import com.example.myidol.model.Photo;
import com.example.myidol.ui.image.ImageFullActivity;

import java.util.ArrayList;
public class FragmentHot extends BaseFragment<FragHotBinding,HotViewmodel> {
    ArrayList<IdolHot> arrTemp = new ArrayList<>();
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
         binding.rvHotidol.addOnScrollListener(new RecyclerView.OnScrollListener() {
             @Override
             public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                 super.onScrollStateChanged(recyclerView, newState);
                 if (!recyclerView.canScrollVertically(1)) {
                     Toast.makeText(getContext(), "Hết rùi.Đợi tí đang tải thêm :v", Toast.LENGTH_LONG).show();
                     viewmodel.getmoreIdol();
                 }
             }
         });
    }

    @Override
    public void ViewCreated() {
           viewmodel.getArrayIDol().observe(this, new Observer<ArrayList<IdolHot>>() {
               @Override
               public void onChanged(ArrayList<IdolHot> idolHots) {
                   viewmodel.adapter.setList(idolHots);
                   viewmodel.adapter.setCallback(new IdolCallback() {
                       @Override
                       public void onPhotoClick(IdolHot idolHot) {
                           Intent intent = new Intent(getActivity(), ImageFullActivity.class);
                           intent.putExtra("photo",new Photo(idolHot.getLinkphoto()));
                           startActivity(intent);
                       }
                   });
               }
           });
    }
}
