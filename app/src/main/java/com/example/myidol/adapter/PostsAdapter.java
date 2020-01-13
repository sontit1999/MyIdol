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
    private final int VIEW_TYPE_ITEM=0,VIEW_TYPE_LOADING=1;
    ILoadMore loadMore;
    boolean isLoading;
    Context context;
    ArrayList<Post> arrayList;
    Postcallback listener;
    int visibleThreshold=5;
    int lastVisibleItem,totalItemCount;
    public PostsAdapter(RecyclerView recyclerView,Context context, ArrayList<Post> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = (Postcallback) context;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if(!isLoading && totalItemCount <= (lastVisibleItem+visibleThreshold))
                {
                    Log.d("xxxx","on scroll adapter");
                    if(loadMore != null)
                        loadMore.onLoadMore();
                    isLoading = true;
                }

            }
        });
    }
    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }
    @Override
    public int getItemViewType(int position) {
        return arrayList.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM)
        {
            Log.d("xxxx","create view item");
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent,false);
            return new Myviewhoder(view);
        }
        else if(viewType == VIEW_TYPE_LOADING)
        {
            Log.d("xxxx","create view loading");
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }
        return null;
    }
    public void additem(Object object){
        arrayList.add((Post) object);
        notifyItemInserted(arrayList.size());
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof Myviewhoder){
            final Post post = arrayList.get(position);
            final Myviewhoder myviewhoder = (Myviewhoder) holder;
            myviewhoder.bind(post);
            cloneUser(post.getPublisher(), myviewhoder.iv_author, myviewhoder.tv_nameAuthor);
            islike(post.getIdpost(),myviewhoder.ivlike,myviewhoder.tv_numlike,myviewhoder.tv_numcomment);
            myviewhoder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCommentClick(post);
                }
            });
            myviewhoder.ivpost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPhotoClick(post);
                }
            });
            myviewhoder.iv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onShareClick(post);
                    PopupMenu popup = new PopupMenu(context, myviewhoder.iv_more);
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
            myviewhoder.tv_nameAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onAuthorclickClick(post);
                }
            });
            myviewhoder.iv_author.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onAuthorclickClick(post);
                }
            });
            myviewhoder.ivlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "like click", Toast.LENGTH_SHORT).show();
                    if(myviewhoder.ivlike.getTag().equals("liked")){
                        // romove like
                        myviewhoder.ivlike.setImageResource(R.drawable.icons8like);
                        FirebaseDatabase.getInstance().getReference().child("likes").child(post.getIdpost()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                        Toast.makeText(context, "unlikes", Toast.LENGTH_SHORT).show();

                    }else{
                        // add like
                        addNotification(post);
                        Animation rotate = AnimationUtils.loadAnimation(context,R.anim.like);
                        myviewhoder.ivlike.startAnimation(rotate);
                        myviewhoder.ivlike.setImageResource(R.drawable.icons8liked);
                        FirebaseDatabase.getInstance().getReference().child("likes").child(post.getIdpost()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);

                        Toast.makeText(context, "likes", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            myviewhoder.tv_numlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("type","likes");
                    intent.putExtra("idpost",post.getIdpost());
                    context.startActivity(intent);
                }
            });
        }else if(holder instanceof LoadingViewHolder)
        {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder)holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
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
                Picasso.get().load(user.getImageUrl()).into(ivavatar);
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
            // get number like
            FirebaseDatabase.getInstance().getReference("likes").child(postid).
            addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("number like :" ,dataSnapshot.getChildrenCount() + "likes");
                    tvnumberlike.setText(dataSnapshot.getChildrenCount() + " likes");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            // get number comment
            FirebaseDatabase.getInstance().getReference("comments").
            child(postid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("sizecmt",dataSnapshot.getChildrenCount() + "");
                    tvnumberComment.setText(dataSnapshot.getChildrenCount() + " comments");
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
    public void setLoaded() {
        isLoading = false;
    }
}
