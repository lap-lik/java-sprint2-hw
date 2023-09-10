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
        boolean monthlyRep = monthlyReport.getMonthlyReport().isEmpty();
        boolean yearlyRep = yearlyReport.getYearlyReport().isEmpty();
        if (monthlyRep && yearlyRep) {
            System.out.println("Отчеты не считаны выполните пункты №1 и №2");
        } else if (monthlyRep) {
            System.out.println("Отчеты не считаны выполните пункт №1");
        } else if (yearlyRep) {
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
        System.out.println();
        for (String year : mapMonth.keySet()) {
            HashMap<String, Integer> transactionsByMonthReport = mapMonth.get(year);
            HashMap<String, Integer> transactionsByYearReport = mapYear.get(year);
            if (transactionsByYearReport == null) {
                System.out.printf("Отчет о %sах по месяцам за %s год есть, но годового отчета нет.\n", text, year);
                continue;
            }
            for (String transaction : transactionsByMonthReport.keySet()) {
                int sumByMonthReport = transactionsByMonthReport.get(transaction);
                int sumByYearReport = transactionsByYearReport.getOrDefault(transaction, 0);
                if (sumByMonthReport != sumByYearReport) {
                    System.out.printf("%s %s года, %s в отчете за месяц был %d, а в отчете за год %s %d.\n", transaction, year, text, sumByMonthReport, text, sumByYearReport);
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
