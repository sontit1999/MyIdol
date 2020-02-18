package com.example.myidol.model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myidol.R;
import com.squareup.picasso.Picasso;

public class Myviewhoder extends RecyclerView.ViewHolder{
    Context context;
    public ImageView iv_author,ivpost,ivlike,ivcomment, ivshare,iv_more;
    public TextView tv_nameAuthor,tv_describe,tv_time,tv_numlike,tv_numcomment;
    public LinearLayout like,comment,share;
    public Myviewhoder(@NonNull View itemView,Context context) {
        super(itemView);
        this.context = context;
        iv_more = (ImageView) itemView.findViewById(R.id.iv_more);
        iv_author = (ImageView) itemView.findViewById(R.id.iv_avatar);
        ivshare = (ImageView) itemView.findViewById(R.id.iv_shares);
        ivlike = (ImageView) itemView.findViewById(R.id.iv_hearts);
        ivcomment = (ImageView) itemView.findViewById(R.id.iv_comments);
        ivpost = ( ImageView) itemView.findViewById(R.id.iv_post);
        tv_nameAuthor = (TextView) itemView.findViewById(R.id.tv_nameAuthor);
        tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        tv_numlike = (TextView) itemView.findViewById(R.id.numberlikepost);
        tv_numcomment = (TextView) itemView.findViewById(R.id.numbercommentpost);
        tv_describe = (TextView) itemView.findViewById(R.id.tv_describe);
        comment = (LinearLayout) itemView.findViewById(R.id.comments);
        like = (LinearLayout) itemView.findViewById(R.id.likes);
        share = (LinearLayout) itemView.findViewById(R.id.shares);
    }
    public void bind(Post post){
        tv_describe.setText(post.getDecribe());
        tv_time.setText(post.getTimepost());
        if(post.getLinkImage().equals("no")){
            ivpost.setVisibility(View.GONE);
        }else{
            Glide
                    .with(context)
                    .load(post.getLinkImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .override(500,250)
                    .into(ivpost);
        }

    }
}
