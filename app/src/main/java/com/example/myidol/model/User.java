package com.example.myidol.model;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User {
    String id;
    String imageUrl;
    String username;
    String sentenceslike;
    String address;


    public User(String id, String imageUrl, String username,String sentenceslike,String address) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.username = username;
        this.sentenceslike = sentenceslike;
        this.address = address;
    }

    public User() {
    }

    public String getSentenceslike() {
        return sentenceslike;
    }

    public void setSentenceslike(String sentenceslike) {
        this.sentenceslike = sentenceslike;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void checkFollow(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("follows").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(id).exists()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
