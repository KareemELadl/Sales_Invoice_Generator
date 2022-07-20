package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvoiceHeader {
    // receipt details
    private int invoiceNumber; // this object invoice number
    private Date invoiceDate;

    public int getInvoiceNumber() {
        return invoiceNumber;
    }


    public InvoiceHeader(Date invoiceDate, String customerName, ArrayList<InvoiceLine> invoiceLines) {
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
        this.invoiceLines = invoiceLines;
        this.invoiceNumber = FileOperations.getnextItemNumber();
    }
    public InvoiceHeader(Date invoiceDate, String customerName, ArrayList<InvoiceLine> invoiceLines , int invoiceNumber) {
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
        this.invoiceLines = invoiceLines;
        this.invoiceNumber = invoiceNumber;
    }

    private String customerName;
    private ArrayList<InvoiceLine> invoiceLines;

    public Date getInvoiceDate() {
        return invoiceDate;
    }



    public String getCustomerName() {
        return customerName;
    }

    public ArrayList<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }
    public String toString(){
        Date d = this.getInvoiceDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        String date = dateFormat.format(d);
        return "\n\n\n"+ getInvoiceNumber() + "\n" +"{\n" +  date + ", " + getCustomerName() + "\n" + display(getInvoiceLines()) + "}";

    }
    public String display(ArrayList<InvoiceLine> invoiceLines){
        String s = "";
        for (int i = 0 ; i < invoiceLines.size() ; i++){
            InvoiceLine in = invoiceLines.get(i);
            s += in.getItemName() + " " + in.getItemPrice() + " " + in.getCount() + "\n";
        }
        return s;
    }
    public double getTotal(){
        double sum = 0;
        for (int i = 0 ; i < invoiceLines.size() ; i++){
            sum += invoiceLines.get(i).getItemPrice() * invoiceLines.get(i).getCount();
        }
        return sum;
    }
    public void removeItem(InvoiceLine item){
        invoiceLines.remove(item);
    }
}
