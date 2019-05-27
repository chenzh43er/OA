package com.factory.test;

import com.factory.LowPersonFactory;
import com.factory.abstractFactory.IAbstractFactory;
import com.factory.abstractFactory.IBreakFast;
import com.factory.simple.Car;

public class FactoryTest {
    public static void main(String args[]){
        IAbstractFactory factory = new LowPersonFactory();
        Car car = factory.getCar();
        IBreakFast breakFast = factory.getBreakFast();
        car.gotoWork();
        breakFast.eat();
    }
}
