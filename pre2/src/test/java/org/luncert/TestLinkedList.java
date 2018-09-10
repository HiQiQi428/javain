package org.luncert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * 反转单链表
 */
@RunWith(JUnit4.class)
public class TestLinkedList {

    @Test
    public void test() {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < 100; i++)
            list.addTail(i);
        list.reverse();
        System.out.println(list);
    }

}