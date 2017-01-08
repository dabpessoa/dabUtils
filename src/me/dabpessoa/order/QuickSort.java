package me.dabpessoa.order;

import java.util.Arrays;
import java.util.Random;
 
public class QuickSort {
    public static final Random RND = new Random();
 
    private static void swap(Object[] array, int i, int j) {
        Object tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
 
    private static <E extends Comparable<? super E>> int partition(E[] array, int begin, int end) {
        int index = begin + RND.nextInt(end - begin + 1);
        E pivot = array[index];
        swap(array, index, end);
        for (int i = index = begin; i < end; ++i) {
            if (array[i].compareTo(pivot) <= 0) {
                swap(array, index++, i);
            }
        }
        swap(array, index, end);
        return (index);
    }
 
    private static <E extends Comparable<? super E>> void qsort(E[] array, int begin, int end) {
        if (end > begin) {
            int index = partition(array, begin, end);
            qsort(array, begin, index - 1);
            qsort(array, index + 1, end);
        }
    }
 
    public static <E extends Comparable<? super E>> void sort(E[] array) {
        qsort(array, 0, array.length - 1);
    }
 
    // Exemplo de uso
    public static void main(String[] args) {
 
        // Ordenando Inteiros
        Integer[] l1 = { 5, 1024, 1, 88, 0, 1024 };
        System.out.println("l1  start:" + Arrays.toString(l1));
        QuickSort.sort(l1);
        System.out.println("l1 sorted:" + Arrays.toString(l1));
 
        // Ordenando Strings
        String[] l2 = { "gamma", "beta", "alpha", "zoolander" };
        System.out.println("l2  start:" + Arrays.toString(l2));
        QuickSort.sort(l2);
        System.out.println("l2 sorted:" + Arrays.toString(l2));
    }
}
