public class Transaction {
    private String itemName;
    private boolean isExpense;
    private int quantity;
    private int unitPrice;

    public Transaction(String itemName, boolean isExpense, int quantity, int unitPrice) {
        this.itemName = itemName;
        this.isExpense = isExpense;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isExpense() {
        return isExpense;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

}
