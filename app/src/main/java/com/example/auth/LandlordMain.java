package com.example.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_landlord_main);

        // nut setting --> chuyen huong qua class setting
        settingBtn = (Button) findViewById(R.id.landlordSettingsBtn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LandlordMain.this, SettingsActivity.class));
            }
        });


        // hien thi ten cua khach hang
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Name = (TextView) findViewById(R.id.landLordName);
        Name.setText(firebaseAuth.getCurrentUser().getDisplayName());

        // hien thi revenue
        revenueSum = (TextView) findViewById(R.id.landLordTotalRevenue);


        rootNode = FirebaseDatabase.getInstance();
        //rootNode.setPersistenceEnabled(true);


        // add property --> chuyen huong qua class addproperty
        addProperty = (Button) findViewById(R.id.landLordAddPropertyBtn);
        addProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LandlordMain.this, AddProperty.class));
            }
        });

        // display properties
        propertyList = (LinearLayout) findViewById(R.id.landlordScroll);


        // remove all properties
        refresh = (Button) findViewById(R.id.landlordRefresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                propertyList.removeAllViews();
                revenue = 0;
                refreshFromDataBase();
            }
        });
    }

    private void refreshFromDataBase() {
        propertyList.removeAllViews();
        reference = rootNode.getReference("room"); // accent to node room in the database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot room_snapshot:snapshot.getChildren()) {
                     Room room = room_snapshot.getValue(Room.class);

                    if (room.getOwner().equals(FirebaseUID))
                    {
                        // get data about room
                        final String id = room_snapshot.getKey();
                        final String inviteCode = room.getInviteCode();
                        final String address = room.getAddress();
                        final String billDate = room.getBillDate();
                        final String roomType = room.getRoomType();
                        final Boolean fur = room.getFurnished();
                        final String Price = Integer.toString(room.getPrice());
                        List<String> Occupant = room.getOccupant();
                        final String[] occupant = Occupant.toArray(new String[Occupant.size()]); // list of rentals

                        // display data
                        TextView textView = new TextView(LandlordMain.this);
                        textView.setText("Room: " + "\n Address:" + room.getAddress()
                                + "\n Revenue: " + room.getPrice()
                                + "\n Billing Date: " + room.getBillDate()
                                + "\n Invite Code: " +room.getInviteCode());

                        // view details
                        textView.setTextSize(18);
                        textView.setClickable(true);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(LandlordMain.this, PropertyEdit.class);
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

    protected void onResume() {
        super.onResume();
        propertyList.removeAllViews();
        revenue = 0;
        refreshFromDataBase();
    }


}
