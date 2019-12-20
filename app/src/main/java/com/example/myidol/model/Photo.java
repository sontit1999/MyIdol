package com.example.myidol.model;

import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import java.io.Serializable;

public class Photo implements Serializable {
    String linkImage;

    public Photo() {
    }

    public Photo(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

}
