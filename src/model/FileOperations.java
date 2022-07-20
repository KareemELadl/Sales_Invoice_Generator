package model;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileOperations {
    public static String path = null;
    public static String outputPath = null;
    public static String itemsPath = null;
    public static String outputItemsPath = null;
    public static ArrayList<InvoiceLine> readInvoiceLines(String filename){
        BufferedReader br = null;
        ArrayList<InvoiceLine> result = new ArrayList<>();
        if (filename == null)
            itemsPath = "InvoiceLine.csv";
        else
            itemsPath = filename;
        try {
            br = new BufferedReader(new FileReader(itemsPath));
            String line = br.readLine();
            while(line != null) {
                String[] Invoice = line.split(",");
                InvoiceLine in = new InvoiceLine(Invoice[1],
                        Integer.parseInt(Invoice[2]),
                        Integer.parseInt(Invoice[3])
                        ,Integer.parseInt(Invoice[0]));
                result.add(in);
                line = br.readLine();
            }
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null,"FILE NOT FOUND!!!!!!!");
            e.printStackTrace();
        }
        finally {
            try {
                br.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }
    public static ArrayList<InvoiceLine> getInvoiceLineByNumber(int number){
        ArrayList<InvoiceLine> invoiceLines = readInvoiceLines(itemsPath);
        ArrayList<InvoiceLine> result = new ArrayList<>();
        for (InvoiceLine invoiceLine : invoiceLines) {
            if (invoiceLine.getItemNumber() == number)
                result.add(invoiceLine);
        }
        return result;
    }
    public static ArrayList<InvoiceHeader> readFile(String filename){
        if (filename == null)
            path = "InvoiceHeader.csv";
        else
            path = filename;
        ArrayList<InvoiceHeader> result = new ArrayList<>();
        BufferedReader br = null;
        try {
             br = new BufferedReader(new FileReader(path));
             String line = br.readLine();
             while(line != null){
                 String[] Invoice = line.split(",");
                 SimpleDateFormat stringToDate=new SimpleDateFormat("dd-MM-yyyy");
                 String dateString = Invoice[1];
                 Date date = stringToDate.parse(dateString);
                 InvoiceHeader in = new InvoiceHeader(date , Invoice[2] , getInvoiceLineByNumber(Integer.parseInt(Invoice[0])) , Integer.parseInt(Invoice[0]));
                 result.add(in);
                 line = br.readLine();
             }
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null , "FILE NOT FOUND!!!");
            e.printStackTrace();
        }
        return result;
    }

    public static void writeFile(ArrayList<InvoiceHeader> invoices , String fileName){
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;
        if (fileName == null){
            outputPath = "InvoiceHeader.csv";
        }
        else
            outputPath = fileName;
        try{
            fw = new FileWriter(outputPath);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);
            for (int i = 0 ; i<invoices.size() ; i++){
                InvoiceHeader in = invoices.get(i);
                int invoiceNumber = in.getInvoiceNumber();
                Date d = in.getInvoiceDate();
                SimpleDateFormat dateToString = new SimpleDateFormat("dd-MM-YYYY");
                String date = dateToString.format(d);
                String customerName = in.getCustomerName();
                String toWrite = invoiceNumber + "," + date + "," + customerName;
                pw.println(toWrite);
                pw.flush();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void loadInvoiceLines(ArrayList<InvoiceLine> invoiceLines , String fileName){
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;
        if (fileName == null){
            outputItemsPath = "InvoiceLine.csv";
        }
        else
            outputItemsPath = fileName;
        try{
            fw = new FileWriter(outputItemsPath);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);
            for (int i = 0 ; i<invoiceLines.size() ; i++){
                InvoiceLine in = invoiceLines.get(i);
                int invoiceNumber = in.getItemNumber();
                int price = in.getItemPrice();
                int count = in.getCount();
                String itemName = in.getItemName();
                String toWrite =invoiceNumber + "," + itemName + "," + price + "," + count;
                pw.println(toWrite);
                pw.flush();

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                pw.close();
                bw.close();
                fw.close();
            }
            catch (Exception e){

                e.printStackTrace();
            }
        }
    }
    public static int getnextItemNumber(){
        ArrayList<InvoiceHeader> invoiceHeaders = readFile(path);
        int number = 1;
        for (int i = 0 ; i < invoiceHeaders.size() ; i++)
            number++;
        return number;
//        return 2;
    }
}
