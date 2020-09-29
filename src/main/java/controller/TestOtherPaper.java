package controller;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import pojo.Client;
import pojo.Vehicle;
import scala.Int;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/9/28.
 */
@Deprecated
public class TestOtherPaper {


    public static void main(String[] args) {
        try {
            Workbook wb = WorkbookFactory.create(new File("emp.xlsx")); //拿到文件
            Sheet sheet = wb.getSheetAt(0); //读取第一张表
            int lastRowNum = sheet.getLastRowNum();//获取到总行数 -->最后一行就是总行数
            for (int i = 2; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i); //拿每一行
                short lastCellNum = row.getLastCellNum(); //拿到对应行的总列数
                for (int j = 0; j < lastCellNum; j++) {
                    Cell cell = row.getCell(j);
                    System.out.print(cell.getStringCellValue() + " --> ");
                }
                System.out.println();
            }




        } catch (IOException e) {
            e.printStackTrace();

        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }


    }


    private List<Client> getClient(Sheet sheet) {

        List<Client> clients = new ArrayList<>();
        int i = 8;
        while (true) {
            Row row = sheet.getRow(i); //拿每一行
            String value = row.getCell(0).getStringCellValue();
            if(value.equals(""))
                break;

            int id = Integer.valueOf(value);
            double x = Double.valueOf(row.getCell(1).getStringCellValue());
            double y = Double.valueOf(row.getCell(2).getStringCellValue());
            double demand = Double.valueOf(row.getCell(3).getStringCellValue());
            clients.add(new Client(id, demand, y, x));
            i++;
        }
        return clients;
    }


    private List<Vehicle> getVehicles(Sheet sheet) {
        List<Vehicle> vehicleList = new ArrayList<>();
        int i = 4;
        while (true) {
            Row row = sheet.getRow(i); //拿每一行
            String value = row.getCell(0).getStringCellValue();
            if(value.equals(""))
                break;

            int id = Integer.valueOf(value);
            double fixedCost = Double.valueOf(row.getCell(1).getStringCellValue());
            int FleetSize = Integer.valueOf(row.getCell(2).getStringCellValue());
            double Capacity = Double.valueOf(row.getCell(3).getStringCellValue());
            vehicleList.add(new Vehicle(id, Capacity, 1, FleetSize));
            i++;
        }
        return vehicleList;
    }


    private double[][][] getDisCost(Sheet sheet,int n,int k) {
        double[][][] dis = new double[n][n][k];
        int i = 25;
        while (true) {
            Row row = sheet.getRow(i); //拿每一行
            String value = row.getCell(0).getStringCellValue();
            if(value.equals(""))
                break;

            int carType = Integer.valueOf(value);
            int from = Integer.valueOf(row.getCell(1).getStringCellValue());
            int to = Integer.valueOf(row.getCell(2).getStringCellValue());
            double cost1 = Double.valueOf(row.getCell(4).getStringCellValue());
            dis[from][to][carType] = cost1;
        }
        return dis;
    }








}
