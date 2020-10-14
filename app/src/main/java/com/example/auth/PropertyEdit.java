package com.example.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertyEdit extends AppCompatActivity {

    Toolbar toolbar;

    private boolean _FURNISHED, furnishedEdit;
    private String _ADDRESS, _ID, _INVITECODE, _BILLDATE, _ROOMTYPE, _PRICE;
    private String roomTypeEdit;
    private String[] _OCCUPANT;
    private Button editBtn;
    private TextInputLayout address, price, billDate;
    private TextView inviteCode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_edit);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Property settings");

        //editTextLayout hook
        address = (TextInputLayout) findViewById(R.id.editAddress);
        price = (TextInputLayout) findViewById(R.id.editRevenue);
        billDate = (TextInputLayout) findViewById(R.id.editBillDate);
        inviteCode = (TextView) findViewById(R.id.editInviteCode);
        editBtn = (Button) findViewById(R.id.editConfirm);

        reference = FirebaseDatabase.getInstance().getReference("room");


        displayDetails();
        createRadioGroup();

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

        address.getEditText().setText(_ADDRESS);
        price.getEditText().setText(_PRICE);
        billDate.getEditText().setText(_BILLDATE);
        inviteCode.setText("Invite Code: " + _INVITECODE);

        getOccupant();
    }

    private void getOccupant() {
        LinearLayout listOccupantLayout = (LinearLayout) findViewById(R.id.editListOccupantLayout);

        listOccupantLayout.removeAllViews();

        //display
        LinearLayout.LayoutParams btnLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        btnLayoutParams.setMargins(6, 6, 6, 6);
        btnLayoutParams.gravity = Gravity.END;

        //getdata
        if (_OCCUPANT.length == 0) {
            TextView empty = new TextView(this);
            empty.setText("None");
            empty.setTextSize(24);
            listOccupantLayout.addView(empty);
        } else {
            for(int i = 0; i < _OCCUPANT.length; i++)
            {
                LinearLayout OccupantLayout = new LinearLayout(this);
                OccupantLayout.setOrientation(LinearLayout.HORIZONTAL);
                OccupantLayout.removeAllViews();

                TextView occupant = new TextView(this);
                final String email = _OCCUPANT[i];
                occupant.setText(email);
                occupant.setTextSize(18);
                OccupantLayout.addView(occupant);
                Button remove = new Button(this);
                remove.setText("Remove");
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeTenant(email, _OCCUPANT, _ID);
                    }
                });
                OccupantLayout.addView(remove,btnLayoutParams);
                listOccupantLayout.addView(OccupantLayout);
            }
        }
    }

    private void removeTenant(final String email, final String[] occupants, final String ID) {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("room");

        // pop up a message
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PropertyEdit.this);
        builder1.setMessage("Are you sure about removing this tenant?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        List<String> list = new ArrayList<String>(Arrays.asList(occupants));
                        list.removeAll(Arrays.asList(email));
                        _OCCUPANT = list.toArray(occupants);
                        reference.child(ID).child("occupant").setValue(list);
                        getOccupant();
                        dialog.cancel();
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

    private void createRadioGroup() {
        RadioGroup radioGroupEditRoomType = (RadioGroup) findViewById(R.id.radio_group_edit_type);
        RadioGroup radioGroupEditFurnished = (RadioGroup) findViewById(R.id.radio_group_edit_furnished);

        final String[] roomTypeArray = getResources().getStringArray(R.array.room_type);
        for(int i = 0; i < roomTypeArray.length; i++)
        {
            final String type = roomTypeArray[i];
            RadioButton button = new RadioButton(this);
            button.setText(getString(R.string.Type) + type);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(PropertyEdit.this, "Room type " + type + " chosen",Toast.LENGTH_SHORT)
                            .show();
                    roomTypeEdit = type;
                }
            });

            radioGroupEditRoomType.addView(button);
            if(type.equals(_ROOMTYPE))
            {
                button.setChecked(true);
            }
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
                    if (furnished.equals("True")) furnishedEdit = true;
                    else furnishedEdit = false;
                }
            });
            radioGroupEditFurnished.addView(button);
            if(furnished.equalsIgnoreCase(Boolean.toString(_FURNISHED)))
            {
                button.setChecked(true);
            }
        }
    }

    public void update(View view){
        if(isAddressChanged() || isPriceChanged() || isBillDateChanged() || isRoomTypeChanged() || isFurnishedChanged()){
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Data has not changed", Toast.LENGTH_SHORT).show();
    }

    private boolean isAddressChanged() {
        if(!_ADDRESS.equals(address.getEditText().getText().toString()))
        {
            reference.child(_ID).child("address").setValue(address.getEditText().getText().toString().trim());
            return true;
        }else return false;
    }

    private boolean isPriceChanged() {
        if(!_PRICE.equals(price.getEditText().getText().toString()) || validateInput())
        {
            if (price.getEditText().getText().toString().equals(""))
                reference.child(_ID).child("price").setValue(0);
            else reference.child(_ID).child("price").setValue(price.getEditText().getText().toString().trim());
            return true;
        }else return false;
    }

    private boolean isBillDateChanged() {
        if(!_BILLDATE.equals(billDate.getEditText().getText().toString()) || validateInput())
        {
            if (billDate.getEditText().getText().toString().equals(""))
                reference.child(_ID).child("billDate").setValue(1);
            else reference.child(_ID).child("billDate").setValue(billDate.getEditText().getText().toString().trim());
            return true;
        }else return false;
    }

    private boolean isRoomTypeChanged() {
        if(!_ROOMTYPE.equals(roomTypeEdit))
        {
            reference.child(_ID).child("roomType").setValue(roomTypeEdit);
            return true;
        }else return false;
    }

    private boolean isFurnishedChanged() {
        if(_FURNISHED != furnishedEdit)
        {
            reference.child(_ID).child("furnished").setValue(furnishedEdit);
            return true;
        }else return false;
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
}