package org.luncert;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestHashMap {
    
    @Test
    public void test() {
        Map<Integer, String> map = new HashMap<>();
        map.put(17, "17");
        System.out.println(map.get(1));
        // for (int i = 0; i < 32; i++) {
        //     if (i == 17)
        //         continue;
        //     map.put(i, String.valueOf(i));
        // }
    }
    
}