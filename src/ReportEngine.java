import java.util.HashMap;

public class ReportEngine {
    private MonthlyReport monthlyReport;
    private YearlyReport yearlyReport;

    public ReportEngine(MonthlyReport monthlyReport, YearlyReport yearlyReport) {
        this.monthlyReport = monthlyReport;
        this.yearlyReport = yearlyReport;
        check();
    }

    private void check() {
        System.out.print("\u001B[32m");
        if (monthlyReport.getMonthlyReport().isEmpty() && yearlyReport.getYearlyReport().isEmpty()) {
            System.out.println("Отчеты не считаны выполните пункты №1 и №2");
        } else if (monthlyReport.getMonthlyReport().isEmpty() && !yearlyReport.getYearlyReport().isEmpty()) {
            System.out.println("Отчеты не считаны выполните пункт №1");
        } else if (!monthlyReport.getMonthlyReport().isEmpty() && yearlyReport.getYearlyReport().isEmpty()) {
            System.out.println("Отчеты не считаны выполните пункт №2");
        } else {
            checkAllReports();
        }
        System.out.print("\u001B[38m");
    }

    private void checkAllReports() {
        HashMap<String, HashMap<String, Integer>> mapMonthExpense = checkMonth(true);
        HashMap<String, HashMap<String, Integer>> mapYearExpense = checkYear(true);
        HashMap<String, HashMap<String, Integer>> mapMonthProfit = checkMonth(false);
        HashMap<String, HashMap<String, Integer>> mapYearProfit = checkYear(false);
        if (!mapMonthExpense.equals(mapYearExpense)) {
            checkReport(mapMonthExpense, mapYearExpense, "расход");
        }
        if (!mapMonthProfit.equals(mapYearProfit)) {
            checkReport(mapMonthProfit, mapYearProfit, "доход");
        }
        if (mapMonthExpense.equals(mapYearExpense) && mapMonthProfit.equals(mapYearProfit)) {
            System.out.println("Сверка данных прошла успешно!");
        }
    }

    private void checkReport(HashMap<String, HashMap<String, Integer>> mapMonth, HashMap<String, HashMap<String, Integer>> mapYear, String text) {
        for (String year : mapMonth.keySet()) {
            HashMap<String, Integer> transactionsByMonthReport = mapMonth.get(year);
            HashMap<String, Integer> transactionsByYearReport = mapYear.get(year);
            if (transactionsByYearReport == null) {
                System.out.println("Отчет о " + text + "ах по месяцам за " + year + " год есть, но годового отчета нет.");
                continue;
            }
            for (String transaction : transactionsByMonthReport.keySet()) {
                int sumByMonthReport = transactionsByMonthReport.get(transaction);
                int sumByYearReport = transactionsByYearReport.getOrDefault(transaction, 0);
                if (sumByMonthReport != sumByYearReport) {
                    System.out.println(transaction + " " + year + " года, " + text + " в отчете за месяц был " + sumByMonthReport + ", а в отчете за год " + text + " " + sumByYearReport);
                }
            }
        }
    }

    private HashMap<String, HashMap<String, Integer>> checkYear(Boolean isExpense) {
        HashMap<String, HashMap<String, Integer>> map = new HashMap<>();
        for (String name : yearlyReport.getYearlyReport().keySet()) {
            String nameForMapKey = name.replaceAll("[^0-9 ]", "").replaceAll(" +", " ").trim();
            if (!map.containsKey(nameForMapKey)) {
                map.put(nameForMapKey, new HashMap<>());
            }
            HashMap<String, Integer> monthMap = map.get(nameForMapKey);
            for (MonthTotalPerYear monthTotalPerYear : yearlyReport.getYearlyReport().get(name)) {
                if (monthTotalPerYear.isExpense() == isExpense) {
                    monthMap.put(monthTotalPerYear.getMonth(), monthTotalPerYear.getAmount());
                }
            }
        }
        return map;
    }

    private HashMap<String, HashMap<String, Integer>> checkMonth(Boolean isExpense) {
        HashMap<String, HashMap<String, Integer>> map = new HashMap<>();
        for (String name : monthlyReport.getMonthlyReport().keySet()) {
            String nameForMapKey = name.replaceAll("[^0-9 ]", "").replaceAll(" +", " ").trim();
            if (!map.containsKey(nameForMapKey)) {
                map.put(nameForMapKey, new HashMap<>());
            }
            HashMap<String, Integer> monthMap = map.get(nameForMapKey);
            int expense = 0;
            for (Transaction transaction : monthlyReport.getMonthlyReport().get(name)) {
                if (transaction.isExpense() == isExpense) {
                    expense += transaction.getUnitPrice() * transaction.getQuantity();
                }
            }
            monthMap.put(name.substring(0, name.indexOf(" ")), expense);
        }
        return map;
    }
}
