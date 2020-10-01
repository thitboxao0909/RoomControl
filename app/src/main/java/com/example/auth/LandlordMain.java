package com.example.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class LandlordMain extends AppCompatActivity {

    private Button settingBtn, addProperty, refresh;
    private TextView Name, revenueSum;
    private FirebaseDatabase rootNode;
    private LinearLayout propertyList;
    private DatabaseReference reference;
    private int revenue = 0;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String FirebaseUID = firebaseAuth.getCurrentUser().getUid();
    String UserID = FirebaseUID.substring(0, 4);


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

        revenueSum = (TextView) findViewById(R.id.landLordTotalRevenue);


        rootNode = FirebaseDatabase.getInstance();
        rootNode.setPersistenceEnabled(true);

        addProperty = (Button) findViewById(R.id.landLordAddPropertyBtn);
        addProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LandlordMain.this, AddProperty.class));
            }
        });

        propertyList = (LinearLayout) findViewById(R.id.landlordScroll);

        refresh = (Button) findViewById(R.id.landlordRefresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                propertyList.removeAllViewsInLayout();
                refreshFromDataBase();
            }
        });
    }

    private void refreshFromDataBase() {
        reference = rootNode.getReference("room");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot room_snapshot:snapshot.getChildren()) {
                    Room room = room_snapshot.getValue(Room.class);

                    if (room.getOwner().equals(FirebaseUID))
                    {
                        final String ID = room.getInviteCode();
                        TextView textView = new TextView(LandlordMain.this);
                        textView.setText("Room: " + "\n Address:" + room.getAddress()
                                + "\n Revenue: " + room.getPrice()
                                + "\n Billing Date: " + room.getBillDate());
                        textView.setTextSize(18);
                        textView.setClickable(true);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(LandlordMain.this, PropertyEdit.class);
                                intent.putExtra("Room id", ID);
                                startActivity(intent);
                            }
                        });
                        propertyList.addView(textView);
                        revenue += room.getPrice();
                        revenueSum.setText("Total Revenue: $" + revenue);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
