package com.example.myidol.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myidol.R;
import com.example.myidol.callback.Postcallback;
import com.example.myidol.model.Post;
import com.example.myidol.model.User;
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
    public void onBindViewHolder(@NonNull Myviewhoder holder, int position) {
         final Post post = arrayList.get(position);
         holder.bind(post);
         cloneUser(post.getPublisher(),holder.iv_author,holder.tv_nameAuthor);
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
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Myviewhoder extends RecyclerView.ViewHolder{
        ImageView iv_author,ivpost;
        TextView tv_nameAuthor,tv_describe,tv_time;
        LinearLayout like,comment,share;
        public Myviewhoder(@NonNull View itemView) {
            super(itemView);
            iv_author = (ImageView) itemView.findViewById(R.id.iv_avatar);
            ivpost = ( ImageView) itemView.findViewById(R.id.iv_post);
            tv_nameAuthor = (TextView) itemView.findViewById(R.id.tv_nameAuthor);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_describe = (TextView) itemView.findViewById(R.id.tv_describe);
            comment = (LinearLayout) itemView.findViewById(R.id.comments);
            like = (LinearLayout) itemView.findViewById(R.id.comments);
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
}
