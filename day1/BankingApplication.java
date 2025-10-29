import java.util.Scanner;

// ========== Interface Segregation + Dependency Inversion ==========
// Common Account interface
interface Account {
    void deposit(double amount);
    void withdraw(double amount);
    void displayBalance();
}

// Loan Calculator interface
interface LoanCalculator {
    double calculateInterest();
}

// ========== Open/Closed + Liskov ==========
// Abstract BankAccount (base class with reusable logic)
abstract class BankAccount implements Account {
    protected String accountNumber;
    protected String accountHolderName;
    protected double balance;

    public BankAccount(String accountNumber, String accountHolderName, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount!");
        }
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        } else {
            System.out.println("Insufficient balance or invalid amount!");
        }
    }

    @Override
    public void displayBalance() {
        System.out.println("Account Holder: " + accountHolderName);
        System.out.println("Balance: " + balance);
    }
}

// ========== Single Responsibility + Extension ==========
// Savings Account
class SavingAccount extends BankAccount {
    private double interestRate = 0.05; // 5%

    public SavingAccount(String accountNumber, String accountHolderName, double balance) {
        super(accountNumber, accountHolderName, balance);
    }

    public void addInterest() {
        double interest = balance * interestRate;
        balance += interest;
        System.out.println("Interest added: " + interest);
    }
}

// Current Account
class CurrentAccount extends BankAccount {
    private double overdraftLimit = 5000;

    public CurrentAccount(String accountNumber, String accountHolderName, double balance) {
        super(accountNumber, accountHolderName, balance);
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && (balance + overdraftLimit) >= amount) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        } else {
            System.out.println("Overdraft limit exceeded!");
        }
    }
}

// ========== Loan Variations using OCP ==========
// Simple Interest Loan
class SimpleLoan implements LoanCalculator {
    private double principal;
    private double rate;
    private int time;

    public SimpleLoan(double principal, double rate, int time) {
        this.principal = principal;
        this.rate = rate;
        this.time = time;
    }

    @Override
    public double calculateInterest() {
        return (principal * rate * time) / 100;
    }
}

// ========== Main Application (Menu Driven) ==========
public class BankingApplication {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Account account = null;

        System.out.println("====== Welcome to Banking System ======");
        System.out.println("Choose Account Type:");
        System.out.println("1. Saving Account");
        System.out.println("2. Current Account");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        System.out.print("Enter Account Holder Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Initial Balance: ");
        double balance = sc.nextDouble();

        if (choice == 1) {
            account = new SavingAccount(accNo, name, balance);
        } else if (choice == 2) {
            account = new CurrentAccount(accNo, name, balance);
        } else {
            System.out.println("Invalid choice! Exiting...");
            return;
        }

        int option;
        do {
            System.out.println("\n====== Menu ======");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Display Balance");
            if (account instanceof SavingAccount) {
                System.out.println("4. Add Interest (Only for Savings)");
            }
            System.out.println("5. Calculate Loan Interest");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            option = sc.nextInt();

            switch (option) {
                case 1:
                    System.out.print("Enter amount to deposit: ");
                    double dep = sc.nextDouble();
                    account.deposit(dep);
                    break;

                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double wdr = sc.nextDouble();
                    account.withdraw(wdr);
                    break;

                case 3:
                    account.displayBalance();
                    break;

                case 4:
                    if (account instanceof SavingAccount) {
                        ((SavingAccount) account).addInterest();
                    } else {
                        System.out.println("Invalid option for Current Account.");
                    }
                    break;

                case 5:
                    System.out.print("Enter Loan Principal: ");
                    double p = sc.nextDouble();
                    System.out.print("Enter Loan Rate (% per year): ");
                    double r = sc.nextDouble();
                    System.out.print("Enter Time (years): ");
                    int t = sc.nextInt();

                    LoanCalculator loan = new SimpleLoan(p, r, t);
                    System.out.println("Loan Interest: " + loan.calculateInterest());
                    break;

                case 6:
                    System.out.println("Exiting... Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        } while (option != 6);

        sc.close();
    }
}