package com.example.myidol.adapter;



import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myidol.BR;
import com.example.myidol.R;
import com.example.myidol.base.BaseAdapter;
import com.example.myidol.base.CBAdapter;
import com.example.myidol.callback.CommentCallback;
import com.example.myidol.databinding.ItemCommentBinding;
import com.example.myidol.model.Comment;
import com.example.myidol.model.User;
import com.example.myidol.ui.MainActivity;
import com.example.myidol.ui.profile.ProfileUserClientActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Myviewhoder> {
    Context context;
    ArrayList<Comment> arrayList;

    public CommentAdapter(Context context,ArrayList<Comment> arrayList) {
        this.context = context;
        this.arrayList =arrayList;
    }

    public CommentAdapter() {
    }
    public void setList(ArrayList<Comment> arrayList){
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override

    public Myviewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return new Myviewhoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewhoder holder, int position) {
           final Comment comment = arrayList.get(position);
           holder.bind(comment);
           inforAuthorcomment(comment.getIduser(),holder.iv_avatar,holder.tv_author);
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(context, ProfileUserClientActivity.class);
                   intent.putExtra("iduser",comment.getIduser());
                   context.startActivity(intent);
               }
           });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Myviewhoder extends RecyclerView.ViewHolder{
        TextView tv_author,tv_content,tv_time;
        ImageView iv_avatar;
        public Myviewhoder(@NonNull View itemView) {
            super(itemView);
            tv_author = (TextView) itemView.findViewById(R.id.tv_nameAuthorComment);
            tv_content = (TextView) itemView.findViewById(R.id.tv_contentComment);
            tv_time = (TextView) itemView.findViewById(R.id.tv_timeComment);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatarComment);
        }
        private void bind(Comment comment){
            tv_content.setText(comment.getContent());
            tv_time.setText(comment.getTimecomment());
        }
    }
    public void inforAuthorcomment(String iduser, final ImageView ivavatar, final TextView tvNameAuthor){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(iduser);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("user",user.getUsername());
//                Picasso.get().load(user.getImageUrl()).fit().into(ivavatar);
                Glide
                        .with(context)
                        .load(user.getImageUrl())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(ivavatar);
                tvNameAuthor.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
