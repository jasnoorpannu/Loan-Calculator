import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class LoanCalculator {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = currentDate.format(dateFormatter);
        System.out.println("Program run date: " + formattedDate);

        System.out.print("Choose Task: 1 to Calculate Payments (EMI), 2 to Calculate Loan Tenure based on EMI: ");
        int choice = input.nextInt();

        if (choice == 1) {
            System.out.print("Choose Payment Frequency: 1 for Monthly, 2 for Biweekly, 3 for Weekly, 4 for Semi-Monthly: ");
            int frequencyChoice = input.nextInt();

            switch (frequencyChoice) {
                case 1:
                    calculateMonthlyEMI(input, currentDate);
                    break;
                case 2:
                    calculateBiweeklyEMI(input, currentDate);
                    break;
                case 3:
                    calculateWeeklyEMI(input, currentDate);
                    break;
                case 4:
                    calculateSemiMonthlyEMI(input, currentDate);
                    break;
                default:
                    System.out.println("Invalid payment frequency choice entered.");
            }
        }
        else if (choice == 2) {
            calculateLoanTenure(input);
        }
        else {
            System.out.println("Invalid choice entered.");
        }
        input.close();
    }

    public static void calculateMonthlyEMI(Scanner scanner, LocalDate startDate) {
        System.out.print("Enter the Principal: ");
        double principal = scanner.nextDouble();

        System.out.print("Enter the Annual Interest Rate (in %): ");
        double annualInterestRate = scanner.nextDouble();

        System.out.print("Enter the Loan Tenure (in Years): ");
        int years = scanner.nextInt();

        double monthlyInterestRate = annualInterestRate / 12 / 100;
        int numberOfPayments = years * 12;

        double emi = calculateEMI(principal, monthlyInterestRate, numberOfPayments);

        System.out.printf("The Monthly Payment (EMI) is: %.4f%n", emi);

        printPaymentSchedule(principal, monthlyInterestRate, emi, numberOfPayments, startDate, 1);
    }

    public static void calculateBiweeklyEMI(Scanner scanner, LocalDate startDate) {
        System.out.print("Enter the Principal: ");
        double principal = scanner.nextDouble();

        System.out.print("Enter the Annual Interest Rate (in %): ");
        double annualInterestRate = scanner.nextDouble();

        System.out.print("Enter the Loan Tenure (in Years): ");
        int years = scanner.nextInt();

        double biweeklyInterestRate = annualInterestRate / 26 / 100;
        int numberOfPayments = years * 26;

        double emi = calculateEMI(principal, biweeklyInterestRate, numberOfPayments);

        System.out.printf("The Biweekly Payment is: %.4f%n", emi);

        printPaymentSchedule(principal, biweeklyInterestRate, emi, numberOfPayments, startDate, 2);
    }

    public static void calculateWeeklyEMI(Scanner scanner, LocalDate startDate) {
        System.out.print("Enter the Principal: ");
        double principal = scanner.nextDouble();

        System.out.print("Enter the Annual Interest Rate (in %): ");
        double annualInterestRate = scanner.nextDouble();

        System.out.print("Enter the Loan Tenure (in Years): ");
        int years = scanner.nextInt();

        double weeklyInterestRate = annualInterestRate / 52 / 100;
        int numberOfPayments = years * 52;

        double emi = calculateEMI(principal, weeklyInterestRate, numberOfPayments);

        System.out.printf("The Weekly Payment is: %.4f%n", emi);

        printPaymentSchedule(principal, weeklyInterestRate, emi, numberOfPayments, startDate, 3);
    }

    public static void calculateSemiMonthlyEMI(Scanner scanner, LocalDate startDate) {
        System.out.print("Enter the Principal: ");
        double principal = scanner.nextDouble();

        System.out.print("Enter the Annual Interest Rate (in %): ");
        double annualInterestRate = scanner.nextDouble();

        System.out.print("Enter the Loan Tenure (in Years): ");
        int years = scanner.nextInt();

        double semiMonthlyInterestRate = annualInterestRate / 24 / 100;
        int numberOfPayments = years * 24;

        double emi = calculateEMI(principal, semiMonthlyInterestRate, numberOfPayments);

        System.out.printf("The Semi-Monthly Payment is: %.4f%n", emi);

        printSemiMonthlyPaymentSchedule(principal, semiMonthlyInterestRate, emi, numberOfPayments, startDate);
    }

    public static void calculateLoanTenure(Scanner scanner) {
        System.out.print("Enter the loan amount (principal): ");
        double principal = scanner.nextDouble();

        System.out.print("Enter the annual interest rate (in %): ");
        double annualInterestRate = scanner.nextDouble();

        System.out.print("Enter the Payment Amount (EMI): ");
        double payment = scanner.nextDouble();

        System.out.print("Enter the Payment Frequency: 1 for Monthly, 2 for Biweekly, 3 for Weekly, 4 for Semi-Monthly: ");
        int frequencyChoice = scanner.nextInt();

        double periodicInterestRate = 0;
        int paymentsPerYear = 0;

        switch (frequencyChoice) {
            case 1 -> {
                periodicInterestRate = annualInterestRate / 12 / 100;
                paymentsPerYear = 12;
            }
            case 2 -> {
                periodicInterestRate = annualInterestRate / 26 / 100;
                paymentsPerYear = 26;
            }
            case 3 -> {
                periodicInterestRate = annualInterestRate / 52 / 100;
                paymentsPerYear = 52;
            }
            case 4 -> {
                periodicInterestRate = annualInterestRate / 24 / 100;
                paymentsPerYear = 24;
            }
            default -> {
                System.out.println("Invalid payment frequency choice entered.");
                return;
            }
        }

        int numberOfPayments = (int) Math.ceil(Math.log(payment / (payment - principal * periodicInterestRate)) / Math.log(1 + periodicInterestRate));
        int years = numberOfPayments / paymentsPerYear;

        System.out.printf("The Loan tenure (in years) is approximately: %d years%n", years);
    }

    public static double calculateEMI(double principal, double periodicInterestRate, int numberOfPayments) {
        return principal * periodicInterestRate * Math.pow(1 + periodicInterestRate, numberOfPayments)
                / (Math.pow(1 + periodicInterestRate, numberOfPayments) - 1);
    }

    public static void printPaymentSchedule(double principal, double periodicInterestRate, double emi, int numberOfPayments, LocalDate startDate, int frequency) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate paymentDate = startDate;

        System.out.println("\nPayment Schedule:");
        System.out.printf("%-15s %-15s %-20s %-20s %-20s %-20s%n", "Payment Date", "Payment No.", "Interest", "Principal", "EMI", "Remaining Principal");

        for (int i = 1; i <= numberOfPayments; i++) {
            double interest = principal * periodicInterestRate;
            double principalPayment = emi - interest;
            principal -= principalPayment;

            System.out.printf("%-15s %-15d %-20.4f %-20.4f %-20.4f %-20.4f%n", paymentDate.format(dateFormatter), i, interest, principalPayment, emi, principal);

            paymentDate = switch (frequency) {
                case 1 -> paymentDate.plusMonths(1);
                case 2 -> paymentDate.plusWeeks(2);
                case 3 -> paymentDate.plusWeeks(1);
                default -> paymentDate;
            };
        }
    }

    public static void printSemiMonthlyPaymentSchedule(double principal, double periodicInterestRate, double emi, int numberOfPayments, LocalDate startDate) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate paymentDate;
        if (startDate.getDayOfMonth() <= 15) {
            paymentDate = startDate.withDayOfMonth(15);
        }
        else {
            paymentDate = startDate.plusMonths(1).withDayOfMonth(1);
        }

        System.out.println("\nSemi-Monthly Payment Schedule:");
        System.out.printf("%-15s %-15s %-20s %-20s %-20s %-20s%n", "Payment Date", "Payment No.", "Interest", "Principal", "EMI", "Remaining Principal");

        for (int i = 1; i <= numberOfPayments; i++) {
            double interest = principal * periodicInterestRate;
            double principalPayment = emi - interest;
            principal -= principalPayment;

            System.out.printf("%-15s %-15d %-20.4f %-20.4f %-20.4f %-20.4f%n", paymentDate.format(dateFormatter), i, interest, principalPayment, emi, principal);

            if (paymentDate.getDayOfMonth() == 1) {
                paymentDate = paymentDate.withDayOfMonth(15);
            }
            else {
                paymentDate = paymentDate.plusMonths(1).withDayOfMonth(1);
            }
        }
    }
}
