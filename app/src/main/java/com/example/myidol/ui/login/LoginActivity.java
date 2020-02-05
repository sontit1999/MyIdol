package com.example.myidol.ui.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myidol.R;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityLoginBinding;
import com.example.myidol.ui.MainActivity;
import com.example.myidol.ui.postnew.PostNewActivity;
import com.example.myidol.ui.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends BaseActivity<ActivityLoginBinding,LoginViewModel> {
    FirebaseAuth mauth;
    ProgressDialog pd;
    @Override
    public Class<LoginViewModel> getViewmodel() {
        return LoginViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    public void setBindingViewmodel() {
        mauth = FirebaseAuth.getInstance();
        if(mauth.getCurrentUser() != null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        binding.setViewmodel(viewmodel);
        binding.tvNoacount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, PostNewActivity.class));
            }
        });
        binding.tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String email = binding.etEmail.getText().toString().trim();
                    String password = binding.etPassword.getText().toString().trim();
                    if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                        Toast.makeText(LoginActivity.this, "Must not empty", Toast.LENGTH_SHORT).show();
                    }else if(email.length()<11){
                        Toast.makeText(LoginActivity.this, "Email at least 11 character!", Toast.LENGTH_SHORT).show();
                    }else if(password.length() < 6){
                        Toast.makeText(LoginActivity.this, "Password at least 6 character!", Toast.LENGTH_SHORT).show();
                    }else {
                        pd = new ProgressDialog(LoginActivity.this);
                        pd.setCanceledOnTouchOutside(false);
                        pd.setMessage("Đang login ^^ ");
                        pd.show();
                        login(email,password);
                    }
            }
        });
    }
    public void login(String email,String password){
        mauth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            pd.dismiss();
                            Log.d("hihi", "createUserWithEmail:success");
                            FirebaseUser user = mauth.getCurrentUser();
                            Log.d("hihi", "id: " + user.getUid());
                            Intent intent =  new Intent(LoginActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            pd.dismiss();
                            Log.w("hihi", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

}
