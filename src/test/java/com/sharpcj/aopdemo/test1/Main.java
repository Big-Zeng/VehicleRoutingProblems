package com.sharpcj.aopdemo.test1;

import java.util.*;

/**
 * Created by Administrator on 2020/8/28.
 */
public class Main {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String lineData = scan.nextLine();
        String[] data = lineData.split(" ");

        if (data[0].equals("and") || data[0].equals("or"))
            System.out.println("error");
        else {
            List<String> results = new ArrayList<>(Arrays.asList(data));
            if (results.size() > 1 && results.size() % 2 == 0) {
                System.out.println("error");
                return;
            }

            for (int i = 0; i < results.size(); i++) {
                if (i % 2 == 1) {
                    if (!results.get(i).equals("and") && !results.get(i).equals("or")) {
                        System.out.println("error");
                        return;
                    }

                } else {
                    if (!results.get(i).equals("true") && !results.get(i).equals("false")) {
                        System.out.println("error");
                        return;
                    }
                }

                if (results.get(i).equals("and")) {
                    boolean flag1 = results.get(i - 1).equals("false") ? false : true;
                    boolean flag2 = results.get(i + 1).equals("false") ? false : true;

                    if (flag1 && flag2 == false) {
                        results.set(i - 1, "false");
                        results.set(i, "or");
                        results.set(i + 1, "false");
                    } else {
                        results.set(i - 1, "true");
                        results.set(i, "or");
                        results.set(i + 1, "true");
                    }
                    i++;
                }
            }

            boolean finnal = false;
            for (int i = 0; i < results.size(); i++) {
                if (results.get(i).equals("true"))
                    finnal = true;
            }
            System.out.println(finnal ? "true" : "false");


        }

    }



    public static void insert(String data) {
        char[] datas = data.toCharArray();
        Character[] result = new Character[datas.length];
        for (int i = 0; i < datas.length; i++) {
            result[i] = datas[i];
        }
        insert2(result);
    }

    public static void insert2(Character[] result) {
        int size = 1;
        int finnalFlag = 0;
        int maxSize = 1;
        for (int i = 0; i < result.length - 1; i++) {
            if (result[i] == result[i + 1]) {
                size++;
            } else {
                if (size > maxSize) {
                    finnalFlag = i;
                    maxSize = size;
                }
                size = 1;
            }
        }
        sortData(finnalFlag, maxSize, result);

    }

    public static void sortData(int finnalFlag, int maxSize, Character[] datas) {
        if (maxSize >= 2) {
            System.out.println(datas[finnalFlag] + " " + finnalFlag);
            Character[] newdatas = new Character[datas.length - maxSize];
            for (int i = 0, j = 0; i < datas.length; i++) {
                if (i <= finnalFlag && i > finnalFlag - maxSize) {
                    continue;
                }
                newdatas[j] = datas[i];
                j++;
            }
            insert2(newdatas);
        }else{
            List<Character> resultList = new ArrayList<>(datas.length);
            Collections.addAll(resultList,datas);
            resultList.add(finnalFlag, resultList.get(finnalFlag));
            insert2(datas);
        }


    }

}


class node implements  Comparable<node>{
    public  int size;
    public int flag;

    public node(int size, int flag) {
        this.size = size;
        this.flag = flag;
    }

    @Override
    public int compareTo(node o) {

        if (this.size > o.size) {
            return 1;
        } else
            return -1;
    }
}
