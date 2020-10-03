package com.example.auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RentView extends AppCompatActivity {

    private TextView address, inviteCode, rent, billDate, roomType, furnished;
    private LinearLayout occupantListLayout;
    private String _ADDRESS, _ID, _INVITECODE, _BILLDATE, _ROOMTYPE, _PRICE;
    private Boolean _FURNISHED;
    private String[] _OCCUPANT;
    private Button removeRentBtn;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_view);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rent Details");

        address = (TextView) findViewById(R.id.rentViewAddress);
        inviteCode = (TextView) findViewById(R.id.rentViewInviteCode);
        rent = (TextView) findViewById(R.id.rentViewPrice);
        billDate = (TextView) findViewById(R.id.rentViewBillDate);
        roomType = (TextView) findViewById(R.id.rentViewRoomType);
        furnished = (TextView) findViewById(R.id.rentViewFurnished);
        occupantListLayout = (LinearLayout) findViewById(R.id.rentViewOccupantLayout);
        removeRentBtn = (Button) findViewById(R.id.rentViewRemoveRentBtn);

        displayDetails();

        final String _EMAIL = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        removeRentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRent(_EMAIL, _OCCUPANT, _ID);
            }
        });
    }

    private void displayDetails() {
        Intent intent = getIntent();
        _ID = intent.getStringExtra("ID");
        _ADDRESS = intent.getStringExtra("Address");
        _PRICE = intent.getStringExtra("Price");
        _BILLDATE = intent.getStringExtra("Bill Date");
        _INVITECODE = intent.getStringExtra("Invite code");
        _ROOMTYPE = intent.getStringExtra("Room type");
        _FURNISHED = intent.getBooleanExtra("Furnished", false);
        _OCCUPANT = intent.getStringArrayExtra("Occupant list");

        address.setText("Address: " + _ADDRESS);
        inviteCode.setText("Invite Code: " + _INVITECODE);
        rent.setText("Rent per month: $" + _PRICE);
        billDate.setText("Bill Date: " + _BILLDATE);
        roomType.setText("Room Type: " + _ROOMTYPE);
        furnished.setText("Furnished: " + _FURNISHED.toString());
        for(String occupant : _OCCUPANT)
        {
            TextView Occupant = new TextView(RentView.this);
            Occupant.setText(occupant);
            Occupant.setTextSize(18);
            occupantListLayout.addView(Occupant);
        }
    }

    private void removeRent(final String email, final String[] occupants, final String ID) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("room");
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RentView.this);
        builder1.setMessage("Are you sure about removing this rent?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        List<String> list = new ArrayList<String>(Arrays.asList(occupants));
                        list.removeAll(Arrays.asList(email));
                        _OCCUPANT = list.toArray(occupants);
                        reference.child(ID).child("occupant").setValue(list);
                        dialog.cancel();
                        Toast.makeText(RentView.this, "Rent removed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog removeAlert = builder1.create();
        removeAlert.show();
    }
}