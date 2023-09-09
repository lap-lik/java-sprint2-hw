import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        // Поехали!
        Scanner scanner = new Scanner(System.in);
        List<String> fileNames = new FileNameReader().getName("./resources/");
        MonthlyReport monthlyReport = new MonthlyReport();
        YearlyReport yearlyReport = new YearlyReport();

        loop:
        while (true) {
            printMenu();
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    for (String filterFileName : filterFileNames(fileNames, "m")) {
                        monthlyReport.loadReport(filterFileName);
                    }
                    monthlyReport.printReadMonths();
                    break;
                case 2:
                    for (String filterFileName : filterFileNames(fileNames, "y")) {
                        yearlyReport.loadReport(filterFileName);
                    }
                    yearlyReport.printReadYears();
                    break;
                case 3:
                    ReportEngine reportEngine = new ReportEngine(monthlyReport, yearlyReport);
                    break;
                case 4:
                    monthlyReport.report();
                    break;
                case 5:
                    yearlyReport.report();
                    break;
                case 6:
                    System.out.println("Программа завершена.");
                    break loop;
                default:
                    System.out.println("Извините, такой команды пока нет.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("Выберите одно из действий:" +
                "\n1. Считать все месячные отчёты." +
                "\n2. Считать годовой отчёт." +
                "\n3. Сверить отчёты." +
                "\n4. Вывести информацию обо всех месячных отчётах." +
                "\n5. Вывести информацию о годовом отчёте." +
                "\n6. Выход.");
    }

    private static List<String> filterFileNames(List<String> fileNames, String elem) {
        return fileNames.stream().filter(e -> e.contains(elem)).collect(Collectors.toList());
    }
}

