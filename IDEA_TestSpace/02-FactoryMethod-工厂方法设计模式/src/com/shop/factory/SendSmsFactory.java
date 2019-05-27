package com.shop.factory;

import com.shop.method.SMSSender;
import com.shop.method.Sender;

public class SendSmsFactory implements Provider{
    @Override
    public Sender produce() {
        return new SMSSender();
    }
}
