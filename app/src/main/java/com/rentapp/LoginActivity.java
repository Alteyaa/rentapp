package com.rentapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

      CallbackManager callbackManager;
      private LoginButton facebookButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



       facebookButton = findViewById(R.id.login_button);
        facebookButton.setPermissions("email");
        callbackManager = CallbackManager.Factory.create();
        facebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("TAG","onSuccess"+loginResult.toString());
                Log.e("TAG","onSuccess"+loginResult.getAccessToken());
                singIn(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.e("TAG","onCancel");
            }

            @Override
            public void onError(FacebookException error) {
              Log.e("TAG","onError");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
    private void singIn(AccessToken accessToken){
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    close(task.getResult().getUser().getPhotoUrl().toString(),
                            task.getResult().getUser().getDisplayName(),
                            task.getResult().getUser().getEmail());

                    Log.e("TAG","singInWithCredential:success");
                }else{
                    Log.e("TAG","signInwithCredential:failure",task.getException());
                }
            }
        });

    }

    private void close(String url,String name,String mail){
        Intent intent = new Intent();
        intent.putExtra("photo",url);
        intent.putExtra("name",name);
        intent.putExtra("mail",mail);

        setResult(RESULT_OK,intent);
        finish();
    }

    }

