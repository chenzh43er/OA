package com.shop.method;

public class SMSSender implements Sender{

    @Override
    public void send() {
        System.out.println("this is a sms Sender");
    }
}
