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
public class Order {
    DecimalFormat df = new DecimalFormat("0000"); //same ID pattern as for customers
    private static double pizzaCost = 3.99, burgerCost = 5.99, friesCost = 2.99, sodaCost = 1.99, hotdogCost = 3.99; //arbrtrarily declared food costs. can be changed w/ some slight modifications to program
    private int pizzaAmt, burgerAmt, friesAmt, sodaAmt, hotdogAmt;
    private String orderCustomerIndex, orderIndex; //orderCustomerIndex refers to the customer's ID, orderIndex is the actual order ID
    private static int index = 1; //works similar to the Customer class to track IDs and the orderList
    public Order(int pizzaAmt, int burgerAmt, int friesAmt, int sodaAmt, int hotdogAmt, String id,String orderCustomerIndex) {
        this.pizzaAmt = pizzaAmt;
        this.burgerAmt = burgerAmt;
        this.friesAmt = friesAmt;
        this.sodaAmt = sodaAmt;
        this.hotdogAmt = hotdogAmt;
        this.orderCustomerIndex = orderCustomerIndex;
        this.orderIndex = id; //constructor to create order
    }
    public static void incrementIndex(){
        index++;
    }

    public static void setIndex(int index) {
        Order.index = index;
    }


    @Override
    public String toString() { //I/O
        return pizzaAmt + "," + burgerAmt + "," + friesAmt + "," + sodaAmt + "," + hotdogAmt + "," + orderCustomerIndex + "," + orderIndex;
    }

    public static int getIndex() { //getters and setters
        return index;
    }

    public String getOrderIndex() {
        return orderIndex;
    }

    public String getOrderCustomerIndex() {
        return orderCustomerIndex;
    }


    public int getPizzaAmt() {
        return pizzaAmt;
    }

    public int getFriesAmt() {
        return friesAmt;
    }

    public int getSodaAmt() {
        return sodaAmt;
    }

    public int getHotdogAmt() {
        return hotdogAmt;
    }

    public int getBurgerAmt() {
        return burgerAmt;
    }

    public static double getPizzaCost() {
        return pizzaCost;
    }

    public static double getBurgerCost() {
        return burgerCost;
    }

    public static double getFriesCost() {
        return friesCost;
    }

    public static double getSodaCost() {
        return sodaCost;
    }

    public static double getHotdogCost() {
        return hotdogCost;
    }

}
