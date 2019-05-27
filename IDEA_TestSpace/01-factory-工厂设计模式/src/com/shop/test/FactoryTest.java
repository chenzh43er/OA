package com.shop.test;

import com.shop.factory.SendFactory;
import com.shop.factory.StaticFactory;
import com.shop.method.Sender;

public class FactoryTest {
    public static void main(String args[]){
        /*SendFactory factory = new SendFactory();
        Sender sender = factory.produce("mail");
        sender.send();*/

        //静态工厂创建
        Sender sender = StaticFactory.produceMail();
        sender.send();
    }
}
