package com.example.myidol.fragment.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myidol.R;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.databinding.FragHomeBinding;
import com.example.myidol.model.Post;

import java.util.ArrayList;

public class FragmentHome extends BaseFragment<FragHomeBinding,HomeViewmodel> {
    @Override
    public Class<HomeViewmodel> getViewmodel() {
        return HomeViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_home;
    }

    @Override
    public void setBindingViewmodel() {
        binding.setViewmodel(viewmodel);
        binding.rvPost.setHasFixedSize(true);
        binding.rvPost.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.rvPost.setAdapter(viewmodel.adapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    viewmodel.adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(binding.rvPost);
    }

    @Override
    public void ViewCreated() {
          viewmodel.getarrPost().observe(this, new Observer<ArrayList<Post>>() {
              @Override
              public void onChanged(ArrayList<Post> posts) {
                  viewmodel.adapter.setList(posts);
              }
          });
    }
}
