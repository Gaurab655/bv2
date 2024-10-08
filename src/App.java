import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class App {
    private static final String url = "jdbc:postgresql://localhost:5432/bv2";
    private static final String username = "postgres";
    private static final String password = "password";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found. " + e.getMessage());
            return;
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            if (!Objects.isNull(connection)) {
                System.out.println("Connection successful.");
                mainMenu(user, accounts, accountManager);
            } else {
                System.out.println("Error connecting to the database.");
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    public static void mainMenu(User user, Accounts accounts, AccountManager accountManager) throws SQLException {
        String email;
        int account_number;
        while (true) {
            System.out.println("Choose the following options:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");

            int choose;
            try {
                choose = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number." + e.getMessage());
                continue;
            }
            switch (choose) {
                case 1:
                    user.register_user();
                    break;
                case 2:
                    email = user.login_user();
                    if (email != null) {
                        System.out.println("Logged in");
                        if (!accounts.account_exists(email)) {
                            System.out.println("1. Open account");
                            System.out.println("2. Get account number");
                            System.out.println("3. Exit");
                            int choice2 = scanner.nextInt();
                            if (choice2 == 1) {
                                account_number = accounts.open_Account(email);
                                System.out.println("Account successfully created!");
                                System.out.println("Your account number is: " + account_number);
                            } else if (choice2 == 2) {
                                try {
                                    int accountNumber = accounts.getAccountNumber(email);
                                    System.out.println("Account number: " + accountNumber);
                                } catch (RuntimeException e) {
                                    System.out.println(e.getMessage());
                                    mainMenu(user, accounts, accountManager);
                                }
                            } else {
                                break;
                            }
                        }
                        account_number = accounts.getAccountNumber(email);
                        int choice2 = 0;
                        while (choice2 != 5) {
                            System.out.println();
                            System.out.println("1. Debit Money");
                            System.out.println("2. Credit Money");
                            System.out.println("3. Transfer Money");
                            System.out.println("4. Check Balance");
                            System.out.println("5. Log Out");
                            System.out.print("Enter your Choice: ");
                            choice2 = scanner.nextInt();

                            switch (choice2) {
                                case 1:
                                    accountManager.debit_amount(account_number);
                                    break;
                                case 2:
                                    accountManager.credit_amount(account_number);
                                    break;
                                case 3:
                                    accountManager.transferMoney(account_number);
                                    break;
                                case 4:
                                    accountManager.checkBalance(account_number);
                                    break;
                                case 5:
                                    System.out.println("Logging out...");
                                    accountManager.logOut(user, accounts, accountManager);
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please try again.");
                            }
                        }
                    }
                    break;
                case 0:
                    System.out.println("Exiting the application. Goodbye!");
                    return;
                default:
                    System.out.println("Please select a valid option.");
            }
        }
    }

    public static String getFullName() {
        scanner.nextLine();
        String fullName;
        while (true) {
            System.out.print("Enter full name: ");
            fullName = scanner.next().trim();

            if (fullName.isEmpty()) {
                System.out.println("Please enter a valid full name:");
            } else {
                return fullName;
            }
        }
    }


    public static Double getBalance() {
        Double balance ;
        while (true) {
            System.out.print("Enter balance: ");
            balance=scanner.nextDouble();
            if (balance==null){
                System.out.println("Please enter balance");
            }else {
               return balance;
            }
        }
    }

    public static String getPin() {
        String pin;
        while (true) {
            scanner.nextLine();
            System.out.print("Enter  pin: ");
            pin = scanner.nextLine();
            if (pin.length()!=4) {
                System.out.println("Please enter a valid PIN (exactly 4 characters).");
            } else {
                return pin;
            }
        }
    }
    public static String getEmail() {
        String email;
        while (true) {
            scanner.nextLine();
            System.out.print("Enter email: ");
            email = scanner.nextLine().trim();
            if (email.isEmpty()) {
                System.out.println("Please enter valid email");
            } else {
                return email;
            }
        }
    }
    public static String getPassword(){
        String password;
        while (true){
            System.out.print("Enter password : ");
            password=scanner.nextLine();
            if (password.isEmpty()||password==null){
                System.out.println("Please enter valid password");
            }else {
                return password;
            }
        }
    }
}
