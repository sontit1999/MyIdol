package com.example.myidol.fragment.hot;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    LinearLayoutManager manager;
    int totalitem;
    boolean isloading = false;
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
        // binding.rvHotidol.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
         binding.rvHotidol.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
         binding.rvHotidol.setAdapter(viewmodel.adapter);

         manager = (LinearLayoutManager) binding.rvHotidol.getLayoutManager();

         binding.rvHotidol.addOnScrollListener(new RecyclerView.OnScrollListener() {
             @Override
             public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                 super.onScrolled(recyclerView, dx, dy);
                 Log.d("sontit"," vị trí item cuối cùng nhìn thấy "  + manager.findLastVisibleItemPosition());
                 if(totalitem-1==manager.findLastVisibleItemPosition() && !isloading && totalitem<=500 ){
                    // Toast.makeText(getActivity(), "Đã hết", Toast.LENGTH_SHORT).show();
                     viewmodel.showProgress(binding.progressCircular);
                     isloading = true;
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
                   totalitem = viewmodel.adapter.getItemCount();
                   viewmodel.hiddenProgress(binding.progressCircular);
                   isloading = false;
                   Log.d("sontit", manager.getItemCount() + "");
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
