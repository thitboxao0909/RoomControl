package com.example.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class LandlordMain extends AppCompatActivity {

    private Button settingBtn;
    private TextView Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landlord_main);

        settingBtn = (Button) findViewById(R.id.landlordSettingsBtn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LandlordMain.this, SettingsActivity.class));
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Name = (TextView) findViewById(R.id.landLordName);
        Name.setText(firebaseAuth.getCurrentUser().getDisplayName());


    }
}