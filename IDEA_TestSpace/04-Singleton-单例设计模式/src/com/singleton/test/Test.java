package com.singleton.test;

public class Test {
    public static void main(String args[]){
        Singleton a = Singleton.getInstance();
        System.out.println(a.getA());
    }
}
