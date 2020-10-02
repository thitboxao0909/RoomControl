package com.example.auth;

import java.util.ArrayList;
import java.util.List;

public class Room {
    String address, billDate, roomType, inviteCode, owner;
    int price;
    Boolean furnished,occupied;
    List<String> Occupant = new ArrayList<String>();

    public Room(){}

    public Room(String address, String billDate, String roomType, int price, Boolean furnished)
    {
        this.address = address;
        this.billDate = billDate;
        this.roomType = roomType;
        this.price = price;
        this.furnished = furnished;
        this.occupied = false;
        this.inviteCode = null;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Boolean getFurnished() {
        return furnished;
    }

    public void setFurnished(Boolean furnished) {
        this.furnished = furnished;
    }

    public Boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getOccupant() {
        return Occupant;
    }

    public void setOccupant(List<String> occupant) {
        Occupant = occupant;
    }
}
