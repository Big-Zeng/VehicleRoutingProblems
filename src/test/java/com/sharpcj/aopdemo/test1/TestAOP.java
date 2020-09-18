package com.sharpcj.aopdemo.test1;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2020/8/23.
 */
public class TestAOP {

    public static void main(String[] args) {
     //   AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
       // Boy boy = context.getBean("boy",Boy.class);


       //"\"" true
        // "123" true
        //"\\"" FALSE

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
     /*   char[] datas = input.toCharArray();

        int j = 0;
        for (int i = 0; i < datas.length; i++) {
            if (datas[i] == '\"') {
                j++;
            }
        }
        if(j % 2 == 0)*/


        String pattern = "\"{1,2}[^\"][^\\\\][^\\\\].*\"{0,2}";

       // String patern = "^\"[^\\\\{2}].+\"$";
        //String patern = "^\"([^\"]).+\"$";
        if(Pattern.matches(pattern,input))
            System.out.println("true");
        else
            System.out.println("false");

    }





    public static String[] listToTree(String[][] departments) {

        HashMap<String,department> departmentNodes = new HashMap();


        for (String[] department : departments) {
            departmentNodes.put(department[0], new department(department[0], department[1], department[2]));
        }

        for (String s : departmentNodes.keySet()) {
            department department = departmentNodes.get(s);
            if (!department.parentId.equals("")) {
                departmentNodes.get(department.parentId).subDepartments.add(department);
            }
        }

        List<String> result = new ArrayList<>();
        result.add(departmentNodes.get("d0").value);
        return preSort(departmentNodes.get("d0"), result);


    }

    public static String[] preSort(department rootDepart,List<String> results) {


        for (department subDepartment : rootDepart.subDepartments) {
            results.add(subDepartment.value);
            if (subDepartment.subDepartments.size() != 0) {
                preSort(subDepartment, results);
            }
        }
        String[] strArray = new String[results.size()];
         return  results.toArray(strArray);

    }





}


class department implements Comparable<department> {

    public String id;
    public String value;
    public String parentId;
    public TreeSet<department> subDepartments = new TreeSet<>() ;

    public department(String id, String parentId, String value) {
        this.id = id;
        this.value = value;
        this.parentId = parentId;

    }


    @Override
    public int compareTo(department o) {
        return this.value.compareTo(o.value);
    }
}
