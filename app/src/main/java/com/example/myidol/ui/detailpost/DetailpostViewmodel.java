package com.example.myidol.ui.detailpost;

import android.app.Application;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.myidol.R;
import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.databinding.ActivityDetailpostBinding;
import com.example.myidol.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailpostViewmodel  extends BaseViewmodel {
    public DetailpostViewmodel(@NonNull Application application) {
        super(application);
    }

}
