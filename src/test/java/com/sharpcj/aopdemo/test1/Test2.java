package com.sharpcj.aopdemo.test1;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Administrator on 2020/8/27.
 */
public class Test2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = Integer.valueOf(scanner.nextLine());
        LinkedHashMap<String, Long > map = new LinkedHashMap<>(N, 0.75f, true);


        while (scanner.hasNext()) {

            String[] scanDatas = scanner.nextLine().split(" ");
            if (map.containsKey(scanDatas[0])) {
                if (map.get(scanDatas[0]) < Long.valueOf(scanDatas[1])) {
                    map.put(scanDatas[0], Long.valueOf(scanDatas[1]));
                }
            }else{
                if (map.size() < N) {
                    map.put(scanDatas[0], Long.valueOf(scanDatas[1]));
                } else {
                    if (map.size() == N) {
                        Map.Entry entry = map.entrySet().iterator().next();
                        System.out.print(entry.getKey() + " " + entry.getValue());
                        System.out.println();
                        map.remove(entry.getKey());
                        map.put(scanDatas[0], Long.valueOf(scanDatas[1]));
                    }
                }
            }
        }

    }

//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        String[] data1 = scanner.nextLine().split(" ");
//        int n = Integer.valueOf(data1[0]);
//        int m = Integer.valueOf(data1[1]);
//
//        int[] result = new int[n];
//        int[] Ls = new int[m];
//        int[] Rs = new int[m];
//
//        for (int i = 0; i < m; i++) {
//            String[] data2 = scanner.nextLine().split(" ");
//            Ls[i] = Integer.valueOf(data2[0]);
//            Rs[i] =  Integer.valueOf(data2[1]);
//        }
//
//        for (int i = 0; i < m; i++) {
//            int l = Ls[i];
//            int r = Rs[i];
//            for (int j = l; j <= r; j++) {
//                result[j] = i + 1;
//            }
//        }
//
//        int results = 0;
//        for (int i = 0; i < result.length; i++) {
//            results += i*result[i];
//        }
//        System.out.println(results % 100000009);
//
//    }
}









