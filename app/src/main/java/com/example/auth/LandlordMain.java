package com.example.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LandlordMain extends AppCompatActivity {

    private Button settingBtn, addProperty;
    private TextView Name;
    private FirebaseDatabase rootNode;
    private LinearLayout propertyList;

    DatabaseReference reference;
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

        rootNode = FirebaseDatabase.getInstance();
        addProperty = (Button) findViewById(R.id.landLordAddPropertyBtn);
        addProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LandlordMain.this, AddProperty.class));
            }
        });

        propertyList = (LinearLayout) findViewById(R.id.landlordScroll);

    }
}