package org.luncert;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CompareList {

    private void run(List<Integer> list) {
        long tmp, sum = 0;
        for (int i = 0; i < 10000000; i++) {
            tmp = System.currentTimeMillis(); 
            list.add(i);
            sum += System.currentTimeMillis() - tmp;
        }
        System.out.println(String.format("%f", sum / 10000000d));
    }

    /**
     * compare ArrayList and LinkedList
     */
    @Test
    public void test() {
        List<Integer> list = new ArrayList<>();
        run(list);
        list = new LinkedList<>();
        run(list);
    }
}