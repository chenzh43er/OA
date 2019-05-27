package com.shop.test;

import com.shop.factory.Provider;
import com.shop.factory.SenderMailFactory;
import com.shop.method.Sender;

public class Test {
    public static void main(String args[]){
        Provider provider = new SenderMailFactory();
        Sender sender = provider.produce();
        sender.Send();
    }
}
