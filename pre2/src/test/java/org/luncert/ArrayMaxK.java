package org.luncert;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * 寻找一数组中前K个最大的数
 */
@RunWith(JUnit4.class)
public class ArrayMaxK {

    public void quickSort(int[] array, int l, int r) {
        if (l >= r) return;
        int left = l, right = r;
        int cen = right, tmp;
        while (left < right) {
            while (left < right && array[left] <= array[cen]) left++;
            while (left < right && array[right] >= array[cen]) right--;
            tmp = array[left];
            array[left] = array[right];
            array[right] = tmp;
        }
        tmp = array[left];
        array[left] = array[cen];
        array[cen] = tmp;
        quickSort(array, l, left - 1);
        quickSort(array, left + 1, r);
    }
    
    public void quickSort(int[] array) {
        quickSort(array, 0, array.length - 1);
    }

    public void maxK(int k, int[] array) {
        quickSort(array);
        for (int i = array.length - 1, bottom = array.length - 1 - k; i > bottom; i--)
            System.out.println(array[i]);
    }

    @Test
    public void test() {
        int n = 100000;
        Random random = new Random();
        int[] array = new int[n];
        for (int i = 0; i < n; i++)
            array[i] = random.nextInt(n * 100);
        maxK(10, array);
    }

}