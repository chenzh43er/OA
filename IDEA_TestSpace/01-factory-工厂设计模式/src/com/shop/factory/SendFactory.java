package com.shop.factory;

import com.shop.method.MailSernder;
import com.shop.method.SMSSender;
import com.shop.method.Sender;

public class SendFactory {
    public Sender produce(String msg){
        if("mail".equals(msg)){
            return new MailSernder();
        }else if("sms".equals(msg)){
            return new SMSSender();
        }else{
            System.out.println("fail");
            return null;
        }
    }
}
