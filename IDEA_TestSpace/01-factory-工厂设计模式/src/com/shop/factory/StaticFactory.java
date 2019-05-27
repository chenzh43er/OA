package com.shop.factory;

import com.shop.method.MailSernder;
import com.shop.method.SMSSender;
import com.shop.method.Sender;

public class StaticFactory {
    public static Sender produceMail(){
        return new MailSernder();
    }
    public static Sender produceSms(){
        return new SMSSender();
    }
}
