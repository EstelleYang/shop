package com.o2o;

import java.util.Date;

/**
 * @author: 496934  Date: 2017/12/26 Time: 10:03
 */
public class ThreadTest implements Runnable{
    //public class ThreadTest extends Thread{
    int pauseTime;
    String name;
    public ThreadTest(String n){

        name = n;
    }
    Account account1 = new Account("yyf",0);
    public void run(){
        try {
            account1.deposite(2000);
            System.out.println(account1.checkBalance()+account1.getHolderName());
            account1.withdraw(1000);
            System.out.println(account1.checkBalance()+account1.getHolderName());
            account1.withdraw(3);
            System.out.println(account1.checkBalance()+account1.getHolderName());
            account1.withdraw(322);
            System.out.println(account1.checkBalance()+account1.getHolderName());
            account1.withdraw(233);
            System.out.println(account1.checkBalance()+account1.getHolderName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[]args){
        //ThreadTest tt1 = new ThreadTest(1000,"FastGuy");
        Thread tt1 = new Thread(new ThreadTest("FastGuy"));
        tt1.start();
        //ThreadTest tt2 = new ThreadTest(3000,"SlowGuy");
        Thread tt2 = new Thread(new ThreadTest("slowGuy"));
        tt2.start();
    }
}