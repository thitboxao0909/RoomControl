package com.example.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RentView extends AppCompatActivity {

    private TextView address, inviteCode, rent, billDate, roomType, furnished;
    private LinearLayout occupantListLayout;
    private String _ADDRESS, _ID, _INVITECODE, _BILLDATE, _ROOMTYPE, _PRICE;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_view);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rent Details");

    }
}