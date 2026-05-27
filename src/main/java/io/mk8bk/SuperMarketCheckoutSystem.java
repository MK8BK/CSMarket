package io.mk8bk;

import java.util.Arrays;
import java.util.Scanner;

public class SuperMarketCheckoutSystem {
    static final UserBase userBase = new UserBase();
    static final UserSession userSession = new UserSession(userBase);
    static final Inventory inventory = new Inventory();

    public static void main(String[] args) {
        try {
            userBase.registerManager("ceo", "123456789");
        } catch (UserBase.UserAlreadyRegisteredException e) {
            // dead branch
            throw new RuntimeException(e);
        }
        handleCLI();
    }

    public static void handleCLI() {
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.println("====================================================");
        System.out.println(" Supermarket Cash Register 2026 ");
        System.out.println("====================================================");
        printHelp();
        while (true) {
            System.out.print("> ");
            input = scanner.nextLine().trim();
            if (input.isEmpty())
                continue;
            if (input.equalsIgnoreCase("quit"))
                break;
            if (input.equalsIgnoreCase("help")) {
                printHelp();
                continue;
            }
            String[] parts = input.split("\\s+");
            String command = parts[0];
            String[] arguments = Arrays.copyOfRange(parts, 1, parts.length);
            handleCommand(command, arguments);
            System.out.print("\n");
        }
        scanner.close();
    }

    static void handleCommand(String command, String[] arguments) {
        String helpC = command;
        if ("login".equals(command)) {
            if (arguments.length != 2) {
                System.out.println("Command `login` takes exactly two arguments: <username> <password>.");
                return;
            }
            handleLogin(arguments[0], arguments[1]);
        } else if ("logout".equals(command)) {
            if (arguments.length != 0) {
                System.out.println("Command `logout` takes no arguments.");
                return;
            }
            handleLogout();
        } else if ("registerCashier".equals(command)) {
            if (arguments.length != 4) {
                System.out.println(
                        "Command `registerCashier` takes exactly four arguments: <firstname> <lastname> <username> <password>"
                );
                return;
            }
            handleRegisterCashierCommand(arguments[0], arguments[1], arguments[2], arguments[3]);
        } else if ("registerCustomer".equals(command)) {
            if (arguments.length != 5) {
                System.out.println(
                 "Command `registerCustomer` takes exactly five arguments: <firstname> <lastname> <username> <address> <password>"
                );
                return;
            }
            handleRegisterCustomerCommand(arguments[0], arguments[1], arguments[2], arguments[3], arguments[4]);
        } else if ("setup".equals(command)) {
            if (arguments.length != 0) {
                System.out.println(
                        "Command `setup` takes no arguments."
                );
            }
        }else{
            System.out.println("Command `"+command+"` is invalid, type `help` for available commands.");
        }
        //setup
        //registerCashier <firstname> <lastname> <username> <password>
        //registerCustomer <firstname> <lastname> <username> <address> <password>
        //addItem <itemName> <categoryName> <unitPrice> <weight> <initialStock>
        //restock <itemName> <quantity>
        //setCategoryDiscount <categoryName> <discountPercent>
        //subscribeToPlan <planName>
        //startCheckout <customerUsername>
        //scanItem <itemName> <quantity>
        //computeBill
        //requestDelivery <address>
        //pay <cardNumber> <pin>
        //simulatePayment <outcome>
        //showInventory
        //showRevenue
        //runTest <testScenario-file>
    }

    private static void handleRegisterCashierCommand(String firstname, String lastname, String username, String password) {
         if(!userSession.isUserLoggedIn()){
             System.out.println("No manager logged in. Can't register Cashier.");
             return;
         }
         if(userSession.getLoggedInUser().getClass()!=Manager.class){
             System.out.println("Only a Manager can register a new cashier.");
             return;
         }
        try {
            userBase.registerCashier(firstname, lastname, username, password);
            System.out.println("Cashier `"+username+"` successfully registered.");
        } catch (UserBase.UserAlreadyRegisteredException e) {
            System.out.println("A user with the username `"+username+"` is already registered.");
        }
    }

    private static void handleRegisterCustomerCommand(String firstname, String lastname, String username, String address, String password) {
        if(!userSession.isUserLoggedIn()){
            System.out.println("No cashier or manager logged in. Can't register customer");
            return;
        }
    }


    private static void handleLogout() {
        try {
            userSession.logout();
            System.out.println("Logout successfull.");
        } catch (UserSession.NoUserLoggedInException e) {
            System.out.println("No user logged in, can't logout.");
        }
    }

    private static void handleLogin(String username, String password) {
        try {
            userSession.login(username, password);
            User u = userSession.getLoggedInUser();
            if(u instanceof Cashier c){
                System.out.println("Cashier `"+username+"` logged in successfully.");
            }else if(u instanceof Manager m){
                System.out.println("Manager `"+username+"` logged in successfully.");
            }else{
                // dead branch, lookup sealed classes
                // TODO: lookup more details on sealed classes to assert correct behavior
               throw new RuntimeException("Unknown user type.");
            }
        } catch (UserSession.UserAlreadyLoggedInException e) {
            System.out.println("A user is already logged in ("+userSession.getLoggedInUser().getUsername()+"). Logout first.");
        } catch (UserBase.NoSuchUserException e) {
            System.out.println("No user with username `"+username+"` is registered. Register first.");
        } catch (UserSession.InvalidPasswordException e) {
            System.out.println("Invalid password for user `"+username+"`. Try again.");
        }
    }


    static void printHelp() {
        System.out.println("\n\t\tCommands");
        System.out.println("\tlogin <username> <password>");
        System.out.println("\tlogout");
        System.out.println("\tsetup");
        System.out.println("\tregisterCashier <firstname> <lastname> <username> <password>");
        System.out.println("\tregisterCustomer <firstname> <lastname> <username> <address> <password>");
        System.out.println("\taddItem <itemName> <categoryName> <unitPrice> <weight> <initialStock>");
        System.out.println("\trestock <itemName> <quantity>");
        System.out.println("\tsetCategoryDiscount <categoryName> <discountPercent>");
        System.out.println("\tsubscribeToPlan <planName>");
        System.out.println("\tstartCheckout <customerUsername>");
        System.out.println("\tscanItem <itemName> <quantity>");
        System.out.println("\tcomputeBill");
        System.out.println("\trequestDelivery <address>");
        System.out.println("\tpay <cardNumber> <pin>");
        System.out.println("\tsimulatePayment <outcome>");
        System.out.println("\tshowInventory");
        System.out.println("\tshowRevenue");
        System.out.println("\trunTest <testScenario-file>");
        System.out.println("\thelp");
        System.out.println("\tquit");
    }

}
