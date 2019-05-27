package com.shop.method;

public class MailSernder implements Sender{

    @Override
    public void send() {
        System.out.println("this is a mailSender");
    }
}
