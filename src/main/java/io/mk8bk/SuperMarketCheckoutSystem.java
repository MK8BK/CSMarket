package io.mk8bk;

import java.util.Arrays;
import java.util.Scanner;

public class SuperMarketCheckoutSystem {
    static SessionController session = new SessionController();

    public static void main(String[] args) {
        loadData();
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
            session.login(arguments[0], arguments[1]);
        } else if ("logout".equals(command)) {
            if (arguments.length != 0) {
                System.out.println("Command `logout` takes no arguments.");
                return;
            }
            session.logout();
        } else if ("registerCustomer".equals(command)) {
            if (arguments.length != 5) {
                System.out.println(
                 "Command `registerCustomer` takes exactly five arguments: <firstname> <lastname> <username> <address> <password>"
                );
                return;
            }
            session.registerCustomer(arguments[0], arguments[1], arguments[2], arguments[3], arguments[4]);
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

    private static void loadData() {
        session.registerCustomer("ceo", "ceo", "ceo",
                "sillicon valley", "123456789");
        if(false)
            System.out.println("[COULD NOT INITIALIZE DATA]");
    }
}
