package com.shop.factory;

import com.shop.method.Sender;

public interface Provider {
    public Sender produce();
}
