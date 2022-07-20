package view;

import model.FileOperations;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class ProgramInterface extends JFrame {
    private String[] leftPanelTableHeaders = {"No." , "Date" , "Customer" , "Total"};
    private JPanel leftPanel = new JPanel();
    private JPanel rightPanel = new JPanel();
    private DefaultTableModel model = new DefaultTableModel(leftPanelTableHeaders , 0); // model for left panel table
    JTable leftPanelTable;
    private JButton newInvoice;
    private JButton deleteInvoice;

    private JButton save;
    private JButton cancel;

    private String[] invoiceItemsTableHeaders = {"No." , "Item Name" , "Item Price" , "Count" , "Item Total"};
    private DefaultTableModel invoiceItemsModel = new DefaultTableModel(invoiceItemsTableHeaders , 0);
    private JTable invoiceItemsTable;
    private JMenuItem loadFile;
    private JMenuItem saveFile;
    private JTextField invoiceDateField;
    private JTextField invoiceNumberField;
    private JTextField invoiceTotalField;
    private JTextField invoiceNameField;

    private JButton createItem;
    private JButton deleteItem;

    public JTable getInvoiceItemsTable() {
        return invoiceItemsTable;
    }

    public void setInvoiceItemsTable(JTable invoiceItemsTable) {
        this.invoiceItemsTable = invoiceItemsTable;
    }

    public JTextField getInvoiceTotalField() {
        return invoiceTotalField;
    }

    public void setInvoiceTotalField(JTextField invoiceTotalField) {
        this.invoiceTotalField = invoiceTotalField;
    }

    public JTextField getInvoiceNameField() {
        return invoiceNameField;
    }

    public void setInvoiceNameField(JTextField invoiceNameField) {
        this.invoiceNameField = invoiceNameField;
    }

    public JTextField getInvoiceDateField() {
        return invoiceDateField;
    }

    public void setInvoiceDateField(JTextField invoiceDateField) {
        this.invoiceDateField = invoiceDateField;
    }

    public JTextField getInvoiceNumberField() {
        return invoiceNumberField;
    }

    public void setInvoiceNumberField(JTextField invoiceNumberField) {
        this.invoiceNumberField = invoiceNumberField;
    }

    public JTable getLeftPanelTable() {
        return leftPanelTable;
    }

    public void setLeftPanelTable(JTable leftPanelTable) {
        this.leftPanelTable = leftPanelTable;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public void setModel(DefaultTableModel model) {
        this.model = model;
    }

    public DefaultTableModel getInvoiceItemsModel() {
        return invoiceItemsModel;
    }

    public void setInvoiceItemsModel(DefaultTableModel invoiceItemsModel) {
        this.invoiceItemsModel = invoiceItemsModel;
    }

    public ProgramInterface(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        this.setLayout(new GridLayout(1,2));

        leftPanel.setLayout(new BorderLayout());

        JLabel invoicesTableText = new JLabel("Invoices Table");
        invoicesTableText.setBorder(BorderFactory.createEmptyBorder(30,60,10,0));

        leftPanelTable = new JTable(model);
        leftPanelTable.setAutoCreateRowSorter(true);

        JPanel leftTopPanel = new JPanel();
        leftTopPanel.setLayout(new BorderLayout());
        leftTopPanel.add((invoicesTableText) , BorderLayout.NORTH);
        leftTopPanel.setBorder(BorderFactory.createEmptyBorder(0,30,0,0));
        JScrollPane leftPanelTablePane = new JScrollPane(leftPanelTable);
//        leftPanelTablePane.setBorder(BorderFactory.createEmptyBorder());
        leftTopPanel.add(leftPanelTablePane);

        JPanel leftBottomPanel = new JPanel();
        leftBottomPanel.setLayout(new FlowLayout());
        leftBottomPanel.setBorder(BorderFactory.createEmptyBorder(0,0,40,0));
        this.add(leftPanel);
        this.add(rightPanel);
        leftPanel.add(leftTopPanel );
        leftPanel.add(leftBottomPanel , BorderLayout.SOUTH);
        newInvoice = new JButton("Create New Invoice");
        newInvoice.setFocusable(false);
        newInvoice.setBackground(Color.lightGray);
        newInvoice.setActionCommand("newInvoice");

        deleteInvoice = new JButton("Delete Invoice");
        deleteInvoice.setBackground(Color.lightGray);
        deleteInvoice.setFocusable(false);
        deleteInvoice.setActionCommand("Delete");

        leftBottomPanel.add(newInvoice,BorderLayout.WEST);
        leftBottomPanel.add(deleteInvoice,BorderLayout.SOUTH);


        JPanel invoiceData = new JPanel(); // right top panel
        invoiceData.setLayout(new BoxLayout(invoiceData,BoxLayout.Y_AXIS));
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(invoiceData , BorderLayout.NORTH);
        invoiceData.setAlignmentX(LEFT_ALIGNMENT);

        JPanel invoiceItems = new JPanel(); // right center panel

        JLabel invoiceNumber = new JLabel("Invoice Number       ");
        invoiceNumberField = new JTextField(30);
        invoiceNumberField.setEditable(false);
        invoiceNumberField.setBorder(BorderFactory.createEmptyBorder());
        invoiceNumberField.setMaximumSize(invoiceNumberField.getPreferredSize() );
        invoiceNumberField.setText("");
        JPanel invoiceNumberPanel = new JPanel();
        invoiceNumberPanel.add(invoiceNumber);
        invoiceNumberPanel.add(invoiceNumberField);
        invoiceNumberPanel.setLayout(new FlowLayout());

        JLabel invoiceDate = new JLabel("Invoice Date           ");
        invoiceDateField = new JTextField(30);
        invoiceDateField.setMaximumSize(invoiceDateField.getPreferredSize() );
        JPanel invoiceDatePanel = new JPanel();
        invoiceDatePanel.add(invoiceDate);
        invoiceDatePanel.add(invoiceDateField);

        JLabel customerName = new JLabel("Customer Name     ");
        invoiceNameField = new JTextField(30);
        JPanel invoiceNamePanel = new JPanel();
        invoiceNameField.setMaximumSize(invoiceNameField.getPreferredSize() );
        invoiceNamePanel.add(customerName);
        invoiceNamePanel.add(invoiceNameField);

        JLabel invoiceTotal = new JLabel("Invoice Total           ");
        invoiceTotalField = new JTextField(30);
        invoiceTotalField.setEditable(false);
        invoiceTotalField.setMaximumSize(invoiceTotalField.getPreferredSize() );
        invoiceTotalField.setBorder(BorderFactory.createEmptyBorder());
        invoiceTotalField.setText("");
        JPanel invoiceTotalPanel = new JPanel();
        invoiceTotalPanel.add(invoiceTotal);
        invoiceTotalPanel.add(invoiceTotalField);

        createItem = new JButton("Add new item");
        createItem.setFocusable(false);
        createItem.setBackground(Color.lightGray);
        createItem.setActionCommand("createItem");

        deleteItem = new JButton("Remove selected item");
        deleteItem.setFocusable(false);
        deleteItem.setBackground(Color.lightGray);
        deleteItem.setActionCommand("deleteItem");

        JPanel itemsCtrl = new JPanel();
        itemsCtrl.add(createItem);
        itemsCtrl.add(deleteItem);
        itemsCtrl.setLayout(new FlowLayout());


        invoiceData.add(invoiceNumberPanel);
        invoiceData.add(invoiceDatePanel);
        invoiceData.add(invoiceNamePanel);
        invoiceData.add(invoiceTotalPanel);
        invoiceData.add(itemsCtrl);
        invoiceData.setBorder(BorderFactory.createEmptyBorder(0,0,0,150));

        invoiceItemsTable = new JTable(invoiceItemsModel);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(invoiceItemsTable.getModel());
        invoiceItemsTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();

        JPanel invoiceItemsPanel = new JPanel();
        invoiceItemsPanel.setLayout(new BorderLayout());
        invoiceItemsPanel.add((new JLabel("Invoice Items")) , BorderLayout.NORTH);
        JScrollPane invoiceItemsTablePane = new JScrollPane(invoiceItemsTable);
        invoiceItemsPanel.add(invoiceItemsTablePane);
        invoiceItemsPanel.setBorder(BorderFactory.createEmptyBorder(20,20,0,30));
        rightPanel.add(invoiceItemsPanel , BorderLayout.CENTER);

        JPanel rightBottomPanel = new JPanel();
        save = new JButton("Save");
        save.setFocusable(false);
        save.setBackground(Color.lightGray);
        save.setActionCommand("Save");

        cancel = new JButton("Cancel");
        cancel.setBackground(Color.lightGray);
        cancel.setFocusable(false);
        cancel.setActionCommand("Cancel");
        rightBottomPanel.add(save,BorderLayout.WEST);
        rightBottomPanel.add(cancel,BorderLayout.SOUTH);

        rightPanel.add(rightBottomPanel , BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu file = new JMenu("File");
        menuBar.add(file);

        loadFile = new JMenuItem("Load File" , 'L');
        loadFile.setAccelerator(KeyStroke.getKeyStroke('O' , KeyEvent.CTRL_DOWN_MASK));
        loadFile.setActionCommand("loadFile");

        saveFile = new JMenuItem("Save File" , 'S');
        saveFile.setAccelerator(KeyStroke.getKeyStroke('S' , KeyEvent.CTRL_DOWN_MASK));
        saveFile.setActionCommand("saveFile");

        file.add(loadFile);
        file.add(saveFile);
    }



    public void addActionListener(ActionListener l){
        newInvoice.addActionListener(l);
        deleteInvoice.addActionListener(l);
        save.addActionListener(l);
        cancel.addActionListener(l);
        loadFile.addActionListener(l);
        saveFile.addActionListener(l);
        createItem.addActionListener(l);
        deleteItem.addActionListener(l);
    }

    public void addTableListener(ListSelectionListener l){
        leftPanelTable.getSelectionModel().addListSelectionListener(l);
        invoiceItemsTable.getSelectionModel().addListSelectionListener(l);
    }
    public void addMouseListener(MouseListener l ){
        leftPanelTable.addMouseListener(l);
    }
}
