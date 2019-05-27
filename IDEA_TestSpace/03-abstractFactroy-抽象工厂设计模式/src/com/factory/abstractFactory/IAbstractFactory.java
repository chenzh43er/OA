package com.factory.abstractFactory;

import com.factory.simple.Car;

public interface IAbstractFactory {

    Car getCar();

    IBreakFast getBreakFast();
}
