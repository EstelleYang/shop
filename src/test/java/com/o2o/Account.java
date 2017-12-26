package com.o2o;

/**
 * @author: 496934  Date: 2017/12/26 Time: 10:28
 */
public class Account {
    private String holderName;
    private float amount;
    public Account(String holderName,float amount){
        this.holderName = holderName;
        this.amount = amount;
    }
    public  void deposite(float amt){
        amount += amt;
    }
    public  void withdraw(float amt){
        amount -= amt;
    }
    public float checkBalance(){
        return amount;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}