package com.factory;

import com.factory.abstractFactory.IAbstractFactory;
import com.factory.abstractFactory.IBreakFast;
import com.factory.abstractFactory.Orange;
import com.factory.simple.Bike;
import com.factory.simple.Car;

public class LowPersonFactory implements IAbstractFactory {
    @Override
    public Car getCar() {
        return new Bike();
    }

    @Override
    public IBreakFast getBreakFast() {
        return new Orange();
    }
}
