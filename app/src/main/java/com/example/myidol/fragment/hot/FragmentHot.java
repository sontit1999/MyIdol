package com.example.myidol.fragment.hot;

import android.util.Log;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myidol.R;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.databinding.FragHotBinding;
import com.example.myidol.model.Photo;

import java.util.ArrayList;

import me.yuqirong.cardswipelayout.CardItemTouchHelperCallback;
import me.yuqirong.cardswipelayout.CardLayoutManager;
import me.yuqirong.cardswipelayout.OnSwipeListener;

public class FragmentHot extends BaseFragment<FragHotBinding,HotViewmodel> {
    ArrayList<Photo> arrayList = new ArrayList<>();
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
    }

    @Override
    public void ViewCreated() {

       viewmodel.getArrayPhoto().observe(this, new Observer<ArrayList<Photo>>() {
           @Override
           public void onChanged(ArrayList<Photo> photos) {
               viewmodel.adapter.setList(photos);
           }
       });
    }
}
