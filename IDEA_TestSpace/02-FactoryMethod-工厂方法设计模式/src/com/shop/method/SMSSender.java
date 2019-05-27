package com.shop.method;

public class SMSSender implements Sender{
    @Override
    public void Send() {
        System.out.println("this is sms sender");
    }
}
