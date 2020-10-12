package com.example.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TenantMain extends AppCompatActivity {

    private String _EMAIL;
    private Button settingBtn, addRentBtn, refreshDataBtn;
    private TextView Name, currentRent;
    private LinearLayout rentListLayout;
    private int totalRent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tennant_main);

        settingBtn = (Button) findViewById(R.id.tenantSettingsBtn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TenantMain.this, SettingsActivity.class));
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Name = (TextView) findViewById(R.id.tenantName);
        Name.setText(firebaseAuth.getCurrentUser().getDisplayName());

        _EMAIL = firebaseAuth.getCurrentUser().getEmail();

        currentRent = (TextView) findViewById(R.id.tenantCurrentRentText);

        rentListLayout = (LinearLayout) findViewById(R.id.tenantRentListLayout);
        rentListLayout.removeAllViewsInLayout();

        addRentBtn = (Button) findViewById(R.id.tenantAddRentBtn);
        addRentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TenantMain.this, AddRent.class));
            }
        });

        refreshDataBtn = (Button) findViewById(R.id.tenantRefreshBtn);
        refreshDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rentListLayout.removeAllViews();
                totalRent = 0;
                refreshFromDataBase();
            }
        });

    }

    private void refreshFromDataBase() {
        rentListLayout.removeAllViews();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("room");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot room_snapshot:snapshot.getChildren()) {
                    Room room = room_snapshot.getValue(Room.class);
                    ArrayList<String> currentOccupant = (ArrayList<String>) room.getOccupant();

                    // getdata of tenant
                    for(String tenant : currentOccupant)
                    {
                        if(tenant.equals(_EMAIL))
                        {
                            final String id = room_snapshot.getKey();
                            final String inviteCode = room.getInviteCode();
                            final String address = room.getAddress();
                            final String billDate = room.getBillDate();
                            final String roomType = room.getRoomType();
                            final Boolean fur = room.getFurnished();
                            final String Price = Integer.toString(room.getPrice());
                            List<String> Occupant = room.getOccupant();
                            final String[] occupant = Occupant.toArray(new String[Occupant.size()]);

                            TextView textView = new TextView(TenantMain.this);
                            String description = "Room: " + "\n Address:" + room.getAddress()
                                    + "\n Rent: " + room.getPrice()
                                    + "\n Billing Date: " + room.getBillDate();
                            textView.setText(description);
                            textView.setTextSize(18);
                            textView.setClickable(true);
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(TenantMain.this, RentView.class);
                                    intent.putExtra("ID", id);
                                    intent.putExtra("Invite code", inviteCode);
                                    intent.putExtra("Address", address);
                                    intent.putExtra("Price",Price);
                                    intent.putExtra("Bill Date", billDate);
                                    intent.putExtra("Furnished", fur);
                                    intent.putExtra("Room type", roomType);
                                    intent.putExtra("Occupant list", occupant);
                                    startActivity(intent);
                                }
                            });
                            rentListLayout.addView(textView);
                            totalRent += room.getPrice();
                            currentRent.setText("Current Rent per month: $" + totalRent);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    protected void onResume() {
        super.onResume();
        rentListLayout.removeAllViews();
        totalRent = 0;
        refreshFromDataBase();
    }
}