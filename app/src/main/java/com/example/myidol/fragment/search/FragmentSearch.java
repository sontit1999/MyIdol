package com.example.myidol.fragment.search;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myidol.R;
import com.example.myidol.adapter.UsersAdapter;
import com.example.myidol.base.BaseFragment;
import com.example.myidol.callback.UserCallback;
import com.example.myidol.databinding.FragSearchBinding;
import com.example.myidol.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentSearch extends BaseFragment<FragSearchBinding,SearchViewmodel> {
    DatabaseReference mdatabase;
    UsersAdapter adapter;
    ArrayList<User> arrayList;
    int chooseUser = 0,choosePost = 0;
    @Override
    public Class<SearchViewmodel> getViewmodel() {
        return SearchViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_search;
    }

    @Override
    public void setBindingViewmodel() {
        mdatabase = FirebaseDatabase.getInstance().getReference();
        binding.setViewmodel(viewmodel);
        // set up recyclerview
        init();
        setuprecycleview();
        searchUser("");
        action();
    }

    private void init() {
        arrayList = new ArrayList<>();
    }

    private void action() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(choosePost==1){
                    choosePost = 0;
                }else {
                    choosePost = 1;
                }
                if(choosePost == 0){
                    binding.tvPost.setBackgroundResource(R.drawable.bg_follow);
                }else {
                    binding.tvPost.setBackgroundResource(R.drawable.bg_unfollow);
                }
                Log.d("choose","choose post : " + choosePost);
            }
        });
        binding.tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chooseUser==1){
                    chooseUser = 0;
                }else {
                    chooseUser = 1;
                }
                Log.d("choose","choose user : " + chooseUser);
                if(chooseUser == 0){
                    binding.tvUser.setBackgroundResource(R.drawable.bg_follow);
                }else {
                    binding.tvUser.setBackgroundResource(R.drawable.bg_unfollow);
                }
            }
        });
        binding.filter.setVisibility(View.GONE);
    }

    private void setuprecycleview() {
        adapter = new UsersAdapter(getContext(),arrayList);
        binding.rvUser.setHasFixedSize(true);
        binding.rvUser.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.rvUser.setAdapter(adapter);
    }

    @Override
    public void ViewCreated() {
         viewmodel.getArrUser().observe(this, new Observer<List<User>>() {
             @Override
             public void onChanged(List<User> users) {
                adapter.setList((ArrayList<User>) users);
             }
         });
    }
    private void searchUser(String s){
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").startAt(s).endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<User> temp = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    temp.add(user);
                }
                Log.d("hihi",temp.size() + "");
                viewmodel.arrUser.postValue(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
