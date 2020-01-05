package com.example.myidol.ui.register;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myidol.R;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding,RegisterViewModel> {
    FirebaseAuth mauth;
    DatabaseReference mdatabase;
    ProgressDialog pd;
    @Override
    public Class<RegisterViewModel> getViewmodel() {
        return RegisterViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_register;
    }

    @Override
    public void setBindingViewmodel() {
        // get instance
        mauth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference();
        binding.setViewmodel(viewmodel);
        action();
    }

    private void action() {
        binding.tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setCanceledOnTouchOutside(false);
                pd.setMessage("Please wait ...");
                String username = binding.etUsername.getText().toString().trim();
                String email = binding.etEmail.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();
                String address = binding.etAddress.getText().toString().trim();
                String sentenceslike = binding.etSentencelike.getText().toString().trim();
                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(username) || TextUtils.isEmpty(username) || TextUtils.isEmpty(address) || TextUtils.isEmpty(sentenceslike) ){
                    Toast.makeText(RegisterActivity.this, "Must not empty!!!", Toast.LENGTH_SHORT).show();
                }else if(username.length()<6 || password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Username and Password at least 6 character!", Toast.LENGTH_SHORT).show();
                }else if(email.length()<11){
                    Toast.makeText(RegisterActivity.this, "Email at least 11 character!", Toast.LENGTH_SHORT).show();
                }else{
                    pd.show();
                    register(username,email,password,address,sentenceslike);
                }

            }
        });
    }

    public void register(final String username, String email, String password, final String address, final String sentencelike){
        mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("hihi", "createUserWithEmail:success");
                    FirebaseUser user = mauth.getCurrentUser();
                    Log.d("hihi", "id: " + user.getUid());
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("id",user.getUid());
                    hashMap.put("username",username);
                    hashMap.put("sentenceslike",sentencelike);
                    hashMap.put("address",address);
                    hashMap.put("imageUrl","http://icons.iconarchive.com/icons/papirus-team/papirus-status/512/avatar-default-icon.png");
                    mdatabase.child("Users").child(user.getUid()).setValue(hashMap);
                    finish();
                } else {
                    // If sign in fails, display a message to the user.
                    pd.dismiss();
                    Log.w("hihi", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
