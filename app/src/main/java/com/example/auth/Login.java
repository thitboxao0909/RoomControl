package com.example.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {

    public static final int GOOGLE_SIGN_IN_CODE = 100;
    SignInButton signIn;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;
    TextView Text;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Text = findViewById(R.id.textView);
        signIn = findViewById(R.id.sign_in_button);
        firebaseAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("463148419152-jtri3odp06qii47tvhod7j9afnptnsaj.apps.googleusercontent.com")
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null || firebaseAuth.getCurrentUser() != null)
        {
            Toast.makeText(this, "User is already logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent sign = signInClient.getSignInIntent();
                startActivityForResult(sign, GOOGLE_SIGN_IN_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN_IN_CODE)
        {
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount signInAccount = signInTask.getResult(ApiException.class);

                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);

                firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getApplicationContext(),"Your Account is connected", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
             } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
}