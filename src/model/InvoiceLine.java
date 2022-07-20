package model;

public class InvoiceLine {
    // details of each item in the receipt
    private String itemName;
    private int itemPrice;
    private int count;

    private int itemTotal;

    private int itemNumber;
    private static int number = 1;
    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public InvoiceLine(String itemName, int itemPrice, int count) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
        this.itemNumber = FileOperations.getnextItemNumber();
    }
    public InvoiceLine(String itemName, int itemPrice, int count ,int itemNumber) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
        this.itemNumber = itemNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public int getCount() {
        return count;
    }
}
