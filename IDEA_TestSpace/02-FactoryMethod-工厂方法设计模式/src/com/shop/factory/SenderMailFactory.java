package com.shop.factory;

import com.shop.method.MailSender;
import com.shop.method.Sender;

public class SenderMailFactory implements Provider{
    @Override
    public Sender produce() {
        return new MailSender();
    }
}