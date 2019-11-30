package com.example.myidol.fragment.home;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myidol.R;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.callback.Postcallback;
import com.example.myidol.databinding.FragHomeBinding;
import com.example.myidol.model.Photo;
import com.example.myidol.model.Post;
import com.example.myidol.ui.comment.CommentActivity;
import com.example.myidol.ui.image.ImageFullActivity;

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
                  viewmodel.adapter.setCallback(new Postcallback() {
                      @Override
                      public void onPhotoClick(Post post) {
                          Intent intent = new Intent(getActivity(), ImageFullActivity.class);
                          intent.putExtra("photo",new Photo(post.getLinkImage()));
                          startActivity(intent);
                      }

                      @Override
                      public void onLikeClick(Post post) {
                         // Toast.makeText(getActivity(), "likeclick:" + post.getLinkImage(), Toast.LENGTH_SHORT).show();
                      }

                      @Override
                      public void onCommentClick(Post post) {
                          Intent intent = new Intent(getActivity(), CommentActivity.class);
                          startActivity(intent);
                      }

                      @Override
                      public void onDownloadClick(Post post) {
                         // Toast.makeText(getActivity(), "Downloadclick:" + post.getLinkImage(), Toast.LENGTH_SHORT).show();
                      }

                      @Override
                      public void onDownAuthorclickClick(Post post) {
                          //Toast.makeText(getActivity(), "Authorclick:" + post.getLinkImage(), Toast.LENGTH_SHORT).show();
                      }
                  });
              }
          });
    }
}
