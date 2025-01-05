/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalculminating_joshuawu;

import java.text.DecimalFormat;

/**
 *
 * @author 335181541
 */
public class Customer {
    DecimalFormat df = new DecimalFormat("0000"); //format for customer IDs (ex. 0001, 0002)
    private static int idIndex = 1; //index for keeping track of IDs and the customerList

    private String firstName, lastName, address, postalCode, email, phone,
            orderDate, billDate, id, country; //customer variables

    public Customer(String firstName, String lastName, String address, String postalCode, String email, String phone, String orderDate, String billDate, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postalCode = postalCode;
        this.email = email;
        this.phone = phone;
        this.orderDate = orderDate;
        this.billDate = billDate;
        this.country = country;
        this.id = df.format(idIndex);
    } //contructor used when "Create Customer" is selected

    public Customer(String firstName, String lastName, String address, String postalCode, String email, String phone, String orderDate, String billDate, String id, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postalCode = postalCode;
        this.email = email;
        this.phone = phone;
        this.orderDate = orderDate;
        this.billDate = billDate;
        this.id = id;
        this.country = country;
    } //contructor used to read data from customer txt file

    @Override
    public String toString() { //toString for I/O
        return firstName + "," + lastName + "," + address + "," + postalCode + "," + email + "," + phone + "," + orderDate + "," + billDate + "," + id + "," + country;
    }
    
    public static void incrementIndex(){ //increments the index by 1
        idIndex++;
    }

    public static void setIdIndex(int idIndex) { //getters and setters
        Customer.idIndex = idIndex;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getFirstName() { //getters
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getBillDate() {
        return billDate;
    }

    public String getId() {
        return id;
    }

    public static int getIdIndex() {
        return idIndex;
    }
    
}
