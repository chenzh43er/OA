package com.factory;

import com.factory.abstractFactory.IAbstractFactory;
import com.factory.abstractFactory.IBreakFast;
import com.factory.abstractFactory.Milk;
import com.factory.simple.Bus;
import com.factory.simple.Car;

public class HighPersonFactoy implements IAbstractFactory {
    @Override
    public Car getCar() {
        return new Bus();
    }

    @Override
    public IBreakFast getBreakFast() {
        return new Milk();
    }
}
