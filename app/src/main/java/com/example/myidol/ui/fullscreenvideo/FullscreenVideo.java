package com.example.myidol.ui.fullscreenvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.myidol.R;
import com.gw.swipeback.SwipeBackLayout;

import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.VideoInfo;


public class FullscreenVideo extends AppCompatActivity {
    String urlvideo = "";
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_video);
        setswipedismissActivity();
        Intent intent = getIntent();
        if(intent!=null){
            urlvideo = intent.getStringExtra("url");
        }
        GiraffePlayer.play(this,new VideoInfo(urlvideo));
    }
    private void setswipedismissActivity() {
        // set swipe destroy activity
        SwipeBackLayout mSwipeBackLayout = new SwipeBackLayout(this);
        mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_TOP);
        mSwipeBackLayout.setMaskAlpha(125);
        mSwipeBackLayout.setSwipeBackFactor(0.5f);
        mSwipeBackLayout.attachToActivity(this);
    }
}
