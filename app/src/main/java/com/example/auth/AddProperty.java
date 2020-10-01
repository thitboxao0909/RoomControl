package com.example.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddProperty extends AppCompatActivity {

    private Toolbar toolbar;
    private String roomType;
    private boolean Furnished;
    private TextInputLayout address, price, billDate;
    private Button addProperty;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String FirebaseUID = firebaseAuth.getCurrentUser().getUid();
    String UserID = FirebaseUID.substring(0, 4);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        address = (TextInputLayout) findViewById(R.id.inputAddress);
        price = (TextInputLayout) findViewById(R.id.inputPrice);
        billDate = (TextInputLayout) findViewById(R.id.inputBillDate);

        toolbar = (Toolbar) findViewById(R.id.toolbarAddProperty);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Property");
        createRadioGroup();
        addProperty = (Button) findViewById(R.id.addPropertyBtn);
        addProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProperty();
            }
        });
    }

    private void createRadioGroup() {
        RadioGroup radioGroupRoomType = (RadioGroup) findViewById(R.id.radio_group_room_type);
        RadioGroup radioGroupFurnished = (RadioGroup) findViewById(R.id.radio_group_furnished);

        final String[] roomTypeArray = getResources().getStringArray(R.array.room_type);
        for(int i = 0; i < roomTypeArray.length; i++)
        {
            final String type = roomTypeArray[i];
            RadioButton button = new RadioButton(this);
            button.setText(getString(R.string.Type) + type);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(AddProperty.this, "Room type " + type + " chosen",Toast.LENGTH_SHORT)
                            .show();
                    roomType = type;
                }
            });

            radioGroupRoomType.addView(button);

        }
        final String[] furnishedArray = getResources().getStringArray(R.array.furnished);
        for(int i = 0; i < furnishedArray.length; i++)
        {
            final String furnished = furnishedArray[i];
            RadioButton button = new RadioButton(this);
            button.setText(furnished);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (furnished.equals("True")) Furnished = true;
                    else Furnished = false;
                }
            });

            radioGroupFurnished.addView(button);
        }
    }

    private boolean validateInput() {
        String addressInput = address.getEditText().getText().toString();
        String priceInput = price.getEditText().getText().toString();
        String billDateInput = billDate.getEditText().getText().toString();

        if(addressInput.isEmpty())
        {
            address.setError("Empty");
            return false;
        } else if(priceInput.isEmpty())
        {
            price.setError("Empty");
            return false;
        } else if(billDateInput.isEmpty())
        {
            billDate.setError("Empty");
            return false;
        } else
        {
            address.setError(null);
            price.setError(null);
            billDate.setError(null);
            address.setErrorEnabled(false);
            price.setErrorEnabled(false);
            billDate.setErrorEnabled(false);
            return true;
        }
    }

    private void addProperty() {

        if(!validateInput()) return;
        else
        {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("room");

            String Address = address.getEditText().getText().toString();
            int Price = Integer.parseInt(price.getEditText().getText().toString());
            String BillDate = billDate.getEditText().getText().toString();
            String Type = roomType;
            Boolean fur = Furnished;

            Room room = new Room(Address, BillDate, Type, Price, fur);
            String dbRoomID = UserID + Integer.toString(room.id);
            room.setInviteCode(dbRoomID);
            room.setOwner(FirebaseUID);
            reference.child(dbRoomID).setValue(room);

        }
    }
}