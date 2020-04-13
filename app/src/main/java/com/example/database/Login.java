package com.example.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {


    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private TextView Register;
    private int counter = 5;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPassword;
    private CallbackManager mCallbackManager;
    private LoginButton loginButton;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private static final String TAG= "FacebookAuthentication";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Info = (TextView)findViewById(R.id.tvInfo);
        Login = (Button)findViewById(R.id.btnLogin);
        Register = (TextView)findViewById(R.id.tvRegister);
        forgotPassword = (TextView)findViewById(R.id.tvForgotPassword);

        Info.setText("Number of Attempts Remaining: 5");

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        final FirebaseUser user = firebaseAuth.getCurrentUser();


        loginButton=findViewById(R.id.login_button);
        loginButton.setReadPermissions("email","public_profile");
        mCallbackManager=CallbackManager.Factory.create();
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,"onSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
            }
            @Override
            public void onError(FacebookException error) {
                Log.d(TAG,"onError"+ error);
            }
        });


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    firebaseAuth.signOut();
                }
            }
        };

        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user != null){
                    finish();
                    startActivity(new Intent(Login.this, MainActivity.class));
                }

            }
        };

        if(user != null){
            finish();
            startActivity(new Intent(Login.this, MainActivity.class));
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ResetPassword.class));
            }
        });

    }

    private void validate(String userName, String userPassword){

        progressDialog.setMessage("Loading...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.profilefile), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String []quest=Name.getText().toString().split("@");
                   String f=quest[1].replace(".","");
                    String ans=quest[0]+f;
                    editor.putString(getString(R.string.profilename), ans);
                    editor.apply();

                    progressDialog.dismiss();
                    //Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    checkEmailVerification();
                }else{
                    Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    counter--;
                    Info.setText("Number of Attempts Remaining: " + counter);
                    progressDialog.dismiss();
                    if(counter == 0){
                        Login.setEnabled(false);
                        Toast.makeText(Login.this, "No More Login Attempts, Please Reset Your Password", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Login.this, ResetPassword.class));
                    }
                }
            }
        });

    }

private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

        if(emailflag){
            finish();
            startActivity(new Intent(Login.this, MainActivity.class));
        }else{
            Toast.makeText(this, "Verify your Email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
}

//    protected void onStart() {
//        super.onStart();
//
//        //opens the shared preference
//        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.profilefile), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(getString(R.string.profilename), Name.getText().toString());
//        editor.apply();
//
//
//
//    }


private void handleFacebookToken (AccessToken token){

    Log.d(TAG, "handleFacebookToken" + token);
    AuthCredential credential= FacebookAuthProvider.getCredential(token.getToken());
    firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                Log.d(TAG,"sign in with Credential: Successful");
                FirebaseUser user;
                user = firebaseAuth.getCurrentUser();
                updateUI(user);
            } else {
                Log.d(TAG, "Sign in Failed", task.getException());
                Toast.makeText(Login.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        }
    });
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI (FirebaseUser user){
        if(user !=null){
            startActivity(new Intent(Login.this, MainActivity.class));
        }
        else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
    @Override
    protected void onStop(){
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }


}
