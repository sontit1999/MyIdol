package com.example.myidol.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myidol.MainAplication;
import com.example.myidol.R;
import com.example.myidol.callback.ILoadMore;
import com.example.myidol.callback.Postcallback;
import com.example.myidol.fragment.home.FragmentHome;
import com.example.myidol.model.Comment;
import com.example.myidol.model.LoadingViewHolder;
import com.example.myidol.model.Myviewhoder;
import com.example.myidol.model.Notification;
import com.example.myidol.model.Post;
import com.example.myidol.model.User;
import com.example.myidol.ui.comment.CommentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_POST = 151;
    public static final int TYPE_NULL = 1515;
    Context context;
    ArrayList<Post> arrayList;
    Postcallback listener;
    public PostsAdapter() {
    }

    public PostsAdapter(Context context, ArrayList<Post> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = (Postcallback) context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_POST)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_post,parent,false);
            return new Holder(view);
        }
        else if(viewType == TYPE_NULL)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
            final Post post = arrayList.get(position);
            if (holder instanceof LoadingViewHolder){
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder)holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
            if(holder instanceof Holder){
                final Holder myhoder = (Holder) holder;
                myhoder.bind(post);
                new Thread(new Runnable() {
                    public void run() {
                        cloneUser(post.getPublisher(),myhoder.iv_author,((Holder) holder).tv_nameAuthor);
                        islike(post.getIdpost(),myhoder.ivlike,myhoder.tv_numlike,myhoder.tv_numcomment);
                    }
                }).start();

                myhoder.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onCommentClick(post);
                    }
                });
                myhoder.ivpost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onPhotoClick(post);
                    }
                });
                myhoder.iv_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onShareClick(post);
                        PopupMenu popup = new PopupMenu(context, myhoder.iv_more);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
                        popup.show();//showing popup menu
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()){
                                    case R.id.one:
                                        Toast.makeText(context, "Reported succes!", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                                return true;
                            }
                        });
                    }
                });
                myhoder.tv_nameAuthor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onAuthorclickClick(post);
                    }
                });
                myhoder.iv_author.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onAuthorclickClick(post);
                    }
                });
                myhoder.ivlike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "like click", Toast.LENGTH_SHORT).show();
                        if(myhoder.ivlike.getTag().equals("liked")){
                            // romove like
                            myhoder.ivlike.setImageResource(R.drawable.icons8like);
                            FirebaseDatabase.getInstance().getReference().child("likes").child(post.getIdpost()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

                        }else{
                            // add like
                            addNotification(post);
                            Animation rotate = AnimationUtils.loadAnimation(context,R.anim.like);
                            myhoder.ivlike.startAnimation(rotate);
                            myhoder.ivlike.setImageResource(R.drawable.icons8liked);
                            FirebaseDatabase.getInstance().getReference().child("likes").child(post.getIdpost()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);

                        }
                    }
                });
                myhoder.tv_numlike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, CommentActivity.class);
                        intent.putExtra("type","likes");
                        intent.putExtra("idpost",post.getIdpost());
                        context.startActivity(intent);
                    }
                });
            }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return arrayList.get(position) == null ? TYPE_NULL:TYPE_POST;
    }
    class LoadingViewHolder extends RecyclerView.ViewHolder
    {

        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar)itemView.findViewById(R.id.pdBar);
        }
    }
    public void setList(ArrayList<Post> arrayList){
         this.arrayList = arrayList;
         notifyDataSetChanged();
    }
    public void cloneUser(String iduser, final ImageView ivavatar, final TextView tvname){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(iduser);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("user",user.getUsername());
                Glide
                        .with(context)
                        .load(user.getImageUrl())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .override(250,250)
                        .into(ivavatar);
                tvname.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void islike(final String postid, final ImageView ivlike, final TextView tvnumberlike, final TextView tvnumberComment){
        {
            final FirebaseUser curentUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("likes").child(postid);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      if(dataSnapshot.child(curentUser.getUid()).exists()){
                          ivlike.setImageResource(R.drawable.icons8liked);
                          ivlike.setTag("liked");
                      }else {
                          ivlike.setImageResource(R.drawable.iconsheart);
                          ivlike.setTag("like");
                      }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }
    public void addNotification(Post post){
        FirebaseUser currentUer = FirebaseAuth.getInstance().getCurrentUser();
        Notification notification = new Notification(post.getIdpost(),currentUer.getUid(),"like your post","post",System.currentTimeMillis()+"");
        FirebaseDatabase.getInstance().getReference("notification").child(post.getPublisher()).child(System.currentTimeMillis()+"").setValue(notification);
    }

   public class Holder extends RecyclerView.ViewHolder{
       public ImageView iv_author,ivpost,ivlike,ivcomment, ivshare,iv_more;
       public TextView tv_nameAuthor,tv_describe,tv_time,tv_numlike,tv_numcomment;
       public LinearLayout like,comment,share;
       public Holder(@NonNull View itemView) {
           super(itemView);
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
                       .placeholder(R.drawable.ic_launcher_foreground)
                       .override(500,250)
                       .into(ivpost);
           }

       }
   }
   public void setContext(Context context){
        this.context = context;
   }
}
