package edu.neu.mad_sea.lishawang.groupexpensetracker.models;

import java.io.Serializable;
import java.util.HashMap;

public class Expense implements Serializable {
    String itemName; // Name of the item
    String owner; // The owner of the expense
    double price;  // Price of the item
    HashMap<String, Double> splitMap; // The amount of money each member owns for the item

    public Expense(String itemName, String owner, double price, HashMap<String, Double> splitMap) {
        this.itemName = itemName;
        this.owner = owner;
        this.price = price;
        this.splitMap = splitMap;
    }

    public String getItemName() {
        return itemName;
    }

    public String getOwner() {
        return owner;
    }

    public double getPrice() {
        return price;
    }

    public HashMap<String, Double> getSplitMap() {
        return splitMap;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSplitMap(HashMap<String, Double> splitMap) {
        this.splitMap = splitMap;
    }
}


