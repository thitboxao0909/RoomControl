package com.example.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddRent extends AppCompatActivity {

    Toolbar toolbar;
    private TextInputLayout inputInviteCode;
    private Button findInviteCode;
    private LinearLayout previewLayout;
    private String key, _EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rent);

        previewLayout = (LinearLayout) findViewById(R.id.addRentPreviewLayout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Rent");

        _EMAIL = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

        inputInviteCode = (TextInputLayout) findViewById(R.id.addRentInviteCode);
        findInviteCode = (Button) findViewById(R.id.addRentOkBtn);
        findInviteCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewLayout.removeAllViews();
                findInviteCode();
            }
        });

    }

    private boolean validateInput() {
        String inviteCode = inputInviteCode.getEditText().getText().toString();
        if(inviteCode.isEmpty())
        {
            inputInviteCode.setError("Empty");
            return false;
        } else {
            inputInviteCode.setError(null);
            inputInviteCode.setErrorEnabled(false);
            return true;
        }
    }

    private void findInviteCode() {
        if(!validateInput()) return;
        else {
            final String inviteCode = inputInviteCode.getEditText().getText().toString();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("room");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean found = false;
                    Room room = new Room();
                    for(DataSnapshot room_snapshot:snapshot.getChildren()) {
                        room = room_snapshot.getValue(Room.class);
                        key = room_snapshot.getKey();
                        if(room != null && room.getInviteCode().equals(inviteCode))
                        {
                            found = true;
                            break;
                        }
                    }
                    if(found)
                    {
                        TextView preview = new TextView(AddRent.this);
                        final ArrayList<String> currentOccupant = (ArrayList<String>) room.getOccupant();
                        String description = "Room: " + "\n Address:" + room.getAddress()
                                + "\n Rent per month: $" + room.getPrice()
                                + "\n Room Type: " + room.getRoomType()
                                + "\n Furnished: " + room.getFurnished().toString()
                                + "\n Billing Date: " + room.getBillDate();
                        preview.setText(description);
                        previewLayout.addView(preview);
                        Button acceptRent = new Button(AddRent.this);
                        acceptRent.setText("Accept");
                        acceptRent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                updateOccupant(key, _EMAIL, currentOccupant);
                            }
                        });
                        previewLayout.addView(acceptRent);
                    } else {
                        Toast.makeText(AddRent.this, "Invite Code not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    private void updateOccupant(final String key, String email,final ArrayList<String> currentOccupant) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("room");
        currentOccupant.add(email);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddRent.this);
        builder1.setMessage("Are you sure about adding this rent?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        reference.child(key).child("occupant").setValue(currentOccupant);
                        reference.child(key).child("occupied").setValue(true);
                        Toast.makeText(AddRent.this, "Rent added. Database update may take a moment", Toast.LENGTH_SHORT)
                                .show();
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