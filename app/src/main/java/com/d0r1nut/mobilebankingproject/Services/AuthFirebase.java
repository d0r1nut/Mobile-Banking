package com.d0r1nut.mobilebankingproject.Services;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthFirebase {
    private final FirebaseAuth mAuth;

    public AuthFirebase(){
        mAuth = FirebaseAuth.getInstance();
    }

    public void signUpUser(String email, String password, Activity activity){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                activity, result -> {
                    if(result.isSuccessful()){
                        Toast.makeText(activity, "Registration is Successful!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(activity, "Registration Failed! " + result.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public interface AuthResultCallback{
        void onComplete(boolean isSuccess);
    }

    public void signInUser(String email, String password, Activity activity, AuthResultCallback callback){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                activity, result -> {
                    if(result.isSuccessful()){
                        Toast.makeText(activity, "Login is Successful!", Toast.LENGTH_LONG).show();
                        callback.onComplete(true);
                    }
                    else{
                        Toast.makeText(activity, "Login Failed!" + result.getException().getMessage(), Toast.LENGTH_LONG).show();
                        callback.onComplete(false);
                    }
                }
        );
    }

    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }

    public void signInWithEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Log.d("signInWithEmailAndPassword", "signInWithEmail:success");
        } else {
            Log.w("signInWithEmailAndPassword", "signInWithEmail:failure");
        }
    }
    public void signOut() {
        mAuth.signOut();
    }
}
