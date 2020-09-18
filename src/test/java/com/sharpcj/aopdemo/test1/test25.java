package com.sharpcj.aopdemo.test1;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2020/9/15.
 */
public class test25 {

    public static void main(String[] args) {
        String fileName = "inputs.txt";
        List<String> datas = new ArrayList<>();
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader bf = new BufferedReader(fr);
            String str;

            while ((str = bf.readLine()) != null) {
                datas.add(str);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(datas);String outPath = "out.txt";BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(outPath));
            for (String data : datas) {
                bw.write(data);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
