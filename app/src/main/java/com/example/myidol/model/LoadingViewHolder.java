package com.example.myidol.model;

import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myidol.R;

public class LoadingViewHolder extends RecyclerView.ViewHolder
{

    public ProgressBar progressBar;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar)itemView.findViewById(R.id.pdBar);
    }
}