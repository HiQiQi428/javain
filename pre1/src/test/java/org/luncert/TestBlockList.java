package org.luncert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.list.BlockList;

@RunWith(JUnit4.class)
public class TestBlockList {

    BlockList<Integer> list = new BlockList<>();

    @Test
    public void test() {
        for (int i = 0; i < 100; i++)
            list.add(i);
        list.remove(new Integer(45));
        list.add(100);
        list.add(101);
        list.add(0, 999);
        System.out.println(list.prettyString());
        System.out.println(list.set(100, 1000));
        System.out.println(list.get(100));
        System.out.println(list.indexOf(1000));
        System.out.println("Block Size: " + list.getBlockSize());
        System.out.println("Elem Size: " + list.getElemSize());
        System.out.println("Space Size: " + list.getSpace());
        System.out.println("Space Usage: " + list.getUsage() + '%');
    }
}