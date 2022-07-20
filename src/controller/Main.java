package controller;

import model.FileOperations;
import model.InvoiceHeader;
import model.InvoiceLine;
import view.ProgramInterface;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ProgramInterface view = new ProgramInterface();
        Controller c = new Controller(view);
        ArrayList<InvoiceHeader> invoices = FileOperations.readFile(null); // read from invoiceHeaders in the project folder
        for (int i = 0 ; i < invoices.size() ; i++)
            System.out.println(invoices.get(i));
    }
}
