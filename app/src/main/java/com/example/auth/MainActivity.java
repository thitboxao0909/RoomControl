package com.example.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button getStartedBtn;
    private Button settingButton;
    private TextView Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            Toast.makeText(MainActivity.this, "First Run", Toast.LENGTH_LONG)
                    .show();
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).apply();

        settingButton = (Button) findViewById(R.id.SettingsBtn);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Name = (TextView) findViewById(R.id.nameText);
        Name.setText(firebaseAuth.getCurrentUser().getDisplayName());

        getStartedBtn = (Button) findViewById(R.id.getStartedBtn);
        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = SettingsActivity.getClientType(MainActivity.this);
                if(type.equals("Landlord"))
                {
                    startActivity(new Intent(MainActivity.this, LandlordMain.class));
                }else if(type.equals("Tenant"))
                {
                    startActivity((new Intent(MainActivity.this, TenantMain.class)));
                }else Toast.makeText(MainActivity.this, "Client type setting not saved, Open settings to reconfigure", Toast.LENGTH_LONG)
                            .show();
            }
        });


    }
}