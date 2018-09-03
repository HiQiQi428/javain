package org.luncert;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.list.ArrayList;

@RunWith(JUnit4.class)
public class TestArrayList {

    @Test
    public void test() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(i);
        System.out.println(list);
    }
    
}