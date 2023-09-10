import java.util.ArrayList;
import java.util.HashMap;

public class YearlyReport {
    private HashMap<String, ArrayList<MonthTotalPerYear>> yearlyReport = new HashMap<>();

    public void loadReport(String path) {
        ArrayList<String> content = new FileReader().readFileContents(path);
        ArrayList<MonthTotalPerYear> monthTotalPerYears = new ArrayList<>();
        for (int i = 1; i < content.size(); i++) {
            String[] parts = content.get(i).split(",");
            MonthTotalPerYear monthTotal = new MonthTotalPerYear(Month.values()[Integer.parseInt(parts[0]) - 1].getName(), Integer.parseInt(parts[1]), Boolean.parseBoolean(parts[2]));
            monthTotalPerYears.add(monthTotal);
        }
        yearlyReport.put(path.substring(2, 6) + " год", monthTotalPerYears);
    }

    public void printReadYears() {
        System.out.print("\u001B[32m");
        if (yearlyReport.isEmpty()) {
            System.out.println("Нет файлов с годовыми отчетами.");
        }
        System.out.println("Считаны годовые отчеты за: ");
        for (String year : yearlyReport.keySet()) {
            System.out.println(year);
        }
        System.out.print("\u001B[38m");
    }

    public void report() {
        System.out.print("\u001B[32m");
        if (yearlyReport.isEmpty()) {
            System.out.println("Отчеты не считаны выполните пункт №2");
        } else {
            for (String year : yearlyReport.keySet()) {
                System.out.println(year);
                printProfitForMonth(year);
                printAverageExpensePerYear(year);
                printAverageProfitPerYear(year);
            }
        }
        System.out.print("\u001B[38m");
    }

    private void printProfitForMonth(String year) {
        for (MonthTotalPerYear monthProfit : yearlyReport.get(year)) {
            if (!monthProfit.isExpense()) {
                for (MonthTotalPerYear monthExpense : yearlyReport.get(year)) {
                    if (monthProfit.getMonth().equals(monthExpense.getMonth()) && monthExpense.isExpense()) {
                        System.out.printf("Прибыль за %s составила %d\n", monthProfit.getMonth(), (monthProfit.getAmount() - monthExpense.getAmount()));
                    }
                }
            }
        }
    }

    private void printAverageExpensePerYear(String year) {
        int count = 0;
        int expense = 0;
        for (MonthTotalPerYear monthExpense : yearlyReport.get(year)) {
            if (monthExpense.isExpense()) {
                count++;
                expense += monthExpense.getAmount();
            }
        }
        if (count == 0) {
            System.out.println("В этом году не было расходов.");
        } else {
            System.out.println("Средний расход за все имеющиеся операции в году: " + expense / count);
        }
    }

    private void printAverageProfitPerYear(String year) {
        int count = 0;
        int profit = 0;
        for (MonthTotalPerYear monthProfit : yearlyReport.get(year)) {
            if (!monthProfit.isExpense()) {
                count++;
                profit += monthProfit.getAmount();
            }
        }
        if (count == 0) {
            System.out.println("В этом году не было доходов.");
        } else {
            System.out.println("Средний доход за все имеющиеся операции в году: " + profit / count);
        }
    }

    public HashMap<String, ArrayList<MonthTotalPerYear>> getYearlyReport() {
        return yearlyReport;
    }
}
