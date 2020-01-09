package com.example.myidol.fragment.chat;

import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myidol.R;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.callback.UserCallback;
import com.example.myidol.databinding.FragChatbasicBinding;
import com.example.myidol.model.User;
import com.example.myidol.ui.detailchat.DetailChatActivity;
import com.example.myidol.ui.profile.ProfileUserClientActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FragmentChatBasic extends BaseFragment<FragChatbasicBinding,ChatBasicViewmodel> {
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public Class<ChatBasicViewmodel> getViewmodel() {
        return ChatBasicViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return  R.layout.frag_chatbasic;
    }

    @Override
    public void setBindingViewmodel() {
         binding.setViewmodel(viewmodel);
         setupRecyclervew();
    }

    private void setupRecyclervew() {
        binding.rvRecentChat.setHasFixedSize(true);
        binding.rvRecentChat.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.rvRecentChat.setAdapter(viewmodel.adapter);
    }

    @Override
    public void ViewCreated() {
          viewmodel.getArrUser().observe(this, new Observer<ArrayList<User>>() {
              @Override
              public void onChanged(final ArrayList<User> users) {
                  viewmodel.adapter.setList(users);
                  viewmodel.adapter.setCallback(new UserCallback() {
                      @Override
                      public void onAvatarClick(User user) {
                          Intent intent = new Intent(getContext(), DetailChatActivity.class);
                          intent.putExtra("iduser",user.getId());
                          startActivity(intent);
                      }

                      @Override
                      public void onFollowClick(User user) {

                      }
                  });
              }
          });
          getUserrecent();
    }

    private void getUserrecent() {
        FirebaseDatabase.getInstance().getReference("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> temp = new ArrayList<>();
                for(DataSnapshot i : dataSnapshot.getChildren()){
                    if(i.getKey().contains(currentUser.getUid())){
                        temp.add(i.getKey().replace(currentUser.getUid(),"").replace("-",""));
                        Log.d("count",i.getKey().replace(currentUser.getUid(),"").replace("-",""));
                    }
                    final ArrayList<User> arrUser = new ArrayList<>();
                    FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot i : dataSnapshot.getChildren()){
                                User user = i.getValue(User.class);
                                if(temp.contains(user.getId())){
                                    arrUser.add(user);
                                }
                            }
                            Log.d("numberRecent",arrUser.size()+"");
                            viewmodel.setListUser(arrUser);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
