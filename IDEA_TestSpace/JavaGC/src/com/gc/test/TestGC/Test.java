package com.gc.test.TestGC;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String args[]){
        int x = 0;
        while(x!=1){
            Object Object = new Object();
            System.out.println("test system gc");
            //System.gc();
        }

        List<String> list = new ArrayList<>();
    }
}
