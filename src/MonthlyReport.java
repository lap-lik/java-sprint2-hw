import java.util.ArrayList;
import java.util.HashMap;

public class MonthlyReport {
    private HashMap<String, ArrayList<Transaction>> monthlyReport = new HashMap<>();

    public void loadReport(String path) {
        ArrayList<String> content = new FileReader().readFileContents(path);
        ArrayList<Transaction> transactions = new ArrayList<>();
        for (int i = 1; i < content.size(); i++) {
            String[] parts = content.get(i).split(",");
            Transaction transaction = new Transaction(parts[0], Boolean.parseBoolean(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
            transactions.add(transaction);
        }
        monthlyReport.put(Month.values()[Integer.parseInt(path.substring(6, 8)) - 1].getName() + " " + path.substring(2, 6) + " года", transactions);
    }

    public void printReadMonths() {
        System.out.print("\u001B[32m");
        if (monthlyReport.isEmpty()) {
            System.out.println("Нет файлов с месячными отчетами.");
        }
        System.out.println("Считаны месячные отчеты за: ");
        for (String month : monthlyReport.keySet()) {
            System.out.println(month);
        }
        System.out.print("\u001B[38m");
    }

    public void report() {
        System.out.print("\u001B[32m");
        if (monthlyReport.isEmpty()) {
            System.out.println("Отчеты не считаны выполните пункт №1");
        } else {
            for (String month : monthlyReport.keySet()) {
                System.out.println(month);
                printMostProfitProduct(month);
                printMaxExpense(month);
            }
        }
        System.out.print("\u001B[38m");
    }

    private void printMostProfitProduct(String month) {
        String productName = "";
        int profit = 0;
        for (Transaction transaction : monthlyReport.get(month)) {
            int temp = 0;
            if (!transaction.isExpense()) {
                temp = transaction.getUnitPrice() * transaction.getQuantity();
                if (temp > profit) {
                    profit = temp;
                    productName = transaction.getItemName();
                }
            }
        }
        if (profit == 0) {
            System.out.println("В этом месяце не было продаж.");
        } else {
            System.out.println("Самый прибыльный товар: " + productName + " на сумму " + profit);
        }
    }

    private void printMaxExpense(String month) {
        String productName = "";
        int expense = 0;
        for (Transaction transaction : monthlyReport.get(month)) {
            int temp = 0;
            if (transaction.isExpense()) {
                temp = transaction.getUnitPrice() * transaction.getQuantity();
                if (temp > expense) {
                    expense = temp;
                    productName = transaction.getItemName();
                }
            }
        }
        if (expense == 0) {
            System.out.println("В этом месяце не было покупок.");
        } else {
            System.out.println("Самая больщая трата: " + productName + " на сумму " + expense);
        }
    }

    public HashMap<String, ArrayList<Transaction>> getMonthlyReport() {
        return monthlyReport;
    }
}
