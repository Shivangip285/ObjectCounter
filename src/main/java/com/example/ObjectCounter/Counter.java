package com.example.ObjectCounter;

import lombok.Data;

@Data
public class Counter {
    private int countValue;

    public Counter() {}

    public int incrementCountValue(int countValue) {
        return countValue+1;
    }
    public int decrementCountValue(int countValue) {
        return countValue-1;
    }

}
