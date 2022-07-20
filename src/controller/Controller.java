package controller;

import model.FileOperations;
import model.InvoiceHeader;
import model.InvoiceLine;
import view.ProgramInterface;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Controller extends JFrame implements ActionListener, MouseListener, ListSelectionListener {
    private ProgramInterface view;
    ArrayList<InvoiceHeader> newInvoices; // used to store invoices until user decides either to save them or discard them
    ArrayList<InvoiceHeader> invoiceHeaders;
    DefaultTableModel newItemsModel;
    public Controller(ProgramInterface view) {
        this.view = view;
        newInvoices = new ArrayList<>();
        invoiceHeaders = FileOperations.readFile(null);
        DefaultTableModel newModel = view.getModel();
        newItemsModel = view.getInvoiceItemsModel();


        for (int i = 0 ; i<invoiceHeaders.size() ; i++){
            InvoiceHeader currentInvoice = invoiceHeaders.get(i);
            for (int j = 0 ; j < currentInvoice.getInvoiceLines().size() ; j++){
                InvoiceLine currentItem = currentInvoice.getInvoiceLines().get(j);
                Object[] newItemRow = convertInvoiceItems(currentItem);
                newItemsModel.addRow(newItemRow);
            }
            Object[] newRow = convertInvoices(currentInvoice);
            newModel.addRow(newRow);
//            view.setModel(newModel);
        }
        view.setVisible(true);
        JTable leftPanelTable = view.getLeftPanelTable();
        ListSelectionModel listSelectionModel = leftPanelTable.getSelectionModel();
//        listSelectionModel.addSelectionInterval(0,0);
        view.addActionListener(this);
        view.addTableListener(this);
        view.addMouseListener(this);
        if (newModel.getRowCount()>0)
            view.getLeftPanelTable().setRowSelectionInterval(0,0);
    }
    private Object[] convertInvoices(InvoiceHeader currentInvoice){
        Object[] result = new Object[4];
        int number = currentInvoice.getInvoiceNumber();
        Date date = currentInvoice.getInvoiceDate();
        String customerName = currentInvoice.getCustomerName();
        double total = currentInvoice.getTotal();
        result[0] = number;
        result[1] = date;
        result[2] = customerName;
        result[3] = total;
        return result;
    }
    private Object[] convertInvoiceItems(InvoiceLine invoiceItem){
        Object[] result = new Object[5];
        int number = invoiceItem.getItemNumber();
        int count = invoiceItem.getCount();
        int price = invoiceItem.getItemPrice();
        String name = invoiceItem.getItemName();
        int total = price * count;
        result[0] = number;
        result[1] = name;
        result[2] = price;
        result[3] = count;
        result[4] = total;
        
        return result;

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "newInvoice"){
            try{
            String yearS = JOptionPane.showInputDialog("Please enter year of creation of the new invoice");
            String monthS = JOptionPane.showInputDialog("Please enter month of creation of the new invoice");
            String dayS = JOptionPane.showInputDialog("Please enter day of creation of the new invoice");
            String name = JOptionPane.showInputDialog("Please enter the customer name");
            String date = dayS + "-" + monthS + "-" + yearS;
            SimpleDateFormat stringToDate=new SimpleDateFormat("dd-MM-yyyy");
            Date d = stringToDate.parse(date);
            ArrayList<InvoiceLine> invoiceItems = new ArrayList<>();
            int currentNumber = FileOperations.getnextItemNumber() + newInvoices.size();
            InvoiceHeader newInvoice = new InvoiceHeader(d , name , invoiceItems , currentNumber);
            DefaultTableModel newModel = view.getModel();
            newInvoices.add(newInvoice);
            Object[] newRow = convertInvoices(newInvoice);
            newModel.addRow(newRow);
            view.getLeftPanelTable().setRowSelectionInterval(newModel.getRowCount()-1 , newModel.getRowCount()-1);

            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null , "INVAILD DATE!!!!");
            }
        }
        if(e.getActionCommand() == "createItem"){
            if (view.getLeftPanelTable().getSelectedRow() == -1){
                JOptionPane.showMessageDialog(null , "PLEASE SELECT INVOICE TO CREATE ITEM");
                return;
            }
            String itemName = JOptionPane.showInputDialog("Please enter the name of the item") ;
            String itemPriceS = JOptionPane.showInputDialog("Please enter the price of the item");
            String countS = JOptionPane.showInputDialog("Please enter the count of the item");
            int itemPrice = Integer.parseInt(itemPriceS);
            int count = Integer.parseInt(countS);
            int itemTotal =  itemPrice * count;
            int number = (int) view.getLeftPanelTable().getValueAt(view.getLeftPanelTable().getSelectedRow() , 0);
            InvoiceLine newInvoice = new InvoiceLine(itemName , itemPrice , count , number);
            InvoiceHeader selectedInvoice = getInvoiceByNumber(number);
            selectedInvoice.getInvoiceLines().add(newInvoice);
            view.getLeftPanelTable().setValueAt(selectedInvoice.getTotal() , view.getLeftPanelTable().getSelectedRow() , 3 );
            view.getInvoiceTotalField().setText(selectedInvoice.getTotal() + " ");
            Object[] newRow = convertInvoiceItems(newInvoice);
            newItemsModel.addRow(newRow);

        }
        if(e.getActionCommand() == "Save" || e.getActionCommand() == "saveFile"){
            ArrayList<InvoiceLine> newItems = new ArrayList<>();
            for (int i = 0 ; i < view.getModel().getRowCount() ; i++){
                int number = (int) view.getModel().getValueAt(i,0);
                Date date = (Date) view.getModel().getValueAt(i , 1);
                String customerName = (String) view.getModel().getValueAt(i , 2);
                ArrayList<InvoiceLine> items = new ArrayList<>();
                for (int j = 0 ; j < view.getInvoiceItemsModel().getRowCount() ; j++){
                    int itemNumber = (int) view.getInvoiceItemsModel().getValueAt(j , 0);
                    String itemName = (String) view.getInvoiceItemsModel().getValueAt(j , 1);
                    int itemPrice = (int) view.getInvoiceItemsModel().getValueAt(j,2);
                    int itemCount = (int) view.getInvoiceItemsModel().getValueAt(j , 3);
                    int itemTotal = (int) view.getInvoiceItemsModel().getValueAt(j , 4);

                    if (itemNumber == number){
                        InvoiceLine newItem = new InvoiceLine(itemName , itemPrice , itemCount , itemNumber);
                        items.add(newItem);
                        newItems.add(newItem);
                    }
                }
                InvoiceHeader newInvoice = new InvoiceHeader(date, customerName , items , number);
                newInvoices.add(newInvoice);
            }
            if (e.getActionCommand() == "Save") {
                FileOperations.writeFile(newInvoices, null);
                FileOperations.loadInvoiceLines(newItems, null);
            }
            else{
                JFileChooser fileChooser = new JFileChooser();
                String path = null;
                String itemsPath = null;
                if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
                    path = fileChooser.getSelectedFile().getPath();
                if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
                    itemsPath =  fileChooser.getSelectedFile().getPath();
                System.out.println(path);
                System.out.println(itemsPath);
                FileOperations.writeFile(newInvoices, path);
                FileOperations.loadInvoiceLines(newItems, itemsPath);
            }

            newInvoices.clear();
        }
        if (e.getActionCommand() == "Cancel") {
            ArrayList<InvoiceHeader> invoices = FileOperations.readFile(null);
            DefaultTableModel model = view.getModel();
            while(model.getRowCount() != 0){
                model.removeRow(0);
            }
            DefaultTableModel itemsModel = view.getInvoiceItemsModel();
            while(itemsModel.getRowCount() != 0)
                itemsModel.removeRow(0);

        for (int i = 0 ; i<invoices.size() ; i++){
            InvoiceHeader currentInvoice = invoices.get(i);
            for (int j = 0 ; j < currentInvoice.getInvoiceLines().size() ; j++){
                InvoiceLine currentItem = currentInvoice.getInvoiceLines().get(j);
                Object[] newItemRow = convertInvoiceItems(currentItem);
                itemsModel.addRow(newItemRow);
            }
            Object[] newRow = convertInvoices(currentInvoice);
            model.addRow(newRow);
//            view.setModel(newModel);
        }
        view.getInvoiceTotalField().setText("");
        view.getInvoiceNumberField().setText("");
        view.getInvoiceNameField().setText("");
        view.getInvoiceDateField().setText("");


        }
        if (e.getActionCommand() == "deleteItem"){
            JTable invoiceItems = view.getInvoiceItemsTable();
            if (invoiceItems.getSelectedRow() == -1)
                JOptionPane.showMessageDialog(null , "NO ELEMENT SELECTED TO BE DELETED!!!");
            else{
                int number = (int) view.getInvoiceItemsModel().getValueAt(invoiceItems.getSelectedRow() , 0);
                int price = (int) view.getInvoiceItemsModel().getValueAt(invoiceItems.getSelectedRow() , 2);
                String name = (String) view.getInvoiceItemsModel().getValueAt(invoiceItems.getSelectedRow() , 1);
                InvoiceHeader selectedInvoice = getInvoiceByNumber(number);

                for(int i = 0 ; i < selectedInvoice.getInvoiceLines().size() ; i++){
                    InvoiceLine in = selectedInvoice.getInvoiceLines().get(i);
                    if (in.getItemName().equals(name)){
                        selectedInvoice.getInvoiceLines().remove(i);
                        break;
                    }

                }
                double newTotal = selectedInvoice.getTotal();
                int num = selectedInvoice.getInvoiceNumber();
                view.getModel().setValueAt(newTotal , num-1, 3);
                view.getInvoiceItemsModel().removeRow(invoiceItems.getSelectedRow());
            }
        }
        if (e.getActionCommand() == "Delete"){
            if (view.getLeftPanelTable().getSelectedRow() == -1){
                JOptionPane.showMessageDialog(null, "YOU MUST SELECT A ROW TO DELETE IT!!!");
                return;
            }
            int number = (int) view.getModel().getValueAt(view.getLeftPanelTable().getSelectedRow() , 0);
            view.getModel().removeRow(view.getLeftPanelTable().getSelectedRow());
            for (int i = 0 ; i < view.getInvoiceItemsModel().getRowCount() ; i++){
                int itemNumber = (int) view.getInvoiceItemsModel().getValueAt(i,0);
                if (number == itemNumber) {
                    view.getInvoiceItemsModel().removeRow(i);
                    i--;
                }
            }
        }
        if (e.getActionCommand() == "loadFile"){
            JFileChooser fileChooser = new JFileChooser();
            String path = null;
            String itemsPath = null;
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                path = fileChooser.getSelectedFile().getPath();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                itemsPath =  fileChooser.getSelectedFile().getPath();

             DefaultTableModel model = view.getModel();
             while(model.getRowCount() != 0){
                 model.removeRow(0);
             }
             DefaultTableModel itemsModel = view.getInvoiceItemsModel();
             while(itemsModel.getRowCount() != 0)
                 itemsModel.removeRow(0);                                  
            ArrayList<InvoiceLine> invoiceItems = FileOperations.readInvoiceLines(itemsPath);
            ArrayList<InvoiceHeader> invoices = FileOperations.readFile(path);
            for (int i = 0 ; i<invoices.size() ; i++){
                InvoiceHeader currentInvoice = invoices.get(i);
                for (int j = 0 ; j < currentInvoice.getInvoiceLines().size() ; j++){
                    System.out.println(currentInvoice.getInvoiceLines().size());
                    InvoiceLine currentItem = currentInvoice.getInvoiceLines().get(j);
                    Object[] newItemRow = convertInvoiceItems(currentItem);
                    view.getInvoiceItemsModel().addRow(newItemRow);
                }
                Object[] newRow = convertInvoices(currentInvoice);
                view.getModel().addRow(newRow);
            }











        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        JTable temp = view.getLeftPanelTable();
        if (e.getClickCount() == 1){
            if (e.getSource() == temp ){
                view.getInvoiceNumberField().setText(temp.getValueAt(temp.getSelectedRow() , 0) + "");
                view.getInvoiceDateField().setText(temp.getValueAt(temp.getSelectedRow() , 1) + "");
                view.getInvoiceNameField().setText(temp.getValueAt(temp.getSelectedRow() , 2) + "");
                view.getInvoiceTotalField().setText(temp.getValueAt(temp.getSelectedRow() , 3) + "");
            }

        }




    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        JTable temp = view.getLeftPanelTable();
        if (temp.getSelectedRow() != -1) {
            view.getInvoiceNumberField().setText(temp.getValueAt(temp.getSelectedRow(), 0) + "");
            view.getInvoiceDateField().setText(temp.getValueAt(temp.getSelectedRow(), 1) + "");
            view.getInvoiceNameField().setText(temp.getValueAt(temp.getSelectedRow(), 2) + "");
            view.getInvoiceTotalField().setText(temp.getValueAt(temp.getSelectedRow(), 3) + "");
        }
    }

    public InvoiceHeader getInvoiceByNumber(int n){
         for (int i = 0 ; i<invoiceHeaders.size() ; i++){
             if (invoiceHeaders.get(i).getInvoiceNumber() == n)
                 return invoiceHeaders.get(i);
         }
         for (int i = 0 ; i< newInvoices.size() ; i++){
             if (newInvoices.get(i).getInvoiceNumber() == n)
                 return newInvoices.get(i);
         }
         return null;
    }
}
