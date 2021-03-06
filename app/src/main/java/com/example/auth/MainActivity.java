package com.example.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button getStartedBtn;
    private Button settingButton;
    private TextView Name;
    private TextView wc;

    Animation topAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        // first run
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            Toast.makeText(MainActivity.this, "First Run", Toast.LENGTH_LONG)
                    .show();
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).apply();


        wc = findViewById(R.id.welcomeText);
        wc.setAnimation(topAnim);

        // set up setting button
        settingButton = (Button) findViewById(R.id.SettingsBtn);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        // display user name
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Name = (TextView) findViewById(R.id.nameText);
        Name.setAnimation(topAnim);
        Name.setText(firebaseAuth.getCurrentUser().getDisplayName());

        // get data
        String fireBaseUID = firebaseAuth.getCurrentUser().getUid();
        String userName = firebaseAuth.getCurrentUser().getDisplayName().toString();
        String userEmail = firebaseAuth.getCurrentUser().getEmail().toString();
        addUserToDatabase(fireBaseUID, userName, userEmail);

        // get started button
        getStartedBtn = (Button) findViewById(R.id.getStartedBtn);
        getStartedBtn.setAnimation(bottomAnim);
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

    private void addUserToDatabase(final String fireBaseUID, final String name, final String email) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean found = false;
                for(DataSnapshot dSnapShot:snapshot.getChildren())
                {
                    User user = dSnapShot.getValue(User.class);
                    if (user.getFireBaseUID().equals(fireBaseUID))
                    {
                        found = true;
                        break;
                    }
                }
                if(!found)
                {
                    DatabaseReference userRef = reference.push();
                    User user = new User(name, email, fireBaseUID);
                    userRef.setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}