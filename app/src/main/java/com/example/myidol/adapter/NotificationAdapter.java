package com.example.myidol.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myidol.R;
import com.example.myidol.model.Notification;
import com.example.myidol.model.Post;
import com.example.myidol.model.User;
import com.example.myidol.ui.MainActivity;
import com.example.myidol.ui.detailchat.DetailChatActivity;
import com.example.myidol.ui.detailpost.DetailpostActivity;
import com.example.myidol.ui.profile.ProfileUserClientActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyHoder> {
    Context context;
    List<Notification> arraylist;

    public NotificationAdapter(Context context, List<Notification> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
    }
    public void setList(ArrayList<Notification> arraylist){
        this.arraylist = arraylist;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false);
        return new MyHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHoder holder, int position) {
        final Notification notification = arraylist.get(position);
        holder.bindata(notification);
        cloneUser(notification.getIduser(),holder.iv_user,holder.tv_nameuser);
        if(notification.getType().equals("post")){
            getImagePost(notification.getIdpost(),holder.iv_post);
        }else{
            holder.iv_post.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notification.getType().equals("follow")){
                    Intent intent = new Intent(context, ProfileUserClientActivity.class);
                    intent.putExtra("iduser",notification.getIduser());
                    context.startActivity(intent);
                }
                if(notification.getType().equals("post")){
                    Intent intent = new Intent(context, DetailpostActivity.class);
                    intent.putExtra("idpost",notification.getIdpost());
                    context.startActivity(intent);
                }
            }
        });
    }

    private void getImagePost(String idpost, final ImageView ivpost) {
        FirebaseDatabase.getInstance().getReference("post").child(idpost).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                Picasso.get().load(post.getLinkImage()).into(ivpost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    class MyHoder extends RecyclerView.ViewHolder{
        ImageView iv_user,iv_post;
        TextView tv_nameuser,tv_text,tv_time;

        public MyHoder(@NonNull View itemView) {
            super(itemView);
            iv_post = (ImageView) itemView.findViewById(R.id.iv_postNotification);
            iv_user = (ImageView) itemView.findViewById(R.id.iv_avatarNotification);
            tv_text = (TextView) itemView.findViewById(R.id.tv_textNotification);
            tv_nameuser = (TextView) itemView.findViewById(R.id.tv_usernameNotification);
            tv_time = (TextView) itemView.findViewById(R.id.tv_timeNotifi);
        }
        public void bindata(Notification notification){
            tv_text.setText(notification.getText());
            String timenotifi;
            long totaltime = (System.currentTimeMillis() - Long.parseLong(notification.getTime()))/60000;
            if(totaltime<60){
                timenotifi = totaltime + " mins";
            }else if(totaltime>=60 && totaltime < 1440){
                timenotifi = totaltime/60 + " hours";
            }else{
                timenotifi = totaltime/1440 + " days";
            }
            tv_time.setText(timenotifi);
        }
    }
    public void cloneUser(String iduser, final ImageView ivavatar, final TextView tvname){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(iduser);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("user",user.getUsername());
                Picasso.get().load(user.getImageUrl()).placeholder(R.drawable.ic_launcher_foreground).into(ivavatar);
                tvname.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
