package com.example.myidol.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myidol.R;
import com.example.myidol.callback.Postcallback;
import com.example.myidol.model.Comment;
import com.example.myidol.model.Post;
import com.example.myidol.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.Myviewhoder> {
    Context context;
    ArrayList<Post> arrayList;
    Postcallback listener;
    public PostsAdapter(Context context, ArrayList<Post> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = (Postcallback) context;
    }

    @NonNull
    @Override
    public Myviewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent,false);
        return new Myviewhoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myviewhoder holder, int position) {
         final Post post = arrayList.get(position);
         holder.bind(post);
         cloneUser(post.getPublisher(),holder.iv_author,holder.tv_nameAuthor);
         islike(post.getIdpost(),holder.ivlike,holder.tv_numlike,holder.tv_numcomment);
         holder.comment.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 listener.onCommentClick(post);
             }
         });
         holder.ivpost.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 listener.onPhotoClick(post);
             }
         });
         holder.iv_more.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 listener.onShareClick(post);
                 PopupMenu popup = new PopupMenu(context, holder.iv_more);
                 //Inflating the Popup using xml file
                 popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
                 popup.show();//showing popup menu
             }
         });
         holder.tv_nameAuthor.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 listener.onAuthorclickClick(post);
             }
         });
         holder.iv_author.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 listener.onAuthorclickClick(post);
             }
         });
         holder.ivlike.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Toast.makeText(context, "like click", Toast.LENGTH_SHORT).show();
                 if(holder.ivlike.getTag().equals("liked")){
                     // romove like
                     holder.ivlike.setImageResource(R.drawable.icons8like);
                     FirebaseDatabase.getInstance().getReference().child("likes").child(post.getIdpost()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                     Toast.makeText(context, "unlikes", Toast.LENGTH_SHORT).show();

                 }else{
                     // add like
                     Animation rotate = AnimationUtils.loadAnimation(context,R.anim.like);
                     holder.ivlike.startAnimation(rotate);
                     holder.ivlike.setImageResource(R.drawable.icons8liked);
                     FirebaseDatabase.getInstance().getReference().child("likes").child(post.getIdpost()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                     Toast.makeText(context, "likes", Toast.LENGTH_SHORT).show();
                 }
             }
         });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Myviewhoder extends RecyclerView.ViewHolder{
        ImageView iv_author,ivpost,ivlike,ivcomment, ivshare,iv_more;
        TextView tv_nameAuthor,tv_describe,tv_time,tv_numlike,tv_numcomment;
        LinearLayout like,comment,share;
        public Myviewhoder(@NonNull View itemView) {
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
        private void bind(Post post){
            tv_describe.setText(post.getDecribe());
            tv_time.setText(post.getTimepost());
            Picasso.get().load(post.getLinkImage()).into(ivpost);
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
}
