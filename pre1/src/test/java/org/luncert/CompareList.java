package org.luncert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.list.BlockList;

@RunWith(JUnit4.class)
public class CompareList {

    int num;
    PrintWriter pw;

    private void testAdd(List<Integer> list) {
        long tmp, sum = 0;
        for (int i = 0; i < num; i++) {
            tmp = System.currentTimeMillis();
            list.add(i);
            sum += System.currentTimeMillis() - tmp;
        }
        pw.println(String.format("%fms", sum / (double)num));
    }

    public void testArrayList() {
        List<Integer> list = new ArrayList<>();
        testAdd(list);
    }

    public void testBlockList() {
        List<Integer> list = new BlockList<>();
        testAdd(list);
    }

    public void testLinkedList() {
        List<Integer> list = new LinkedList<>();
        testAdd(list);
    }

    @Before
    public void before() throws FileNotFoundException {
        File file = new File("out1.txt");
        pw = new PrintWriter(new FileOutputStream(file));
    }

    @After
    public void after() {
        pw.close();
    }

    @Test
    public void test() {
        num = 10000;
        testArrayList();
        testBlockList();
        testLinkedList();
        // for (int i = 0; i < 1000; i++) {
        //     num += 100;
        //     testArrayList();
        //     testBlockList();
        //     testLinkedList();
        //     System.gc();
        // }
    }
}