package com.example.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {


    Toolbar toolbar;
    TextView logInText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("App Settings");
        logInText = (TextView) findViewById(R.id.logInText);
        //Firebase auth
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Log.d("tag","on create " + firebaseAuth.getCurrentUser().getEmail());
        logInText.setText("Log in as: " + firebaseAuth.getCurrentUser().getEmail());
        //Client Type radio buttons
        createRadioButton();
        //TODO: fix
        String type = getClientType(this);
        applySettingButton(type);
        Toast.makeText(this, type, Toast.LENGTH_SHORT)
                .show();
    }
    //TODO: fix apply setting
    private void applySettingButton(final String type)
    {
        Button button = (Button) findViewById(R.id.applyClient);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals("Landlord"))
                {
                    startActivity(new Intent(SettingsActivity.this, LandlordMain.class));
                }else if(type.equals("Tenant"))
                {
                    startActivity((new Intent(SettingsActivity.this, TenantMain.class)));
                }else Toast.makeText(SettingsActivity.this, "Client type setting not saved", Toast.LENGTH_SHORT);
            }
        });
    }

    private void createRadioButton()
    {
        RadioGroup group = (RadioGroup) findViewById(R.id.radio_group_CType);
        final String[] clientType = getResources().getStringArray(R.array.client_type);
        for(int i = 0; i < clientType.length; i++)
        {
            final String type = clientType[i];
            RadioButton button = new RadioButton(this);
            button.setText(getString(R.string.Type) + type);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(SettingsActivity.this, "Client type: " + type + " chosen",Toast.LENGTH_SHORT)
                            .show();

                }
            });
            //add to radio group
            group.addView(button);
            //select default button
            if(type.equals(getClientType(this)))
            {
                button.setChecked(true);
            }
        }
    }

    private void saveClientType(String type)
    {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Client Type", type);
        editor.apply();
    }

    static public String getClientType(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        //TODO: fix get type
        return prefs.getString("Client Type", "DEFAULT");
    }

    public void logout(final View view)
    {
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                .signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(view.getContext(), Login.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SettingsActivity.this, "Sign out failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}