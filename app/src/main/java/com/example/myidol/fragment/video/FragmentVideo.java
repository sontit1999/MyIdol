package com.example.myidol.fragment.video;

import android.content.res.Resources;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.myidol.R;
import com.example.myidol.adapter.VideoPlayerRecyclerAdapter;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.base.Resource;
import com.example.myidol.databinding.FragVideoBinding;
import com.example.myidol.model.MediaObject;
import com.example.myidol.ui.customview.VerticalSpacingItemDecorator;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentVideo extends BaseFragment<FragVideoBinding,VideoViewmodel> {
    @Override
    public Class<VideoViewmodel> getViewmodel() {
        return VideoViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_video;
    }

    @Override
    public void setBindingViewmodel() {
       binding.setViewmodel(viewmodel);
       initRecyclerView();
    }

    @Override
    public void ViewCreated() {

    }
    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        binding.recyclerView.addItemDecoration(itemDecorator);

        ArrayList<MediaObject> mediaObjects = new ArrayList<MediaObject>(Arrays.asList(Resource.MEDIA_OBJECTS));
        binding.recyclerView.setMediaObjects(mediaObjects);
        VideoPlayerRecyclerAdapter adapter = new VideoPlayerRecyclerAdapter(mediaObjects, initGlide(),getContext());
        binding.recyclerView.setAdapter(adapter);
    }

    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(binding.recyclerView!=null)
            binding.recyclerView.releasePlayer();
    }
}

